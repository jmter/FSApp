package com.jmt.fsapp.POJO;

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
