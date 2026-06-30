package com.vorix.gitservice.domain.repository;

import com.vorix.gitservice.domain.model.ConnectedRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConnectedRepositoryRepository extends JpaRepository<ConnectedRepository, UUID> {

    Optional<ConnectedRepository> findByGithubRepositoryId(Long githubRepositoryId);

    List<ConnectedRepository> findByProjectId(UUID projectId);

    List<ConnectedRepository> findByInstallation_GithubInstallationId(Long installationId);

    boolean existsByGithubRepositoryId(Long githubRepositoryId);

    void deleteByGithubRepositoryId(Long githubRepositoryId);
}
