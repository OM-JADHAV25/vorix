package com.vorix.gitservice.dto.github.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CheckRunWebhookPayload(
        String action,
        CheckRun checkRun,
        Repository repository,
        Installation installation
) {

    public record CheckRun(
            Long id,
            String status,
            String conclusion,
            @JsonProperty("head_sha")
            String headSha,
            String name
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

    public record Installation(
            Long id
    ) {
    }
}