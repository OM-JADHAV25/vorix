package com.vorix.authservice.service.impl;

import com.vorix.authservice.config.AppProperties;
import com.vorix.authservice.dto.request.*;
import com.vorix.authservice.dto.response.LoginResponse;
import com.vorix.authservice.dto.response.RefreshTokenResponse;
import com.vorix.authservice.dto.response.RegisterResponse;
import com.vorix.authservice.entity.*;
import com.vorix.authservice.enums.AuthProvider;
import com.vorix.authservice.enums.RoleName;
import com.vorix.authservice.enums.SecurityEventType;
import com.vorix.authservice.event.EmailVerificationEvent;
import com.vorix.authservice.event.PasswordResetEmailEvent;
import com.vorix.authservice.exception.*;
import com.vorix.authservice.repository.*;
import com.vorix.authservice.security.jwt.JwtProperties;
import com.vorix.authservice.security.jwt.JwtService;
import com.vorix.authservice.security.jwt.JwtTokenType;
import com.vorix.authservice.security.token.TokenHashService;
import com.vorix.authservice.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
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
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final TokenHashService tokenHashService;
    private final JwtProperties jwtProperties;
    private final TokenSecurityService tokenSecurityService;
    private final VerificationTokenService verificationTokenService;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final AppProperties appProperties;
    private final PasswordResetTokenService passwordResetTokenService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final AuditService auditService;
    private final GoogleAuthService googleAuthService;

    @Override
    @Transactional
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
                .orElseThrow(() -> new ResourceNotFoundException("USER role not found"));

        User user = User.builder()
                        .username(request.username())
                        .email(request.email())
                        .passwordHash(passwordEncoder.encode(request.password()))
                        .roles(Set.of(userRole))
                        .build();

        UserAuthProvider localProvider = UserAuthProvider.builder()
                                         .user(user)
                                         .provider(AuthProvider.LOCAL)
                                         .createdAt(Instant.now())
                                         .build();

        user.getAuthProviders().add(localProvider);

        User savedUser;

        try {

            savedUser = userRepository.saveAndFlush(user);

        } catch (DataIntegrityViolationException ex) {

            throw new DuplicateResourceException("Email or username already exists");
        }

        String verificationToken = verificationTokenService.createEmailVerificationToken(savedUser);

        String verificationUrl = appProperties.frontendUrl() + "/verify-email?token=" + verificationToken;
        applicationEventPublisher.publishEvent(new EmailVerificationEvent(savedUser.getEmail(), verificationUrl));

        auditService.log(
                savedUser.getId(),
                SecurityEventType.EMAIL_VERIFICATION_REQUESTED,
                "Email verification requested"
        );

        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                "User registered successfully"
        );
    }

    @Override
    @Transactional
    public LoginResponse loginWithGoogle(GoogleLoginRequest request) {

        return googleAuthService.authenticate(request);
    }

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> {

                    auditService.log(
                            null,
                            SecurityEventType.LOGIN_FAILED,
                            "Failed login attempt for email=" + request.email()
                    );

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

            log.warn("Failed login attempt {} for userId={}", attempts, user.getId());

            if (attempts >= MAX_FAILED_LOGIN_ATTEMPTS) {
                user.setAccountLocked(true);

                auditService.log(
                        user.getId(),
                        SecurityEventType.ACCOUNT_LOCKED,
                        "Maximum failed login attempts exceeded"
                );

                log.warn("Account locked due to repeated failed login attempts. UserId={}", user.getId());
            }

            userRepository.save(user);

            log.warn("Failed login attempt for email={}", request.email());

            auditService.log(
                    user.getId(),
                    SecurityEventType.LOGIN_FAILED,
                    "Invalid password"
            );

            throw new AuthenticationException("Invalid email or password");
        }

        Instant now = Instant.now();

        user.setLastLoginAt(now);

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
                         .expiresAt(now.plusMillis(jwtProperties.refreshTokenExpiration()))
                        .revoked(false)
                        .createdAt(now)
                        .build();

        refreshTokenRepository.save(refreshTokenEntity);

        log.info("User logged in successfully. UserId={}", user.getId());

        auditService.log(
                user.getId(),
                SecurityEventType.LOGIN_SUCCESS,
                "User logged in successfully"
        );

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
                                  .findByTokenHash(tokenHash)
                                  .orElseThrow(() -> new RefreshTokenException("Refresh token not found"));

        if (storedToken.isRevoked()) {

            UUID userId = storedToken.getUser().getId();

            tokenSecurityService.revokeAllActiveRefreshTokens(userId);

            log.error("Refresh token reuse detected. UserId={}", userId);

            auditService.log(
                    userId,
                    SecurityEventType.REFRESH_TOKEN_REUSE_DETECTED,
                    "Refresh token reuse detected"
            );

            throw new RefreshTokenException("Refresh token reuse detected. Please login again.");
        }

        Instant now = Instant.now();

        if (storedToken.getExpiresAt().isBefore(now)) {

            throw new RefreshTokenException("Refresh token expired");
        }

        User user = storedToken.getUser();

        boolean hasLocalProvider = user.getAuthProviders()
                                       .stream()
                                       .anyMatch(provider -> provider.getProvider() == AuthProvider.LOCAL);

        if (!hasLocalProvider) {

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
                        .revoked(false)
                        .createdAt(now)
                        .build();

        refreshTokenRepository.save(newToken);

        log.info("Refresh token rotated successfully. UserId={}", user.getId());

        auditService.log(
                user.getId(),
                SecurityEventType.REFRESH_TOKEN_ROTATED,
                "Refresh token rotated successfully"
        );

        return new RefreshTokenResponse(
                newAccessToken,
                newRefreshToken,
                "Bearer",
                jwtProperties.accessTokenExpiration() / 1000
        );
    }

    @Override
    @Transactional
    public void logout(LogoutRequest request) {

        String refreshToken = request.refreshToken();

        String tokenHash = tokenHashService.hash(refreshToken);

        RefreshToken storedToken = refreshTokenRepository
                                   .findByTokenHashAndRevokedFalse(tokenHash)
                                   .orElseThrow(() -> new RefreshTokenException("Refresh token not found"));

        storedToken.setRevoked(true);
        storedToken.setRevokedAt(Instant.now());

        refreshTokenRepository.save(storedToken);

        log.info("User logged out successfully. UserId={}", storedToken.getUser().getId());

        auditService.log(
                storedToken.getUser().getId(),
                SecurityEventType.LOGOUT,
                "User logged out successfully"
        );
    }

    @Override
    @Transactional
    public void verifyEmail(String token) {

        String tokenHash = tokenHashService.hash(token);

        EmailVerificationToken verificationToken = emailVerificationTokenRepository
                        .findByTokenHash(tokenHash)
                        .orElseThrow(() -> new ResourceNotFoundException("Verification token not found"));

        if (verificationToken.isConsumed()) {

            throw new EmailVerificationException("Verification token already used");
        }

        Instant now = Instant.now();

        if (verificationToken.getExpiresAt().isBefore(now)) {

            throw new EmailVerificationException("Verification token expired");
        }

        User user = verificationToken.getUser();

        user.setEmailVerified(true);

        auditService.log(
                user.getId(),
                SecurityEventType.EMAIL_VERIFIED,
                "Email verified successfully"
        );

        user.setActive(true);

        verificationToken.setConsumed(true);

        verificationToken.setVerifiedAt(now);

        userRepository.save(user);

        emailVerificationTokenRepository.save(verificationToken);

        log.info("Email verified successfully. UserId={}", user.getId());
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {

        log.info("FORGOT PASSWORD CALLED: {}", request.email());

        User user = userRepository.findByEmail(request.email()).orElse(null);

        boolean shouldProcess = user != null
                                && user.isActive()
                                && !user.isDeleted();

        /*
         * IMPORTANT:
         * Never reveal whether email exists.
         */

        // TODO:
        // Normalize execution timing to mitigate
        // forgot-password user enumeration attacks.

        if (shouldProcess) {

            String rawToken = passwordResetTokenService.createPasswordResetToken(user);

            String resetUrl = appProperties.frontendUrl()
                            + "/reset-password?token="
                            + rawToken;

            applicationEventPublisher.publishEvent(new PasswordResetEmailEvent(user.getEmail(), resetUrl)
            );

            auditService.log(
                    user.getId(),
                    SecurityEventType.PASSWORD_RESET_REQUESTED,
                    "Password reset requested"
            );

            log.info("Password reset email sent. UserId={}", user.getId());
        }

        log.info("Forgot password flow completed for email={}", request.email());
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {

        String tokenHash = tokenHashService.hash(request.token());

        PasswordResetToken resetToken = passwordResetTokenRepository
                                       .findByTokenHash(tokenHash)
                                       .orElseThrow(() -> new PasswordResetException("Invalid password reset token"));

        Instant now = Instant.now();

        if (resetToken.isConsumed()) {

            throw new PasswordResetException("Password reset token already used");
        }

        if (resetToken.getExpiresAt().isBefore(now)) {

            throw new PasswordResetException("Password reset token expired");
        }

        User user = resetToken.getUser();

        if (user.isDeleted()) {

            throw new PasswordResetException("Account is deleted");
        }

        if (!user.isActive()) {

            throw new PasswordResetException("Account is inactive");
        }

        String encodedPassword = passwordEncoder.encode(request.newPassword());

        user.setPasswordHash(encodedPassword);

        auditService.log(
                user.getId(),
                SecurityEventType.PASSWORD_RESET_COMPLETED,
                "Password reset completed successfully"
        );

        user.setFailedLoginAttempts(0);

        userRepository.save(user);

        resetToken.setConsumed(true);

        resetToken.setUsedAt(now);

        passwordResetTokenRepository.save(resetToken);

        tokenSecurityService.revokeAllActiveRefreshTokens(user.getId());

        log.info("Password reset completed successfully. UserId={}", user.getId());
    }
}