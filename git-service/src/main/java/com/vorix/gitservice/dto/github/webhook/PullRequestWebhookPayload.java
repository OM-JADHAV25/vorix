package com.vorix.gitservice.dto.github.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PullRequestWebhookPayload(
        String action,
        Long number,
        PullRequest pullRequest,
        Repository repository,
        Installation installation
) {

    public record Installation(
            Long id
    ) {
    }

    public record Repository(
            Long id,
            String name,
            @JsonProperty("full_name")
            String fullName,
            Owner owner
    ) {
    }

    public record Owner(
            String login
    ) {
    }

    public record PullRequest(
            Long id,
            String title,
            String state,
            Head head,
            Base base
    ) {
    }

    public record Head(
            String ref,
            String sha
    ) {
    }

    public record Base(
            String ref,
            String sha
    ) {
    }
}