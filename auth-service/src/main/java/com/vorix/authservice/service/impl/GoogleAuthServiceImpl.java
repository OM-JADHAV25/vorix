package com.vorix.authservice.service.impl;

import com.vorix.authservice.dto.oauth.GoogleUserInfo;
import com.vorix.authservice.dto.request.GoogleLoginRequest;
import com.vorix.authservice.dto.response.LoginResponse;
import com.vorix.authservice.entity.RefreshToken;
import com.vorix.authservice.entity.Role;
import com.vorix.authservice.entity.User;
import com.vorix.authservice.entity.UserAuthProvider;
import com.vorix.authservice.enums.AuthProvider;
import com.vorix.authservice.enums.RoleName;
import com.vorix.authservice.enums.SecurityEventType;
import com.vorix.authservice.exception.AuthenticationException;
import com.vorix.authservice.exception.ResourceNotFoundException;
import com.vorix.authservice.repository.RefreshTokenRepository;
import com.vorix.authservice.repository.RoleRepository;
import com.vorix.authservice.repository.UserAuthProviderRepository;
import com.vorix.authservice.repository.UserRepository;
import com.vorix.authservice.service.GoogleUserProvisioningService;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.vorix.authservice.security.jwt.JwtProperties;
import com.vorix.authservice.security.jwt.JwtService;
import com.vorix.authservice.security.token.TokenHashService;
import com.vorix.authservice.service.AuditService;
import com.vorix.authservice.service.GoogleAuthService;
import com.vorix.authservice.service.GoogleTokenVerifierService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleAuthServiceImpl implements GoogleAuthService {

    private final GoogleTokenVerifierService googleTokenVerifierService;

    private final UserRepository userRepository;
    private final UserAuthProviderRepository userAuthProviderRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final TokenHashService tokenHashService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;
    private final AuditService auditService;
    private final PasswordEncoder passwordEncoder;
    private final GoogleUserProvisioningService googleUserProvisioningService;

    @Override
    @Transactional
    public LoginResponse authenticate(GoogleLoginRequest request) {

        GoogleUserInfo googleUser = googleTokenVerifierService.verify(request.idToken());

        User user = resolveUser(googleUser);

        user = activateUnverifiedLocalUserIfEligible(user, googleUser);

        validateUser(user);

        return issueTokens(user);
    }

    private User resolveUser(GoogleUserInfo googleUser) {

        return userAuthProviderRepository
                .findByProviderAndProviderId(AuthProvider.GOOGLE, googleUser.providerId())
                .map(UserAuthProvider::getUser)
                .orElseGet(() -> {

                    User user = userRepository
                                    .findByEmail(googleUser.email())
                            .orElseGet(() -> {

                                try {

                                    return googleUserProvisioningService.createGoogleUser(googleUser);

                                } catch (DataIntegrityViolationException ex) {

                                    return userRepository.findByEmail(googleUser.email()).orElseThrow(() -> ex);
                                }
                            });;

                    linkGoogleProvider(user, googleUser);

                    return user;
                });
    }

    private User activateUnverifiedLocalUserIfEligible(User user, GoogleUserInfo googleUser) {

        if (user.isDeleted()) {
            return user;
        }

        if (user.isAccountLocked()) {
            return user;
        }

        if (user.isActive()) {
            return user;
        }

        if (user.isEmailVerified()) {
            return user;
        }

        if (!googleUser.emailVerified()) {
            return user;
        }

        user.setEmailVerified(true);
        user.setActive(true);

        User updatedUser = userRepository.save(user);

        auditService.log(
                updatedUser.getId(),
                SecurityEventType.EMAIL_VERIFIED,
                "Local account activated through Google OAuth"
        );

        log.info(
                "Activated local account through Google OAuth. UserId={}",
                updatedUser.getId()
        );

        return updatedUser;
    }

    private void linkGoogleProvider(User user, GoogleUserInfo googleUser) {

        UserAuthProvider provider = UserAuthProvider.builder()
                                                    .user(user)
                                                    .provider(AuthProvider.GOOGLE)
                                                    .providerId(googleUser.providerId())
                                                    .createdAt(Instant.now())
                                                    .build();

        try {

            userAuthProviderRepository.saveAndFlush(provider);

            log.info("Linked Google provider. UserId={}", user.getId());

            auditService.log(
                    user.getId(),
                    SecurityEventType.OAUTH_PROVIDER_LINKED,
                    "Google provider linked"
            );

        } catch (DataIntegrityViolationException ex) {

            log.info("Google provider already linked. UserId={}", user.getId());
        }
    }

    private String generateUsername(String email) {

        String base = email.substring(0, email.indexOf('@'));

        return base + "_"
                + UUID.randomUUID()
                .toString()
                .substring(0, 8);
    }

    private void validateUser(User user) {

        if (!user.isActive()) {

            throw new AuthenticationException("Account is inactive");
        }

        if (user.isAccountLocked()) {

            throw new AuthenticationException("Account is locked");
        }

        if (user.isDeleted()) {

            throw new AuthenticationException("Account is deleted");
        }
    }

    private LoginResponse issueTokens(User user) {

        Instant now = Instant.now();

        user.setLastLoginAt(now);

        user.setLastLoginAt(now);

        Set<String> roles = user.getRoles()
                                .stream()
                                .map(role -> role.getName().name())
                                .collect(Collectors.toUnmodifiableSet());

        String accessToken = jwtService.generateAccessToken(user.getId(), user.getEmail(), roles);

        String refreshToken =
                jwtService.generateRefreshToken(
                        user.getId()
                );

        RefreshToken refreshTokenEntity = RefreshToken.builder()
                                          .user(user)
                                          .tokenHash(tokenHashService.hash(refreshToken))
                                          .expiresAt(now.plusMillis(jwtProperties.refreshTokenExpiration()))
                                          .revoked(false)
                                          .createdAt(now)
                                          .build();

        refreshTokenRepository.save(refreshTokenEntity);

        auditService.log(
                user.getId(),
                SecurityEventType.LOGIN_SUCCESS,
                "User authenticated via Google OAuth"
        );

        log.info("Google login successful. UserId={}", user.getId());

        return new LoginResponse(

                accessToken,
                refreshToken,
                "Bearer",
                jwtProperties.accessTokenExpiration() / 1000
        );
    }
}