package com.vorix.gitservice.service;

import com.vorix.gitservice.dto.github.GitHubRepositorySyncPayload;

public interface RepositorySyncService {

    void synchronizeRepositories(GitHubRepositorySyncPayload payload);
}