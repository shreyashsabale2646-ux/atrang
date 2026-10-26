-- ==============================================================================
-- ATRANG 2026–27 FULL-STACK DBMS MINI-PROJECT (IIIT TIRUCHIRAPPALLI)
-- Course: 24CS305 / CS3106 Database Management Systems Laboratory
-- Contributor: Yogesh (Database Architect)
-- File: database/schema.sql
-- Description: Consolidated PostgreSQL DDL with 3NF Relational Tables & Constraints
-- ==============================================================================

DROP TABLE IF EXISTS event_audit_logs CASCADE;
DROP TABLE IF EXISTS event_sessions CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS event_categories CASCADE;
DROP TABLE IF EXISTS admin_users CASCADE;

-- 1. Table: admin_users (Administrative Security & Identity)
CREATE TABLE admin_users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE CONSTRAINT chk_admin_username_len CHECK (char_length(username) >= 3),
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE CONSTRAINT chk_admin_email_format CHECK (email LIKE '%@%._%'),
    role VARCHAR(20) NOT NULL DEFAULT 'ROLE_ADMIN'
        CONSTRAINT chk_admin_users_role CHECK (role IN ('ROLE_ADMIN', 'ROLE_SUPERADMIN', 'ROLE_COORDINATOR')),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 2. Table: event_categories (Festival Biomes & Domains)
CREATE TABLE event_categories (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE CONSTRAINT chk_category_code_len CHECK (char_length(code) >= 2),
    name VARCHAR(100) NOT NULL UNIQUE CONSTRAINT chk_category_name_len CHECK (char_length(name) >= 3),
    description TEXT,
    color_hex VARCHAR(7) NOT NULL DEFAULT '#a46bff'
        CONSTRAINT chk_category_color_hex CHECK (color_hex ~* '^#[0-9A-Fa-f]{6}$'),
    icon_name VARCHAR(50) NOT NULL DEFAULT 'Terminal',
    display_order INT NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 3. Table: events (Event Catalogue)
CREATE TABLE events (
    id BIGSERIAL PRIMARY KEY,
    category_id BIGINT NOT NULL,
    slug VARCHAR(100) NOT NULL UNIQUE CONSTRAINT chk_event_slug_format CHECK (slug ~* '^[a-z0-9-]+$'),
    title VARCHAR(150) NOT NULL CONSTRAINT chk_event_title_len CHECK (char_length(title) >= 3),
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

-- 4. Table: event_sessions (Day-wise Schedule Slots)
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

-- 5. Table: event_audit_logs (Security & Auditing)
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

-- Performance Indexes
CREATE INDEX idx_admin_users_username ON admin_users(username);
CREATE INDEX idx_event_categories_code ON event_categories(code);
CREATE INDEX idx_events_category_id ON events(category_id);
CREATE INDEX idx_events_slug ON events(slug);
CREATE INDEX idx_events_published ON events(is_published);
CREATE INDEX idx_events_display_order ON events(display_order);
CREATE INDEX idx_events_search ON events(is_published, category_id, display_order);
CREATE INDEX idx_event_sessions_event_id ON event_sessions(event_id);
CREATE INDEX idx_event_sessions_day ON event_sessions(fest_day);
CREATE INDEX idx_audit_logs_event_id ON event_audit_logs(event_id);
CREATE INDEX idx_audit_logs_created_at ON event_audit_logs(created_at);
