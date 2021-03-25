package com.jmt.fsapp.POJO;

import android.widget.Switch;

import com.jmt.fsapp.datatype.Dim;
import com.jmt.fsapp.datatype.Hkm;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Equipo {

    private int preid;
    private String id;
    private String marca;
    private String modelo;
    private String categoria;
    private String subcategoria;
    private String foto;


    public Equipo(){

    }

    public Equipo(String marca, String modelo, String categoria, String subcategoria) {
        this.marca = marca;
        this.modelo = modelo;
        this.categoria = categoria;
        this.subcategoria = subcategoria;
        this.foto = "https://firebasestorage.googleapis.com/v0/b/fsapp-b233f.appspot.com/o/sin%20imagen.jpg?alt=media&token=a78f6e9d-295d-4623-a6ac-579ff6301201";

    }

    public String getId() {
        return id;
    }

    public int getPreid() {
        return preid;
    }

    public void setPreid(int preid) {
        this.preid = preid;
        switch (categoria){
            case "Maquinaria vial":
                switch (subcategoria){
                    case "Motoniveladoras": this.id = "MVM"+preid; break;
                    case "Compactadores":this.id = "MVC"+preid; break;
                    case "Barredoras":this.id = "MVB"+preid; break;
                    case "Gravilladoras":this.id = "MVG"+preid; break;
                    case "Retroexcavadoras":this.id = "MVE"+preid; break;
                    case "Combinadas": this.id = "MVC"+preid; break;
                    case "Palas": this.id = "MVP"+preid; break;
                }
                break;
            case "Autos y camionetas":
                switch (subcategoria) {
                    case "Autos": this.id = "AA"+preid; break;
                    case "Camionetas": this.id = "CC"+preid;break;
                } break;
            case "Camiones":
                switch (subcategoria) {
                    case "Transporte":this.id = "CT"+preid; break;
                    case "Especiales":this.id = "CE"+preid; break;
                } break;
            case "Tanques":
                switch (subcategoria) {
                    case "Asfalto": this.id = "TH"+preid;break;
                    case "Agua":this.id = "TA"+preid; break;
                    case "Gasoil":this.id = "TG"+preid; break;
                } break;
            case "Equipos Menores":
                switch (subcategoria) {
                    case "Bombas":this.id = "EMB"+preid; break;
                    case "Generadores":this.id = "EMG"+preid; break;
                    case "Vibradores":this.id = "EMV"+preid; break;
                    case "Planchas":this.id = "EMP"+preid; break;
                    case "Aserradoras":this.id = "EMA"+preid; break;
                } break;
        }
    }


    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getSubcategoria() {
        return subcategoria;
    }

    public void setSubcategoria(String subcategoria) {
        this.subcategoria = subcategoria;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
