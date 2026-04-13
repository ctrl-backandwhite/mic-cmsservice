-- =============================================================
-- Migration 1.11: Deactivate conflicting seed campaigns
-- 
-- All seed campaigns are ALL-scope (no category/product filter).
-- With the new overlap-prevention rules (OV001), two ALL-scope
-- non-FREE_SHIPPING campaigns cannot coexist in the same period.
-- 
-- We deactivate every active non-FREE_SHIPPING seed campaign so
-- the system starts with a clean slate and E2E tests can create
-- specific campaigns without hitting OV001.
-- 
-- FREE_SHIPPING campaigns (018, 019) remain active: they are
-- orthogonal to price-discount campaigns and never conflict.
-- =============================================================

UPDATE campaigns
SET    active = false,
       updated_at = NOW()
WHERE  id IN (
    'seed-campaign-001',
    'seed-campaign-002',
    'seed-campaign-003',
    'seed-campaign-004',
    'seed-campaign-005',
    'seed-campaign-006',
    'seed-campaign-008',
    'seed-campaign-010',
    'seed-campaign-011',
    'seed-campaign-012',
    'seed-campaign-013',
    'seed-campaign-015',
    'seed-campaign-016'
)
  AND active = true;
