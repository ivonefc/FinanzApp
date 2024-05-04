INSERT INTO Usuario(id_usuario, email, password, rol, activo) VALUES(null, 'test@unlam.edu.ar', 'test', 'ADMIN', true);

INSERT INTO Movimiento(id_movimiento, descripcion, tipo, categoria, monto, fechayHora) VALUES (null, 'Compra', 'EGRESO', 'INDUMENTARIA', 20.0, '2024-05-04');
INSERT INTO Movimiento(id_movimiento, descripcion, tipo, categoria, monto, fechayHora) VALUES (null, 'Cobro', 'INGRESO', 'EXTRA', 20.0, '2024-05-04');

INSERT INTO usuarios_movimientos(id_usuario, id_movimiento) VALUES (1, 1);
INSERT INTO usuarios_movimientos(id_usuario, id_movimiento) VALUES (1, 2);