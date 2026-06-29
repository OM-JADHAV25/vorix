package com.vorix.gitservice.domain.repository;

import com.vorix.gitservice.domain.model.GitHubInstallation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GitHubInstallationRepository extends JpaRepository<GitHubInstallation, UUID> {

    Optional<GitHubInstallation> findByGithubInstallationId(Long githubInstallationId);

    Optional<GitHubInstallation> findByGithubAccountId(Long githubAccountId);

    boolean existsByGithubInstallationId(Long githubInstallationId);
}