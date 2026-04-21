-- ────────────────────────────────────────────────────────────────
-- Seed the six gift-card designs that the storefront advertises
-- ("6 diseños exclusivos" on /gift-cards).
--
-- The frontend lists them in src/app/types/giftcard.ts and sends
-- the id as-is to POST /api/v1/gift-cards/purchase. Without these
-- rows the purchase fails with a FK violation on gift_cards.design_id.
-- ────────────────────────────────────────────────────────────────

INSERT INTO gift_card_designs (id, name, gradient_config, emoji, active, created_at, created_by)
VALUES
    ('classic',  'Clásica',     '{"from":"#111827","to":"#374151","accent":"#F9FAFB"}'::jsonb, '✦', true, now(), 'SYSTEM'),
    ('premium',  'Premium',     '{"from":"#78350F","to":"#D97706","accent":"#FEF3C7"}'::jsonb, '◆', true, now(), 'SYSTEM'),
    ('birthday', 'Cumpleaños',  '{"from":"#4C1D95","to":"#7C3AED","accent":"#EDE9FE"}'::jsonb, '✿', true, now(), 'SYSTEM'),
    ('love',     'Amor',        '{"from":"#9D174D","to":"#EC4899","accent":"#FCE7F3"}'::jsonb, '♥', true, now(), 'SYSTEM'),
    ('nature',   'Naturaleza',  '{"from":"#064E3B","to":"#059669","accent":"#D1FAE5"}'::jsonb, '❋', true, now(), 'SYSTEM'),
    ('ocean',    'Ocean',       '{"from":"#1E3A5F","to":"#0EA5E9","accent":"#E0F2FE"}'::jsonb, '◉', true, now(), 'SYSTEM')
ON CONFLICT (id) DO NOTHING;
