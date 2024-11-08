package com.example.senderosseguros.entidad;

import java.util.Date;

public class Usuario {

    private int ID_Usuario;
    private String Nombre;
    private String Apellido;
    private String DNI;
    private String User;
    private String Pass;
    private String Correo;
    private Date FechaRegistro;
    private int Puntaje;
    private boolean Estado;

    public Usuario () {

    }

    public Usuario(int ID_Usuario, String nombre, String apellido, String DNI, String user, String pass, String correo, Date fechaRegistro, int puntaje, boolean estado) {
        this.ID_Usuario = ID_Usuario;
        Nombre = nombre;
        Apellido = apellido;
        this.DNI = DNI;
        User = user;
        Pass = pass;
        Correo = correo;
        FechaRegistro = fechaRegistro;
        Puntaje = puntaje;
        Estado = estado;
    }

    public int getID_Usuario() {
        return ID_Usuario;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public String getDNI() {
        return DNI;
    }

    public String getUser() {
        return User;
    }

    public String getPass() {
        return Pass;
    }

    public int getPuntaje() {
        return Puntaje;
    }

    public String getCorreo() {
        return Correo;
    }

    public boolean isEstado() {
        return Estado;
    }

    public Date getFechaRegistro() {
        return FechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        FechaRegistro = fechaRegistro;
    }

    /*public void setID_Usuario(int ID_Usuario) {
        this.ID_Usuario = ID_Usuario;
    }*/

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public void setApellido(String apellido) {
        Apellido = apellido;
    }

    public void setDNI(String DNI) {
        this.DNI = DNI;
    }

    public void setUser(String user) {
        User = user;
    }

    public void setPass(String pass) {
        Pass = pass;
    }

    public void setPuntaje(int puntaje) {
        Puntaje = puntaje;
    }

    public void setEstado(boolean estado) {
        Estado = estado;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "ID_Usuario=" + ID_Usuario +
                ", Nombre='" + Nombre + '\'' +
                ", Apellido='" + Apellido + '\'' +
                ", DNI='" + DNI + '\'' +
                ", User='" + User + '\'' +
                ", Pass='" + Pass + '\'' +
                ", Correo='" + Correo + '\'' +
                ", FechaRegistro=" + FechaRegistro +
                ", Puntaje=" + Puntaje +
                ", Estado=" + Estado +
                '}';
    }
}
