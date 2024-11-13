package com.example.senderosseguros.entidad;

import java.util.Date;

public class Obstaculo {

    private int idObstaculo;
    private TipoObstaculo tipoObstaculo;
    private String comentarios;
    private String imagen;
    private Punto punto;
    private Usuario usuario;
    private Date fechaCreacion;
    private Date fechaBaja;
    private int contadorSolucion;
    private boolean estado;

    public Obstaculo () {

    }

    public Obstaculo(int idObstaculo, TipoObstaculo tipoObstaculo, String comentarios, String imagen, Punto punto, Usuario usuario, Date fechaCreacion, Date fechaBaja, int contadorSolucion, boolean estado) {
        this.idObstaculo = idObstaculo;
        this.tipoObstaculo = tipoObstaculo;
        this.comentarios = comentarios;
        this.imagen = imagen;
        this.punto = punto;
        this.usuario = usuario;
        this.fechaCreacion = fechaCreacion;
        this.fechaBaja = fechaBaja;
        this.contadorSolucion = contadorSolucion;
        this.estado = estado;
    }

    public Obstaculo(TipoObstaculo tipoObstaculo, String comentarios, String imagen, Punto punto, Usuario usuario, Date fechaBaja, int contadorSolucion, boolean estado) {
        this.tipoObstaculo = tipoObstaculo;
        this.comentarios = comentarios;
        this.imagen = imagen;
        this.punto = punto;
        this.usuario = usuario;
        this.fechaBaja = fechaBaja;
        this.contadorSolucion = contadorSolucion;
        this.estado = estado;
    }

    public int getIdObstaculo() {
        return idObstaculo;
    }

    public void setIdObstaculo(int idObstaculo) {
        this.idObstaculo = idObstaculo;
    }

    public TipoObstaculo getTipoObstaculo() {
        return tipoObstaculo;
    }

    public void setTipoObstaculo(TipoObstaculo tipoObstaculo) {
        this.tipoObstaculo = tipoObstaculo;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Punto getPunto() {
        return punto;
    }

    public void setPunto(Punto punto) {
        this.punto = punto;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(Date fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public int getContadorSolucion() {
        return contadorSolucion;
    }

    public void setContadorSolucion(int contadorSolucion) {
        this.contadorSolucion = contadorSolucion;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Obstaculo{" +
                "idObstaculo=" + idObstaculo +
                ", tipoObstaculo=" + tipoObstaculo +
                ", comentarios='" + comentarios + '\'' +
                ", imagen='" + imagen + '\'' +
                ", punto=" + punto +
                ", usuario=" + usuario +
                ", fechaCreacion=" + fechaCreacion +
                ", fechaBaja=" + fechaBaja +
                ", contadorSolucion=" + contadorSolucion +
                ", estado=" + estado +
                '}';
    }
}
