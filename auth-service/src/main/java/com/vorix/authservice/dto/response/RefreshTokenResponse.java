package com.vorix.authservice.dto.response;

public record RefreshTokenResponse(

        String accessToken,
        String refreshToken,
        String tokenType,
        Long expiresIn
) {
}
