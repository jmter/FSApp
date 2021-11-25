package com.jmt.fsapp.POJO;

public class ReciboDeSueldo {
    private String cid;
    private String fecha;
    private String firma;
    private String mes;
    private String tipo;
    private String url;
    private String usuario;

    public ReciboDeSueldo() {

    }

    public ReciboDeSueldo(String fecha, String firma, String mes, String tipo, String url) {
        this.fecha = fecha;
        this.firma = firma;
        this.mes = mes;
        this.tipo = tipo;
        this.url = url;
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
}
