package com.vorix.authservice.repository;

import com.vorix.authservice.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {

    Optional<PasswordResetToken> findByTokenHash(String tokenHash);

    Optional<PasswordResetToken> findByUser_Id(UUID userId);

    void deleteByUser_Id(UUID userId);

    @Modifying
    @Query("""
       DELETE FROM PasswordResetToken prt
       WHERE prt.expiresAt < :now
       """)
    int deleteExpiredTokens(@Param("now") Instant now);
}
