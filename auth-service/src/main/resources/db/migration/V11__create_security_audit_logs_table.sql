CREATE TABLE security_audit_logs(
    id UUID PRIMARY KEY,
    user_id UUID,
    event_type VARCHAR(100) NOT NULL,
    event_details TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_security_audit_logs_user_id
ON security_audit_logs(user_id);

CREATE INDEX idx_security_audit_logs_created_at
ON security_audit_logs(created_at);

CREATE INDEX idx_security_audit_logs_event_type
ON security_audit_logs(event_type);