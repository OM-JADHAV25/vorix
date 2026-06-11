package com.vorix.authservice.dto.oauth;

public record GitHubUserInfo(

        String providerId,
        String email,
        String username,
        boolean emailVerified
) {
}
