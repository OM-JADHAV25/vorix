package com.vorix.gitservice.dto.github;

import java.util.List;

public record InstallationRepositoriesPayload(
        Long installationId,
        List<RepositoryPayload> repositoriesAdded,
        List<RepositoryPayload> repositoriesRemoved
) {

    public record RepositoryPayload(
            Long repositoryId,
            String repositoryName,
            String fullName,
            boolean isPrivate
    ) {
    }
}