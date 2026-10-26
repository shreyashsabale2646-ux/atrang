-- V6__add_constraints_and_indexes.sql
-- Purpose: Adds business constraints, text-integrity checks, and search indexes

ALTER TABLE admin_users
    ADD CONSTRAINT chk_admin_username_len CHECK (char_length(username) >= 3),
    ADD CONSTRAINT chk_admin_email_format CHECK (email LIKE '%@%._%');

ALTER TABLE event_categories
    ADD CONSTRAINT chk_category_code_len CHECK (char_length(code) >= 2),
    ADD CONSTRAINT chk_category_name_len CHECK (char_length(name) >= 3);

ALTER TABLE events
    ADD CONSTRAINT chk_event_title_len CHECK (char_length(title) >= 3),
    ADD CONSTRAINT chk_event_slug_len CHECK (char_length(slug) >= 2),
    ADD CONSTRAINT chk_event_slug_format CHECK (slug ~* '^[a-z0-9-]+$');

-- Compound and search indexes for query performance
CREATE INDEX idx_events_search ON events(is_published, category_id, display_order);
CREATE INDEX idx_events_title_lower ON events(LOWER(title));
