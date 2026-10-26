-- V7__create_reporting_views.sql
-- Purpose: Relational reporting views demonstrating JOINs, aggregations, and business metrics

-- View 1: Detailed Event View joining events with category metadata and session count
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

-- View 2: Category Metrics View with aggregate functions and GROUP BY
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
