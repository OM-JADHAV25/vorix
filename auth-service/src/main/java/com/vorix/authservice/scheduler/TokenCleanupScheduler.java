package com.vorix.authservice.scheduler;

import com.vorix.authservice.repository.EmailVerificationTokenRepository;
import com.vorix.authservice.repository.PasswordResetTokenRepository;
import com.vorix.authservice.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenCleanupScheduler {

    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Transactional
    @Scheduled(cron = "${app.scheduler.token-cleanup.cron}")
    public void cleanupExpiredTokens() {

        Instant now = Instant.now();

        int deletedRefreshTokensCount  = refreshTokenRepository.deleteExpiredTokens(now);

        int deletedPasswordResetTokensCount  = passwordResetTokenRepository.deleteExpiredTokens(now);

        int deletedVerificationTokensCount  = emailVerificationTokenRepository.deleteExpiredTokens(now);

        log.info(
                """
                Token cleanup execution completed successfully.
                Metrics -> RefreshTokensDeleted: {}, PasswordResetTokensDeleted: {}, VerificationTokensDeleted: {}
                """,
                deletedRefreshTokensCount,
                deletedPasswordResetTokensCount,
                deletedVerificationTokensCount
        );
    }
}
