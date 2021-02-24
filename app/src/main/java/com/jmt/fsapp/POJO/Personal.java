package com.jmt.fsapp.POJO;

import com.jmt.fsapp.datatype.Cid;
import com.jmt.fsapp.datatype.Csalud;
import com.jmt.fsapp.datatype.Fecha;
import com.jmt.fsapp.datatype.LicienciaC;
import com.jmt.fsapp.datatype.Seccion;

public class Personal {
    private String nombre;
    private String apellido;
    private String picture;
    private String domicilio;
    private String usuario;
    private Fecha ingreso;
    private int acceso;
    private Fecha nacimiento;
    private Cid cid = new Cid();
    private String telefono;
    private LicienciaC licienciaC = new LicienciaC();
    private String categoria;
    private Csalud csalud = new Csalud();
    private Fecha cind;
    private Seccion seccion;
    private String credencial; // Verificar si es necesario agregar tipo de dato

    public Personal() {

    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Fecha getIngreso() {
        return ingreso;
    }

    public void setIngreso(Fecha ingreso) {
        this.ingreso = ingreso;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getAcceso() {
        return acceso;
    }

    public void setAcceso(int acceso) {
        this.acceso = acceso;
    }

    public Fecha getNacimiento() {
        return nacimiento;
    }

    public void setNacimiento(Fecha nacimiento) {
        this.nacimiento = nacimiento;
    }

    public Cid getCid() {
        return cid;
    }

    public void setCid(Cid cid) {
        this.cid = cid;
    }

    public void setCidnumero(String cidnumero){
        this.cid.numero = cidnumero;
    }

    public void setCidvenc(Fecha cidvenc){
        this.cid.venc = cidvenc;
    }
    public void setCSvenc(Fecha cSvenc){
        this.csalud.venc = cSvenc;
    }
    public void setLCvenc(Fecha lCvenc){
        this.licienciaC.venc = lCvenc;
    }
    public void setLCcategoria(String lCcategoria){
        this.licienciaC.cat = lCcategoria;
    }
    public void setCidimagen(String cidimagen){
        this.cid.imagen = cidimagen;
    }
    public void setLCimagen(String lCimagen){
        this.licienciaC.imagen = lCimagen;
    }
    public void setCSimagen(String cSimagen){
        this.csalud.imagen = cSimagen;
    }
    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public LicienciaC getLicienciaC() {
        return licienciaC;
    }

    public void setLicienciaC(LicienciaC licienciaC) {
        this.licienciaC = licienciaC;
    }

    public Csalud getCsalud() {
        return csalud;
    }

    public void setCsalud(Csalud csalud) {
        this.csalud = csalud;
    }

    public Fecha getCind() {
        return cind;
    }

    public void setCind(Fecha cind) {
        this.cind = cind;
    }

    public Seccion getSeccion() {
        return seccion;
    }

    public void setSeccion(Seccion seccion) {
        this.seccion = seccion;
    }

    public String getCredencial() {
        return credencial;
    }

    public void setCredencial(String credencial) {
        this.credencial = credencial;
    }
}
