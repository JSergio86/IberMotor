package com.example.socialapp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Post {
    public String uid;
    public String mediaUrl;
    String fotoCoche;
    String precioTotal, precioText, nombreText, ciudadText, kilometrosText, añosText, combustibleText, garantia;

    // Constructor vacio requerido por Firestore
    public Post() {}


    public Post(String uid, String mediaUrl, String fotoCoche, String precioTotal, String precioText) {
        this.uid = uid;
        this.mediaUrl = mediaUrl;
        this.fotoCoche = fotoCoche;
        this.precioTotal = precioTotal;
        this.precioText = precioText;
    }

    public Post(String uid, String author, String fotoCoche, String precioTotal, String precioText, String nombreText, String ciudadText, String kilometrosText, String añosText, String combustibleText, String garantia) {
        this.uid = uid;
        this.mediaUrl = mediaUrl;
        this.fotoCoche = fotoCoche;
        this.precioTotal = precioTotal;
        this.precioText = precioText;
        this.nombreText = nombreText;
        this.ciudadText = ciudadText;
        this.kilometrosText = kilometrosText;
        this.añosText = añosText;
        this.combustibleText = combustibleText;
        this.garantia = garantia;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getFotoCoche() {
        return fotoCoche;
    }

    public void setFotoCoche(String fotoCoche) {
        this.fotoCoche = fotoCoche;
    }

    public String getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(String precioTotal) {
        this.precioTotal = precioTotal;
    }

    public String getPrecioText() {
        return precioText;
    }

    public void setPrecioText(String precioText) {
        this.precioText = precioText;
    }
}
