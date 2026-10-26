-- V3__create_events.sql
-- Table: events
-- Purpose: Primary festival event catalogue with schedule, venue, registration, and status metadata

CREATE TABLE events (
    id BIGSERIAL PRIMARY KEY,
    category_id BIGINT NOT NULL,
    slug VARCHAR(100) NOT NULL UNIQUE,
    title VARCHAR(150) NOT NULL,
    summary VARCHAR(300) NOT NULL,
    description TEXT NOT NULL,
    organizer VARCHAR(100) NOT NULL DEFAULT 'Atrang Technical Committee',
    participation VARCHAR(30) NOT NULL DEFAULT 'Individual'
        CONSTRAINT chk_events_participation CHECK (participation IN ('Individual', 'Team', 'Individual or Team')),
    team_size VARCHAR(50),
    duration VARCHAR(50),
    prize VARCHAR(100),
    venue VARCHAR(100) NOT NULL,
    eligibility VARCHAR(150) DEFAULT 'Open to all college students',
    fee VARCHAR(50) DEFAULT 'Free',
    registration_url TEXT,
    registration_deadline TIMESTAMP WITH TIME ZONE,
    is_published BOOLEAN NOT NULL DEFAULT FALSE,
    display_order INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_events_category FOREIGN KEY (category_id) REFERENCES event_categories(id) ON DELETE RESTRICT
);

CREATE INDEX idx_events_category_id ON events(category_id);
CREATE INDEX idx_events_slug ON events(slug);
CREATE INDEX idx_events_published ON events(is_published);
CREATE INDEX idx_events_display_order ON events(display_order);
