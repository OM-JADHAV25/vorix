package com.vorix.gitservice.service.github;

import com.vorix.gitservice.dto.github.commit.GitHubCommitResponse;
import com.vorix.gitservice.dto.github.compare.GitHubCompareResponse;
import com.vorix.gitservice.dto.github.content.GitHubContentResponse;
import com.vorix.gitservice.dto.github.repository.GitHubRepositoryResponse;

public interface GitHubApiGateway {

    GitHubRepositoryResponse getRepository(
            Long installationId,
            String owner,
            String repository
    );

    GitHubCommitResponse getCommit(
            Long installationId,
            String owner,
            String repository,
            String sha
    );

    GitHubCompareResponse compareCommits(
            Long installationId,
            String owner,
            String repository,
            String base,
            String head
    );

    GitHubContentResponse getFileContent(
            Long installationId,
            String owner,
            String repository,
            String path,
            String ref
    );
}