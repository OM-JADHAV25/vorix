package com.vorix.gitservice.service.impl;

import com.vorix.gitservice.domain.repository.ConnectedRepositoryRepository;
import com.vorix.gitservice.domain.repository.GitHubInstallationRepository;
import com.vorix.gitservice.service.RepositorySyncService;

public class RepositorySyncServiceImpl implements RepositorySyncService {

    private final ConnectedRepositoryRepository repository;
    private final GitHubInstallationRepository installationRepository;

}
