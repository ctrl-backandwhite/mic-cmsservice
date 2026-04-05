-- =============================================
-- Seed data: 20 campaigns (idempotent)
-- Types: PERCENTAGE, FIXED, FLASH, BUNDLE, BUY2GET1, FREE_SHIPPING
-- =============================================

INSERT INTO campaigns (id, name, type, value, badge, start_date, end_date, applies_to_categories, applies_to_products, active, created_at, updated_at)
VALUES
-- ── PERCENTAGE campaigns ────────────────────────────────────────────────
('seed-campaign-001', 'Verano 2026', 'PERCENTAGE', 20.00,
 'Descuento de temporada',
 '2026-06-01 00:00:00', '2026-08-31 23:59:59',
 NULL, NULL, true, NOW(), NOW()),

('seed-campaign-002', 'Vuelta al cole', 'PERCENTAGE', 15.00,
 '-15% escolar',
 '2026-08-20 00:00:00', '2026-09-15 23:59:59',
 NULL, NULL, true, NOW(), NOW()),

('seed-campaign-003', 'Bienvenida nuevos clientes', 'PERCENTAGE', 10.00,
 '10% bienvenida',
 '2026-01-01 00:00:00', '2026-12-31 23:59:59',
 NULL, NULL, true, NOW(), NOW()),

('seed-campaign-004', 'Primavera Tech', 'PERCENTAGE', 25.00,
 '-25% tech',
 '2026-04-01 00:00:00', '2026-04-30 23:59:59',
 NULL, NULL, true, NOW(), NOW()),

-- ── FIXED discount campaigns ────────────────────────────────────────────
('seed-campaign-005', 'Descuento 10€ en electrónica', 'FIXED', 10.00,
 '-10€',
 '2026-04-01 00:00:00', '2026-04-30 23:59:59',
 NULL, NULL, true, NOW(), NOW()),

('seed-campaign-006', 'Vale 5€ primera compra', 'FIXED', 5.00,
 '-5€ primera compra',
 '2026-01-01 00:00:00', '2026-12-31 23:59:59',
 NULL, NULL, true, NOW(), NOW()),

('seed-campaign-007', 'Descuento 20€ para pedidos +100€', 'FIXED', 20.00,
 '-20€ en +100€',
 '2026-05-01 00:00:00', '2026-05-31 23:59:59',
 NULL, NULL, false, NOW(), NOW()),

-- ── FLASH sale campaigns ────────────────────────────────────────────────
('seed-campaign-008', 'Flash Friday', 'FLASH', 30.00,
 '⚡ -30% solo hoy',
 '2026-04-10 00:00:00', '2026-04-10 23:59:59',
 NULL, NULL, true, NOW(), NOW()),

('seed-campaign-009', 'Locura de mediodía', 'FLASH', 40.00,
 '🔥 -40% 2h',
 '2026-04-08 12:00:00', '2026-04-08 14:00:00',
 NULL, NULL, false, NOW(), NOW()),

('seed-campaign-010', 'Midnight Madness', 'FLASH', 50.00,
 '🌙 -50% medianoche',
 '2026-04-15 00:00:00', '2026-04-15 02:00:00',
 NULL, NULL, true, NOW(), NOW()),

('seed-campaign-011', 'Flash Gaming Week', 'FLASH', 35.00,
 '🎮 -35% gaming',
 '2026-04-20 00:00:00', '2026-04-26 23:59:59',
 NULL, NULL, true, NOW(), NOW()),

-- ── BUNDLE campaigns ────────────────────────────────────────────────────
('seed-campaign-012', 'Compra 3 lleva 1 gratis', 'BUNDLE', 33.00,
 '3x2 selección',
 '2026-04-01 00:00:00', '2026-06-30 23:59:59',
 NULL, NULL, true, NOW(), NOW()),

('seed-campaign-013', 'Pack familia hogar', 'BUNDLE', 25.00,
 'Pack familia',
 '2026-05-01 00:00:00', '2026-05-31 23:59:59',
 NULL, NULL, true, NOW(), NOW()),

('seed-campaign-014', 'Bundle tecnológico', 'BUNDLE', 20.00,
 'Bundle tech',
 '2026-04-05 00:00:00', '2026-04-30 23:59:59',
 NULL, NULL, false, NOW(), NOW()),

-- ── BUY2GET1 campaigns ──────────────────────────────────────────────────
('seed-campaign-015', '2x1 en accesorios', 'BUY2GET1', 50.00,
 '2x1 accesorios',
 '2026-04-01 00:00:00', '2026-04-30 23:59:59',
 NULL, NULL, true, NOW(), NOW()),

('seed-campaign-016', '2x1 moda primavera', 'BUY2GET1', 50.00,
 '2x1 moda',
 '2026-04-15 00:00:00', '2026-05-15 23:59:59',
 NULL, NULL, true, NOW(), NOW()),

('seed-campaign-017', '2x1 cosmética y belleza', 'BUY2GET1', 50.00,
 '2x1 beauty',
 '2026-03-01 00:00:00', '2026-03-31 23:59:59',
 NULL, NULL, false, NOW(), NOW()),

-- ── FREE_SHIPPING campaigns ─────────────────────────────────────────────
('seed-campaign-018', 'Envío gratis +50€', 'FREE_SHIPPING', 0.00,
 '🚚 Envío gratis',
 '2026-01-01 00:00:00', '2026-12-31 23:59:59',
 NULL, NULL, true, NOW(), NOW()),

('seed-campaign-019', 'Envío gratis fin de semana', 'FREE_SHIPPING', 0.00,
 'Free shipping weekend',
 '2026-04-11 00:00:00', '2026-04-13 23:59:59',
 NULL, NULL, true, NOW(), NOW()),

('seed-campaign-020', 'Envío gratis día del padre', 'FREE_SHIPPING', 0.00,
 '🎁 Envío gratis papá',
 '2026-03-15 00:00:00', '2026-03-19 23:59:59',
 NULL, NULL, false, NOW(), NOW())

ON CONFLICT (id) DO NOTHING;
