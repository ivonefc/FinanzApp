package com.tallerwebi.presentacion;

public class DatosAgregarMovimiento {

    private String descripcion;
    private String tipo;
    private String categoria;
    private Double monto;

    public DatosAgregarMovimiento(String descripcion, String tipo, String categoria, Double monto) {
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.categoria = categoria;
        this.monto = monto;
    }

    public DatosAgregarMovimiento(){

    }

    public Double getMonto() {
        return monto;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "DatosAgregarMovimiento{" +
                "descripcion='" + descripcion + '\'' +
                ", tipo='" + tipo + '\'' +
                ", categoria='" + categoria + '\'' +
                ", monto=" + monto +
                '}';
    }
}
