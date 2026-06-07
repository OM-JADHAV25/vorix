package com.vorix.projectservice.dto.response;

import com.vorix.projectservice.entity.enums.ProjectStatus;

import java.time.LocalDateTime;

public record ProjectResponse (

        Long id,
        String projectName,
        String description,
        String githubUrl,
        ProjectStatus status,
        LocalDateTime createdAt
) {}
