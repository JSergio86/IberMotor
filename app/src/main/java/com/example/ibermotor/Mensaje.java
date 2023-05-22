package com.example.ibermotor;

import java.io.Serializable;

public class Mensaje implements Serializable {
    private String contenido;

    public Mensaje() {
        // Constructor vacío requerido para Firebase Database
    }

    public Mensaje(String contenido) {
        this.contenido = contenido;
    }

    public String getContenido() {
        return contenido;
    }
}
