package com.vorix.gitservice.service.impl;

import com.vorix.gitservice.domain.model.ConnectedRepository;
import com.vorix.gitservice.domain.model.GithubConnection;
import com.vorix.gitservice.domain.repository.ConnectedRepositoryRepository;
import com.vorix.gitservice.domain.repository.GithubConnectionRepository;
import com.vorix.gitservice.dto.github.InstallationRepositoriesPayload;
import com.vorix.gitservice.service.ConnectedRepositoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ConnectedRepositoryServiceImpl implements ConnectedRepositoryService {

    private final ConnectedRepositoryRepository connectedRepositoryRepository;
    private final GithubConnectionRepository githubConnectionRepository;

    @Override
    public void repositoriesAdded(InstallationRepositoriesPayload payload) {

        GithubConnection githubConnection = githubConnectionRepository.findByInstallationId(payload.installationId())
                        .orElseThrow(() -> new IllegalStateException("GitHub connection not found for installation: " + payload.installationId()));

        List<ConnectedRepository> repositoriesToSave = new ArrayList<>();

        for (InstallationRepositoriesPayload.RepositoryPayload repository : payload.repositoriesAdded()) {

            if (connectedRepositoryRepository.existsByGithubRepositoryId(repository.repositoryId())) {

                log.debug("Repository {} already exists. Skipping.", repository.fullName());
                continue;
            }

            String owner = repository.fullName().split("/")[0];

            ConnectedRepository connectedRepository =
                    ConnectedRepository.builder()
                            .projectId(UUID.randomUUID())      // Temporary
                            .githubConnection(githubConnection)
                            .githubRepositoryId(repository.repositoryId())
                            .owner(owner)
                            .repositoryName(repository.repositoryName())
                            .fullName(repository.fullName())
                            .defaultBranch(null)
                            .webhookId(null)
                            .isActive(true)
                            .build();

            repositoriesToSave.add(connectedRepository);
        }

        if (!repositoriesToSave.isEmpty()) {

            connectedRepositoryRepository.saveAll(repositoriesToSave);

            log.info("Added {} repositories for installation {}", repositoriesToSave.size(), payload.installationId());
        }
    }

    @Override
    public void repositoriesRemoved(InstallationRepositoriesPayload payload) {

        for (InstallationRepositoriesPayload.RepositoryPayload repository :
                payload.repositoriesRemoved()) {

            connectedRepositoryRepository.deleteByGithubRepositoryId(repository.repositoryId());

            log.info("Removed repository {}", repository.fullName());
        }
    }
}
