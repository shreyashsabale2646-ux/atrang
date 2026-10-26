-- V5__create_event_audit_logs.sql
-- Table: event_audit_logs
-- Purpose: Complete security audit trail for festival event changes and admin actions

CREATE TABLE event_audit_logs (
    id BIGSERIAL PRIMARY KEY,
    event_id BIGINT,
    admin_id BIGINT,
    action VARCHAR(50) NOT NULL
        CONSTRAINT chk_audit_action CHECK (action IN ('CREATE', 'UPDATE', 'DELETE', 'PUBLISH', 'UNPUBLISH', 'LOGIN', 'LOGOUT')),
    details TEXT NOT NULL,
    performed_by VARCHAR(50) NOT NULL DEFAULT 'SYSTEM',
    ip_address VARCHAR(45),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_audit_event FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE SET NULL,
    CONSTRAINT fk_audit_admin FOREIGN KEY (admin_id) REFERENCES admin_users(id) ON DELETE SET NULL
);

CREATE INDEX idx_audit_logs_event_id ON event_audit_logs(event_id);
CREATE INDEX idx_audit_logs_admin_id ON event_audit_logs(admin_id);
CREATE INDEX idx_audit_logs_action ON event_audit_logs(action);
CREATE INDEX idx_audit_logs_created_at ON event_audit_logs(created_at);
