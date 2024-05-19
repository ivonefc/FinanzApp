package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.excepcion.ExcepcionCamposInvalidos;

import java.util.HashMap;
import java.util.Map;

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

    public void validarCampos() throws ExcepcionCamposInvalidos {
        Map<String, String> errores = new HashMap<>();

        if(this.descripcion.isEmpty()){
            errores.put("descripcion", "El campo es requerido");
        }
        if(this.monto == null){
            errores.put("monto", "El campo es requerido");
        }
        if(this.categoria.isEmpty()){
            errores.put("categoria", "El campo es requerido");
        }
        if(this.tipo.isEmpty()){
            errores.put("tipo", "El campo es requerido");
        }
        if(!errores.isEmpty()){
            throw new ExcepcionCamposInvalidos(errores);
        }
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
