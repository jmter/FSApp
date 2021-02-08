package com.jmt.fsapp.POJO;

import com.jmt.fsapp.datatype.Cid;
import com.jmt.fsapp.datatype.LicienciaC;
import com.jmt.fsapp.datatype.Seccion;

import java.util.Date;

public class Personal {
    private String nombre;
    private String apellido;
    private String usuario;
    private int acceso;
    private Date nacimiento;
    private Cid cid;
    private String telefono;
    private LicienciaC licienciaC;
    private Date csalud;
    private Date cind;
    private Seccion seccion;
    private String credencial; // Verificar si es necesario agregar tipo de dato

    public Personal() {

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

    public Date getNacimiento() {
        return nacimiento;
    }

    public void setNacimiento(Date nacimiento) {
        this.nacimiento = nacimiento;
    }

    public Cid getCid() {
        return cid;
    }

    public void setCid(Cid cid) {
        this.cid = cid;
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

    public Date getCsalud() {
        return csalud;
    }

    public void setCsalud(Date csalud) {
        this.csalud = csalud;
    }

    public Date getCind() {
        return cind;
    }

    public void setCind(Date cind) {
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
