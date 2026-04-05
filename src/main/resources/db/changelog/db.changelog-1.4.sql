-- ============================================================
-- db.changelog-1.4.sql · Seed: 10 newsletter subscribers
-- ============================================================

INSERT INTO newsletter_subscribers (id, email, status, subscribed_at, unsubscribed_at, source, created_at, updated_at, created_by, updated_by)
VALUES
  -- 1. Active — subscribed from popup
  ('ns-001', 'maria.garcia@gmail.com',      'ACTIVE',       '2025-11-15 10:23:00', NULL,                      'popup',    '2025-11-15 10:23:00', NULL, 'system', NULL),
  -- 2. Active — subscribed from checkout
  ('ns-002', 'carlos.lopez@outlook.com',     'ACTIVE',       '2025-12-02 14:45:00', NULL,                      'checkout', '2025-12-02 14:45:00', NULL, 'system', NULL),
  -- 3. Active — subscribed from footer
  ('ns-003', 'ana.martinez@hotmail.com',      'ACTIVE',       '2026-01-08 09:12:00', NULL,                      'footer',   '2026-01-08 09:12:00', NULL, 'system', NULL),
  -- 4. Active — subscribed from popup
  ('ns-004', 'pedro.ruiz@yahoo.com',          'ACTIVE',       '2026-01-20 18:30:00', NULL,                      'popup',    '2026-01-20 18:30:00', NULL, 'system', NULL),
  -- 5. Unsubscribed — was subscribed from checkout, then left
  ('ns-005', 'laura.fernandez@gmail.com',     'UNSUBSCRIBED', '2025-10-05 11:00:00', '2026-02-14 08:22:00',     'checkout', '2025-10-05 11:00:00', '2026-02-14 08:22:00', 'system', 'system'),
  -- 6. Active — subscribed from footer
  ('ns-006', 'david.sanchez@protonmail.com',  'ACTIVE',       '2026-02-10 16:45:00', NULL,                      'footer',   '2026-02-10 16:45:00', NULL, 'system', NULL),
  -- 7. Active — subscribed from popup
  ('ns-007', 'sofia.moreno@gmail.com',        'ACTIVE',       '2026-02-25 12:05:00', NULL,                      'popup',    '2026-02-25 12:05:00', NULL, 'system', NULL),
  -- 8. Unsubscribed — was subscribed from popup, then unsubscribed
  ('ns-008', 'javier.diaz@outlook.com',       'UNSUBSCRIBED', '2025-09-18 07:33:00', '2026-01-10 20:15:00',     'popup',    '2025-09-18 07:33:00', '2026-01-10 20:15:00', 'system', 'system'),
  -- 9. Active — subscribed from checkout
  ('ns-009', 'elena.torres@icloud.com',       'ACTIVE',       '2026-03-05 13:20:00', NULL,                      'checkout', '2026-03-05 13:20:00', NULL, 'system', NULL),
  -- 10. Active — subscribed from footer
  ('ns-010', 'roberto.gil@gmail.com',         'ACTIVE',       '2026-03-18 10:55:00', NULL,                      'footer',   '2026-03-18 10:55:00', NULL, 'system', NULL)
ON CONFLICT (id) DO NOTHING;
