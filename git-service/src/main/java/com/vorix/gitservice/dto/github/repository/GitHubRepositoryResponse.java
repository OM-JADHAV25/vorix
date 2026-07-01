package com.vorix.gitservice.dto.github.repository;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record GitHubRepositoryResponse(

        Long id,

        String name,

        @JsonProperty("full_name")
        String fullName,

        Owner owner,

        @JsonProperty("private")
        boolean isPrivate,

        String visibility,

        boolean archived,

        boolean disabled,

        @JsonProperty("default_branch")
        String defaultBranch,

        @JsonProperty("clone_url")
        String cloneUrl,

        @JsonProperty("html_url")
        String htmlUrl,

        String language,

        @JsonProperty("created_at")
        Instant createdAt,

        @JsonProperty("updated_at")
        Instant updatedAt
) {

    public record Owner(
            String login
    ) {
    }
}