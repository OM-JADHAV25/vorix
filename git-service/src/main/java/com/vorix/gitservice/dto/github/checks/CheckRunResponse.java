package com.vorix.gitservice.dto.github.checks;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CheckRunResponse(
        Long id,
        String status,
        String conclusion,
        @JsonProperty("head_sha")
        String headSha
) {
}