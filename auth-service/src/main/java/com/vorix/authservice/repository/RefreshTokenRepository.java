package com.vorix.authservice.repository;

import com.vorix.authservice.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByTokenHash(String tokenHash);

    Optional<RefreshToken> findByTokenHashAndRevokedFalse(String tokenHash);

    @Query("""
        SELECT rt
        FROM RefreshToken rt
        WHERE rt.user.id = :userId
        AND rt.revoked = false
        """)
    List<RefreshToken> findActiveTokensByUserId(@Param("userId") UUID userId);
}