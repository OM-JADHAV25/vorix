package com.vorix.projectservice.repository;

import com.vorix.projectservice.entity.Project;
import com.vorix.projectservice.entity.enums.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    boolean existsByGithubUrl(String githubUrl);

    Optional<Project> findByGithubUrl(String githubUrl);

    List<Project> findByOwnerIdAndStatusNot(UUID ownerId, ProjectStatus status);

    Optional<Project> findByIdAndOwnerIdAndStatusNot(
            Long id,
            UUID ownerId,
            ProjectStatus status
    );

    Optional<Project> findByIdAndOwnerId(Long id, UUID ownerId);

    Page<Project> findByOwnerIdAndStatusAndProjectNameContainingIgnoreCase(
            UUID ownerId,
            ProjectStatus status,
            String search,
            Pageable pageable
    );

    Page<Project> findByOwnerIdAndStatusNotAndProjectNameContainingIgnoreCase(
            UUID ownerId,
            ProjectStatus status,
            String search,
            Pageable pageable
    );
}