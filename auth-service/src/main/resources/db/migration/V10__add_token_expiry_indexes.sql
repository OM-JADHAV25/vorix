CREATE INDEX idx_refresh_tokens_expires_at
ON refresh_tokens(expires_at);

CREATE INDEX idx_password_reset_tokens_expires_at
ON password_reset_tokens(expires_at);

CREATE INDEX idx_email_verification_tokens_expires_at
ON email_verification_tokens(expires_at);