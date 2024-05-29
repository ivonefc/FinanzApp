package com.tallerwebi.presentacion.meta;

import com.tallerwebi.dominio.categoria.CategoriaMovimiento;
import com.tallerwebi.dominio.excepcion.ExcepcionCamposInvalidos;
import com.tallerwebi.dominio.meta.Meta;

import java.util.HashMap;
import java.util.Map;

public class DatosEditarMeta {

    private Long id;
    private CategoriaMovimiento categoriaMovimiento;
    private Double montoMeta;

    public DatosEditarMeta(Long id, CategoriaMovimiento categoriaMovimiento, Double montoMeta) {
        this.id = id;
        this.categoriaMovimiento = categoriaMovimiento;
        this.montoMeta = montoMeta;
    }

    public DatosEditarMeta() {
    }

    public static DatosEditarMeta construirDesdeMeta(Meta meta) {
        Long id = meta.getId();
        CategoriaMovimiento categoriaMovimiento = meta.getCategoriaMovimiento();
        Double montoMeta = meta.getMontoMeta();

        return new DatosEditarMeta(id, categoriaMovimiento, montoMeta);
    }

    public void validarCampos() throws ExcepcionCamposInvalidos {
        Map<String, String> errores = new HashMap<>();
        if (this.categoriaMovimiento == null || this.categoriaMovimiento.getNombre().isEmpty()) {
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

    public CategoriaMovimiento getCategoriaMovimiento() {
        return categoriaMovimiento;
    }

    public Double getMontoMeta() {
        return montoMeta;
    }

}
