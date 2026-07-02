package com.vorix.gitservice.mapper;

import com.vorix.gitservice.domain.model.commit.ChangedFile;
import com.vorix.gitservice.domain.model.commit.CommitDetails;
import com.vorix.gitservice.domain.model.commit.CommitComparison;
import com.vorix.gitservice.domain.model.commit.FileChangeType;
import com.vorix.gitservice.dto.github.compare.GitHubCompareResponse;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class GitHubCompareMapper {

    public CommitComparison toDomain(GitHubCompareResponse response) {

        List<CommitDetails> commits = response.commits()
                .stream()
                .map(this::toCommit)
                .toList();

        List<ChangedFile> changedFiles = response.files()
                .stream()
                .map(this::toChangedFile)
                .toList();

        return new CommitComparison(
                response.baseCommit().sha(),
                response.mergeBaseCommit().sha(),
                commits,
                changedFiles
        );
    }

    private CommitDetails toCommit(GitHubCompareResponse.Commit commit) {

        return new CommitDetails(
                commit.sha(),
                commit.commit().message(),
                null,
                null,
                Instant.now(),
                null,
                null,
                Instant.now(),
                List.of()
        );
    }

    private ChangedFile toChangedFile(GitHubCompareResponse.File file) {

        return new ChangedFile(
                file.filename(),
                file.previousFilename(),
                map(file.status()),
                file.additions(),
                file.deletions(),
                file.changes(),
                file.patch()
        );
    }

    private FileChangeType map(String status) {

        if (status == null) {
            return FileChangeType.MODIFIED;
        }

        return switch (status.toLowerCase()) {
            case "added" -> FileChangeType.ADDED;
            case "removed" -> FileChangeType.REMOVED;
            case "renamed" -> FileChangeType.RENAMED;
            default -> FileChangeType.MODIFIED;
        };
    }
}