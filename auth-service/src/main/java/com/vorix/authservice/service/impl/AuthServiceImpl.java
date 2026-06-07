package com.vorix.authservice.service.impl;

import com.vorix.authservice.dto.request.LoginRequest;
import com.vorix.authservice.dto.request.RefreshTokenRequest;
import com.vorix.authservice.dto.request.RegisterRequest;
import com.vorix.authservice.dto.response.LoginResponse;
import com.vorix.authservice.dto.response.RefreshTokenResponse;
import com.vorix.authservice.dto.response.RegisterResponse;
import com.vorix.authservice.entity.RefreshToken;
import com.vorix.authservice.entity.Role;
import com.vorix.authservice.entity.User;
import com.vorix.authservice.enums.AuthProvider;
import com.vorix.authservice.enums.RoleName;
import com.vorix.authservice.exception.AuthenticationException;
import com.vorix.authservice.exception.DuplicateResourceException;
import com.vorix.authservice.exception.RefreshTokenException;
import com.vorix.authservice.exception.ResourceNotFoundException;
import com.vorix.authservice.repository.RefreshTokenRepository;
import com.vorix.authservice.repository.RoleRepository;
import com.vorix.authservice.repository.UserRepository;
import com.vorix.authservice.security.jwt.JwtProperties;
import com.vorix.authservice.security.jwt.JwtService;
import com.vorix.authservice.security.jwt.JwtTokenType;
import com.vorix.authservice.security.token.TokenHashService;
import com.vorix.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.vorix.authservice.security.constants.SecurityConstants.MAX_FAILED_LOGIN_ATTEMPTS;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final TokenHashService tokenHashService;
    private final JwtProperties jwtProperties;

    @Override
    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException(
                    "Email already exists"
            );
        }

        if (userRepository.existsByUsername(request.username())) {
            throw new DuplicateResourceException(
                    "Username already exists"
            );
        }

        Role userRole = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() ->
                        new ResourceNotFoundException("USER role not found"));

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .provider(AuthProvider.LOCAL)
                .roles(Set.of(userRole))
                .build();

        User savedUser = userRepository.save(user);

        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                "User registered successfully"
        );
    }

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> {

                    log.warn("Failed login attempt for email={}", request.email());

                    return new AuthenticationException("Invalid email or password");
                });

        if (!user.isActive()) {
            throw new AuthenticationException("Account is inactive");
        }

        if (user.isAccountLocked()) {
            throw new AuthenticationException("Account is locked");
        }

        if (!passwordEncoder.matches(
                request.password(),
                user.getPasswordHash()
        )) {

            int attempts = user.getFailedLoginAttempts() + 1;

            user.setFailedLoginAttempts(attempts);

            if (attempts >= MAX_FAILED_LOGIN_ATTEMPTS) {
                user.setAccountLocked(true);
                log.warn("Account locked due to repeated failed login attempts. UserId={}", user.getId());
            }

            userRepository.save(user);

            log.warn("Failed login attempt for email={}", request.email());

            throw new AuthenticationException("Invalid email or password");
        }

        user.setLastLoginAt(Instant.now());

        user.setFailedLoginAttempts(0);

        userRepository.save(user);

        Set<String> roles = user.getRoles()
                .stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());

        String accessToken = jwtService.generateAccessToken(
                        user.getId(),
                        user.getEmail(),
                        roles
                );

        String refreshToken = jwtService.generateRefreshToken(user.getId());

        RefreshToken refreshTokenEntity = RefreshToken.builder()
                        .user(user)
                        .tokenHash(tokenHashService.hash(refreshToken))
                         .expiresAt(Instant.now().plusMillis(jwtProperties.refreshTokenExpiration()))
                        .revoked(false)
                        .createdAt(Instant.now())
                        .build();

        refreshTokenRepository.save(refreshTokenEntity);

        log.info("User logged in successfully. UserId={}", user.getId());

        return new LoginResponse(accessToken, refreshToken, "Bearer", jwtProperties.accessTokenExpiration() / 1000);
    }

    @Override
    @Transactional
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {

        String refreshToken = request.refreshToken();

        if (!jwtService.isTokenValid(refreshToken)) {
            throw new RefreshTokenException("Invalid refresh token");
        }

        if (jwtService.extractTokenType(refreshToken) != JwtTokenType.REFRESH) {

            throw new RefreshTokenException("Invalid token type");
        }

        String tokenHash = tokenHashService.hash(refreshToken);

        RefreshToken storedToken = refreshTokenRepository
                        .findByTokenHashAndRevokedFalse(tokenHash)
                        .orElseThrow(() -> new RefreshTokenException("Refresh token not found"));

        Instant now = Instant.now();

        if (storedToken.getExpiresAt().isBefore(now)) {

            throw new RefreshTokenException("Refresh token expired");
        }

        User user = storedToken.getUser();

        if (user.getProvider() != AuthProvider.LOCAL) {

            throw new RefreshTokenException("Invalid authentication provider");
        }

        if (!user.isActive()) {
            throw new RefreshTokenException("Account is inactive");
        }

        if (user.isAccountLocked()) {
            throw new RefreshTokenException("Account is locked");
        }

        if (user.isDeleted()) {
            throw new RefreshTokenException("Account is deleted");
        }

        storedToken.setRevoked(true);
        storedToken.setRevokedAt(now);

        refreshTokenRepository.save(storedToken);

        Set<String> roles = user.getRoles()
                                .stream()
                                .map(role -> role.getName().name())
                                .collect(Collectors.toUnmodifiableSet());

        String newAccessToken = jwtService.generateAccessToken(
                                user.getId(),
                                user.getEmail(),
                                roles
        );

        String newRefreshToken = jwtService.generateRefreshToken(user.getId());

        RefreshToken newToken =
                RefreshToken.builder()
                        .user(user)
                        .tokenHash(tokenHashService.hash(newRefreshToken))
                        .expiresAt(now.plusMillis(jwtProperties.refreshTokenExpiration()))
                        .createdAt(now)
                        .build();

        refreshTokenRepository.save(newToken);

        log.info("Refresh token rotated successfully. UserId={}", user.getId());

        return new RefreshTokenResponse(
                newAccessToken,
                newRefreshToken,
                "Bearer",
                jwtProperties.accessTokenExpiration() / 1000
        );
    }
}
