package com.vorix.gitservice.dto.github.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;

public record IssueCommentWebhookPayload(
        String action,
        Comment comment,
        Issue issue,
        Repository repository,
        Installation installation,
        Sender sender
) {

    public record Comment(
            Long id,
            String body,
            @JsonProperty("html_url")
            String htmlUrl,
            User user
    ) {}

    public record Issue(
            Long id,
            Long number,
            String title,
            String state,
            @JsonProperty("pull_request")
            PullRequest pullRequest
    ) {
        public record PullRequest(
                @JsonProperty("html_url")
                String htmlUrl
        ) {}
    }

    public record Repository(
            Long id,
            String name,
            @JsonProperty("full_name")
            String fullName,
            Owner owner
    ) {
        public record Owner(String login) {}
    }

    public record Installation(Long id) {}

    public record Sender(String login) {}

    public record User(String login) {}
}
