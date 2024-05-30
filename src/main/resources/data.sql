INSERT INTO usuarios(id, email, password, rol, activo) VALUES(null, 'test@unlam.edu.ar', 'test', 'ADMIN', true);

INSERT INTO tipos_movimiento(id, nombre) VALUES(null, 'INGRESO');
INSERT INTO tipos_movimiento(id, nombre) VALUES(null, 'EGRESO');


INSERT INTO categorias_movimiento(id, nombre, id_tipo, icono, color) VALUES(null,  'SUELDO', 1, 'bi bi-wallet2','#E39E1E');
INSERT INTO categorias_movimiento(id, nombre, id_tipo, icono, color) VALUES(null, 'EXTRA', 1, 'bi bi-cash','#28EB1C');
INSERT INTO categorias_movimiento(id, nombre, id_tipo, icono, color) VALUES(null, 'REEMBOLSO', 1, 'bi bi-arrow-counterclockwise','#671EE3');
INSERT INTO categorias_movimiento(id, nombre, id_tipo, icono, color) VALUES(null, 'REGALO', 1, 'bi bi-gift','#EB2727');

INSERT INTO categorias_movimiento(id, nombre, id_tipo, icono, color) VALUES(null, 'RESTAURANTE', 2, 'bi bi-cup-hot','#553722');
INSERT INTO categorias_movimiento(id, nombre, id_tipo, icono, color) VALUES(null, 'ENTRETENIMIENTO', 2, 'bi bi-controller','#E31E76');
INSERT INTO categorias_movimiento(id, nombre, id_tipo, icono, color) VALUES(null, 'INDUMENTARIA', 2, 'bi bi-bag','#671EE3');
INSERT INTO categorias_movimiento(id, nombre, id_tipo, icono, color) VALUES(null, 'TRANSPORTE', 2, 'bi bi-bus-front-fill','#6E0E32');
INSERT INTO categorias_movimiento(id, nombre, id_tipo, icono, color) VALUES(null, 'PAGO_DE_SERVICIOS', 2, 'bi bi-receipt','#1E73E3');
INSERT INTO categorias_movimiento(id, nombre, id_tipo, icono, color) VALUES(null, 'SUPERMERCADO', 2, 'bi bi-cart3','#6F106C');
INSERT INTO categorias_movimiento(id, nombre, id_tipo, icono, color) VALUES(null, 'SALUD_BELLEZA', 2, 'bi bi-heart-pulse','#EBE21C');


INSERT INTO movimientos(id, descripcion, fechayHora, monto, id_categoria, id_usuario) VALUES(null, 'Ropa', '2023-05-15', 30000, 7, 1);
INSERT INTO movimientos(id, descripcion, fechayHora, monto, id_categoria, id_usuario) VALUES(null, 'Cobro', '2024-05-15', 100000, 1, 1);
INSERT INTO movimientos(id, descripcion, fechayHora, monto, id_categoria, id_usuario) VALUES(null, 'Mercaderia', '2024-05-15', 20000, 10, 1);
INSERT INTO movimientos(id, descripcion, fechayHora, monto, id_categoria, id_usuario) VALUES(null, 'Gas', '2024-02-15', 30000, 9, 1);
INSERT INTO movimientos(id, descripcion, fechayHora, monto, id_categoria, id_usuario) VALUES(null, 'Regalo', '2024-02-20', 50000, 4, 1);
INSERT INTO movimientos(id, descripcion, fechayHora, monto, id_categoria, id_usuario) VALUES(null, 'Micro', '2024-03-15', 20000, 8, 1);
INSERT INTO movimientos(id, descripcion, fechayHora, monto, id_categoria, id_usuario) VALUES(null, 'Zapatillas', '2024-04-15', 20000, 7, 1);
