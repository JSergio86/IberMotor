package com.example.ibermotor;

import java.util.Date;
import java.util.List;

public class Post {
    public String uid, postId;
    public List<String> fotoCoche, favoritos;
    public String marca, modelo, combustible, color, cambioMarchas, descripcion, ciudad, codigoPostal;
    public int año, puertas, potencia, kilometros, precio;
    public double latitud, longitud;
    public Date date;

    public Post() {}

    public Post(String uid,String postId, List<String> fotosCoches, String marca, String modelo, int año, String combustible, int puertas, String color, int kilometros, String cambioMarchas, int potencia, int precio, String descripcion, String ciudad, String codigoPostal, double latitud, double longitud, Date date,  List<String> favoritos) {
        this.uid = uid;
        this.postId = postId;
        this.fotoCoche = fotosCoches;
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
        this.codigoPostal = codigoPostal;
        this.latitud = latitud;
        this.longitud = longitud;
        this.date = date;
        this.favoritos = favoritos;
    }
}
