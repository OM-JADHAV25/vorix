package com.vorix.gitservice.dto.github;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;

public record InstallationRepositoriesPayload(

        Long installationId,
        List<RepositoryPayload> repositoriesAdded,
        List<RepositoryPayload> repositoriesRemoved
) {

    public record RepositoryPayload(

            @JsonProperty("id")
            Long repositoryId,

            @JsonProperty("name")
            String repositoryName,

            @JsonProperty("full_name")
            String fullName,

            @JsonProperty("default_branch")
            String defaultBranch,

            @JsonProperty("private")
            Boolean isPrivate,

            @JsonProperty("visibility")
            String visibility,

            @JsonProperty("archived")
            Boolean archived,

            @JsonProperty("disabled")
            Boolean disabled,

            @JsonProperty("html_url")
            String htmlUrl,

            @JsonProperty("clone_url")
            String cloneUrl,

            @JsonProperty("language")
            String language,

            @JsonProperty("created_at")
            Instant createdAt,

            @JsonProperty("updated_at")
            Instant updatedAt
    ) {
    }
}