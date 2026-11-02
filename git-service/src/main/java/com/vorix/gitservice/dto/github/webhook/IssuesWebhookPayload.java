package com.vorix.gitservice.dto.github.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;

public record IssuesWebhookPayload(
        String action,
        Issue issue,
        Repository repository,
        Installation installation,
        Sender sender
) {

    public record Issue(
            Long id,
            Long number,
            String title,
            String body,
            String state,
            @JsonProperty("html_url")
            String htmlUrl,
            User user
    ) {}

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
