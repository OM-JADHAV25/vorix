package com.vorix.projectservice.service.impl;

import com.vorix.projectservice.dto.request.CreateProjectRequest;
import com.vorix.projectservice.dto.response.ProjectResponse;
import com.vorix.projectservice.dto.request.UpdateProjectRequest;
import com.vorix.projectservice.entity.Project;
import com.vorix.projectservice.entity.enums.ProjectStatus;
import com.vorix.projectservice.exception.DuplicateResourceException;
import com.vorix.projectservice.exception.ResourceNotFoundException;
import com.vorix.projectservice.repository.ProjectRepository;
import com.vorix.projectservice.service.ProjectService;
import com.vorix.projectservice.mapper.ProjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    private final ProjectMapper projectMapper;

    @Override
    @Transactional
    public ProjectResponse createProject(CreateProjectRequest request, UUID ownerId) {

        log.info("Creating project with name: {}", request.projectName());

        String githubUrl = request.githubUrl();

        if (githubUrl != null && !githubUrl.isBlank() && projectRepository.existsByGithubUrl(githubUrl)) {

            log.warn("Duplicate github URL found: {}", githubUrl);

            throw new DuplicateResourceException("Project with this GitHub URL already exists");

        }

        Project project = Project.builder()
                                 .ownerId(ownerId)
                                 .projectName(request.projectName())
                                 .description(request.description())
                                 .githubUrl(githubUrl)
                                 .status(ProjectStatus.ACTIVE)
                                 .build();

        Project savedProject = projectRepository.save(project);

        log.info("Project created successfully with id: {}", savedProject.getId());

        return projectMapper.toResponse(savedProject);
    }


    @Override
    public ProjectResponse getProjectById(Long id, UUID ownerId) {

        log.info("Fetching project with id: {}", id);

        Project project = projectRepository
                        .findByIdAndOwnerIdAndStatusNot(
                                id,
                                ownerId,
                                ProjectStatus.DELETED
                        )
                        .orElseThrow(() -> {

                            log.warn("Project not found with id: {}", id);

                            return new ResourceNotFoundException("Project not found");
                        });

        return projectMapper.toResponse(project);
    }

    @Override
    public Page<ProjectResponse> getAllProjects(
            UUID ownerId,
            String search,
            ProjectStatus status,
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        log.info(
                "Fetching projects page={}, size={}, sortBy={}, direction={}, search: {}, status: {}",
                page,
                size,
                sortBy,
                direction,
                search,
                status
        );

        Sort sort = direction.equalsIgnoreCase("desc")
                        ? Sort.by(sortBy).descending()
                        : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        String searchTerm =
                search == null
                        ? ""
                        : search;

        Page<Project> projects;

        if (status != null) {
            projects = projectRepository
                    .findByOwnerIdAndStatusAndProjectNameContainingIgnoreCase(
                            ownerId,
                            status,
                            searchTerm,
                            pageable
                    );

        } else {
            projects = projectRepository
                    .findByOwnerIdAndStatusNotAndProjectNameContainingIgnoreCase(
                            ownerId,
                            ProjectStatus.DELETED,
                            searchTerm,
                            pageable
                    );
        }


        return projects.map(projectMapper::toResponse);
    }

    @Override
    @Transactional
    public ProjectResponse updateProject(Long id, UpdateProjectRequest request, UUID ownerId) {

        log.info("Updating project with id: {}", id);

        Project project = projectRepository
                        .findByIdAndOwnerIdAndStatusNot(
                                id,
                                ownerId,
                                ProjectStatus.DELETED
                        )
                        .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        String githubUrl = request.githubUrl();

        if (
                githubUrl != null
                        && !githubUrl.isBlank()
                        && !githubUrl.equals(
                        project.getGithubUrl()
                )
                        && projectRepository.existsByGithubUrl(
                        githubUrl
                )
        ) {
            throw new DuplicateResourceException("Project already exists");
        }

        project.setProjectName(request.projectName());

        project.setDescription(request.description());

        project.setGithubUrl(githubUrl);

        projectRepository.save(project);

        log.info("Project updated successfully with id: {}", project.getId());

        return projectMapper.toResponse(project);
    }

    @Override
    @Transactional
    public void softDeleteProject(Long id, UUID ownerId) {

        log.info("Soft deleting project with id: {}", id);

        Project project = projectRepository
                .findByIdAndOwnerIdAndStatusNot(
                        id,
                        ownerId,
                        ProjectStatus.DELETED
                )
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        project.setStatus(ProjectStatus.DELETED);

        project.setDeletedAt(LocalDateTime.now());

        projectRepository.save(project);

        log.info("Project soft deleted with id: {}", id);

    }

    @Override
    @Transactional
    public void hardDeleteProject(Long id, UUID ownerId) {

        log.warn("Hard deleting project with id: {}", id);

        Project project = projectRepository
                        .findByIdAndOwnerId(
                                id,
                                ownerId
                        )
                        .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        projectRepository.delete(project);

        log.warn("Project permanently deleted with id: {}", id);
    }

}