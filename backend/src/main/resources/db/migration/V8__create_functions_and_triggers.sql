-- V8__create_functions_and_triggers.sql
-- Purpose: Procedural logic, automated audit triggers, timestamp triggers, and statistics functions

-- 1. Timestamp management function
CREATE OR REPLACE FUNCTION fn_update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Apply timestamp triggers
CREATE TRIGGER trg_admin_users_updated_at
BEFORE UPDATE ON admin_users
FOR EACH ROW
EXECUTE FUNCTION fn_update_timestamp();

CREATE TRIGGER trg_event_categories_updated_at
BEFORE UPDATE ON event_categories
FOR EACH ROW
EXECUTE FUNCTION fn_update_timestamp();

CREATE TRIGGER trg_events_updated_at
BEFORE UPDATE ON events
FOR EACH ROW
EXECUTE FUNCTION fn_update_timestamp();

-- 2. Automated Event Audit Trail Trigger Function
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

-- Apply event audit trigger
CREATE TRIGGER trg_events_audit_log
AFTER INSERT OR UPDATE OR DELETE ON events
FOR EACH ROW
EXECUTE FUNCTION fn_audit_event_mutation();

-- 3. Stored Function: Get festival system summary statistics
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

-- 4. Stored Procedure: Safe publication status toggle
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
