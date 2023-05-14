package com.example.socialapp;

public class Usuario {
    public String uid,fotoPerfil, nombre, correo;
    public double latitud, longitud;
    public boolean registrado;

    public Usuario() {
    }

    public Usuario(String uid, String fotoPerfil, String nombre, String correo, double latitud, double longitud, boolean registrado) {
        this.uid = uid;
        this.fotoPerfil = fotoPerfil;
        this.nombre = nombre;
        this.correo = correo;
        this.latitud = latitud;
        this.longitud = longitud;
        this.registrado = registrado;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
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

    public boolean isRegistrado() {
        return registrado;
    }

    public void setRegistrado(boolean registrado) {
        this.registrado = registrado;
    }
}
