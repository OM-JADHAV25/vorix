package com.vorix.gitservice.service.github.repository.impl;

import com.vorix.gitservice.client.GitHubClient;
import com.vorix.gitservice.domain.model.github.RepositoryMetadata;
import com.vorix.gitservice.domain.model.repository.RepositoryFile;
import com.vorix.gitservice.dto.github.content.GitHubContentResponse;
import com.vorix.gitservice.dto.github.repository.GitHubRepositoryResponse;
import com.vorix.gitservice.mapper.GitHubContentMapper;
import com.vorix.gitservice.mapper.GitHubRepositoryMapper;
import com.vorix.gitservice.service.github.GitHubApiGateway;
import com.vorix.gitservice.service.github.repository.RepositoryService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RepositoryServiceImpl implements RepositoryService {

    private final GitHubRepositoryMapper gitHubRepositoryMapper;
    private final GitHubApiGateway gitHubApiGateway;
    private final GitHubContentMapper gitHubContentMapper;

    @Override
    public RepositoryMetadata getRepository(
            Long installationId,
            String owner,
            String repository
    ) {

        GitHubRepositoryResponse response = gitHubApiGateway.getRepository(installationId, owner, repository);

        return gitHubRepositoryMapper.toDomain(response);
    }

    @Override
    public RepositoryFile getFileContent(
            Long installationId,
            String owner,
            String repository,
            String path,
            String ref
    ) {

        log.debug(
                "Fetching repository file. Installation={}, Repository={}/{}, Path={}, Ref={}",
                installationId,
                owner,
                repository,
                path,
                ref
        );

        GitHubContentResponse response = gitHubApiGateway.getFileContent(installationId, owner, repository, path, ref);

        return gitHubContentMapper.toDomain(response);
    }
}