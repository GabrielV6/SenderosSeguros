INSERT INTO Usuarios (ID_Usuario, Nombre, Apellido, DNI, CorreoElectronico, Usuario, Contrasena, FechaRegistro, Puntaje, Estado) VALUES
(1, 'Admin', 'Admin', '44456123', 'admin@admin.com', 'user', 'admin', '2024-11-09', 0, 1),
(2, 'jose', 'jose', '111111', 'jose@jose.com', 'jose', 'jose', '2024-11-09', 0, 1),
(3, 'Roberto', 'Rangogni', '23669302', 'rober.rang@gmail.com', 'Rober', 'Pass01', '2024-11-10', 0, 1),
(4, 'Gabriel', 'Vargas', '1234569', 'gabriel@hotmail.com', 'gabriel', 'Gabriel1', '2024-11-11', 0, 1),
(5, 'Andrea', 'Nunez', '44789456', 'andrea@prueba.com', 'andrea', 'Andrea01', '2024-11-12', 0, 1);


INSERT INTO Barrios (ID_Barrio, Descripcion) VALUES
(1, 'Centro'),
(2, 'San Telmo'),
(3, 'Recoleta'),
(4, 'Palermo'),
(5, 'La Boca'),
(6, 'Pacheco');

INSERT INTO Puntos (ID_Punto, Latitud, Longitud, ID_Barrio) VALUES
(1, -34.454000, -58.625500, 1),
(2, -34.455200, -58.626000, 2),
(3, -34.456500, -58.627000, 3),
(4, -34.457800, -58.628000, 4),
(5, -34.459000, -58.629000, 5);


INSERT INTO CatalogoObstaculos (ID_TipoObstaculo, Descripcion, Estado) VALUES
(1, 'Obra', 1),
(2, 'Piso Resbaladizo', 1),
(3, 'Rampa', 1),
(4, 'Peligro', 1),
(5, 'Cierre', 1),
(6, 'Vereda bloqueada', 1);


INSERT INTO Obstaculos (ID_Obstaculo, ID_TipoObstaculo, Comentarios, Imagen, FechaCreacion, ID_Usuario, ID_Punto, FechaBaja, ContadorSolucion, Estado) VALUES
(1, 1, 'Obra en construcción en el barrio 1', NULL, '2024-11-05 10:00:00', 1, 1, NULL, 0, 1),
(2, 2, 'Piso resbaladizo debido a la lluvia en el barrio 2', NULL, '2024-10-15 11:30:00', 2, 2, NULL, 0, 1),
(3, 3, 'Rampa en mal estado en el barrio 3', NULL, '2024-10-20 12:45:00', 3, 3, NULL, 0, 1),
(4, 4, 'Área de peligro debido a cables expuestos en el barrio 4', NULL, '2024-09-25 09:15:00', 4, 4, NULL, 0, 1),
(5, 6, 'Vereda bloqueada por escombros en el barrio 5', NULL, '2024-09-30 14:20:00', 5, 5, NULL, 0, 1);


INSERT INTO PuntuacionUsuarios (ID_Obstaculo, ID_Usuario_que_puntua, Puntaje_asignado) VALUES
(1, 2, 5),
(2, 3, 4),
(3, 4, 3),
(4, 5, 2),
(5, 1, 4);



