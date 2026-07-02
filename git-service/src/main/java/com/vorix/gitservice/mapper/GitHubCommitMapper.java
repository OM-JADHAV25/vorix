package com.vorix.gitservice.mapper;

import com.vorix.gitservice.domain.model.commit.CommitDetails;
import com.vorix.gitservice.domain.model.commit.ChangedFile;
import com.vorix.gitservice.domain.model.commit.FileChangeType;
import com.vorix.gitservice.dto.github.commit.GitHubCommitResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GitHubCommitMapper {

    public CommitDetails toDomain(GitHubCommitResponse response) {

        List<ChangedFile> changedFiles = response.files()
                                                 .stream()
                                                 .map(this::toChangedFile)
                                                 .toList();

        return new CommitDetails(
                response.sha(),
                response.commit().message(),
                response.commit().author().name(),
                response.commit().author().email(),
                response.commit().author().date(),
                response.commit().committer().name(),
                response.commit().committer().email(),
                response.commit().committer().date(),
                changedFiles
        );
    }

    private ChangedFile toChangedFile(
            GitHubCommitResponse.File file
    ) {

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

        return switch (status) {
            case "added" -> FileChangeType.ADDED;
            case "removed" -> FileChangeType.REMOVED;
            case "renamed" -> FileChangeType.RENAMED;
            default -> FileChangeType.MODIFIED;
        };
    }
}