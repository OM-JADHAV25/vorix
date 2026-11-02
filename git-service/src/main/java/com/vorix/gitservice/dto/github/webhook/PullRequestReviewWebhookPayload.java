package com.vorix.gitservice.dto.github.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PullRequestReviewWebhookPayload(
        String action,
        Review review,
        PullRequest pullRequest,
        Repository repository,
        Installation installation
) {

    public record Review(
            Long id,
            String state,
            String body
    ) {
    }

    public record PullRequest(
            Long number,
            Head head
    ) {

        public record Head(
                String sha
        ) {
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

    public record Installation(
            Long id
    ) {
    }
}