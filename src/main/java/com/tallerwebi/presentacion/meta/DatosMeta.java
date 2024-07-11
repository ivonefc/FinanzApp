package com.tallerwebi.presentacion.meta;

import com.tallerwebi.dominio.excepcion.ExcepcionCamposInvalidos;
import com.tallerwebi.dominio.usuario.Usuario;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DatosMeta {
    private String categoria;
    private Double monto;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaInicio;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaFin;



    public DatosMeta(String categoria, Double monto, Date fechaInicio, Date fechaFin) {
        this.categoria = categoria;
        this.monto = monto;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public DatosMeta(String categoria, Double monto) {
        this.categoria = categoria;
        this.monto = monto;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
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
        if(fechaInicio == null){
            errores.put("fechaInicio", "La fecha de inicio es requerida");
        }
        if(fechaFin == null){
            errores.put("fechaFin", "La fecha de fin es requerida");
        }
        if(!(fechaFin == null) && fechaFin.before(fechaInicio)){
            errores.put("fechaFin", "La fecha de fin es incorrecta");
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

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }


}
