package com.vorix.gitservice.service;

import com.vorix.gitservice.domain.model.ConnectedRepository;
import com.vorix.gitservice.dto.github.InstallationRepositoriesPayload;

public interface ConnectedRepositoryService {

    ConnectedRepository getConnectedRepository(Long installationId, Long githubRepositoryId);

    void repositoriesAdded(InstallationRepositoriesPayload payload);

    void repositoriesRemoved(InstallationRepositoriesPayload payload);
}
