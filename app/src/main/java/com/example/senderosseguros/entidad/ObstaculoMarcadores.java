package com.example.senderosseguros.entidad;

public class ObstaculoMarcadores {
    private int idObstaculo;
    private int idTipoObstaculo;
    private String descripcionObstaculo;
    private double latitud;
    private double longitud;

    public ObstaculoMarcadores(int idObstaculo, int idTipoObstaculo, String descripcionObstaculo, double latitud, double longitud) {
        this.idObstaculo = idObstaculo;
        this.idTipoObstaculo = idTipoObstaculo;
        this.descripcionObstaculo = descripcionObstaculo;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public int getIdObstaculo() { return idObstaculo; }
    public int getIdTipoObstaculo() { return idTipoObstaculo; }
    public String getDescripcionObstaculo() { return descripcionObstaculo; }
    public double getLatitud() { return latitud; }
    public double getLongitud() { return longitud; }
}
