package com.vorix.gitservice.dto.github;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record GitHubRepositoryPayload(

        Long id,

        String name,

        @JsonProperty("full_name")
        String fullName,

        @JsonProperty("private")
        Boolean privateRepository,

        @JsonProperty("default_branch")
        String defaultBranch,

        Boolean archived,

        Boolean disabled,

        String visibility,

        @JsonProperty("clone_url")
        String cloneUrl,

        @JsonProperty("html_url")
        String htmlUrl,

        String language,

        @JsonProperty("created_at")
        Instant createdAt,

        @JsonProperty("updated_at")
        Instant updatedAt,

        GitHubOwnerPayload owner
) {
}