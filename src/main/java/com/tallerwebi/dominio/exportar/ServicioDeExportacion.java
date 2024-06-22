package com.tallerwebi.dominio.exportar;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionExportacionDeArchivo;
import com.tallerwebi.dominio.exportar.estrategias.EstrategiaDeExportacion;

import java.util.Map;

public interface ServicioDeExportacion {

    byte[] generarArchivo(Long idUsuario, TipoDeArchivo tipoDeDoc) throws ExcepcionBaseDeDatos, ExcepcionExportacionDeArchivo;
    Map<TipoDeArchivo, EstrategiaDeExportacion> obtenerEstrategiasDeExportacion();
}
