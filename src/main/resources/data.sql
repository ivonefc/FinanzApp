INSERT INTO usuarios(email, password, rol, activo, nombre, apellido, nombreUsuario, fechaNacimiento, pais, telefono) VALUES('test@unlam.edu.ar', 'test', 'FREE', true, 'Messi', 'Grupo 10', 'grupo_10', '2024/05/15', 'Argentina', '1122334455');
INSERT INTO usuarios(email, password, rol, activo, nombre, apellido, nombreUsuario, fechaNacimiento, pais, telefono) VALUES('clarisacandia0108@gmail.com', '1234', 'FREE', true, 'Clarisa', 'Rodriguez', 'clari', '2024/05/15', 'Argentina', '1122334455');
INSERT INTO usuarios(email, password, rol, activo) VALUES('test2@unlam.edu.ar', 'test', 'PREMIUM', true);
INSERT INTO usuarios(email, password, rol, activo, nombre, apellido, nombreUsuario, fechaNacimiento, pais, telefono) VALUES('test3@unlam.edu.ar', 'test', 'PREMIUM', true, 'Alan', 'Tevez', 'Alan', '2003/07/28', 'Argentina', '1122334455');

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
INSERT INTO movimientos(id, descripcion, fechayHora, monto, id_categoria, id_usuario) VALUES(null, 'Zapatillas', '2024-04-15', 15500, 7, 1);
INSERT INTO movimientos(id, descripcion, fechayHora, monto, id_categoria, id_usuario) VALUES(null, 'Corte de pelo', '2024-04-14', 2500, 11, 1);
INSERT INTO movimientos(id, descripcion, fechayHora, monto, id_categoria, id_usuario) VALUES(null, 'Extra', '2024-04-13', 116000, 5, 1);
INSERT INTO movimientos(id, descripcion, fechayHora, monto, id_categoria, id_usuario) VALUES(null, 'Mc Donalds', '2024-04-15', 14000, 5, 1);
INSERT INTO movimientos(id, descripcion, fechayHora, monto, id_categoria, id_usuario) VALUES(null, 'Sueldo', '2024-04-15', 25100, 1, 1);
INSERT INTO movimientos(id, descripcion, fechayHora, monto, id_categoria, id_usuario) VALUES(null, 'Carga Sube', '2022-04-13', 4000, 8, 1);
INSERT INTO movimientos(id, descripcion, fechayHora, monto, id_categoria, id_usuario) VALUES(null, 'Boleta Agua', '2024-04-14', 9000, 9, 1);
INSERT INTO movimientos(id, descripcion, fechayHora, monto, id_categoria, id_usuario) VALUES(null, 'Pantalon', '2021-04-12', 20000, 7, 1);
INSERT INTO movimientos(id, descripcion, fechayHora, monto, id_categoria, id_usuario) VALUES(null, 'Regalo', '2024-04-12', 11250, 4, 1);
INSERT INTO movimientos(id, descripcion, fechayHora, monto, id_categoria, id_usuario) VALUES(null, 'Regalo', '2023-04-15', 530000, 4, 1);
INSERT INTO movimientos(id, descripcion, fechayHora, monto, id_categoria, id_usuario) VALUES(null, 'Zapatillas', '2024-04-12', 20000, 7, 1);
INSERT INTO movimientos(id, descripcion, fechayHora, monto, id_categoria, id_usuario) VALUES(null, 'Remera y Buzo', '2022-04-13', 32000, 7, 1);
INSERT INTO movimientos(id, descripcion, fechayHora, monto, id_categoria, id_usuario) VALUES(null, 'Campera River', '2021-06-14', 20000, 7, 1);
