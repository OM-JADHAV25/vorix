package com.vorix.projectservice.service;

import com.vorix.projectservice.dto.request.CreateProjectRequest;
import com.vorix.projectservice.dto.request.UpdateProjectRequest;
import com.vorix.projectservice.dto.response.ProjectResponse;
import com.vorix.projectservice.entity.enums.ProjectStatus;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface ProjectService {

    ProjectResponse createProject(CreateProjectRequest request, UUID ownerId);

    ProjectResponse getProjectById(Long id, UUID ownerId);

    Page<ProjectResponse> getAllProjects(
            UUID ownerId,
            String search,
            ProjectStatus status,
            int page,
            int size,
            String sortBy,
            String direction
    );

    ProjectResponse updateProject(
            Long id,
            UpdateProjectRequest request,
            UUID ownerId
    );

    void softDeleteProject(Long id, UUID ownerId);

    void hardDeleteProject(Long id, UUID ownerId);
}