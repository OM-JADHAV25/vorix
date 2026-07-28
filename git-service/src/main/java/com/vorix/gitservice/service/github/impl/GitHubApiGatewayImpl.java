package com.vorix.gitservice.service.github.impl;

import com.vorix.gitservice.client.GitHubClient;
import com.vorix.gitservice.dto.github.checks.CheckRunResponse;
import com.vorix.gitservice.dto.github.checks.CreateCheckRunRequest;
import com.vorix.gitservice.dto.github.checks.UpdateCheckRunRequest;
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

        String accessToken = getAccessToken(installationId);

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

        String accessToken = getAccessToken(installationId);

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

        String accessToken = getAccessToken(installationId);

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

        String token = getAccessToken(installationId);

        return gitHubClient.get(

                "/repos/%s/%s/contents/%s?ref=%s".formatted(owner, repository, path, ref),
                token,
                GitHubContentResponse.class
        );
    }

    @Override
    public CheckRunResponse createCheckRun(
            Long installationId,
            String owner,
            String repository,
            CreateCheckRunRequest request
    ) {

        log.info(
                "Creating GitHub Check Run for repository {}/{}",
                owner,
                repository
        );

        String accessToken = getAccessToken(installationId);

        return gitHubClient.post(
                "/repos/%s/%s/check-runs".formatted(owner, repository),
                accessToken,
                request,
                CheckRunResponse.class
        );
    }

    @Override
    public CheckRunResponse updateCheckRun(
            Long installationId,
            String owner,
            String repository,
            Long checkRunId,
            UpdateCheckRunRequest request
    ) {

        log.info(
                "Updating GitHub Check Run {} for repository {}/{}",
                checkRunId,
                owner,
                repository
        );

        String accessToken = getAccessToken(installationId);

        return gitHubClient.post(
                "/repos/%s/%s/check-runs/%d".formatted(
                        owner,
                        repository,
                        checkRunId
                ),
                accessToken,
                request,
                CheckRunResponse.class
        );
    }

    private String getAccessToken(Long installationId) {

        return installationTokenService.getAccessToken(installationId);
    }
}