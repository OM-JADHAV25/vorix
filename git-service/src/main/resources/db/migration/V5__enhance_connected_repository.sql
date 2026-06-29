ALTER TABLE connected_repositories

-- Repository Identity
ADD COLUMN full_name VARCHAR(255) NOT NULL,
ADD COLUMN github_repository_id BIGINT NOT NULL UNIQUE,

-- Repository Metadata
ADD COLUMN is_private BOOLEAN NOT NULL DEFAULT FALSE,
ADD COLUMN visibility VARCHAR(20),
ADD COLUMN archived BOOLEAN NOT NULL DEFAULT FALSE,
ADD COLUMN disabled BOOLEAN NOT NULL DEFAULT FALSE,

-- Git Information
ADD COLUMN clone_url VARCHAR(500),
ADD COLUMN html_url VARCHAR(500),
ADD COLUMN primary_language VARCHAR(100),

-- GitHub Timestamps
ADD COLUMN github_created_at TIMESTAMP WITH TIME ZONE,
ADD COLUMN github_updated_at TIMESTAMP WITH TIME ZONE;