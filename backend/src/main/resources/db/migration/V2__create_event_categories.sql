-- V2__create_event_categories.sql
-- Table: event_categories
-- Purpose: Categorizes events into technical domains / Minecraft-inspired biomes

CREATE TABLE event_categories (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    color_hex VARCHAR(7) NOT NULL DEFAULT '#a46bff'
        CONSTRAINT chk_category_color_hex CHECK (color_hex ~* '^#[0-9A-Fa-f]{6}$'),
    icon_name VARCHAR(50) NOT NULL DEFAULT 'Terminal',
    display_order INT NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_event_categories_code ON event_categories(code);
CREATE INDEX idx_event_categories_order ON event_categories(display_order);
