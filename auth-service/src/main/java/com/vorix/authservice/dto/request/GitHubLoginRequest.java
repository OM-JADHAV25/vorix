package com.vorix.authservice.dto.request;

import jakarta.validation.constraints.NotBlank;

public record GitHubLoginRequest(

        @NotBlank
        String code
) {
}
