-- ==============================================================================
-- ATRANG 2026–27 FULL-STACK DBMS MINI-PROJECT (IIIT TIRUCHIRAPPALLI)
-- Contributor: Yogesh (Database Architect)
-- File: database/views_and_functions.sql
-- Description: Database Views, Triggers, Functions, and Procedures
-- ==============================================================================

-- 1. Timestamp update trigger function
CREATE OR REPLACE FUNCTION fn_update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_admin_users_updated_at ON admin_users;
CREATE TRIGGER trg_admin_users_updated_at
BEFORE UPDATE ON admin_users
FOR EACH ROW EXECUTE FUNCTION fn_update_timestamp();

DROP TRIGGER IF EXISTS trg_event_categories_updated_at ON event_categories;
CREATE TRIGGER trg_event_categories_updated_at
BEFORE UPDATE ON event_categories
FOR EACH ROW EXECUTE FUNCTION fn_update_timestamp();

DROP TRIGGER IF EXISTS trg_events_updated_at ON events;
CREATE TRIGGER trg_events_updated_at
BEFORE UPDATE ON events
FOR EACH ROW EXECUTE FUNCTION fn_update_timestamp();

-- 2. Audit trigger function
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
        v_event_id := OLD.id;
        v_details := format('Deleted event "%s" (slug: %s)', OLD.title, OLD.slug);
    END IF;

    INSERT INTO event_audit_logs (event_id, action, details, performed_by, created_at)
    VALUES (v_event_id, v_action, v_details, 'DATABASE_TRIGGER', CURRENT_TIMESTAMP);

    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_events_audit_log ON events;
CREATE TRIGGER trg_events_audit_log
AFTER INSERT OR UPDATE OR DELETE ON events
FOR EACH ROW EXECUTE FUNCTION fn_audit_event_mutation();

-- 3. Reporting View: Event Details joined with Categories and Session aggregations
CREATE OR REPLACE VIEW v_event_details AS
SELECT 
    e.id AS event_id,
    e.slug,
    e.title,
    e.summary,
    e.description,
    e.organizer,
    e.participation,
    e.team_size,
    e.duration,
    e.prize,
    e.venue,
    e.eligibility,
    e.fee,
    e.registration_url,
    e.registration_deadline,
    e.is_published,
    e.display_order,
    e.created_at,
    e.updated_at,
    c.id AS category_id,
    c.code AS category_code,
    c.name AS category_name,
    c.color_hex AS category_color,
    c.icon_name AS category_icon,
    COALESCE(s.session_count, 0) AS total_sessions
FROM events e
JOIN event_categories c ON e.category_id = c.id
LEFT JOIN (
    SELECT event_id, COUNT(*) AS session_count
    FROM event_sessions
    GROUP BY event_id
) s ON e.id = s.event_id;

-- 4. Reporting View: Category Summary & Aggregation
CREATE OR REPLACE VIEW v_category_metrics AS
SELECT 
    c.id AS category_id,
    c.code AS category_code,
    c.name AS category_name,
    c.color_hex,
    c.icon_name,
    c.display_order,
    COUNT(e.id) AS total_events,
    COUNT(CASE WHEN e.is_published = TRUE THEN 1 END) AS published_events,
    COUNT(CASE WHEN e.is_published = FALSE THEN 1 END) AS draft_events,
    COALESCE(SUM(s.session_count), 0) AS total_sessions
FROM event_categories c
LEFT JOIN events e ON c.id = e.category_id
LEFT JOIN (
    SELECT event_id, COUNT(*) AS session_count
    FROM event_sessions
    GROUP BY event_id
) s ON e.id = s.event_id
GROUP BY c.id, c.code, c.name, c.color_hex, c.icon_name, c.display_order;

-- 5. Stored Function: Festival Statistics
CREATE OR REPLACE FUNCTION fn_get_fest_stats()
RETURNS TABLE (
    total_categories BIGINT,
    total_events BIGINT,
    published_events BIGINT,
    draft_events BIGINT,
    total_sessions BIGINT,
    total_audit_logs BIGINT
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        (SELECT COUNT(*) FROM event_categories)::BIGINT,
        (SELECT COUNT(*) FROM events)::BIGINT,
        (SELECT COUNT(*) FROM events WHERE is_published = TRUE)::BIGINT,
        (SELECT COUNT(*) FROM events WHERE is_published = FALSE)::BIGINT,
        (SELECT COUNT(*) FROM event_sessions)::BIGINT,
        (SELECT COUNT(*) FROM event_audit_logs)::BIGINT;
END;
$$ LANGUAGE plpgsql;

-- 6. Stored Procedure: Atomic Publish Toggle
CREATE OR REPLACE PROCEDURE sp_toggle_event_publication(IN p_event_id BIGINT, OUT p_new_status BOOLEAN)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE events
    SET is_published = NOT is_published
    WHERE id = p_event_id
    RETURNING is_published INTO p_new_status;
    
    IF NOT FOUND THEN
        RAISE EXCEPTION 'Event with id % does not exist', p_event_id;
    END IF;
END;
$$;
