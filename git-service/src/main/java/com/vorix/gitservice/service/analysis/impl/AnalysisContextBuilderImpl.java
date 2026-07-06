package com.vorix.gitservice.service.analysis.impl;

import com.vorix.gitservice.domain.model.analysis.AnalysisContext;
import com.vorix.gitservice.domain.model.commit.CommitComparison;
import com.vorix.gitservice.domain.model.commit.ChangedFile;
import com.vorix.gitservice.domain.model.github.RepositoryMetadata;
import com.vorix.gitservice.domain.model.repository.RepositoryFile;
import com.vorix.gitservice.service.analysis.AnalysisContextBuilder;
import com.vorix.gitservice.service.analysis.filter.AnalysisFileFilter;
import com.vorix.gitservice.service.commit.CommitService;
import com.vorix.gitservice.service.repository.RepositoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalysisContextBuilderImpl implements AnalysisContextBuilder {

    private final RepositoryService repositoryService;
    private final CommitService commitService;
    private final AnalysisFileFilter analysisFileFilter;

    @Override
    public AnalysisContext build(
            UUID projectId,
            Long installationId,
            String owner,
            String repository,
            String base,
            String head
    ) {

        log.info("Building analysis context for repository {}/{}", owner, repository);

        RepositoryMetadata repositoryMetadata = repositoryService.getRepository(installationId, owner, repository);

        CommitComparison comparison = commitService.compareCommits(installationId, owner, repository, base, head);

        List<RepositoryFile> repositoryFiles = comparison.changedFiles()
                        .stream()
                        .filter(analysisFileFilter::shouldAnalyze)
                        .map(file -> repositoryService.getRepositoryFile(
                                installationId,
                                owner,
                                repository,
                                file.path(),
                                head
                        ))
                        .toList();

        return new AnalysisContext(
                projectId,
                installationId,
                repositoryMetadata,
                comparison,
                repositoryFiles
        );
    }
}