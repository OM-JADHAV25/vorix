package com.vorix.authservice.service.impl;

import com.vorix.authservice.dto.oauth.GitHubUserInfo;
import com.vorix.authservice.dto.request.GitHubLoginRequest;
import com.vorix.authservice.dto.response.LoginResponse;
import com.vorix.authservice.entity.RefreshToken;
import com.vorix.authservice.entity.User;
import com.vorix.authservice.entity.UserAuthProvider;
import com.vorix.authservice.enums.AuthProvider;
import com.vorix.authservice.enums.SecurityEventType;
import com.vorix.authservice.exception.AuthenticationException;
import com.vorix.authservice.repository.RefreshTokenRepository;
import com.vorix.authservice.repository.UserAuthProviderRepository;
import com.vorix.authservice.repository.UserRepository;
import com.vorix.authservice.service.*;
import com.vorix.authservice.security.jwt.JwtProperties;
import com.vorix.authservice.security.jwt.JwtService;
import com.vorix.authservice.security.token.TokenHashService;
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
public class GitHubAuthServiceImpl implements GitHubAuthService {

    private final GitHubOAuthClient gitHubOAuthClient;

    private final UserRepository userRepository;
    private final UserAuthProviderRepository userAuthProviderRepository;
    private final JwtService jwtService;
    private final TokenHashService tokenHashService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;
    private final AuditService auditService;
    private final GitHubUserProvisioningService gitHubUserProvisioningService;

    @Override
    @Transactional
    public LoginResponse authenticate(GitHubLoginRequest request) {

        GitHubUserInfo gitHubUser = gitHubOAuthClient.getUserInfo(request.code());

        User user = resolveUser(gitHubUser);

        user = activateUnverifiedLocalUserIfEligible(user, gitHubUser);

        validateUser(user);

        return issueTokens(user);
    }

    private User resolveUser(GitHubUserInfo gitHubUser) {

        return userAuthProviderRepository
                .findByProviderAndProviderId(AuthProvider.GITHUB, gitHubUser.providerId())
                .map(UserAuthProvider::getUser)
                .orElseGet(() -> {

                    User user = userRepository
                            .findByEmail(gitHubUser.email())
                            .orElseGet(() -> {

                                try {

                                    return gitHubUserProvisioningService.createGitHubUser(gitHubUser);

                                } catch (DataIntegrityViolationException ex) {

                                    return userRepository.findByEmail(gitHubUser.email()).orElseThrow(() -> ex);
                                }
                            });;

                    linkGitHubProvider(user, gitHubUser);

                    return user;
                });
    }

    private User activateUnverifiedLocalUserIfEligible(User user, GitHubUserInfo gitHubUser) {

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

        if (!gitHubUser.emailVerified()) {
            return user;
        }

        user.setEmailVerified(true);
        user.setActive(true);

        User updatedUser = userRepository.save(user);

        auditService.log(
                updatedUser.getId(),
                SecurityEventType.EMAIL_VERIFIED,
                "Local account activated through GitHub OAuth"
        );

        log.info(
                "Activated local account through GitHub OAuth. UserId={}",
                updatedUser.getId()
        );

        return updatedUser;
    }

    private void linkGitHubProvider(User user, GitHubUserInfo gitHubUser) {

        UserAuthProvider provider = UserAuthProvider.builder()
                .user(user)
                .provider(AuthProvider.GITHUB)
                .providerId(gitHubUser.providerId())
                .createdAt(Instant.now())
                .build();

        try {

            userAuthProviderRepository.saveAndFlush(provider);

            log.info("Linked GitHub provider. UserId={}", user.getId());

            auditService.log(
                    user.getId(),
                    SecurityEventType.OAUTH_PROVIDER_LINKED,
                    "GitHub provider linked"
            );

        } catch (DataIntegrityViolationException ex) {

            log.info("GitHub provider already linked. UserId={}", user.getId());
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

        Set<String> roles = user.getRoles()
                .stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toUnmodifiableSet());

        String accessToken = jwtService.generateAccessToken(user.getId(), user.getEmail(), roles);

        String refreshToken = jwtService.generateRefreshToken(user.getId());

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
                "User authenticated via GitHub OAuth"
        );

        log.info("GitHub login successful. UserId={}", user.getId());

        return new LoginResponse(

                accessToken,
                refreshToken,
                "Bearer",
                jwtProperties.accessTokenExpiration() / 1000
        );
    }
}