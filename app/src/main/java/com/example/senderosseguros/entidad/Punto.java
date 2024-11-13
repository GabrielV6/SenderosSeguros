package com.example.senderosseguros.entidad;

public class Punto {

    private int idPunto;
    private double latitud;
    private double longitud;
    private Barrio barrio;

    public Punto () {

    }

    public Punto(int idPunto, double latitud, double longitud, Barrio barrio) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.barrio = barrio;
    }

    public int getIdPunto() {
        return idPunto;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public Barrio getBarrio() {
        return barrio;
    }

    public void setBarrio(Barrio barrio) {
        this.barrio = barrio;
    }

    public void setIdPunto(int idPunto) {
        this.idPunto = idPunto;
    }

    @Override
    public String toString() {
        return "Punto{" +
                "idPunto=" + idPunto +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", barrio=" + barrio +
                '}';
    }
}
