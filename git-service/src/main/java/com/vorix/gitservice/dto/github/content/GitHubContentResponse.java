package com.vorix.gitservice.dto.github.content;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GitHubContentResponse(
        String name,
        String path,
        String sha,
        String content,
        String encoding,
        @JsonProperty("download_url")
        String downloadUrl
) {
}