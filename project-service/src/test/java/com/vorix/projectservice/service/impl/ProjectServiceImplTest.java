package com.vorix.projectservice.service.impl;

import com.vorix.projectservice.dto.request.CreateProjectRequest;
import com.vorix.projectservice.dto.response.ProjectResponse;
import com.vorix.projectservice.entity.Project;
import com.vorix.projectservice.entity.enums.ProjectStatus;
import com.vorix.projectservice.exception.DuplicateResourceException;
import com.vorix.projectservice.exception.ResourceNotFoundException;
import com.vorix.projectservice.mapper.ProjectMapper;
import com.vorix.projectservice.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;
import java.util.UUID;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;

    private final ProjectMapper projectMapper = new ProjectMapper();

    private ProjectServiceImpl projectService;

    private final UUID ownerId = UUID.randomUUID();

    @BeforeEach
    void setUp() {

        projectService = new ProjectServiceImpl(
                        projectRepository,
                        projectMapper
        );
    }

    @Test
    void shouldCreateProjectSuccessfully() {

        CreateProjectRequest request = new CreateProjectRequest(
                        "Vorix Backend",
                        "AI backend",
                        "https://github.com/test"
        );

        Project project = Project.builder()
                                 .ownerId(ownerId)
                                 .projectName(request.projectName())
                                 .description(request.description())
                                 .githubUrl(request.githubUrl())
                                 .status(ProjectStatus.ACTIVE)
                                 .build();

        when(projectRepository.existsByGithubUrl(request.githubUrl())).thenReturn(false);

        when(projectRepository.save(any(Project.class))).thenReturn(project);

        ProjectResponse response = projectService.createProject(request, ownerId);

        assertNotNull(response);

        assertEquals("Vorix Backend", response.projectName());

        assertEquals(ProjectStatus.ACTIVE, response.status());

        assertEquals("https://github.com/test", response.githubUrl());

        verify(projectRepository, times(1)).existsByGithubUrl(request.githubUrl());

        verify(projectRepository, times(1)).save(any(Project.class));
    }


    @Test
    void shouldThrowExceptionWhenGithubUrlAlreadyExists() {

        CreateProjectRequest request = new CreateProjectRequest(
                        "Vorix Backend",
                        "AI backend",
                        "https://github.com/test"
        );

        when(projectRepository.existsByGithubUrl(request.githubUrl())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> projectService.createProject(request, ownerId));

        verify(projectRepository, never()).save(any());
    }


    @Test
    void shouldGetProjectByIdSuccessfully() {

        Project project = Project.builder()
                        .projectName("Vorix")
                        .description("Backend")
                        .githubUrl("https://github.com/test")
                        .status(ProjectStatus.ACTIVE)
                        .build();

        when(projectRepository.findByIdAndOwnerIdAndStatusNot(1L, ownerId, ProjectStatus.DELETED)).thenReturn(Optional.of(project));

        ProjectResponse response = projectService.getProjectById(1L, ownerId);

        assertNotNull(response);

        assertEquals("Vorix", response.projectName());
    }


    @Test
    void shouldThrowExceptionWhenProjectNotFound() {

        when(projectRepository.findByIdAndOwnerIdAndStatusNot(1L, ownerId, ProjectStatus.DELETED)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> projectService.getProjectById(1L, ownerId));
    }


    @Test
    void shouldSoftDeleteProjectSuccessfully() {

        Project project = Project.builder()
                        .projectName("Vorix")
                        .status(ProjectStatus.ACTIVE)
                        .build();

        when(projectRepository.findByIdAndOwnerIdAndStatusNot(1L, ownerId, ProjectStatus.DELETED)).thenReturn(Optional.of(project));

        projectService.softDeleteProject(1L, ownerId);

        assertEquals(ProjectStatus.DELETED, project.getStatus());

        verify(projectRepository).save(project);
    }


    @Test
    void shouldHardDeleteProjectSuccessfully() {

        Project project = Project.builder()
                        .projectName("Vorix")
                        .status(ProjectStatus.ACTIVE)
                        .build();

        when(projectRepository.findByIdAndOwnerId(1L, ownerId)).thenReturn(Optional.of(project));

        projectService.hardDeleteProject(1L, ownerId);

        verify(projectRepository).delete(project);
    }
}