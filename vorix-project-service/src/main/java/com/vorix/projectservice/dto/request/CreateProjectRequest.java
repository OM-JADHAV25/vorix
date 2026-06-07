package com.vorix.projectservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateProjectRequest(

        @NotBlank(
                message = "Project name is required"
        )
        @Size(
                min = 3,
                max = 50,
                message = "Project name must be between 3 and 50 characters"
        )
        String projectName,

        @Size(
                max = 1000,
                message = "Description cannot exceed 1000 characters"
        )
        String description,

        String githubUrl

) {}