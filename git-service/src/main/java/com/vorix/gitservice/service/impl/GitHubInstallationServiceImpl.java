package com.vorix.gitservice.service.impl;

import com.vorix.gitservice.domain.model.GitHubInstallation;
import com.vorix.gitservice.domain.repository.GitHubInstallationRepository;
import com.vorix.gitservice.dto.github.GitHubInstallationPayload;
import com.vorix.gitservice.service.GitHubInstallationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class GitHubInstallationServiceImpl implements GitHubInstallationService {

    private final GitHubInstallationRepository repository;

    @Override
    public void createInstallation(GitHubInstallationPayload payload) {

        repository.findByGithubInstallationId(payload.installationId()).ifPresentOrElse(

                        installation -> {
                            log.info("GitHub installation {} already exists. Reactivating.", payload.installationId());

                            installation.setGithubAccountId(payload.accountId());
                            installation.setAccountLogin(payload.accountLogin());
                            installation.setAccountType(payload.accountType());
                            installation.setTargetType(payload.targetType());
                            installation.setActive(true);
                            installation.setSuspendedAt(null);
                            installation.setUpdatedAt(LocalDateTime.now());

                            repository.save(installation);
                        },

                        () -> {
                            GitHubInstallation installation = GitHubInstallation.builder()
                                            .githubInstallationId(payload.installationId())
                                            .githubAccountId(payload.accountId())
                                            .accountLogin(payload.accountLogin())
                                            .accountType(payload.accountType())
                                            .targetType(payload.targetType())
                                            .active(true)
                                            .installedAt(LocalDateTime.now())
                                            .createdAt(LocalDateTime.now())
                                            .updatedAt(LocalDateTime.now())
                                            .build();

                            repository.save(installation);

                            log.info("GitHub installation {} created successfully.", payload.installationId());
                        }
                );
    }

    @Override
    public void suspendInstallation(Long installationId) {

        GitHubInstallation installation = getInstallation(installationId);

        installation.setActive(false);
        installation.setSuspendedAt(LocalDateTime.now());
        installation.setUpdatedAt(LocalDateTime.now());

        repository.save(installation);

        log.info("GitHub installation {} suspended.", installationId);
    }


    @Override
    public void activateInstallation(Long installationId) {

        GitHubInstallation installation = getInstallation(installationId);

        installation.setActive(true);
        installation.setSuspendedAt(null);
        installation.setUpdatedAt(LocalDateTime.now());

        repository.save(installation);

        log.info("GitHub installation {} activated.", installationId);
    }

    @Override
    public void deleteInstallation(Long installationId) {

        GitHubInstallation installation = getInstallation(installationId);

        repository.delete(installation);

        log.info("GitHub installation {} deleted.", installationId);
    }

    private GitHubInstallation getInstallation(Long installationId) {

        return repository.findByGithubInstallationId(installationId)
                .orElseThrow(() ->
                        new EntityNotFoundException("GitHub installation not found: " + installationId)
                );
    }
}