-- =============================================
-- Add missing campaign fields to campaigns table
-- =============================================

ALTER TABLE campaigns ADD COLUMN IF NOT EXISTS description   VARCHAR(500);
ALTER TABLE campaigns ADD COLUMN IF NOT EXISTS min_order     DECIMAL(12,2) DEFAULT 0;
ALTER TABLE campaigns ADD COLUMN IF NOT EXISTS max_discount  DECIMAL(12,2);
ALTER TABLE campaigns ADD COLUMN IF NOT EXISTS buy_qty       INT DEFAULT 2;
ALTER TABLE campaigns ADD COLUMN IF NOT EXISTS get_qty       INT DEFAULT 1;
ALTER TABLE campaigns ADD COLUMN IF NOT EXISTS is_flash      BOOLEAN DEFAULT false;
ALTER TABLE campaigns ADD COLUMN IF NOT EXISTS show_on_home  BOOLEAN DEFAULT false;
ALTER TABLE campaigns ADD COLUMN IF NOT EXISTS priority      INT DEFAULT 3;
