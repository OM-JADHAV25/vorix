package com.vorix.gitservice.exception.github;

public class GitHubRateLimitExceededException extends GitHubApiException {

    public GitHubRateLimitExceededException() {
        super("GitHub API rate limit exceeded.");
    }
}