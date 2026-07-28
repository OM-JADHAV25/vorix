package com.vorix.gitservice.domain.model.checks;

public record CheckRunRequest(
        Long installationId,
        String owner,
        String repository,
        String headSha,
        String name
) {
}