CREATE TABLE webhook_events (
    id UUID PRIMARY KEY,
    delivery_id VARCHAR(255) NOT NULL,
    event_type VARCHAR(100) NOT NULL,
    connected_repository_id UUID,
    payload JSONB NOT NULL,
    status VARCHAR(50) NOT NULL,
    received_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_webhook_repository
        FOREIGN KEY (connected_repository_id)
        REFERENCES connected_repositories(id)
        ON DELETE SET NULL
);

CREATE UNIQUE INDEX idx_webhook_delivery
ON webhook_events(delivery_id);

CREATE INDEX idx_webhook_status
ON webhook_events(status);

CREATE INDEX idx_webhook_event_type
ON webhook_events(event_type);

CREATE INDEX idx_webhook_received_at
ON webhook_events(received_at);