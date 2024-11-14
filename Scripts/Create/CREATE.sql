CREATE TABLE `Barrios` (
  `ID_Barrio` int(11) NOT NULL AUTO_INCREMENT,
  `Descripcion` varchar(255) NOT NULL,
  PRIMARY KEY (`ID_Barrio`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;


CREATE TABLE `CatalogoObstaculos` (
  `ID_TipoObstaculo` int(11) NOT NULL AUTO_INCREMENT,
  `Descripcion` varchar(255) NOT NULL,
  `Estado` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`ID_TipoObstaculo`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;


CREATE TABLE `Puntos` (
  `ID_Punto` int(11) NOT NULL AUTO_INCREMENT,
  `Latitud` decimal(10,8) NOT NULL,
  `Longitud` decimal(11,8) NOT NULL,
  `ID_Barrio` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID_Punto`),
  KEY `ID_Barrio` (`ID_Barrio`),
  CONSTRAINT `Puntos_ibfk_1` FOREIGN KEY (`ID_Barrio`) REFERENCES `Barrios` (`ID_Barrio`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=latin1;


CREATE TABLE `Usuarios` (
  `ID_Usuario` int(11) NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(100) NOT NULL,
  `Apellido` varchar(100) NOT NULL,
  `DNI` varchar(20) NOT NULL,
  `CorreoElectronico` varchar(255) NOT NULL,
  `Usuario` varchar(50) NOT NULL,
  `Contrasena` varchar(255) NOT NULL,
  `FechaRegistro` date NOT NULL,
  `Puntaje` int(11) DEFAULT '0',
  `Estado` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`ID_Usuario`),
  UNIQUE KEY `DNI` (`DNI`),
  UNIQUE KEY `CorreoElectronico` (`CorreoElectronico`),
  UNIQUE KEY `Usuario` (`Usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;



CREATE TABLE `Obstaculos` (
  `ID_Obstaculo` int(11) NOT NULL AUTO_INCREMENT,
  `ID_TipoObstaculo` int(11) DEFAULT NULL,
  `Comentarios` text,
  `Imagen` blob,
  `FechaCreacion` datetime NOT NULL,
  `ID_Usuario` int(11) DEFAULT NULL,
  `ID_Punto` int(11) DEFAULT NULL,
  `FechaBaja` date DEFAULT NULL,
  `ContadorSolucion` int(11) DEFAULT '0',
  `Estado` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`ID_Obstaculo`),
  KEY `ID_TipoObstaculo` (`ID_TipoObstaculo`),
  KEY `ID_Usuario` (`ID_Usuario`),
  KEY `ID_Punto` (`ID_Punto`),
  CONSTRAINT `Obstaculos_ibfk_1` FOREIGN KEY (`ID_TipoObstaculo`) REFERENCES `CatalogoObstaculos` (`ID_TipoObstaculo`),
  CONSTRAINT `Obstaculos_ibfk_2` FOREIGN KEY (`ID_Usuario`) REFERENCES `Usuarios` (`ID_Usuario`),
  CONSTRAINT `Obstaculos_ibfk_3` FOREIGN KEY (`ID_Punto`) REFERENCES `Puntos` (`ID_Punto`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=latin1;


CREATE TABLE `PuntuacionUsuarios` (
  `ID_Obstaculo` int(11) NOT NULL DEFAULT '0',
  `ID_Usuario_que_puntua` int(11) NOT NULL DEFAULT '0',
  `Puntaje_asignado` int(11) NOT NULL,
  PRIMARY KEY (`ID_Obstaculo`,`ID_Usuario_que_puntua`),
  KEY `ID_Usuario_que_puntua` (`ID_Usuario_que_puntua`),
  CONSTRAINT `PuntuacionUsuarios_ibfk_1` FOREIGN KEY (`ID_Obstaculo`) REFERENCES `Obstaculos` (`ID_Obstaculo`),
  CONSTRAINT `PuntuacionUsuarios_ibfk_2` FOREIGN KEY (`ID_Usuario_que_puntua`) REFERENCES `Usuarios` (`ID_Usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE `SolucionObstaculos` (
  `ID_Obstaculo` int(11) NOT NULL DEFAULT '0',
  `ID_Usuario_que_reporta_baja` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID_Obstaculo`,`ID_Usuario_que_reporta_baja`),
  KEY `ID_Usuario_que_reporta_baja` (`ID_Usuario_que_reporta_baja`),
  CONSTRAINT `SolucionObstaculos_ibfk_1` FOREIGN KEY (`ID_Obstaculo`) REFERENCES `Obstaculos` (`ID_Obstaculo`),
  CONSTRAINT `SolucionObstaculos_ibfk_2` FOREIGN KEY (`ID_Usuario_que_reporta_baja`) REFERENCES `Usuarios` (`ID_Usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

