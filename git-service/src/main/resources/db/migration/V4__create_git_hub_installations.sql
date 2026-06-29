CREATE TABLE git_hub_installations
(
    id UUID PRIMARY KEY,
    github_installation_id BIGINT NOT NULL UNIQUE,
    github_account_id BIGINT NOT NULL,
    account_login VARCHAR(255) NOT NULL,
    account_type VARCHAR(50) NOT NULL,
    target_type VARCHAR(50) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    suspended_at TIMESTAMP NULL,
    installed_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_installation_github_id
ON git_hub_installations(github_installation_id);

CREATE INDEX idx_installation_account_id
ON git_hub_installations(github_account_id);

CREATE INDEX idx_installation_active
ON git_hub_installations(active);