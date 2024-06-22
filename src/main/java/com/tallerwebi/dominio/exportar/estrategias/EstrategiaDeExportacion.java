package com.tallerwebi.dominio.exportar.estrategias;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionExportacionDeArchivo;
import com.tallerwebi.dominio.exportar.TipoDeArchivo;

public interface EstrategiaDeExportacion {
    TipoDeArchivo obtenerTipoDeArchivo();

    byte[] generarArchivo(Long idUsuario) throws ExcepcionBaseDeDatos, ExcepcionExportacionDeArchivo;
}
