-- V10__admin_managed_content.sql
-- Purpose: Columns and tables needed so every piece of public event content is managed from the admin panel

-- 1. Rich event content (stored as JSON arrays)
ALTER TABLE events
    ADD COLUMN highlights TEXT,
    ADD COLUMN rules TEXT,
    ADD COLUMN coordinators TEXT;

-- 2. Free-text session times ("All day", "2:00 PM – 5:00 PM")
ALTER TABLE event_sessions
    ADD COLUMN time_window VARCHAR(100),
    ALTER COLUMN start_time DROP NOT NULL,
    ALTER COLUMN end_time DROP NOT NULL;

-- 3. In-world biome name shown beside the category label, e.g. "Syntax Peaks"
ALTER TABLE event_categories
    ADD COLUMN biome_name VARCHAR(100);

-- 4. Calendar date of each fest day; sessions refer to the day number
CREATE TABLE fest_days (
    day_number INT PRIMARY KEY CONSTRAINT chk_fest_days_number CHECK (day_number BETWEEN 1 AND 7),
    fest_date DATE NOT NULL UNIQUE
);

-- 5. The audit trigger referenced the deleted row's id, which violated fk_audit_event and blocked every delete
CREATE OR REPLACE FUNCTION fn_audit_event_mutation()
RETURNS TRIGGER AS $$
DECLARE
    v_action VARCHAR(50);
    v_details TEXT;
    v_event_id BIGINT;
BEGIN
    IF (TG_OP = 'INSERT') THEN
        v_action := 'CREATE';
        v_event_id := NEW.id;
        v_details := format('Created event "%s" (slug: %s) under category_id %s', NEW.title, NEW.slug, NEW.category_id);
    ELSIF (TG_OP = 'UPDATE') THEN
        v_event_id := NEW.id;
        IF (OLD.is_published IS FALSE AND NEW.is_published IS TRUE) THEN
            v_action := 'PUBLISH';
            v_details := format('Published event "%s"', NEW.title);
        ELSIF (OLD.is_published IS TRUE AND NEW.is_published IS FALSE) THEN
            v_action := 'UNPUBLISH';
            v_details := format('Unpublished event "%s"', NEW.title);
        ELSE
            v_action := 'UPDATE';
            v_details := format('Updated event "%s" fields (venue: %s, prize: %s)', NEW.title, NEW.venue, COALESCE(NEW.prize, 'N/A'));
        END IF;
    ELSIF (TG_OP = 'DELETE') THEN
        v_action := 'DELETE';
        v_event_id := NULL;
        v_details := format('Deleted event "%s" (id: %s, slug: %s)', OLD.title, OLD.id, OLD.slug);
    END IF;

    INSERT INTO event_audit_logs (event_id, action, details, performed_by, created_at)
    VALUES (v_event_id, v_action, v_details, 'DATABASE_TRIGGER', CURRENT_TIMESTAMP);

    RETURN NULL;
END;
$$ LANGUAGE plpgsql;
