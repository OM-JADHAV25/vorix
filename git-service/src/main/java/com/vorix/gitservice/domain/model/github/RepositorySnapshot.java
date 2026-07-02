package com.vorix.gitservice.domain.model.github;

import java.util.List;

public record RepositorySnapshot(

        RepositoryMetadata metadata,
        List<String> languages,
        List<String> rootDirectories
) {
}