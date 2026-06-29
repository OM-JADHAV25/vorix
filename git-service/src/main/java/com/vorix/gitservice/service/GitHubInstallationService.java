package com.vorix.gitservice.service;

import com.vorix.gitservice.dto.github.GitHubInstallationPayload;

public interface GitHubInstallationService {

    void createInstallation(GitHubInstallationPayload payload);

    void suspendInstallation(Long installationId);

    void activateInstallation(Long installationId);

    void deleteInstallation(Long installationId);
}