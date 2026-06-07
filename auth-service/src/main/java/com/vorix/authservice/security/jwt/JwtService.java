package com.vorix.authservice.security.jwt;

import io.jsonwebtoken.Claims;

import java.util.Set;
import java.util.UUID;

public interface JwtService {

    String generateAccessToken(UUID userId, String email, Set<String> roles);

    String generateRefreshToken(UUID userId);

    Claims extractAllClaims(String token);

    UUID extractUserId(String token);

    String extractEmail(String token);

    JwtTokenType extractTokenType(String token);

    boolean isTokenValid(String token);
}