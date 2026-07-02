package com.vorix.gitservice.dto.github.commit;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;

public record GitHubCommitResponse(
        String sha,
        Commit commit,
        List<File> files
) {

    public record Commit(
            String message,
            Author author,
            Author committer
    ) {
    }

    public record Author(
            String name,
            String email,
            Instant date
    ) {
    }

    public record File(
            String filename,
            @JsonProperty("previous_filename")
            String previousFilename,
            String status,
            int additions,
            int deletions,
            int changes,
            String patch
    ) {
    }
}