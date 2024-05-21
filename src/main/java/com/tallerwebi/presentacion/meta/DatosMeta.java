package com.tallerwebi.presentacion.meta;

import com.tallerwebi.dominio.excepcion.ExcepcionCamposInvalidos;

import java.util.HashMap;
import java.util.Map;

public class DatosMeta {
    private String categoria;
    private Double monto;

    public DatosMeta(String categoria, Double monto) {
        this.categoria = categoria;
        this.monto = monto;
    }

    public DatosMeta() {
    }

    public boolean tieneCategoria() {
        return categoria != null && !categoria.isEmpty();
    }

    public boolean tieneMonto() {
        return monto != null;
    }

    public void validarCampos() throws ExcepcionCamposInvalidos {
        Map<String, String> errores = new HashMap<>();
        if(!tieneCategoria()){
            errores.put("categoria", "El campo es requerido");
        }
        if(!tieneMonto()){
            errores.put("monto", "El campo es requerido");
        }
        if(!errores.isEmpty()){
            throw new ExcepcionCamposInvalidos(errores);
        }
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }
}
