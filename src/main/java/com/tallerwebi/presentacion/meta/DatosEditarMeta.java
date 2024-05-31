package com.tallerwebi.presentacion.meta;

import com.tallerwebi.dominio.categoria.CategoriaMovimiento;
import com.tallerwebi.dominio.excepcion.ExcepcionCamposInvalidos;
import com.tallerwebi.dominio.meta.Meta;

import java.util.HashMap;
import java.util.Map;

public class DatosEditarMeta {

    private Long id;
    private String categoria;
    private Double montoMeta;

    public DatosEditarMeta(Long id, String categoria, Double montoMeta) {
        this.id = id;
        this.categoria = categoria;
        this.montoMeta = montoMeta;
    }

    public DatosEditarMeta() {
    }

    public static DatosEditarMeta construirDesdeMeta(Meta meta) {
        Long id = meta.getId();
        CategoriaMovimiento categoriaMovimiento = meta.getCategoriaMovimiento();
        Double montoMeta = meta.getMontoMeta();

        return new DatosEditarMeta(id, categoriaMovimiento.getNombre(), montoMeta);
    }

    public void validarCampos() throws ExcepcionCamposInvalidos {
        Map<String, String> errores = new HashMap<>();
        if (this.categoria == null || this.categoria.isEmpty()) {
            errores.put("categoria", "El campo es requerido");
        }
        if (this.montoMeta == null) {
            errores.put("monto", "El campo es requerido");
        }
        if (!errores.isEmpty()) {
            throw new ExcepcionCamposInvalidos(errores);
        }
    }

    public Long getId() {
        return id;
    }

    public String getCategoria() {
        return categoria;
    }

    public Double getMontoMeta() {
        return montoMeta;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMontoMeta(Double montoMeta) {
        this.montoMeta = montoMeta;
    }
}
