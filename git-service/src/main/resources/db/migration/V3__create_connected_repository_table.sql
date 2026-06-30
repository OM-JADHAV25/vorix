CREATE TABLE connected_repositories (
    id UUID PRIMARY KEY,
    project_id UUID NOT NULL,
    installation_id UUID NOT NULL,
    github_repository_id BIGINT NOT NULL,
    owner VARCHAR(255) NOT NULL,
    repository_name VARCHAR(255) NOT NULL,
    default_branch VARCHAR(255),
    webhook_id BIGINT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_connected_repository_installation
        FOREIGN KEY (installation_id)
        REFERENCES git_hub_installations(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_connected_repository_project
ON connected_repositories(project_id);

CREATE INDEX idx_connected_repository_installation
ON connected_repositories(installation_id);

CREATE UNIQUE INDEX idx_connected_repository_github_repo
ON connected_repositories(github_repository_id);