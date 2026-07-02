package com.vorix.gitservice.dto.github.compare;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record GitHubCompareResponse(
        @JsonProperty("base_commit")
        BaseCommit baseCommit,
        @JsonProperty("merge_base_commit")
        BaseCommit mergeBaseCommit,
        List<Commit> commits,
        List<File> files
) {

    public record BaseCommit(
            String sha
    ) {
    }

    public record Commit(
            String sha,
            CommitInfo commit
    ) {
    }

    public record CommitInfo(
            String message
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