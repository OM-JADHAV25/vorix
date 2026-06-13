package com.vorix.gatewayservice.security.jwt;

import io.jsonwebtoken.Claims;

import java.util.Set;
import java.util.UUID;

public interface JwtService {

    Claims extractAllClaims(String token);

    UUID extractUserId(String token);

    String extractEmail(String token);

    Set<String> extractRoles(String token);

    JwtTokenType extractTokenType(String token);

    boolean isTokenValid(String token);
}
