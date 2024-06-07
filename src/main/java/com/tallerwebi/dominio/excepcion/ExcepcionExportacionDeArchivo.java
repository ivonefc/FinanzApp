package com.tallerwebi.dominio.excepcion;

import com.itextpdf.text.DocumentException;

public class ExcepcionExportacionDeArchivo extends Throwable {
    private static final String mensaje = "No se pudo exportar archivo";
    public ExcepcionExportacionDeArchivo(DocumentException e) {
        super(mensaje);
    }
}
