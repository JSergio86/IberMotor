package com.example.socialapp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Post {
    public String uid;
    public String mediaUrl;
    public String fotoCoche,precioTotal, precioText, nombreText, ciudadText, kilometrosText, añosText, combustibleText, garantia;

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

}
