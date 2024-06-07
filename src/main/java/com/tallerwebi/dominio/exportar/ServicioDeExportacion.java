package com.tallerwebi.dominio.exportar;

import com.itextpdf.text.DocumentException;
import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionExportacionDeArchivo;

public interface ServicioDeExportacion {

    byte[] generarArchivo(Long idUsuario, TipoDeArchivo tipoDeDoc) throws DocumentException, ExcepcionBaseDeDatos, ExcepcionExportacionDeArchivo;
}
