package com.vorix.gitservice.service;

import com.vorix.gitservice.dto.github.GitHubInstallationPayload;

public interface RepositorySyncService {

    void synchronizeRepositories(GitHubInstallationPayload payload);
}
