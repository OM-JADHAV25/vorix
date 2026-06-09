CREATE TABLE user_auth_providers
(
    id UUID PRIMARY KEY,

    user_id UUID NOT NULL,

    provider VARCHAR(50) NOT NULL,

    provider_id VARCHAR(255),

    created_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT fk_user_auth_provider_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT uk_user_provider
        UNIQUE (user_id, provider)
);

CREATE UNIQUE INDEX uk_provider_identifier
ON user_auth_providers(provider, provider_id)
WHERE provider_id IS NOT NULL;

INSERT INTO user_auth_providers
(
    id,
    user_id,
    provider,
    provider_id,
    created_at
)
SELECT
    gen_random_uuid(),
    id,
    provider,
    provider_id,
    NOW()
FROM users;

ALTER TABLE users
DROP COLUMN provider;

ALTER TABLE users
DROP COLUMN provider_id;