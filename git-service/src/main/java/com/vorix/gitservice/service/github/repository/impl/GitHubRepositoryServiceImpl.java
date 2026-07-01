package com.vorix.gitservice.service.github.repository.impl;

import com.vorix.gitservice.client.GitHubClient;
import com.vorix.gitservice.domain.model.github.RepositoryMetadata;
import com.vorix.gitservice.dto.github.repository.GitHubRepositoryResponse;
import com.vorix.gitservice.mapper.GitHubRepositoryMapper;
import com.vorix.gitservice.service.github.InstallationTokenService;
import com.vorix.gitservice.service.github.repository.GitHubRepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GitHubRepositoryServiceImpl implements GitHubRepositoryService {

    private final InstallationTokenService installationTokenService;
    private final GitHubRepositoryMapper gitHubRepositoryMapper;

    private final GitHubClient gitHubClient;

    @Override
    public RepositoryMetadata getRepository(
            Long installationId,
            String owner,
            String repository
    ) {

        String token = installationTokenService.getAccessToken(installationId);

        GitHubRepositoryResponse response = gitHubClient.get(
                        "/repos/%s/%s".formatted(owner, repository),
                        token,
                        GitHubRepositoryResponse.class
                );

        return gitHubRepositoryMapper.toDomain(response);
    }
}