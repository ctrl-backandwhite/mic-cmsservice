-- ============================================================
-- db.changelog-1.5.sql · Seed: 10 SEO pages
-- ============================================================

INSERT INTO seo_pages (id, path, meta_title, meta_description, indexable, seo_score, created_at, updated_at, created_by, updated_by)
VALUES
  -- 1. Home
  ('seo-001', '/',
   'NX036 · Tienda Online | Tecnología, Moda y Hogar',
   'Descubre las mejores ofertas en electrónica, moda, hogar y más. Envío rápido, devoluciones gratuitas y atención al cliente 24/7.',
   true, 92,
   '2025-10-01 10:00:00', NULL, 'system', NULL),

  -- 2. Catálogo
  ('seo-002', '/catalog',
   'Catálogo de Productos | NX036',
   'Explora nuestro catálogo completo con miles de productos en tecnología, moda, deportes y hogar. Filtra por categoría, precio y valoraciones.',
   true, 85,
   '2025-10-01 10:00:00', NULL, 'system', NULL),

  -- 3. Ofertas/Campañas
  ('seo-003', '/offers',
   'Ofertas y Descuentos Especiales | NX036',
   'Aprovecha nuestras promociones exclusivas con hasta un 50% de descuento. Flash sales, cupones y ofertas por tiempo limitado.',
   true, 78,
   '2025-11-15 09:30:00', NULL, 'system', NULL),

  -- 4. Carrito
  ('seo-004', '/cart',
   'Tu Carrito de Compras | NX036',
   'Revisa los productos en tu carrito, aplica cupones de descuento y procede al pago seguro.',
   false, 45,
   '2025-10-01 10:00:00', NULL, 'system', NULL),

  -- 5. Checkout
  ('seo-005', '/checkout',
   'Finalizar Compra | NX036',
   'Completa tu pedido de forma segura. Pago con tarjeta, transferencia o contra reembolso.',
   false, 40,
   '2025-10-01 10:00:00', NULL, 'system', NULL),

  -- 6. Cuenta/Perfil
  ('seo-006', '/account',
   'Mi Cuenta | NX036',
   'Gestiona tu perfil, direcciones de envío, métodos de pago y consulta el historial de pedidos.',
   false, 50,
   '2025-10-01 10:00:00', NULL, 'system', NULL),

  -- 7. Contacto
  ('seo-007', '/contact',
   'Contacto y Atención al Cliente | NX036',
   'Ponte en contacto con nuestro equipo de soporte. Chat en vivo, email y teléfono. Resolución en menos de 24 horas.',
   true, 88,
   '2025-12-05 14:00:00', NULL, 'system', NULL),

  -- 8. FAQ / Ayuda
  ('seo-008', '/help',
   'Centro de Ayuda y Preguntas Frecuentes | NX036',
   'Encuentra respuestas a las preguntas más frecuentes sobre envíos, devoluciones, pagos y garantías.',
   true, 82,
   '2026-01-10 11:00:00', NULL, 'system', NULL),

  -- 9. Política de devoluciones
  ('seo-009', '/returns-policy',
   'Política de Devoluciones | NX036',
   'Conoce nuestra política de devoluciones: 30 días para devolver, sin preguntas. Proceso sencillo y reembolso garantizado.',
   true, 75,
   '2026-01-15 09:00:00', NULL, 'system', NULL),

  -- 10. Blog / Novedades
  ('seo-010', '/blog',
   'Blog de Tecnología y Tendencias | NX036',
   'Lee las últimas noticias, guías de compra y tendencias en tecnología, moda y estilo de vida.',
   true, 68,
   '2026-02-20 16:30:00', NULL, 'system', NULL)
ON CONFLICT (id) DO NOTHING;
