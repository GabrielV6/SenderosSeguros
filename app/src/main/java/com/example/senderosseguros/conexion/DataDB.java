package com.example.senderosseguros.conexion;

public class DataDB {
    // Información de la BD
    public static String host="sql10.freesqldatabase.com";
    public static String port="3306";
    public static String nameBD="sql10742684";
    public static String user="sql10742684";
    public static String pass="CFC1SdZ1Nl";

    public static String urlMySQL = "jdbc:mysql://" + host + ":" + port + "/" + nameBD + "?useSSL=false&allowPublicKeyRetrieval=true";
    public static String driver = "com.mysql.jdbc.Driver";
}

//CREACION DE BD
// DROP TABLE IF EXISTS Puntos;
//
//CREATE TABLE Puntos (
//        ID_Punto INT(11) PRIMARY KEY,
//        Latitud DECIMAL(10,8),
//        Longitud DECIMAL(11,8),
//        ID_Barrio INT(11)
//          );

//INSERT INTO Barrios (ID_Barrio, Descripcion)
//VALUES (4, 'General Pacheco');
//
//INSERTO PUNTOS EN PACHECO Y 1 EN PALERMO
//INSERT INTO Puntos (Latitud, Longitud, ID_Barrio)
//VALUES (-34.603722, -58.381592, 1);
//INSERT INTO Puntos (Latitud, Longitud, ID_Barrio)
//VALUES (-34.456328, -58.626699, 4);
//INSERT INTO Puntos (Latitud, Longitud, ID_Barrio)
//VALUES (-34.456032, -58.626114, 4);
//
//INSERT INTO Puntos (Latitud, Longitud, ID_Barrio)
//VALUES (-34.456413, -58.627432, 4);
//INSERT INTO Puntos (Latitud, Longitud, ID_Barrio)
//VALUES (-34.456100, -58.627251, 4);
//INSERT INTO Puntos (Latitud, Longitud, ID_Barrio)
//VALUES (-34.456761, -58.627557, 4);


///INSERTO EN TABLA "obstaculos" para prueba en base a los "Puntos"
//INSERT INTO Obstaculos (ID_TipoObstaculo, Comentarios, Imagen, FechaCreacion, ID_Usuario, ID_Punto, FechaBaja, ContadorSolucion, Estado)
//VALUES (1, 'No existe la vereda un propietario agrandó su garage', NULL, NOW(), 5, 1, NULL, 0, 1);
//
//INSERT INTO Obstaculos (ID_TipoObstaculo, Comentarios, Imagen, FechaCreacion, ID_Usuario, ID_Punto, FechaBaja, ContadorSolucion, Estado)
//VALUES (3, 'La rampa dañada', NULL, NOW(), 5, 2, NULL, 0, 1);
//
//INSERT INTO Obstaculos (ID_TipoObstaculo, Comentarios, Imagen, FechaCreacion, ID_Usuario, ID_Punto, FechaBaja, ContadorSolucion, Estado)
//VALUES (1, 'Vereda bloqueada por escombros', NULL, NOW(), 5, 3, NULL, 0, 1);
//
//INSERT INTO Obstaculos (ID_TipoObstaculo, Comentarios, Imagen, FechaCreacion, ID_Usuario, ID_Punto, FechaBaja, ContadorSolucion, Estado)
//VALUES (3, 'Rampa con desnivel peligroso', NULL, NOW(), 5, 4, NULL, 0, 1);
//
//INSERT INTO Obstaculos (ID_TipoObstaculo, Comentarios, Imagen, FechaCreacion, ID_Usuario, ID_Punto, FechaBaja, ContadorSolucion, Estado)
//VALUES (2, 'Obra EDENOR que bloquea la vereda', NULL, NOW(), 5, 5, NULL, 0, 1);
//
//INSERT INTO Obstaculos (ID_TipoObstaculo, Comentarios, Imagen, FechaCreacion, ID_Usuario, ID_Punto, FechaBaja, ContadorSolucion, Estado)
//VALUES (2, 'Obra EDENOR que bloquea la vereda', NULL, NOW(), 5, 6, NULL, 0, 1);
//
//INSERT INTO Obstaculos (ID_TipoObstaculo, Comentarios, Imagen, FechaCreacion, ID_Usuario, ID_Punto, FechaBaja, ContadorSolucion, Estado)
//VALUES (3, 'AUTO robado abandonado bloqueando la rampa', NULL, NOW(), 5, 7, NULL, 0, 1);
//INSERT INTO Obstaculos (ID_TipoObstaculo, Comentarios, Imagen, FechaCreacion, ID_Usuario, ID_Punto, FechaBaja, ContadorSolucion, Estado)
//VALUES (1, 'No existe la vereda un propietario agrandó su garage', NULL, NOW(), 5, 1, NULL, 0, 1);
//
//INSERT INTO Obstaculos (ID_TipoObstaculo, Comentarios, Imagen, FechaCreacion, ID_Usuario, ID_Punto, FechaBaja, ContadorSolucion, Estado)
//VALUES (3, 'La rampa dañada', NULL, NOW(), 5, 2, NULL, 0, 1);
//
//INSERT INTO Obstaculos (ID_TipoObstaculo, Comentarios, Imagen, FechaCreacion, ID_Usuario, ID_Punto, FechaBaja, ContadorSolucion, Estado)
//VALUES (1, 'Vereda bloqueada por escombros', NULL, NOW(), 5, 3, NULL, 0, 1);
//
//INSERT INTO Obstaculos (ID_TipoObstaculo, Comentarios, Imagen, FechaCreacion, ID_Usuario, ID_Punto, FechaBaja, ContadorSolucion, Estado)
//VALUES (3, 'Rampa con desnivel peligroso', NULL, NOW(), 5, 4, NULL, 0, 1);
//
//INSERT INTO Obstaculos (ID_TipoObstaculo, Comentarios, Imagen, FechaCreacion, ID_Usuario, ID_Punto, FechaBaja, ContadorSolucion, Estado)
//VALUES (2, 'Obra EDENOR que bloquea la vereda', NULL, NOW(), 5, 5, NULL, 0, 1);
//
//INSERT INTO Obstaculos (ID_TipoObstaculo, Comentarios, Imagen, FechaCreacion, ID_Usuario, ID_Punto, FechaBaja, ContadorSolucion, Estado)
//VALUES (2, 'Obra EDENOR que bloquea la vereda', NULL, NOW(), 5, 6, NULL, 0, 1);
//
//INSERT INTO Obstaculos (ID_TipoObstaculo, Comentarios, Imagen, FechaCreacion, ID_Usuario, ID_Punto, FechaBaja, ContadorSolucion, Estado)
//VALUES (3, 'AUTO robado abandonado bloqueando la rampa', NULL, NOW(), 5, 7, NULL, 0, 1);



