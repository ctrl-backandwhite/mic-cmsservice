-- ============================================================
-- i18n for campaigns: campaign_translations table + seed es/en/pt
-- ============================================================

CREATE TABLE IF NOT EXISTS campaign_translations (
    campaign_id  VARCHAR(64)  NOT NULL,
    locale       VARCHAR(16)  NOT NULL,
    name         VARCHAR(255) NOT NULL,
    badge        VARCHAR(50),
    description  VARCHAR(500),
    PRIMARY KEY (campaign_id, locale),
    CONSTRAINT fk_campaign_translations_campaign
        FOREIGN KEY (campaign_id) REFERENCES campaigns(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_campaign_translations_locale
    ON campaign_translations (locale);

-- ---------------------------------------------------------------
-- Seed translations for the 20 campaigns from changelog 1.2.
-- ES mirrors the campaigns row, EN / PT are natural translations.
-- ---------------------------------------------------------------
INSERT INTO campaign_translations (campaign_id, locale, name, badge, description) VALUES

-- 1 Summer 2026 (PERCENTAGE 20%)
('seed-campaign-001', 'es', 'Verano 2026',          'Descuento de temporada', NULL),
('seed-campaign-001', 'en', 'Summer 2026',          'Seasonal discount',      NULL),
('seed-campaign-001', 'pt', 'Verão 2026',           'Desconto de temporada',  NULL),

-- 2 Back to school (PERCENTAGE 15%)
('seed-campaign-002', 'es', 'Vuelta al cole',       '-15% escolar',           NULL),
('seed-campaign-002', 'en', 'Back to school',       '-15% school',            NULL),
('seed-campaign-002', 'pt', 'Volta às aulas',       '-15% escolar',           NULL),

-- 3 Welcome new customers (PERCENTAGE 10%)
('seed-campaign-003', 'es', 'Bienvenida nuevos clientes', '10% bienvenida',   NULL),
('seed-campaign-003', 'en', 'New customer welcome',       '10% welcome',      NULL),
('seed-campaign-003', 'pt', 'Boas-vindas novos clientes', '10% boas-vindas',  NULL),

-- 4 Spring tech (PERCENTAGE 25%)
('seed-campaign-004', 'es', 'Primavera Tech',       '-25% tech',              NULL),
('seed-campaign-004', 'en', 'Spring Tech',          '-25% tech',              NULL),
('seed-campaign-004', 'pt', 'Primavera Tech',       '-25% tech',              NULL),

-- 5 Fixed €10 off electronics (FIXED 10)
('seed-campaign-005', 'es', 'Descuento 10€ en electrónica', '-10€',            NULL),
('seed-campaign-005', 'en', '€10 off electronics',          '-€10',            NULL),
('seed-campaign-005', 'pt', 'Desconto de 10€ em eletrônica','-10€',            NULL),

-- 6 €5 voucher first order (FIXED 5)
('seed-campaign-006', 'es', 'Vale 5€ primera compra',   '-5€ primera compra',   NULL),
('seed-campaign-006', 'en', '€5 voucher on first order','-€5 first order',      NULL),
('seed-campaign-006', 'pt', 'Vale 5€ primeira compra',  '-5€ primeira compra',  NULL),

-- 7 €20 off orders over €100 (FIXED 20)
('seed-campaign-007', 'es', 'Descuento 20€ para pedidos +100€', '-20€ en +100€',  NULL),
('seed-campaign-007', 'en', '€20 off orders over €100',         '-€20 on +€100',  NULL),
('seed-campaign-007', 'pt', 'Desconto 20€ em pedidos +100€',    '-20€ em +100€',  NULL),

-- 8 Flash Friday (FLASH 30%)
('seed-campaign-008', 'es', 'Flash Friday',  '⚡ -30% solo hoy',   NULL),
('seed-campaign-008', 'en', 'Flash Friday',  '⚡ -30% today only', NULL),
('seed-campaign-008', 'pt', 'Flash Friday',  '⚡ -30% só hoje',     NULL),

-- 9 Midday madness (FLASH 40%)
('seed-campaign-009', 'es', 'Locura de mediodía', '🔥 -40% 2h',    NULL),
('seed-campaign-009', 'en', 'Midday madness',     '🔥 -40% 2h',    NULL),
('seed-campaign-009', 'pt', 'Loucura do meio-dia','🔥 -40% 2h',    NULL),

-- 10 Midnight madness (FLASH 50%)
('seed-campaign-010', 'es', 'Midnight Madness',      '🌙 -50% medianoche', NULL),
('seed-campaign-010', 'en', 'Midnight Madness',      '🌙 -50% midnight',   NULL),
('seed-campaign-010', 'pt', 'Loucura da Meia-Noite', '🌙 -50% meia-noite', NULL),

-- 11 Flash Gaming Week (FLASH 35%)
('seed-campaign-011', 'es', 'Flash Gaming Week', '🎮 -35% gaming', NULL),
('seed-campaign-011', 'en', 'Flash Gaming Week', '🎮 -35% gaming', NULL),
('seed-campaign-011', 'pt', 'Flash Gaming Week', '🎮 -35% gaming', NULL),

-- 12 Buy 3 get 1 free (BUNDLE 33%)
('seed-campaign-012', 'es', 'Compra 3 lleva 1 gratis', '3x2 selección',          NULL),
('seed-campaign-012', 'en', 'Buy 3 get 1 free',        '3-for-2 selection',      NULL),
('seed-campaign-012', 'pt', 'Compre 3 leve 1 grátis',  '3x2 seleção',            NULL),

-- 13 Family home pack (BUNDLE 25%)
('seed-campaign-013', 'es', 'Pack familia hogar',  'Pack familia', NULL),
('seed-campaign-013', 'en', 'Family home pack',    'Family pack',  NULL),
('seed-campaign-013', 'pt', 'Pack família casa',   'Pack família', NULL),

-- 14 Tech bundle (BUNDLE 20%)
('seed-campaign-014', 'es', 'Bundle tecnológico', 'Bundle tech', NULL),
('seed-campaign-014', 'en', 'Tech bundle',        'Tech bundle', NULL),
('seed-campaign-014', 'pt', 'Bundle tecnológico', 'Bundle tech', NULL),

-- 15 2-for-1 accessories (BUY2GET1)
('seed-campaign-015', 'es', '2x1 en accesorios',     '2x1 accesorios',   NULL),
('seed-campaign-015', 'en', 'BOGO on accessories',   'BOGO accessories', NULL),
('seed-campaign-015', 'pt', '2x1 em acessórios',     '2x1 acessórios',   NULL),

-- 16 2-for-1 spring fashion (BUY2GET1)
('seed-campaign-016', 'es', '2x1 moda primavera',    '2x1 moda',        NULL),
('seed-campaign-016', 'en', 'BOGO spring fashion',   'BOGO fashion',    NULL),
('seed-campaign-016', 'pt', '2x1 moda primavera',    '2x1 moda',        NULL),

-- 17 2-for-1 cosmetics & beauty (BUY2GET1)
('seed-campaign-017', 'es', '2x1 cosmética y belleza', '2x1 beauty',  NULL),
('seed-campaign-017', 'en', 'BOGO cosmetics & beauty', 'BOGO beauty', NULL),
('seed-campaign-017', 'pt', '2x1 cosméticos e beleza', '2x1 beauty',  NULL),

-- 18 Free shipping over €50 (FREE_SHIPPING)
('seed-campaign-018', 'es', 'Envío gratis +50€',      '🚚 Envío gratis', NULL),
('seed-campaign-018', 'en', 'Free shipping over €50', '🚚 Free shipping',NULL),
('seed-campaign-018', 'pt', 'Frete grátis +50€',      '🚚 Frete grátis', NULL),

-- 19 Free shipping weekend (FREE_SHIPPING)
('seed-campaign-019', 'es', 'Envío gratis fin de semana', 'Free shipping weekend', NULL),
('seed-campaign-019', 'en', 'Free shipping weekend',      'Free shipping weekend', NULL),
('seed-campaign-019', 'pt', 'Frete grátis fim de semana', 'Frete grátis fim de semana', NULL),

-- 20 Free shipping Father's day (FREE_SHIPPING)
('seed-campaign-020', 'es', 'Envío gratis día del padre',   '🎁 Envío gratis papá',   NULL),
('seed-campaign-020', 'en', 'Free shipping on Father''s day','🎁 Free shipping dad',   NULL),
('seed-campaign-020', 'pt', 'Frete grátis dia do pai',      '🎁 Frete grátis pai',    NULL)

ON CONFLICT (campaign_id, locale) DO NOTHING;
