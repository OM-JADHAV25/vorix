package com.vorix.authservice.dto.request;

import jakarta.validation.constraints.NotBlank;
public record GoogleLoginRequest(

        @NotBlank
        String idToken
) {
}
