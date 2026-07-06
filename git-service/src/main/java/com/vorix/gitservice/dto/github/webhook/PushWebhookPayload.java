package com.vorix.gitservice.dto.github.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PushWebhookPayload(
        String before,
        String after,
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
            Owner owner,
            @JsonProperty("default_branch")
            String defaultBranch
    ) {
    }

    public record Owner(
            String login
    ) {
    }
}