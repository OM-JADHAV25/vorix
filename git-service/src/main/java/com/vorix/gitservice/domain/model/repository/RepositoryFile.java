package com.vorix.gitservice.domain.model.repository;

public record RepositoryFile(
        String path,
        String content,
        String sha
) {
}