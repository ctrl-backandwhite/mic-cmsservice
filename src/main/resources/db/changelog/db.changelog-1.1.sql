-- =============================================
-- mic-cmsservice: Seed data — Loyalty tiers & rules
-- =============================================

-- Tiers
INSERT INTO loyalty_tiers (id, name, min_points, max_points, multiplier, benefits, created_at, updated_at)
VALUES
  ('seed-tier-bronze',   'Bronze',   0,     999,   1.0, '[{"label":"Puntos x1"},{"label":"Descuentos básicos"}]'::jsonb,              NOW(), NOW()),
  ('seed-tier-silver',   'Silver',   1000,  2999,  1.5, '[{"label":"Puntos x1.5"},{"label":"Envío gratis +50€"}]'::jsonb,             NOW(), NOW()),
  ('seed-tier-gold',     'Gold',     3000,  4999,  2.0, '[{"label":"Puntos x2"},{"label":"Envío gratis"},{"label":"Soporte prioritario"}]'::jsonb, NOW(), NOW()),
  ('seed-tier-platinum', 'Platinum', 5000,  99999, 3.0, '[{"label":"Puntos x3"},{"label":"Todo incluido"},{"label":"Acceso anticipado"}]'::jsonb,  NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Rules
INSERT INTO loyalty_rules (id, action, points_per_unit, active, created_at, updated_at)
VALUES
  ('seed-rule-purchase',     'PURCHASE',     1,   true,  NOW(), NOW()),
  ('seed-rule-review',       'REVIEW',       50,  true,  NOW(), NOW()),
  ('seed-rule-referral',     'REFERRAL',     200, true,  NOW(), NOW()),
  ('seed-rule-registration', 'REGISTRATION', 100, false, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;
