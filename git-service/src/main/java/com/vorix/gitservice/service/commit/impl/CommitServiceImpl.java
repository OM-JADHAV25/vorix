package com.vorix.gitservice.service.commit.impl;

import com.vorix.gitservice.domain.model.commit.CommitDetails;
import com.vorix.gitservice.domain.model.commit.CommitComparison;
import com.vorix.gitservice.dto.github.commit.GitHubCommitResponse;
import com.vorix.gitservice.dto.github.compare.GitHubCompareResponse;
import com.vorix.gitservice.mapper.GitHubCommitMapper;
import com.vorix.gitservice.mapper.GitHubCompareMapper;
import com.vorix.gitservice.service.commit.CommitService;
import com.vorix.gitservice.service.github.GitHubApiGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommitServiceImpl implements CommitService {

    private final GitHubApiGateway gitHubApiGateway;
    private final GitHubCommitMapper gitHubCommitMapper;
    private final GitHubCompareMapper gitHubCompareMapper;

    @Override
    public CommitDetails getCommit(
            Long installationId,
            String owner,
            String repository,
            String sha
    ) {

        GitHubCommitResponse response = gitHubApiGateway.getCommit(installationId, owner, repository, sha);

        return gitHubCommitMapper.toDomain(response);
    }

    @Override
    public CommitComparison compareCommits(
            Long installationId,
            String owner,
            String repository,
            String base,
            String head
    ) {

        GitHubCompareResponse response = gitHubApiGateway.compareCommits(installationId, owner, repository, base, head);

        return gitHubCompareMapper.toDomain(response);
    }
}