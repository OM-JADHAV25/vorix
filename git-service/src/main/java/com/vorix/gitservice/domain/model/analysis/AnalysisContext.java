package com.vorix.gitservice.domain.model.analysis;

import com.vorix.gitservice.domain.model.commit.CommitComparison;
import com.vorix.gitservice.domain.model.github.RepositoryMetadata;
import com.vorix.gitservice.domain.model.repository.RepositoryFile;

import java.util.List;
import java.util.UUID;

public record AnalysisContext(
        UUID projectId,
        Long installationId,
        RepositoryMetadata repository,
        CommitComparison comparison,
        List<RepositoryFile> repositoryFiles
) {
}