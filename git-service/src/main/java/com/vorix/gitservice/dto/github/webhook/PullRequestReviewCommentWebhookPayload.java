package com.vorix.gitservice.dto.github.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PullRequestReviewCommentWebhookPayload(
        String action,
        Comment comment,
        PullRequest pullRequest,
        Repository repository,
        Installation installation
) {

    public record Comment(
            Long id,
            String body,
            String path,
            @JsonProperty("commit_id")
            String commitId,
            @JsonProperty("original_commit_id")
            String originalCommitId
    ) {
    }

    public record PullRequest(
            Long number,
            Head head
    ) {
        public record Head(String sha) {
        }
    }

    public record Repository(
            Long id,
            String name,
            @JsonProperty("full_name")
            String fullName,
            Owner owner
    ) {
        public record Owner(String login) {
        }
    }

    public record Installation(Long id) {
    }
}
