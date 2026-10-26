-- V11__remove_sample_content.sql
-- Purpose: Start the catalogue empty. Events and categories are created by admins from the admin panel.

DELETE FROM events;
DELETE FROM event_categories;
