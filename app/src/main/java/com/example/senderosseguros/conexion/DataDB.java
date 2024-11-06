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

