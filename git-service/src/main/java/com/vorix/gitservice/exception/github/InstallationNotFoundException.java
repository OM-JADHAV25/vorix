package com.vorix.gitservice.exception.github;

public class InstallationNotFoundException extends GitHubApiException {

    public InstallationNotFoundException(Long installationId) {
        super("GitHub installation not found: " + installationId);
    }
}