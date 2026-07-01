package com.vorix.gitservice.service.github.impl;

import com.vorix.gitservice.client.GitHubClient;
import com.vorix.gitservice.dto.github.auth.InstallationAccessTokenResponse;
import com.vorix.gitservice.service.github.GitHubJwtService;
import com.vorix.gitservice.service.github.InstallationTokenService;
import com.vorix.gitservice.service.github.internal.CachedInstallationToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class InstallationTokenServiceImpl implements InstallationTokenService {

    private final GitHubJwtService gitHubJwtService;
    private final GitHubClient gitHubClient;

    /**
     * In-memory cache.
     * Later can be replaced with Redis if needed.
     */
    private final Map<Long, CachedInstallationToken> tokenCache = new ConcurrentHashMap<>();

    @Override
    public String getAccessToken(Long installationId) {

        CachedInstallationToken cached = tokenCache.get(installationId);

        if (cached != null && cached.expiresAt().isAfter(Instant.now().plusSeconds(60))) {
            return cached.token();
        }

        return refreshToken(installationId);
    }

    private String refreshToken(Long installationId) {

        log.debug("Refreshing installation token for installation {}", installationId);

        String jwt = gitHubJwtService.generateJwt();

        InstallationAccessTokenResponse response = gitHubClient.postWithJwt(
                        "/app/installations/%d/access_tokens".formatted(installationId),
                        jwt,
                        InstallationAccessTokenResponse.class
                );

        CachedInstallationToken cached = new CachedInstallationToken(response.token(), response.expiresAt());

        tokenCache.put(installationId, cached);

        log.info("Installation token refreshed successfully for installation {}", installationId);

        return response.token();
    }
}
