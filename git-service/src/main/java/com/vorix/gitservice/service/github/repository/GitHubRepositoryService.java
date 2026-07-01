package com.vorix.gitservice.service.github.repository;

import com.vorix.gitservice.domain.model.github.RepositoryMetadata;

public interface GitHubRepositoryService {

    RepositoryMetadata getRepository(
            Long installationId,
            String owner,
            String repository
    );
}