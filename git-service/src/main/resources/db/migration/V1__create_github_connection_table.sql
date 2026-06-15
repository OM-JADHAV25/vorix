CREATE TABLE github_connections (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    installation_id BIGINT NOT NULL,
    github_user_id BIGINT NOT NULL,
    github_username VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX idx_github_connection_user
ON github_connections(user_id);

CREATE UNIQUE INDEX idx_github_connection_installation
ON github_connections(installation_id);

CREATE UNIQUE INDEX idx_github_connection_github_user
ON github_connections(github_user_id);