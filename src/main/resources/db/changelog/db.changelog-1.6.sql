-- ============================================================
-- db.changelog-1.6.sql · Seed: Flows & Flow Steps
-- ============================================================

-- Flow 1: Entrega estándar
INSERT INTO flows (id, name, type, active, created_at, created_by)
VALUES ('flow-delivery-standard', 'Entrega estándar', 'DELIVERY', true, NOW(), 'system');

INSERT INTO flow_steps (id, flow_id, position, title, description, icon, sla_days, trigger_type, send_email, send_sms, created_at, created_by)
VALUES
('fs-del-1', 'flow-delivery-standard', 1, 'Pedido confirmado',       'El pedido ha sido confirmado y está listo para preparación.',      'check-circle',  0, 'AUTOMATIC', true,  false, NOW(), 'system'),
('fs-del-2', 'flow-delivery-standard', 2, 'En preparación',          'El almacén está preparando los productos del pedido.',             'package',        1, 'AUTOMATIC', false, false, NOW(), 'system'),
('fs-del-3', 'flow-delivery-standard', 3, 'Enviado',                 'El pedido ha sido entregado al transportista.',                    'truck',          1, 'AUTOMATIC', true,  true,  NOW(), 'system'),
('fs-del-4', 'flow-delivery-standard', 4, 'En tránsito',             'El paquete se encuentra en camino hacia la dirección de destino.', 'navigation',     3, 'AUTOMATIC', false, false, NOW(), 'system'),
('fs-del-5', 'flow-delivery-standard', 5, 'Entregado',               'El pedido fue entregado exitosamente al destinatario.',            'home',           0, 'AUTOMATIC', true,  true,  NOW(), 'system');

-- Flow 2: Devolución
INSERT INTO flows (id, name, type, active, created_at, created_by)
VALUES ('flow-return-standard', 'Devolución estándar', 'RETURN', true, NOW(), 'system');

INSERT INTO flow_steps (id, flow_id, position, title, description, icon, sla_days, trigger_type, send_email, send_sms, created_at, created_by)
VALUES
('fs-ret-1', 'flow-return-standard', 1, 'Solicitud recibida',        'Se ha recibido la solicitud de devolución del cliente.',           'inbox',          0, 'AUTOMATIC', true,  false, NOW(), 'system'),
('fs-ret-2', 'flow-return-standard', 2, 'Aprobada',                  'La solicitud de devolución ha sido aprobada.',                     'check',          2, 'MANUAL',    true,  false, NOW(), 'system'),
('fs-ret-3', 'flow-return-standard', 3, 'Producto recogido',         'El transportista ha recogido el producto devuelto.',               'truck',          3, 'AUTOMATIC', true,  true,  NOW(), 'system'),
('fs-ret-4', 'flow-return-standard', 4, 'Producto recibido',         'El almacén ha recibido e inspeccionado el producto.',              'clipboard',      2, 'MANUAL',    true,  false, NOW(), 'system'),
('fs-ret-5', 'flow-return-standard', 5, 'Reembolso procesado',       'El reembolso ha sido procesado correctamente.',                   'credit-card',    3, 'AUTOMATIC', true,  true,  NOW(), 'system');

-- Flow 3: Cambio
INSERT INTO flows (id, name, type, active, created_at, created_by)
VALUES ('flow-exchange-standard', 'Cambio de producto', 'EXCHANGE', true, NOW(), 'system');

INSERT INTO flow_steps (id, flow_id, position, title, description, icon, sla_days, trigger_type, send_email, send_sms, created_at, created_by)
VALUES
('fs-exc-1', 'flow-exchange-standard', 1, 'Solicitud de cambio',     'El cliente ha solicitado un cambio de producto.',                  'repeat',         0, 'AUTOMATIC', true,  false, NOW(), 'system'),
('fs-exc-2', 'flow-exchange-standard', 2, 'Cambio aprobado',         'La solicitud de cambio ha sido revisada y aprobada.',              'check',          2, 'MANUAL',    true,  false, NOW(), 'system'),
('fs-exc-3', 'flow-exchange-standard', 3, 'Recogida del original',   'Se recoge el producto original del cliente.',                      'truck',          3, 'AUTOMATIC', true,  true,  NOW(), 'system'),
('fs-exc-4', 'flow-exchange-standard', 4, 'Nuevo producto enviado',  'Se ha despachado el nuevo producto al cliente.',                   'send',           2, 'AUTOMATIC', true,  true,  NOW(), 'system'),
('fs-exc-5', 'flow-exchange-standard', 5, 'Cambio completado',       'El cambio ha sido completado satisfactoriamente.',                 'check-circle',   0, 'AUTOMATIC', true,  false, NOW(), 'system');

-- Flow 4: Control de calidad
INSERT INTO flows (id, name, type, active, created_at, created_by)
VALUES ('flow-quality-standard', 'Control de calidad', 'QUALITY', true, NOW(), 'system');

INSERT INTO flow_steps (id, flow_id, position, title, description, icon, sla_days, trigger_type, send_email, send_sms, created_at, created_by)
VALUES
('fs-qua-1', 'flow-quality-standard', 1, 'Incidencia reportada',     'Se ha reportado una incidencia de calidad.',                      'alert-triangle', 0, 'AUTOMATIC', true,  false, NOW(), 'system'),
('fs-qua-2', 'flow-quality-standard', 2, 'En revisión',              'El equipo de calidad está revisando la incidencia.',               'search',         2, 'MANUAL',    false, false, NOW(), 'system'),
('fs-qua-3', 'flow-quality-standard', 3, 'Resolución propuesta',     'Se ha propuesto una solución para la incidencia.',                 'tool',           3, 'MANUAL',    true,  false, NOW(), 'system'),
('fs-qua-4', 'flow-quality-standard', 4, 'Resuelta',                 'La incidencia de calidad ha sido resuelta.',                       'check-circle',   0, 'AUTOMATIC', true,  true,  NOW(), 'system');
