package com.vorix.gitservice.service;

import com.vorix.gitservice.dto.github.InstallationRepositoriesPayload;

public interface ConnectedRepositoryService {

    void repositoriesAdded(InstallationRepositoriesPayload payload);

    void repositoriesRemoved(InstallationRepositoriesPayload payload);
}
