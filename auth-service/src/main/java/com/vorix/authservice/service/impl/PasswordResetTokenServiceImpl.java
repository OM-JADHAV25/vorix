package com.vorix.authservice.service.impl;

import com.vorix.authservice.config.SecurityProperties;
import com.vorix.authservice.entity.PasswordResetToken;
import com.vorix.authservice.entity.User;
import com.vorix.authservice.repository.PasswordResetTokenRepository;
import com.vorix.authservice.security.token.TokenHashService;
import com.vorix.authservice.service.PasswordResetTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final TokenHashService tokenHashService;
    private final SecurityProperties securityProperties;

    @Override
    public String createPasswordResetToken(User user) {

        String rawToken = generateSecureToken();

        // log.warn("PASSWORD RESET TOKEN = {}", rawToken);

        String tokenHash = tokenHashService.hash(rawToken);

        Instant now = Instant.now();

        PasswordResetToken token = passwordResetTokenRepository
                                  .findByUser_Id(user.getId())
                                  .orElseGet(() -> PasswordResetToken.builder().user(user).createdAt(now).build());

        token.setTokenHash(tokenHash);

        token.setExpiresAt(now.plusMillis(securityProperties.passwordResetTokenExpiration()));

        token.setConsumed(false);

        token.setUsedAt(null);

        passwordResetTokenRepository.save(token);

        return rawToken;
    }

    private String generateSecureToken() {

        byte[] bytes = new byte[32];

        SECURE_RANDOM.nextBytes(bytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }
}
