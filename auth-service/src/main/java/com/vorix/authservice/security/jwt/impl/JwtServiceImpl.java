package com.vorix.authservice.security.jwt.impl;

import com.vorix.authservice.security.jwt.JwtProperties;
import com.vorix.authservice.security.jwt.JwtService;
import com.vorix.authservice.security.jwt.JwtTokenType;
import com.vorix.authservice.security.jwt.exception.InvalidJwtTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JwtServiceImpl implements JwtService {

    private final JwtProperties jwtProperties;
    private SecretKey signingKey;

    public JwtServiceImpl(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @PostConstruct
    void initialize() {

        if (jwtProperties.secret() == null || jwtProperties.secret().length() < 32) {

            throw new IllegalStateException(
                    "JWT secret must be at least 32 characters long"
            );
        }

        this.signingKey = Keys.hmacShaKeyFor(
                jwtProperties.secret().getBytes(StandardCharsets.UTF_8)
        );
    }

    @Override
    public String generateAccessToken(UUID userId, String email, Set<String> roles) {

        Date now = new Date();

        Date expiry = new Date(
                now.getTime() + jwtProperties.accessTokenExpiration()
        );

        return Jwts.builder()
                .subject(userId.toString())
                .claim("email", email)
                .claim("roles", roles)
                .claim("token_type", JwtTokenType.ACCESS.name())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(signingKey)
                .compact();
    }

    @Override
    public String generateRefreshToken(UUID userId) {

        Date now = new Date();

        Date expiry = new Date(
                now.getTime() + jwtProperties.refreshTokenExpiration()
        );

        return Jwts.builder()
                .subject(userId.toString())
                .claim("token_type", JwtTokenType.REFRESH.name())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(signingKey)
                .compact();
    }

    @Override
    public Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @Override
    public UUID extractUserId(String token) {

        try {

            return UUID.fromString(
                    extractAllClaims(token).getSubject()
            );

        } catch (IllegalArgumentException | NullPointerException ex) {

            throw new InvalidJwtTokenException("Invalid user identifier in JWT", ex);
        }
    }

    @Override
    public String extractEmail(String token) {

        return extractAllClaims(token).get("email", String.class);
    }

    @Override
    public JwtTokenType extractTokenType(String token) {

        String tokenType = extractAllClaims(token).get("token_type", String.class);

        try {

            return JwtTokenType.valueOf(tokenType);

        } catch (Exception ex) {

            throw new InvalidJwtTokenException("Invalid token type", ex);
        }
    }

    @Override
    public boolean isTokenValid(String token) {

        try {

            extractAllClaims(token);
            return true;

        } catch (JwtException | IllegalArgumentException ex) {

            log.debug("JWT validation failed: {}", ex.getMessage());
            return false;
        }
    }

    @Override
    public Set<String> extractRoles(String token) {

        Object roles = extractAllClaims(token).get("roles");

        if (roles == null) {
            return Set.of();
        }

        if (!(roles instanceof Collection<?> collection)) {
            throw new InvalidJwtTokenException("Invalid roles claim");
        }

        return collection.stream()
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .collect(Collectors.toUnmodifiableSet());
    }
}