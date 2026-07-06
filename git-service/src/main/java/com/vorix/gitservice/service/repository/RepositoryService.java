package com.vorix.gitservice.service.repository;

import com.vorix.gitservice.domain.model.github.RepositoryMetadata;
import com.vorix.gitservice.domain.model.repository.RepositoryFile;

public interface RepositoryService {

    RepositoryMetadata getRepository(
            Long installationId,
            String owner,
            String repository
    );

    RepositoryFile getRepositoryFile(
            Long installationId,
            String owner,
            String repository,
            String path,
            String ref
    );
}