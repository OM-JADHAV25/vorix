package com.vorix.gitservice.service.impl;

import com.vorix.gitservice.domain.repository.ConnectedRepositoryRepository;
import com.vorix.gitservice.dto.github.GitHubRepositorySyncPayload;
import com.vorix.gitservice.service.RepositorySyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RepositorySyncServiceImpl implements RepositorySyncService {

    private final ConnectedRepositoryRepository repository;

    @Override
    public void synchronizeRepositories(GitHubRepositorySyncPayload payload) {

        log.info(
                "Synchronizing repositories for installation {}",
                payload.installation().id()
        );

    }
}