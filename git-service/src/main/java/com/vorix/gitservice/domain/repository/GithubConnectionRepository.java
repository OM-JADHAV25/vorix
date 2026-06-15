package com.vorix.gitservice.domain.repository;

import com.vorix.gitservice.domain.model.GithubConnection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GithubConnectionRepository extends JpaRepository<GithubConnection, UUID> {

    Optional<GithubConnection> findByUserId(UUID userId);

    Optional<GithubConnection> findByInstallationId(Long installationId);

    Optional<GithubConnection> findByGithubUserId(Long githubUserId);
}