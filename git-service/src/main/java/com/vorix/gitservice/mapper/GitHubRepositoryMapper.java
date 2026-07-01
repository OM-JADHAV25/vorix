package com.vorix.gitservice.mapper;

import com.vorix.gitservice.domain.model.github.RepositoryMetadata;
import com.vorix.gitservice.dto.github.repository.GitHubRepositoryResponse;
import org.springframework.stereotype.Component;

@Component
public class GitHubRepositoryMapper {

    public RepositoryMetadata toDomain(GitHubRepositoryResponse response) {

        return new RepositoryMetadata(
                response.id(),
                response.owner().login(),
                response.name(),
                response.fullName(),
                response.isPrivate(),
                response.visibility(),
                response.archived(),
                response.disabled(),
                response.defaultBranch(),
                response.cloneUrl(),
                response.htmlUrl(),
                response.language(),
                response.createdAt(),
                response.updatedAt()
        );
    }
}