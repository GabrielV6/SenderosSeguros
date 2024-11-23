package com.example.senderosseguros.entidad;

import com.google.android.gms.maps.model.LatLng;

public class SessionManager {

    private static SessionManager instance;
    private int ID_User;
    private LatLng origenRuta;
    private LatLng destinoRuta;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public int getID_User() {
        return ID_User;
    }

    public void setID_User(int ID_User) {
        this.ID_User = ID_User;
    }
    public LatLng getOrigenRuta() {
        return origenRuta;
    }

    public void setOrigenRuta(LatLng origenRuta) {
        this.origenRuta = origenRuta;
    }

    public LatLng getDestinoRuta() {
        return destinoRuta;
    }

    public void setDestinoRuta(LatLng destinoRuta) {
        this.destinoRuta = destinoRuta;
    }

    public void clearRuta() {
        this.origenRuta = null;
        this.destinoRuta = null;
    }
}
