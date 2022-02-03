package com.jmt.fsapp.POJO;

public class OrdenCompraEstacionServicio {
    private String operario;
    private String operarioName;
    private String fechayhorasolicitud;
    private String producto;
    private String estado;
    private String responsable;
    private String fechayhoraautorizacion;
    private String codigo;
    private String fechayhoracompleto;
    private String cantidadcargada;
    private String horaskm;
    private String maximoAutorizado;
    private String key;
    private Equipo equipo;
    private Estacion estacion;

    public OrdenCompraEstacionServicio() {
    }
    public void NuevaSolicitud(String operario,String fechayhorasolicitud,Equipo equipo,String estado,Estacion estacion,String producto){
        this.producto = producto;
        this.operario = operario;
        this.fechayhorasolicitud = fechayhorasolicitud;
        this.equipo = equipo;
        this.estado = estado;
        this.estacion = estacion;
    }

    public String getFechayhoracompleto() {
        return fechayhoracompleto;
    }

    public void setFechayhoracompleto(String fechayhoracompleto) {
        this.fechayhoracompleto = fechayhoracompleto;
    }

    public String getOperario() {
        return operario;
    }

    public void setOperario(String operario) {
        this.operario = operario;
    }

    public String getFechayhorasolicitud() {
        return fechayhorasolicitud;
    }

    public void setFechayhorasolicitud(String fechayhorasolicitud) {
        this.fechayhorasolicitud = fechayhorasolicitud;
    }

    public String getOperarioName() {
        return operarioName;
    }

    public void setOperarioName(String operarioName) {
        this.operarioName = operarioName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getHoraskm() {
        return horaskm;
    }

    public void setHoraskm(String horaskm) {
        this.horaskm = horaskm;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getFechayhoraautorizacion() {
        return fechayhoraautorizacion;
    }

    public void setFechayhoraautorizacion(String fechayhoraautorizacion) {
        this.fechayhoraautorizacion = fechayhoraautorizacion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public Estacion getEstacion() {
        return estacion;
    }

    public void setEstacion(Estacion estacion) {
        this.estacion = estacion;
    }

    public String getMaximoAutorizado() {
        return maximoAutorizado;
    }

    public void setMaximoAutorizado(String maximoAutorizado) {
        this.maximoAutorizado = maximoAutorizado;
    }

    public String getCantidadcargada() {
        return cantidadcargada;
    }

    public void setCantidadcargada(String cantidadcargada) {
        this.cantidadcargada = cantidadcargada;
    }
}
