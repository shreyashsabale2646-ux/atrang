-- V4__create_event_sessions.sql
-- Table: event_sessions
-- Purpose: Detailed schedule slots and rounds for each event across festival days

CREATE TABLE event_sessions (
    id BIGSERIAL PRIMARY KEY,
    event_id BIGINT NOT NULL,
    fest_day INT NOT NULL CONSTRAINT chk_event_sessions_day CHECK (fest_day BETWEEN 1 AND 7),
    start_time VARCHAR(50) NOT NULL,
    end_time VARCHAR(50) NOT NULL,
    session_title VARCHAR(150) NOT NULL,
    venue VARCHAR(100),
    details TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_event_sessions_event FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE
);

CREATE INDEX idx_event_sessions_event_id ON event_sessions(event_id);
CREATE INDEX idx_event_sessions_day ON event_sessions(fest_day);
