package com.example.senderosseguros.entidad;

public class TipoObstaculo {

    private int idTipo;
    private String descripcion;

    public TipoObstaculo () {

    }

    public TipoObstaculo(int idTipo, String descripcion) {
        this.idTipo = idTipo;
        this.descripcion = descripcion;
    }

    public int getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(int idTipo) {
        this.idTipo = idTipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "TipoObstaculo{" +
                "idTipo=" + idTipo +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
