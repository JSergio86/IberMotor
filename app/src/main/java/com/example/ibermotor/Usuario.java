package com.example.ibermotor;

public class Usuario {
    public String uid,fotoPerfil, nombre, correo;

    public Usuario() {
    }

    public Usuario(String uid, String fotoPerfil, String nombre, String correo) {
        this.uid = uid;
        this.fotoPerfil = fotoPerfil;
        this.nombre = nombre;
        this.correo = correo;

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

}
