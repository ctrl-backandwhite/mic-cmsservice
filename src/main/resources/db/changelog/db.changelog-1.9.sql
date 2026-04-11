-- =============================================
-- Add badge_color column to campaigns table
-- =============================================

ALTER TABLE campaigns ADD COLUMN IF NOT EXISTS badge_color VARCHAR(20) DEFAULT '#111827';
