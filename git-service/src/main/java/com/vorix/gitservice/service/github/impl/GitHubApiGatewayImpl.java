package com.vorix.gitservice.service.github.impl;

import com.vorix.gitservice.client.GitHubClient;
import com.vorix.gitservice.dto.github.commit.GitHubCommitResponse;
import com.vorix.gitservice.dto.github.compare.GitHubCompareResponse;
import com.vorix.gitservice.dto.github.content.GitHubContentResponse;
import com.vorix.gitservice.dto.github.repository.GitHubRepositoryResponse;
import com.vorix.gitservice.service.github.GitHubApiGateway;
import com.vorix.gitservice.service.github.InstallationTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GitHubApiGatewayImpl implements GitHubApiGateway {

    private final GitHubClient gitHubClient;
    private final InstallationTokenService installationTokenService;

    @Override
    public GitHubRepositoryResponse getRepository(
            Long installationId,
            String owner,
            String repository
    ) {

        log.debug(
                "Fetching repository {}/{}",
                owner,
                repository
        );

        String accessToken =
                installationTokenService.getAccessToken(installationId);

        return gitHubClient.get(
                "/repos/%s/%s".formatted(owner, repository),
                accessToken,
                GitHubRepositoryResponse.class
        );
    }

    @Override
    public GitHubCommitResponse getCommit(
            Long installationId,
            String owner,
            String repository,
            String sha
    ) {

        log.debug(
                "Fetching commit {} from {}/{}",
                sha,
                owner,
                repository
        );

        String accessToken =
                installationTokenService.getAccessToken(installationId);

        return gitHubClient.get(
                "/repos/%s/%s/commits/%s".formatted(owner, repository, sha),
                accessToken,
                GitHubCommitResponse.class
        );
    }

    @Override
    public GitHubCompareResponse compareCommits(
            Long installationId,
            String owner,
            String repository,
            String base,
            String head
    ) {

        String accessToken = installationTokenService.getAccessToken(installationId);

        return gitHubClient.get(
                "/repos/%s/%s/compare/%s...%s".formatted(owner, repository, base, head),
                accessToken,
                GitHubCompareResponse.class
        );
    }

    @Override
    public GitHubContentResponse getFileContent(
            Long installationId,
            String owner,
            String repository,
            String path,
            String ref
    ) {

        String token = installationTokenService.getAccessToken(installationId);

        return gitHubClient.get(

                "/repos/%s/%s/contents/%s?ref=%s".formatted(owner, repository, path, ref),
                token,
                GitHubContentResponse.class
        );
    }
}