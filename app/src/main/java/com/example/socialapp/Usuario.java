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
}
