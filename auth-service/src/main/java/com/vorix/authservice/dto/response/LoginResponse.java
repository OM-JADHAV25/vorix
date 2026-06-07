package com.vorix.authservice.dto.response;

public record LoginResponse(

        String accessToken,
        String refreshToken,
        String tokenType,
        Long expiresIn
) {
}
