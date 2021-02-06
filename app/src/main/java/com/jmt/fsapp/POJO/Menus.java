package com.jmt.fsapp.POJO;

import androidx.annotation.NonNull;

public class Menus {
    private String nombre;
    private String imagen;
    private String intent;

    public Menus(String nombre){
        this.nombre = nombre;

    }
    public Menus(String nombre, String intent){
        this.nombre = nombre;
        this.intent = intent;
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
