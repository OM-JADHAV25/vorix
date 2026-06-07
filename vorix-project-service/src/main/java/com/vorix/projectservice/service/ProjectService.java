package com.vorix.projectservice.service;

import com.vorix.projectservice.dto.request.CreateProjectRequest;
import com.vorix.projectservice.dto.request.UpdateProjectRequest;
import com.vorix.projectservice.dto.response.ProjectResponse;
import com.vorix.projectservice.entity.enums.ProjectStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProjectService {

    ProjectResponse createProject(
            CreateProjectRequest request
    );

    ProjectResponse getProjectById(
            Long id
    );

    Page<ProjectResponse> getAllProjects(
            String search,
            ProjectStatus status,
            int page,
            int size,
            String sortBy,
            String direction
    );

    ProjectResponse updateProject(
            Long id,
            UpdateProjectRequest request
    );

    void softDeleteProject(
            Long id
    );

    void hardDeleteProject(
            Long id
    );
}