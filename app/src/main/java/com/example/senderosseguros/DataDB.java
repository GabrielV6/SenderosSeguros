package com.example.senderosseguros;

public class DataDB {
    // Información de la BD
    public static String host = "ep-raspy-cake-a5bxexqy.us-east-2.aws.neon.tech";
    public static String port = "5432"; // Puerto por defecto de PostgreSQL
    public static String nameBD = "senderosdb";
    public static final String user = "senderosdb_owner";
    public static final String pass = "l0hifBZYwH2e";
    public static final String urlPostgreSQL = "jdbc:postgresql://" + host + ":" + port + "/" + nameBD + "?sslmode=require";



    public static String driver = "org.postgresql.Driver";
}

