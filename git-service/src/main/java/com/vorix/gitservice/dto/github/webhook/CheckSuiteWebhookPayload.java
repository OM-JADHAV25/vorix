package com.vorix.gitservice.dto.github.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CheckSuiteWebhookPayload(
        String action,
        CheckSuite checkSuite,
        Repository repository,
        Installation installation

) {

    public record CheckSuite(
            Long id,
            String status,
            String conclusion,
            @JsonProperty("head_sha")
            String headSha
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