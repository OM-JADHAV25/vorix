package com.vorix.gitservice.domain.model.github;

import java.time.Instant;

public record RepositoryMetadata(

        Long providerRepositoryId,
        String owner,
        String repositoryName,
        String fullName,
        boolean isPrivate,
        String visibility,
        boolean archived,
        boolean disabled,
        String defaultBranch,
        String cloneUrl,
        String htmlUrl,
        String language,
        Instant createdAt,
        Instant updatedAt
) {
}