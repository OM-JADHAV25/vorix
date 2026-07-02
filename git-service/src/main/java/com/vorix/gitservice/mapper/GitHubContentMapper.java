package com.vorix.gitservice.mapper;

import com.vorix.gitservice.domain.model.repository.RepositoryFile;
import com.vorix.gitservice.dto.github.content.GitHubContentResponse;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class GitHubContentMapper {

    public RepositoryFile toDomain(GitHubContentResponse response) {

        return new RepositoryFile(
                response.path(),
                decode(response.content(), response.encoding()),
                response.sha()
        );
    }

    private String decode(String content, String encoding) {

        if (!"base64".equalsIgnoreCase(encoding)) {
            return content;
        }

        return new String(Base64.getDecoder().decode(content));
    }
}