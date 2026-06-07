package com.vorix.authservice.dto.response;

import java.util.UUID;

public record RegisterResponse(
        UUID userId,
        String username,
        String email,
        String message
) {
}
