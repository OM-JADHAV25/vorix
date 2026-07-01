package com.vorix.gitservice.dto.github.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record InstallationAccessTokenResponse(

        String token,

        @JsonProperty("expires_at")
        Instant expiresAt
) {
}