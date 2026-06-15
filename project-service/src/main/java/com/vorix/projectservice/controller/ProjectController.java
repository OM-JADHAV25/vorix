package com.vorix.projectservice.controller;

import com.vorix.projectservice.dto.common.ApiResponse;
import com.vorix.projectservice.dto.request.CreateProjectRequest;
import com.vorix.projectservice.dto.response.ProjectResponse;
import com.vorix.projectservice.entity.enums.ProjectStatus;
import com.vorix.projectservice.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import com.vorix.projectservice.dto.request.UpdateProjectRequest;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ApiResponse<ProjectResponse> createProject(

            @RequestHeader("X-User-Id")
            UUID ownerId,

            @Valid
            @RequestBody
            CreateProjectRequest request
    ) {

        ProjectResponse response = projectService.createProject(request, ownerId);

        return ApiResponse.success("Project created successfully", response);
    }



    @GetMapping
    public ApiResponse<Page<ProjectResponse>> getAllProjects(

            @RequestHeader("X-User-Id")
            UUID ownerId,

            @RequestParam(required = false)
            String search,

            @RequestParam(required = false)
            ProjectStatus status,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "createdAt")
            String sortBy,

            @RequestParam(defaultValue = "desc")
            String direction
    ) {

        return ApiResponse.success(
                "Projects fetched successfully",
                projectService.getAllProjects(
                        ownerId,
                        search,
                        status,
                        page,
                        size,
                        sortBy,
                        direction
                )
        );
    }



    @GetMapping("/{id}")
    public ApiResponse<ProjectResponse> getProjectById(

            @RequestHeader("X-User-Id")
            UUID ownerId,

            @PathVariable
            Long id
    ) {
        return ApiResponse.success(
                "Project fetched successfully",
                projectService.getProjectById(id, ownerId)
        );
    }



    @PutMapping("/{id}")
    public ApiResponse<ProjectResponse> updateProject(

            @RequestHeader("X-User-Id")
            UUID ownerId,

            @PathVariable
            Long id,

            @Valid
            @RequestBody
            UpdateProjectRequest request

    ) {

        return ApiResponse.success(
                "Project updated successfully",
                projectService.updateProject(id, request, ownerId)
        );
    }



    @DeleteMapping("/{id}")
    public ApiResponse<Void> softDeleteProject(

            @RequestHeader("X-User-Id")
            UUID ownerId,

            @PathVariable
            Long id
    ) {

        projectService.softDeleteProject(id, ownerId);

        return ApiResponse.success(
                "Project deleted successfully",
                null
        );
    }



    @DeleteMapping("/{id}/permanent")
    public ApiResponse<Void> hardDeleteProject(

            @RequestHeader("X-User-Id")
            UUID ownerId,

            @PathVariable
            Long id
    ) {

        projectService.hardDeleteProject(id, ownerId);

        return ApiResponse.success(
                "Project permanently deleted",
                null
        );
    }

}
