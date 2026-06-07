CREATE TABLE projects(
    id BIGSERIAL PRIMARY KEY,
    project_name VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    github_url VARCHAR(500) UNIQUE,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    deleted_at TIMESTAMP
);