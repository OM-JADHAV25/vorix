package com.vorix.gitservice.service.github.internal;

import java.time.Instant;

public record CachedInstallationToken(

        String token,
        Instant expiresAt
) {
}