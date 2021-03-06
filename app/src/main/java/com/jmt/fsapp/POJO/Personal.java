package com.jmt.fsapp.POJO;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.jmt.fsapp.datatype.Cid;
import com.jmt.fsapp.datatype.Csalud;
import com.jmt.fsapp.datatype.Fecha;
import com.jmt.fsapp.datatype.LicienciaC;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Personal {
    private DatabaseReference reference;
    private String nombre;
    private String apellido;
    private String picture;
    private String domicilio;
    private String usuario;
    private String contrasena;
    private Fecha ingreso = new Fecha();
    private int acceso;
    private Fecha nacimiento = new Fecha();
    private Cid cid = new Cid();
    private String telefono;
    private LicienciaC licenciaC = new LicienciaC();
    private String categoria;
    private Csalud csalud = new Csalud();
    private Fecha cind = new Fecha();
    private String seccion;
    private String credencial; // Verificar si es necesario agregar tipo de dato



    public Personal() {
        this.nombre = "";
        this.apellido = "";
        this.picture = "https://firebasestorage.googleapis.com/v0/b/fsapp-b233f.appspot.com/o/sin%20imagen.jpg?alt=media&token=a78f6e9d-295d-4623-a6ac-579ff6301201";
        this.cid.imagen = "https://firebasestorage.googleapis.com/v0/b/fsapp-b233f.appspot.com/o/sin%20imagen.jpg?alt=media&token=a78f6e9d-295d-4623-a6ac-579ff6301201";
        this.csalud.imagen = "https://firebasestorage.googleapis.com/v0/b/fsapp-b233f.appspot.com/o/sin%20imagen.jpg?alt=media&token=a78f6e9d-295d-4623-a6ac-579ff6301201";
        this.licenciaC.imagen = "https://firebasestorage.googleapis.com/v0/b/fsapp-b233f.appspot.com/o/sin%20imagen.jpg?alt=media&token=a78f6e9d-295d-4623-a6ac-579ff6301201";
        this.cid.numero = "";
        this.nacimiento.dia = 1;
        this.nacimiento.mes = 1;
        this.nacimiento.ano = 1990;
        this.cid.venc.dia = 1;
        this.cid.venc.mes = 1;
        this.cid.venc.ano = 2021;
        this.licenciaC.cat ="";
        this.licenciaC.venc.dia = 1;
        this.licenciaC.venc.mes = 1;
        this.licenciaC.venc.ano = 2021;
        this.csalud.venc.dia = 1;
        this.csalud.venc.mes = 1;
        this.csalud.venc.ano = 2021;
        this.domicilio = "";
        this.seccion = "";
        this.usuario ="";
        this.telefono = "";
        this.categoria = "";
    }

    public DatabaseReference getReference() {
        return reference;
    }

    public void setReference(DatabaseReference reference) {
        this.reference = reference;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
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

    @Exclude
    public void setIngreso(String ingreso) {
        List<Integer> numeros = extraerNumeros(ingreso);
        this.ingreso.dia = numeros.get(0);
        this.ingreso.mes = numeros.get(1);
        this.ingreso.ano = numeros.get(2);
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

    @Exclude
    public void setNacimiento(String nacimiento) {
        List<Integer> numeros = extraerNumeros(nacimiento);
        this.nacimiento.dia = numeros.get(0);
        this.nacimiento.mes = numeros.get(1);
        this.nacimiento.ano = numeros.get(2);
    }

    public Cid getCid() {
        return cid;
    }

    public void setCid(Cid cid) {
        this.cid = cid;
    }

    @Exclude
    public void setCid(String venc, String numero, String imagen) {
        List<Integer> numeros = extraerNumeros(venc);
        this.cid.venc.dia = numeros.get(0);
        this.cid.venc.mes = numeros.get(1);
        this.cid.venc.ano = numeros.get(2);
        this.cid.numero = numero;
        this.cid.imagen = imagen;
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
        this.licenciaC.venc = lCvenc;
    }
    public void setLCcategoria(String lCcategoria){
        this.licenciaC.cat = lCcategoria;
    }
    public void setCidimagen(String cidimagen){
        this.cid.imagen = cidimagen;
    }
    public void setLCimagen(String lCimagen){
        this.licenciaC.imagen = lCimagen;
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

    public LicienciaC getLicenciaC() {
        return licenciaC;
    }

    public void setLicenciaC(LicienciaC licenciaC) {
        this.licenciaC = licenciaC;
    }
    @Exclude
    public void setLicienciaC(String venc, String cat, String imagen) {
        List<Integer> numeros = extraerNumeros(venc);
        this.licenciaC.venc.dia = numeros.get(0);
        this.licenciaC.venc.mes = numeros.get(1);
        this.licenciaC.venc.ano = numeros.get(2);
        this.licenciaC.cat = cat;
        this.licenciaC.imagen = imagen;
    }

    public Csalud getCsalud() {
        return csalud;
    }

    public void setCsalud(Csalud csalud) {
        this.csalud = csalud;
    }
    @Exclude
    public void setCsalud(String venc, String imagen) {
        List<Integer> numeros = extraerNumeros(venc);
        this.csalud.venc.dia = numeros.get(0);
        this.csalud.venc.mes = numeros.get(1);
        this.csalud.venc.ano = numeros.get(2);
        this.csalud.imagen = imagen;
    }

    public Fecha getCind() {
        return cind;
    }

    public void setCind(Fecha cind) {
        this.cind = cind;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public String getCredencial() {
        return credencial;
    }

    public void setCredencial(String credencial) {
        this.credencial = credencial;
    }


    List<Integer> extraerNumeros(String cadena) {
        List<Integer> todosLosNumeros = new ArrayList<Integer>();
        Matcher encuentrador = Pattern.compile("\\d+").matcher(cadena);
        while (encuentrador.find()) {
            todosLosNumeros.add(Integer.parseInt(encuentrador.group()));
        }
        return todosLosNumeros;
    }
}
