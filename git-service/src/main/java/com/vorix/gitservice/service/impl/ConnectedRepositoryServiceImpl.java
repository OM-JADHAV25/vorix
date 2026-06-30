package com.vorix.gitservice.service.impl;

import com.vorix.gitservice.domain.model.ConnectedRepository;
import com.vorix.gitservice.domain.repository.ConnectedRepositoryRepository;
import com.vorix.gitservice.dto.github.InstallationRepositoriesPayload;
import com.vorix.gitservice.service.ConnectedRepositoryService;
import com.vorix.gitservice.domain.model.GitHubInstallation;
import com.vorix.gitservice.domain.repository.GitHubInstallationRepository;
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
    private final GitHubInstallationRepository gitHubInstallationRepository;

    @Override
    public void repositoriesAdded(InstallationRepositoriesPayload payload) {

        GitHubInstallation installation = gitHubInstallationRepository.findByGithubInstallationId(payload.installationId())
                        .orElseThrow(() -> new IllegalStateException("GitHub installation not found: " + payload.installationId()));

        List<ConnectedRepository> repositoriesToSave = new ArrayList<>();

        for (InstallationRepositoriesPayload.RepositoryPayload repository : payload.repositoriesAdded()) {

            if (connectedRepositoryRepository.existsByGithubRepositoryId(repository.repositoryId())) {

                log.debug("Repository {} already exists. Skipping.", repository.fullName());
                continue;
            }

            String owner = repository.fullName().split("/")[0];

            ConnectedRepository connectedRepository = ConnectedRepository.builder()
                            .projectId(UUID.randomUUID())      // Temporary
                            .installation(installation)
                            .githubRepositoryId(repository.repositoryId())
                            .owner(owner)
                            .repositoryName(repository.repositoryName())
                            .fullName(repository.fullName())
                            .isPrivate(repository.isPrivate())
                            .defaultBranch(null)
                            .visibility(null)
                            .cloneUrl(null)
                            .htmlUrl(null)
                            .primaryLanguage(null)
                            .archived(false)
                            .disabled(false)
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
