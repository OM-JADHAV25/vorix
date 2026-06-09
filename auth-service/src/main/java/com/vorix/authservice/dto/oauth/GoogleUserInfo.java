package com.vorix.authservice.dto.oauth;

public record GoogleUserInfo(

        String providerId,
        String email,
        String name,
        boolean emailVerified
) {
}
