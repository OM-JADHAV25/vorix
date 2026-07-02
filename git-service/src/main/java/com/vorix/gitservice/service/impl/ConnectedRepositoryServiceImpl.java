package com.vorix.gitservice.service.impl;

import com.vorix.gitservice.domain.model.ConnectedRepository;
import com.vorix.gitservice.domain.model.github.RepositoryMetadata;
import com.vorix.gitservice.domain.repository.ConnectedRepositoryRepository;
import com.vorix.gitservice.dto.github.InstallationRepositoriesPayload;
import com.vorix.gitservice.service.ConnectedRepositoryService;
import com.vorix.gitservice.domain.model.GitHubInstallation;
import com.vorix.gitservice.domain.repository.GitHubInstallationRepository;
import com.vorix.gitservice.service.github.repository.RepositoryService;
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
    private final RepositoryService repositoryService;

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

            RepositoryMetadata metadata = repositoryService.getRepository(
                            payload.installationId(),
                            owner,
                            repository.repositoryName()
            );

            ConnectedRepository connectedRepository = ConnectedRepository.builder()
                    .projectId(UUID.randomUUID())     // Temporary until Project Service
                    .installation(installation)
                    .githubRepositoryId(metadata.providerRepositoryId())
                    .owner(metadata.owner())
                    .repositoryName(metadata.repositoryName())
                    .fullName(metadata.fullName())
                    .isPrivate(metadata.isPrivate())
                    .visibility(metadata.visibility())
                    .defaultBranch(metadata.defaultBranch())
                    .cloneUrl(metadata.cloneUrl())
                    .htmlUrl(metadata.htmlUrl())
                    .primaryLanguage(metadata.language())
                    .archived(metadata.archived())
                    .disabled(metadata.disabled())
                    .githubCreatedAt(metadata.createdAt())
                    .githubUpdatedAt(metadata.updatedAt())
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
