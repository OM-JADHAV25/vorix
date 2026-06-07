package com.vorix.projectservice.controller;

import com.vorix.projectservice.dto.common.ApiResponse;
import com.vorix.projectservice.dto.request.CreateProjectRequest;
import com.vorix.projectservice.dto.response.ProjectResponse;
import com.vorix.projectservice.entity.Project;
import com.vorix.projectservice.entity.enums.ProjectStatus;
import com.vorix.projectservice.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
            @Valid
            @RequestBody
            CreateProjectRequest request
    ) {

        ProjectResponse response = projectService.createProject(request);

        return ApiResponse.success("Project created successfully", response);
    }



    @GetMapping
    public ApiResponse<Page<ProjectResponse>> getAllProjects(

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

            @PathVariable
            Long id
    ) {
        return ApiResponse.success(
                "Project fetched successfully",
                projectService.getProjectById(id)
        );
    }



    @PutMapping("/{id}")
    public ApiResponse<ProjectResponse> updateProject(

            @PathVariable
            Long id,

            @Valid
            @RequestBody
            UpdateProjectRequest request

    ) {

        return ApiResponse.success(
                "Project updated successfully",
                projectService.updateProject(
                        id,
                        request
                )
        );
    }



    @DeleteMapping("/{id}")
    public ApiResponse<Void> softDeleteProject(

            @PathVariable
            Long id
    ) {

        projectService.softDeleteProject(id);

        return ApiResponse.success(
                "Project deleted successfully",
                null
        );
    }



    @DeleteMapping("/{id}/permanent")
    public ApiResponse<Void> hardDeleteProject(

            @PathVariable
            Long id
    ) {

        projectService.hardDeleteProject(id);

        return ApiResponse.success(
                "Project permanently deleted",
                null
        );
    }

}
