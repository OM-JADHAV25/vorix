package com.vorix.authservice.dto.response;

public record RefreshTokenResponse(

        String accessToken,
        String tokenType,
        Long expiresIn

) {
}
