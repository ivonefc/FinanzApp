package com.tallerwebi.presentacion.movimiento;

import com.tallerwebi.dominio.excepcion.ExcepcionCamposInvalidos;
import com.tallerwebi.dominio.movimiento.Movimiento;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class DatosEditarMovimiento {
    private Long id;
    private String descripcion;
    private String categoria;
    private String tipo;
    private Double monto;
    private LocalDate fechayHora;

    public DatosEditarMovimiento(Long id, String descripcion, String categoria, String tipo, Double monto, LocalDate fechayHora) {
        this.id = id;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.tipo = tipo;
        this.monto = monto;
        this.fechayHora = fechayHora;
    }

    public DatosEditarMovimiento() {
    }

    public static DatosEditarMovimiento contruirDesdeMovimiento(Movimiento movimiento){
        Long id = movimiento.getId();
        String descripcion = movimiento.getDescripcion();
        String categoria = movimiento.getCategoria().getNombre();
        String tipo = movimiento.getCategoria().getTipo().getNombre();
        LocalDate fechayHora = movimiento.getFechayHora();
        Double monto = movimiento.getMonto();

        return new DatosEditarMovimiento(
                id,
                descripcion,
                categoria,
                tipo,
                monto,
                fechayHora
        );
    }

    public void validarCampos() throws ExcepcionCamposInvalidos {
        Map<String, String> errores = new HashMap<>();

        if(this.descripcion == null || this.descripcion.isEmpty())
            errores.put("descripcion", "El campo es requerido");

        if(this.tipo == null || this.tipo.isEmpty())
            errores.put("tipo", "El campo es requerido");

        if(this.categoria == null || this.categoria.isEmpty())
            errores.put("categoria", "El campo es requerido");

        if(this.monto == null)
            errores.put("monto", "El campo es requerido");

        if(!errores.isEmpty())
            throw new ExcepcionCamposInvalidos(errores);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public LocalDate getFechayHora() {
        return fechayHora;
    }

    public void setFechayHora(LocalDate fechayHora) {
        this.fechayHora = fechayHora;
    }

    @Override
    public String toString() {
        return "DatosEditarMovimiento{" +
                "id=" + id +
                ", descripcion='" + descripcion + '\'' +
                ", categoria='" + categoria + '\'' +
                ", tipo='" + tipo + '\'' +
                ", monto=" + monto +
                ", fechayHora=" + fechayHora +
                '}';
    }
}
