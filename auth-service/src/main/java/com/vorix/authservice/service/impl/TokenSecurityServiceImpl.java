package com.vorix.authservice.service.impl;

import com.vorix.authservice.entity.RefreshToken;
import com.vorix.authservice.repository.RefreshTokenRepository;
import com.vorix.authservice.service.TokenSecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenSecurityServiceImpl implements TokenSecurityService  {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void revokeAllActiveRefreshTokens(UUID userId) {

        List<RefreshToken> activeTokens = refreshTokenRepository.findActiveTokensByUserId(userId);

        Instant now = Instant.now();

        activeTokens.forEach(token -> {
            token.setRevoked(true);
            token.setRevokedAt(now);
        });

        refreshTokenRepository.saveAll(activeTokens);

        log.warn("Revoked {} active refresh tokens for userId={}", activeTokens.size(), userId);
    }
}
