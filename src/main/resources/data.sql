INSERT INTO usuarios(id, email, password, rol, activo) VALUES(null, 'test@unlam.edu.ar', 'test', 'ADMIN', true);

INSERT INTO tipos_movimiento(id, nombre) VALUES(null, 'INGRESO');
INSERT INTO tipos_movimiento(id, nombre) VALUES(null, 'EGRESO');


INSERT INTO categorias_movimiento(id, nombre, id_tipo) VALUES(null,  'SUELDO', 1);
INSERT INTO categorias_movimiento(id, nombre, id_tipo) VALUES(null, 'EXTRA', 1);
INSERT INTO categorias_movimiento(id, nombre, id_tipo) VALUES(null, 'REEMBOLSO', 1);
INSERT INTO categorias_movimiento(id, nombre, id_tipo) VALUES(null, 'REGALO', 1);

INSERT INTO categorias_movimiento(id, nombre, id_tipo) VALUES(null, 'RESTAURANTE', 2);
INSERT INTO categorias_movimiento(id, nombre, id_tipo) VALUES(null, 'ENTRETENIMIENTO', 2);
INSERT INTO categorias_movimiento(id, nombre, id_tipo) VALUES(null, 'INDUMENTARIA', 2);
INSERT INTO categorias_movimiento(id, nombre, id_tipo) VALUES(null, 'TRANSPORTE', 2);
INSERT INTO categorias_movimiento(id, nombre, id_tipo) VALUES(null, 'PAGO_DE_SERVICIOS', 2);
INSERT INTO categorias_movimiento(id, nombre, id_tipo) VALUES(null, 'SUPERMERCADO', 2);
INSERT INTO categorias_movimiento(id, nombre, id_tipo) VALUES(null, 'SALUD_BELLEZA', 2);


INSERT INTO movimientos(id, descripcion, fechayHora, monto, id_categoria, id_usuario) VALUES(null, 'Ropa', '2024-05-15', 30000, 7, 1);
INSERT INTO movimientos(id, descripcion, fechayHora, monto, id_categoria, id_usuario) VALUES(null, 'Cobro', '2024-05-15', 100000, 1, 1);
INSERT INTO movimientos(id, descripcion, fechayHora, monto, id_categoria, id_usuario) VALUES(null, 'Mercaderia', '2024-05-15', 20000, 10, 1);
INSERT INTO movimientos(id, descripcion, fechayHora, monto, id_categoria, id_usuario) VALUES(null, 'Gas', '2024-02-15', 30000, 9, 1);
INSERT INTO movimientos(id, descripcion, fechayHora, monto, id_categoria, id_usuario) VALUES(null, 'Regalo', '2024-02-20', 50000, 4, 1);
INSERT INTO movimientos(id, descripcion, fechayHora, monto, id_categoria, id_usuario) VALUES(null, 'Micro', '2024-03-15', 20000, 8, 1);
INSERT INTO movimientos(id, descripcion, fechayHora, monto, id_categoria, id_usuario) VALUES(null, 'Zapatillas', '2024-04-15', 20000, 7, 1);
