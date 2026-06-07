package com.vorix.projectservice.repository;

import com.vorix.projectservice.entity.Project;
import com.vorix.projectservice.entity.enums.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    boolean existsByGithubUrl(String githubUrl);

    Optional<Project> findByGithubUrl(String githubUrl);

    Page<Project> findByStatusNot(ProjectStatus status, Pageable pageable);

    Optional<Project> findByIdAndStatusNot(Long id, ProjectStatus status);

    Page<Project> findByStatusNotAndProjectNameContainingIgnoreCase(
            ProjectStatus excludedStatus,
            String projectName,
            Pageable pageable
    );

    Page<Project> findByStatusAndProjectNameContainingIgnoreCase(
            ProjectStatus status,
            String projectName,
            Pageable pageable
    );
}