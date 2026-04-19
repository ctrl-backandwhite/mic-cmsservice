-- ============================================================
-- i18n for slides: slide_translations table + seed es/en/pt
-- ============================================================

CREATE TABLE IF NOT EXISTS slide_translations (
    slide_id     VARCHAR(64)  NOT NULL,
    locale       VARCHAR(16)  NOT NULL,
    title        VARCHAR(255) NOT NULL,
    subtitle     VARCHAR(500),
    button_text  VARCHAR(100),
    PRIMARY KEY (slide_id, locale),
    CONSTRAINT fk_slide_translations_slide
        FOREIGN KEY (slide_id) REFERENCES slides(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_slide_translations_locale
    ON slide_translations (locale);

-- ---------------------------------------------------------------
-- Seed translations for the 20 slides inserted in 1.3.
-- ES column mirrors the current slides row so cards show the same
-- text when locale=es; EN and PT are natural translations.
-- ---------------------------------------------------------------
INSERT INTO slide_translations (slide_id, locale, title, subtitle, button_text) VALUES

-- 1 Electronics deals
('SLD-001', 'es', 'Ofertas Flash en Electrónica', 'Hasta 40% de descuento en smartphones y laptops',              'Ver Ofertas'),
('SLD-001', 'en', 'Electronics Flash Deals',        'Up to 40% off on smartphones and laptops',                    'Shop Deals'),
('SLD-001', 'pt', 'Ofertas Relâmpago em Eletrônica','Até 40% de desconto em smartphones e laptops',                'Ver Ofertas'),

-- 2 Fashion season
('SLD-002', 'es', 'Nueva Colección de Verano',  'Descubre las últimas tendencias de temporada',                  'Explorar Colección'),
('SLD-002', 'en', 'New Summer Collection',      'Discover the latest seasonal trends',                           'Shop the Collection'),
('SLD-002', 'pt', 'Nova Coleção de Verão',      'Descubra as últimas tendências da temporada',                   'Ver Coleção'),

-- 3 Gaming promo
('SLD-003', 'es', 'Gaming Week: Hasta 50% OFF', 'Consolas, periféricos y accesorios al mejor precio',            'Ver Gaming'),
('SLD-003', 'en', 'Gaming Week: Up to 50% OFF', 'Consoles, peripherals and accessories at the best prices',      'Shop Gaming'),
('SLD-003', 'pt', 'Semana Gaming: Até 50% OFF', 'Consoles, periféricos e acessórios pelo melhor preço',          'Ver Gaming'),

-- 4 Free shipping
('SLD-004', 'es', 'Envío Gratis en Tu Primera Compra', 'Regístrate y disfruta de envío gratuito sin mínimo',     'Comprar Ahora'),
('SLD-004', 'en', 'Free Shipping on Your First Order', 'Sign up and enjoy free shipping with no minimum',         'Shop Now'),
('SLD-004', 'pt', 'Frete Grátis na Sua Primeira Compra', 'Cadastre-se e aproveite frete grátis sem mínimo',      'Comprar Agora'),

-- 5 Outlet clearance
('SLD-005', 'es', 'Outlet: Liquidación Total',      'Últimas unidades con descuentos de hasta 70%',              'Ir al Outlet'),
('SLD-005', 'en', 'Outlet: Final Clearance',        'Last units with up to 70% off',                             'Shop Outlet'),
('SLD-005', 'pt', 'Outlet: Liquidação Total',       'Últimas unidades com descontos de até 70%',                 'Ir ao Outlet'),

-- 6 Smart home
('SLD-006', 'es', 'Hogar Inteligente desde $29.99', 'Automatiza tu casa con los mejores dispositivos IoT',       'Descubrir'),
('SLD-006', 'en', 'Smart Home from $29.99',         'Automate your home with the best IoT devices',              'Discover'),
('SLD-006', 'pt', 'Casa Inteligente a partir de $29.99','Automatize sua casa com os melhores dispositivos IoT',  'Descobrir'),

-- 7 Back to school
('SLD-007', 'es', 'Vuelta a Clases: Todo lo que Necesitas', 'Laptops, mochilas y accesorios escolares con descuento', 'Ver Ofertas'),
('SLD-007', 'en', 'Back to School: Everything You Need',    'Laptops, backpacks and school gear on sale',             'Shop Deals'),
('SLD-007', 'pt', 'Volta às Aulas: Tudo o Que Você Precisa','Laptops, mochilas e material escolar com desconto',      'Ver Ofertas'),

-- 8 Flash sale
('SLD-008', 'es', 'Venta Flash: Solo 24 Horas', 'Descuentos exclusivos que no te puedes perder',                 'Aprovechar'),
('SLD-008', 'en', 'Flash Sale: 24 Hours Only',  'Exclusive discounts you cannot miss',                           'Grab the Deal'),
('SLD-008', 'pt', 'Venda Relâmpago: Só 24 Horas','Descontos exclusivos que você não pode perder',                'Aproveitar'),

-- 9 Audio deals
('SLD-009', 'es', 'Sonido Premium al Mejor Precio', 'Auriculares y altavoces de las mejores marcas con 35% OFF', 'Escuchar Más'),
('SLD-009', 'en', 'Premium Sound at the Best Price','Headphones and speakers from top brands at 35% OFF',        'Listen More'),
('SLD-009', 'pt', 'Som Premium pelo Melhor Preço',  'Fones e caixas das melhores marcas com 35% OFF',            'Ouvir Mais'),

-- 10 Sports
('SLD-010', 'es', 'Rebajas Deportivas de Temporada','Ropa y equipamiento deportivo desde $14.99',                'Ver Deportes'),
('SLD-010', 'en', 'Sports Seasonal Sale',           'Sportswear and gear from $14.99',                           'Shop Sports'),
('SLD-010', 'pt', 'Saldos Desportivos da Temporada','Roupa e equipamento desportivo a partir de $14.99',         'Ver Desportos'),

-- 11 Appliances
('SLD-011', 'es', 'Electrodomésticos con Hasta 45% OFF','Renueva tu cocina y hogar con las mejores ofertas',      'Ver Ofertas'),
('SLD-011', 'en', 'Appliances Up to 45% OFF',           'Refresh your kitchen and home with top deals',           'Shop Deals'),
('SLD-011', 'pt', 'Eletrodomésticos com Até 45% OFF',   'Renove a sua cozinha e lar com as melhores ofertas',     'Ver Ofertas'),

-- 12 Weekend promo
('SLD-012', 'es', 'Promo Fin de Semana',         'Descuentos exclusivos viernes a domingo en toda la tienda',    'Comprar'),
('SLD-012', 'en', 'Weekend Promo',               'Exclusive discounts Friday to Sunday storewide',               'Shop Now'),
('SLD-012', 'pt', 'Promo de Fim de Semana',      'Descontos exclusivos de sexta a domingo em toda a loja',        'Comprar'),

-- 13 Wearables
('SLD-013', 'es', 'Wearables y Smartwatches desde $49', 'La tecnología que llevas contigo a todas partes',       'Descubrir'),
('SLD-013', 'en', 'Wearables and Smartwatches from $49','The tech you carry with you everywhere',                'Discover'),
('SLD-013', 'pt', 'Wearables e Smartwatches a partir de $49','A tecnologia que você leva a todo lugar',          'Descobrir'),

-- 14 Beauty
('SLD-014', 'es', 'Belleza y Cuidado Personal -30%', 'Los mejores productos de belleza a precios increíbles',    'Ver Belleza'),
('SLD-014', 'en', 'Beauty and Personal Care -30%',   'The best beauty products at unbeatable prices',            'Shop Beauty'),
('SLD-014', 'pt', 'Beleza e Cuidados Pessoais -30%', 'Os melhores produtos de beleza a preços incríveis',        'Ver Beleza'),

-- 15 Black Friday early
('SLD-015', 'es', 'Pre-Black Friday: Adelántate',   'Las mejores ofertas antes que nadie — stock limitado',       'Ver Ofertas'),
('SLD-015', 'en', 'Pre-Black Friday: Get There First','The best deals before anyone else — limited stock',         'Shop Deals'),
('SLD-015', 'pt', 'Pré-Black Friday: Antecipe-se',  'As melhores ofertas antes de todos — estoque limitado',      'Ver Ofertas'),

-- 16 Photography
('SLD-016', 'es', 'Fotografía Profesional desde $199',      'Cámaras, lentes y accesorios con financiación sin interés', 'Explorar'),
('SLD-016', 'en', 'Professional Photography from $199',     'Cameras, lenses and accessories with interest-free financing','Explore'),
('SLD-016', 'pt', 'Fotografia Profissional a partir de $199','Câmeras, lentes e acessórios com financiamento sem juros', 'Explorar'),

-- 17 Toys & Kids
('SLD-017', 'es', 'Juguetes y Diversión para Todos',    'Hasta 40% de descuento en juguetes educativos y más',    'Ver Juguetes'),
('SLD-017', 'en', 'Toys and Fun for Everyone',          'Up to 40% off educational toys and more',                 'Shop Toys'),
('SLD-017', 'pt', 'Brinquedos e Diversão para Todos',   'Até 40% de desconto em brinquedos educativos e mais',     'Ver Brinquedos'),

-- 18 Accessories bundle
('SLD-018', 'es', '2x1 en Accesorios Seleccionados','Lleva dos y paga uno en fundas, cables y más',               'Aprovechar'),
('SLD-018', 'en', 'BOGO on Selected Accessories',   'Buy two and pay for one on cases, cables and more',           'Grab the Deal'),
('SLD-018', 'pt', '2x1 em Acessórios Selecionados', 'Leve dois e pague um em capas, cabos e mais',                 'Aproveitar'),

-- 19 Cyber Monday
('SLD-019', 'es', 'Cyber Monday: Ofertas Digitales','Software, suscripciones y servicios cloud con mega descuentos','Ver Ciber Ofertas'),
('SLD-019', 'en', 'Cyber Monday: Digital Deals',    'Software, subscriptions and cloud services at mega discounts','Shop Cyber Deals'),
('SLD-019', 'pt', 'Cyber Monday: Ofertas Digitais', 'Software, assinaturas e serviços em nuvem com mega descontos','Ver Ofertas Cyber'),

-- 20 Members exclusive
('SLD-020', 'es', 'Exclusivo Miembros NX036',   'Descuentos especiales solo para miembros registrados',           'Unirse Ahora'),
('SLD-020', 'en', 'NX036 Members Only',         'Exclusive discounts for registered members',                      'Join Now'),
('SLD-020', 'pt', 'Exclusivo Membros NX036',    'Descontos especiais só para membros registrados',                 'Juntar-se Agora')

ON CONFLICT (slide_id, locale) DO NOTHING;
