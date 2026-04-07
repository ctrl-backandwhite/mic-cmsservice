-- =============================================
-- Currency: replace € with $ in seed data text
-- =============================================

-- Loyalty tiers: update benefits JSON text
UPDATE loyalty_tiers
SET benefits = REPLACE(benefits::text, '€', '$')::jsonb
WHERE benefits::text LIKE '%€%';

-- Campaigns: update name and badge text
UPDATE campaigns SET name  = REPLACE(name,  '€', '$') WHERE name  LIKE '%€%';
UPDATE campaigns SET badge = REPLACE(badge, '€', '$') WHERE badge LIKE '%€%';
