package com.vorix.authservice.service.impl;

import com.vorix.authservice.config.SecurityProperties;
import com.vorix.authservice.entity.EmailVerificationToken;
import com.vorix.authservice.entity.User;
import com.vorix.authservice.repository.EmailVerificationTokenRepository;
import com.vorix.authservice.security.token.TokenHashService;
import com.vorix.authservice.service.VerificationTokenService;
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
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final TokenHashService tokenHashService;
    private final SecurityProperties securityProperties;

    @Override
    public String createEmailVerificationToken(User user) {

        emailVerificationTokenRepository.deleteByUser_Id(user.getId());

        String rawToken = generateSecureToken();

         // log.info("EMAIL VERIFICATION TOKEN: {}", rawToken);

        Instant now = Instant.now();

        EmailVerificationToken token = EmailVerificationToken.builder()
                        .user(user)
                        .tokenHash(tokenHashService.hash(rawToken))
                        .expiresAt(now.plusMillis(securityProperties.emailVerificationTokenExpiration()))
                        .createdAt(now)
                        .build();

        emailVerificationTokenRepository.save(token);

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
