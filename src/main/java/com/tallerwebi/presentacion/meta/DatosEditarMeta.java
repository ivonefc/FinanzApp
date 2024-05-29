package com.tallerwebi.presentacion.meta;

import com.tallerwebi.dominio.categoria.CategoriaMovimiento;
import com.tallerwebi.dominio.meta.Meta;

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

    public Long getId() {
        return id;
    }
}
