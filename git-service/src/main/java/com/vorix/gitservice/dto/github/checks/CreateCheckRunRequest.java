package com.vorix.gitservice.dto.github.checks;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateCheckRunRequest(
        String name,
        @JsonProperty("head_sha")
        String headSha,
        String status
) {
}