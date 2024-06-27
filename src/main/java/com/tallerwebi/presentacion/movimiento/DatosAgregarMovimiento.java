package com.tallerwebi.presentacion.movimiento;

import com.tallerwebi.dominio.excepcion.ExcepcionCamposInvalidos;

import java.util.HashMap;
import java.util.Map;

public class DatosAgregarMovimiento {

    private String descripcion;
    private String tipo;
    private String categoria;
    private Double monto;
    private Long idAmigo;
    private Double montoAmigo;

    public DatosAgregarMovimiento(String descripcion, String tipo, String categoria, Double monto) {
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.categoria = categoria;
        this.monto = monto;
    }

    public DatosAgregarMovimiento(String descripcion, String tipo, String categoria, Double monto, Long idAmigo, Double montoAmigo) {
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.categoria = categoria;
        this.monto = monto;
        this.idAmigo = idAmigo;
        this.montoAmigo = montoAmigo;
    }

    public DatosAgregarMovimiento(){

    }

    public void validarCampos() throws ExcepcionCamposInvalidos {
        Map<String, String> errores = new HashMap<>();

        if(this.descripcion.isEmpty()){
            errores.put("descripcion", "El campo es requerido");
        }
        if(this.monto == null || this.monto == 0.0){
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

    public void validarCamposAmigo() throws ExcepcionCamposInvalidos {
        Map<String, String> erroresAm = new HashMap<>();

        if(this.descripcion.isEmpty()){
            erroresAm.put("descripcion", "El campo es requerido");
        }
        if(this.monto == null || this.monto == 0.0){
            erroresAm.put("monto", "El campo es requerido");
        }
        if(this.categoria.isEmpty()){
            erroresAm.put("categoria", "El campo es requerido");
        }
        if(this.tipo.isEmpty()){
            erroresAm.put("tipo", "El campo es requerido");
        }
        if(this.idAmigo == null || this.idAmigo == 0){
            erroresAm.put("idAmigo", "El campo es requerido");
        }
        if(this.montoAmigo == null || this.montoAmigo == 0.0){
            erroresAm.put("montoAmigo", "El campo es requerido");
        }
        if(!erroresAm.isEmpty()){
            throw new ExcepcionCamposInvalidos(erroresAm);
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

    public Long getIdAmigo() {
        return idAmigo;
    }

    public Double getMontoAmigo() {
        return montoAmigo;
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

    public void setIdAmigo(Long idAmigo) {
        this.idAmigo = idAmigo;
    }

    public void setMontoAmigo(Double montoAmigo) {
        this.montoAmigo = montoAmigo;
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
