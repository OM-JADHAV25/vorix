package com.vorix.projectservice.mapper;

import com.vorix.projectservice.dto.response.ProjectResponse;
import com.vorix.projectservice.entity.Project;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

    public ProjectResponse toResponse(Project project) {

        return new ProjectResponse(
                project.getId(),
                project.getProjectName(),
                project.getDescription(),
                project.getGithubUrl(),
                project.getStatus(),
                project.getCreatedAt()
        );
    }
}