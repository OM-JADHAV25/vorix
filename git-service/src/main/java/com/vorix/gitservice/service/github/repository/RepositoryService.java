package com.vorix.gitservice.service.github.repository;

import com.vorix.gitservice.domain.model.github.RepositoryMetadata;
import com.vorix.gitservice.domain.model.repository.RepositoryFile;

public interface RepositoryService {

    RepositoryMetadata getRepository(
            Long installationId,
            String owner,
            String repository
    );

    RepositoryFile getFileContent(
            Long installationId,
            String owner,
            String repository,
            String path,
            String ref
    );
}