-- ==============================================================================
-- ATRANG 2026–27 FULL-STACK DBMS MINI-PROJECT (IIIT TIRUCHIRAPPALLI)
-- Contributor: Yogesh (Database Architect)
-- File: database/verify.sql
-- Description: Verification queries, integrity test suite, JOINs & GROUP BY viva scripts
-- ==============================================================================

-- 1. Table Record Count Verification (Verifies at least 10 records per table)
SELECT 'admin_users' AS table_name, COUNT(*) AS record_count FROM admin_users
UNION ALL
SELECT 'event_categories', COUNT(*) FROM event_categories
UNION ALL
SELECT 'events', COUNT(*) FROM events
UNION ALL
SELECT 'event_sessions', COUNT(*) FROM event_sessions
UNION ALL
SELECT 'event_audit_logs', COUNT(*) FROM event_audit_logs;

-- 2. Complex Relational JOIN Query (3-Table JOIN)
-- Retrieves all published events, their biome category details, and session timings
SELECT 
    e.id AS event_id,
    e.title AS event_title,
    c.name AS category_name,
    c.color_hex AS biome_color,
    s.fest_day,
    s.start_time || ' - ' || s.end_time AS schedule_window,
    s.session_title,
    s.venue AS session_venue
FROM events e
INNER JOIN event_categories c ON e.category_id = c.id
LEFT JOIN event_sessions s ON e.id = s.event_id
WHERE e.is_published = TRUE
ORDER BY c.display_order, e.display_order, s.fest_day, s.start_time;

-- 3. Aggregate Query with GROUP BY and HAVING
-- Calculates total prize-bearing events and sessions per category
SELECT 
    c.name AS category_name,
    COUNT(e.id) AS event_count,
    COUNT(CASE WHEN e.is_published = TRUE THEN 1 END) AS published_count,
    COUNT(s.id) AS total_sessions,
    STRING_AGG(DISTINCT e.participation, ', ') AS participation_modes
FROM event_categories c
LEFT JOIN events e ON c.id = e.category_id
LEFT JOIN event_sessions s ON e.id = s.event_id
GROUP BY c.id, c.name
HAVING COUNT(e.id) > 0
ORDER BY event_count DESC;

-- 4. Verification of Database View 1: v_event_details
SELECT event_id, title, category_name, participation, prize, total_sessions
FROM v_event_details
WHERE is_published = TRUE
LIMIT 5;

-- 5. Verification of Database View 2: v_category_metrics
SELECT category_code, category_name, total_events, published_events, draft_events, total_sessions
FROM v_category_metrics;

-- 6. Verification of Stored Function: fn_get_fest_stats()
SELECT * FROM fn_get_fest_stats();

-- 7. Verification of Trigger: trg_events_audit_log
-- Test insertion that triggers automated audit log record creation
DO $$
DECLARE
    v_test_id BIGINT;
    v_audit_count_before INT;
    v_audit_count_after INT;
BEGIN
    SELECT COUNT(*) INTO v_audit_count_before FROM event_audit_logs;
    
    INSERT INTO events (
        category_id, slug, title, summary, description, venue, is_published
    ) VALUES (
        1, 'test-trigger-event', 'Trigger Verification Duel',
        'Testing database trigger execution', 'Automated test of plpgsql audit logging',
        'Virtual Arena', FALSE
    ) RETURNING id INTO v_test_id;
    
    -- Verify audit log was created
    SELECT COUNT(*) INTO v_audit_count_after FROM event_audit_logs;
    IF v_audit_count_after <= v_audit_count_before THEN
        RAISE EXCEPTION 'Trigger verification failed: Audit log count did not increase';
    END IF;
    
    -- Clean up test record (triggers DELETE audit log)
    DELETE FROM events WHERE id = v_test_id;
    
    RAISE NOTICE 'SUCCESS: trg_events_audit_log verified successfully!';
END $$;
