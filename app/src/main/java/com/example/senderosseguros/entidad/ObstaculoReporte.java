package com.example.senderosseguros.entidad;

public class ObstaculoReporte {
    private String descripcion;
    private int cantidad;

    // Constructor
    public ObstaculoReporte(String descripcion, int cantidad) {
        this.descripcion = descripcion;
        this.cantidad = cantidad;
    }


    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
