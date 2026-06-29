package com.vorix.gitservice.dto.github;

public record GitHubInstallationPayload(

        Long installationId,
        Long accountId,
        String accountLogin,
        String accountType,
        String targetType
) {
}