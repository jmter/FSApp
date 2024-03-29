package com.jmt.fsapp.POJO;

import androidx.annotation.NonNull;

public class Menus {
    private String nombre;
    private String imagen;
    private String intent;
    private Integer authR;

    public Menus(String nombre){
        this.nombre = nombre;

    }
    public Menus(String nombre, String intent){
        this.nombre = nombre;
        this.intent = intent;
    }

    public Menus(String nombre, String imagen, String intent) {
        this.nombre = nombre;
        this.imagen = imagen;
        this.intent = intent;
    }

    public Menus(String nombre, String imagen, String intent, Integer authR) {
        this.nombre = nombre;
        this.imagen = imagen;
        this.intent = intent;
        this.authR = authR;
    }

    @NonNull
    @Override
    public String toString() {

        return "Nombre: "+ nombre + " Intent: "+ intent + " Imagen: "+ imagen ;
    }

    public String getNombre() {
        return nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public String getIntent() {
        return intent;
    }
}
