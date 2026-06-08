package com.vorix.authservice.repository;

import com.vorix.authservice.entity.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, UUID> {

    Optional<EmailVerificationToken> findByTokenHash(String tokenHash);

    Optional<EmailVerificationToken> findByUser_Id(UUID userId);

    void deleteByUser_Id(UUID userId);

    @Modifying
    @Query("""
       DELETE FROM EmailVerificationToken evt
       WHERE evt.expiresAt < :now
       """)
    int deleteExpiredTokens(@Param("now") Instant now);
}
