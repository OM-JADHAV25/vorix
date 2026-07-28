package com.vorix.gitservice.exception.github;

public class RepositoryNotFoundException extends GitHubApiException {

    public RepositoryNotFoundException(String repository) {
        super("Repository not found: " + repository);
    }
}