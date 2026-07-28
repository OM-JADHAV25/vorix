package com.vorix.gitservice.dto.github.checks;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateCheckRunRequest(
        String status,
        String conclusion,
        @JsonProperty("completed_at")
        String completedAt,
        Output output
) {

    public record Output(
            String title,
            String summary
    ) {
    }
}