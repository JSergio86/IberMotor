package com.example.ibermotor;

import java.util.Date;
import java.util.List;

public class Post {
    public String uid;
    public List<String> fotoCoche;
    public String marca, modelo, combustible, color,puertas, potencia, precio,kilometros, cambioMarchas, descripcion, ciudad;
    public int año;
    public double latitud, longitud;
    public Date date;

    public Post() {}

    public Post(String uid, List<String> fotoCoche, String marca, String modelo, int año, String combustible, String puertas, String color, String kilometros, String cambioMarchas, String potencia, String precio, String descripcion, String ciudad, double latitud, double longitud, Date date) {
        this.uid = uid;
        this.fotoCoche = fotoCoche;
        this.marca = marca;
        this.modelo = modelo;
        this.año = año;
        this.combustible = combustible;
        this.puertas = puertas;
        this.color = color;
        this.kilometros = kilometros;
        this.cambioMarchas = cambioMarchas;
        this.potencia = potencia;
        this.precio = precio;
        this.descripcion = descripcion;
        this.ciudad = ciudad;
        this.latitud = latitud;
        this.longitud = longitud;
        this.date = date;
    }

}
