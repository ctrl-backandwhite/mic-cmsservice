-- ============================================================
-- SEED: 20 promotional / sale slides in DRAFT mode (active=false)
-- Ready to be published by toggling active = true from the admin
-- ============================================================

INSERT INTO slides (id, title, subtitle, image_url, link, button_text, position, active, created_at)
VALUES
-- 1. Electronics deals
('SLD-001', 'Ofertas Flash en Electrónica', 'Hasta 40% de descuento en smartphones y laptops',
 'https://images.unsplash.com/photo-1517694712202-14dd9538aa97?w=1920&q=80',
 '/?ofertas=true&category=Electrónica', 'Ver Ofertas', 1, false, NOW()),

-- 2. Fashion season
('SLD-002', 'Nueva Colección de Verano', 'Descubre las últimas tendencias de temporada',
 'https://images.unsplash.com/photo-1483985988355-763728e1935b?w=1920&q=80',
 '/?category=Moda', 'Explorar Colección', 2, false, NOW()),

-- 3. Gaming promo
('SLD-003', 'Gaming Week: Hasta 50% OFF', 'Consolas, periféricos y accesorios al mejor precio',
 'https://images.unsplash.com/photo-1542751371-adc38448a05e?w=1920&q=80',
 '/?ofertas=true&category=Gaming', 'Ver Gaming', 3, false, NOW()),

-- 4. Free shipping
('SLD-004', 'Envío Gratis en Tu Primera Compra', 'Regístrate y disfruta de envío gratuito sin mínimo',
 'https://images.unsplash.com/photo-1556742049-0cfed4f6a45d?w=1920&q=80',
 '/?ofertas=true', 'Comprar Ahora', 4, false, NOW()),

-- 5. Outlet clearance
('SLD-005', 'Outlet: Liquidación Total', 'Últimas unidades con descuentos de hasta 70%',
 'https://images.unsplash.com/photo-1607082348824-0a96f2a4b9da?w=1920&q=80',
 '/?ofertas=true', 'Ir al Outlet', 5, false, NOW()),

-- 6. Smart home
('SLD-006', 'Hogar Inteligente desde $29.99', 'Automatiza tu casa con los mejores dispositivos IoT',
 'https://images.unsplash.com/photo-1558002038-1055907df827?w=1920&q=80',
 '/?category=Hogar', 'Descubrir', 6, false, NOW()),

-- 7. Back to school
('SLD-007', 'Vuelta a Clases: Todo lo que Necesitas', 'Laptops, mochilas y accesorios escolares con descuento',
 'https://images.unsplash.com/photo-1503676260728-1c00da094a0b?w=1920&q=80',
 '/?ofertas=true&category=Educación', 'Ver Ofertas', 7, false, NOW()),

-- 8. Flash sale
('SLD-008', 'Venta Flash: Solo 24 Horas', 'Descuentos exclusivos que no te puedes perder',
 'https://images.unsplash.com/photo-1607083206869-4c7672e72a8a?w=1920&q=80',
 '/?ofertas=true', 'Aprovechar', 8, false, NOW()),

-- 9. Audio deals
('SLD-009', 'Sonido Premium al Mejor Precio', 'Auriculares y altavoces de las mejores marcas con 35% OFF',
 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=1920&q=80',
 '/?category=Audio', 'Escuchar Más', 9, false, NOW()),

-- 10. Sports
('SLD-010', 'Rebajas Deportivas de Temporada', 'Ropa y equipamiento deportivo desde $14.99',
 'https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=1920&q=80',
 '/?ofertas=true&category=Deportes', 'Ver Deportes', 10, false, NOW()),

-- 11. Appliances
('SLD-011', 'Electrodomésticos con Hasta 45% OFF', 'Renueva tu cocina y hogar con las mejores ofertas',
 'https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?w=1920&q=80',
 '/?category=Electrodomésticos', 'Ver Ofertas', 11, false, NOW()),

-- 12. Weekend promo
('SLD-012', 'Promo Fin de Semana', 'Descuentos exclusivos viernes a domingo en toda la tienda',
 'https://images.unsplash.com/photo-1472851294608-062f824d29cc?w=1920&q=80',
 '/?ofertas=true', 'Comprar', 12, false, NOW()),

-- 13. Wearables
('SLD-013', 'Wearables y Smartwatches desde $49', 'La tecnología que llevas contigo a todas partes',
 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=1920&q=80',
 '/?category=Wearables', 'Descubrir', 13, false, NOW()),

-- 14. Beauty
('SLD-014', 'Belleza y Cuidado Personal -30%', 'Los mejores productos de belleza a precios increíbles',
 'https://images.unsplash.com/photo-1522335789203-aabd1fc54bc9?w=1920&q=80',
 '/?ofertas=true&category=Belleza', 'Ver Belleza', 14, false, NOW()),

-- 15. Black Friday early
('SLD-015', 'Pre-Black Friday: Adelántate', 'Las mejores ofertas antes que nadie — stock limitado',
 'https://images.unsplash.com/photo-1607083206325-caf1edba7a0f?w=1920&q=80',
 '/?ofertas=true', 'Ver Ofertas', 15, false, NOW()),

-- 16. Photography
('SLD-016', 'Fotografía Profesional desde $199', 'Cámaras, lentes y accesorios con financiación sin interés',
 'https://images.unsplash.com/photo-1502920917128-1aa500764cbd?w=1920&q=80',
 '/?category=Fotografía', 'Explorar', 16, false, NOW()),

-- 17. Toys & Kids
('SLD-017', 'Juguetes y Diversión para Todos', 'Hasta 40% de descuento en juguetes educativos y más',
 'https://images.unsplash.com/photo-1558060370-d644479cb6f7?w=1920&q=80',
 '/?ofertas=true&category=Juguetes', 'Ver Juguetes', 17, false, NOW()),

-- 18. Accessories bundle
('SLD-018', '2x1 en Accesorios Seleccionados', 'Lleva dos y paga uno en fundas, cables y más',
 'https://images.unsplash.com/photo-1601784551446-20c9e07cdbdb?w=1920&q=80',
 '/?ofertas=true&category=Accesorios', 'Aprovechar', 18, false, NOW()),

-- 19. Cyber Monday
('SLD-019', 'Cyber Monday: Ofertas Digitales', 'Software, suscripciones y servicios cloud con mega descuentos',
 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=1920&q=80',
 '/?ofertas=true', 'Ver Ciber Ofertas', 19, false, NOW()),

-- 20. Members exclusive
('SLD-020', 'Exclusivo Miembros NX036', 'Descuentos especiales solo para miembros registrados',
 'https://images.unsplash.com/photo-1557821552-17105176677c?w=1920&q=80',
 '/?ofertas=true', 'Unirse Ahora', 20, false, NOW())

ON CONFLICT (id) DO NOTHING;
