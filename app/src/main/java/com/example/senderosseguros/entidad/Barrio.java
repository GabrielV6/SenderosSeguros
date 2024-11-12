package com.example.senderosseguros.entidad;

public class Barrio {

    int idBarrio;
    String descripcion;

    public Barrio () {

    }

    public Barrio(int idBarrio, String descripcion) {
        this.idBarrio = idBarrio;
        this.descripcion = descripcion;
    }

    public int getIdBarrio() {
        return idBarrio;
    }

    public void setIdBarrio(int idBarrio) {
        this.idBarrio = idBarrio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
