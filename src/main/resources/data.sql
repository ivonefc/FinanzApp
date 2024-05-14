INSERT INTO usuarios(id, email, password, rol, activo) VALUES(null, 'test@unlam.edu.ar', 'test', 'ADMIN', true);

INSERT INTO tipos_movimiento(id, nombre) VALUES(null, 'INGRESO');
INSERT INTO tipos_movimiento(id, nombre) VALUES(null, 'EGRESO');

INSERT INTO categorias_movimiento(id, nombre, id_tipo) VALUES(null,  'SUELDO', 1);
INSERT INTO categorias_movimiento(id, nombre, id_tipo) VALUES(null, 'EXTRA', 1);
INSERT INTO categorias_movimiento(id, nombre, id_tipo) VALUES(null, 'REMBOLSO', 1);
INSERT INTO categorias_movimiento(id, nombre, id_tipo) VALUES(null, 'REGALO', 1);
INSERT INTO categorias_movimiento(id, nombre, id_tipo) VALUES(null, 'INGRESO_MOVIMIENTO_COMPARTIDO', 1);

INSERT INTO categorias_movimiento(id, nombre, id_tipo) VALUES(null, 'RESTAURANTE', 2);
INSERT INTO categorias_movimiento(id, nombre, id_tipo) VALUES(null, 'ENTRETENIMIENTO', 2);
INSERT INTO categorias_movimiento(id, nombre, id_tipo) VALUES(null, 'INDUMENTARIA', 2);
INSERT INTO categorias_movimiento(id, nombre, id_tipo) VALUES(null, 'TRANSPORTE', 2);
INSERT INTO categorias_movimiento(id, nombre, id_tipo) VALUES(null, 'PAGO_DE_SERVICIOS', 2);
INSERT INTO categorias_movimiento(id, nombre, id_tipo) VALUES(null, 'SUPERMERCADO', 2);
INSERT INTO categorias_movimiento(id, nombre, id_tipo) VALUES(null, 'EGRESO_MOVIMIENTO_COMPARTIDO', 2);