package com.vorix.gitservice.dto.github;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record GitHubRepositorySyncPayload(

        String action,
        Installation installation,

        @JsonProperty("repositories_added")
        List<GitHubRepositoryPayload> repositoriesAdded,

        @JsonProperty("repositories_removed")
        List<GitHubRepositoryPayload> repositoriesRemoved
) {

    public record Installation(Long id) {
    }
}