package com.jmt.fsapp.POJO;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

public class ReciboDeSueldo implements Serializable, Comparable<ReciboDeSueldo> {
    private String cid;
    private String fecha;
    private String firma;
    private String mes;
    private String tipo;
    private String url;
    private String usuario;
    private String ano;


    public ReciboDeSueldo() {

    }

    public ReciboDeSueldo(String fecha, String firma, String mes,String ano, String tipo, String url,String cid) {
        this.fecha = fecha;
        this.firma = firma;
        this.mes = mes;
        this.tipo = tipo;
        this.url = url;
        this.cid = cid;
        this.ano = ano;

    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    @Override
    public int compareTo(ReciboDeSueldo reciboDeSueldo) {
        int index;
        if (Integer.valueOf(ano) <= Integer.valueOf(reciboDeSueldo.ano)){
                if(mesToNum(mes) <= mesToNum(reciboDeSueldo.mes)){
                    if(mesToNum(mes) == mesToNum(reciboDeSueldo.mes)){
                        index = 0;
                    }else {
                        index = -1;
                    }
                }
                else{
                    index = 1;
                }
        }
        else{
           index = 1;
        }
        return index;
    }
    public int mesToNum(String mes){
        int index = 0;
        mes = mes.toUpperCase();
        switch (mes){
            case "ENERO":
                index = 1;
                break;
            case "FEBRERO":
                index = 2;
                break;
            case "MARZO":
                index = 3;
                break;
            case "ABRIL":
                index = 4;
                break;
            case "MAYO":
                index = 5;
                break;
            case "JUNIO":
                index = 6;
                break;
            case "JULIO":
                index = 7;
                break;
            case "AGOSTO":
                index = 8;
                break;
            case "SETIEMBRE":
                index = 9;
                break;
            case "SEPTIEMBRE":
                index = 9;
                break;
            case "OCTUBRE":
                index = 10;
                break;
            case "NOVIEMBRE":
                index = 11;
                break;
            case "DICIEMBRE":
                index = 12;
                break;
        }

        return index;
    }
}
