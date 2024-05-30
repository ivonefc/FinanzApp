package com.tallerwebi.dominio.meta;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionCamposInvalidos;
import com.tallerwebi.dominio.excepcion.ExcepcionCategoriaConMetaExistente;
import com.tallerwebi.dominio.excepcion.ExcepcionMetaNoExistente;
import com.tallerwebi.presentacion.meta.DatosEditarMeta;
import com.tallerwebi.presentacion.meta.DatosMeta;

import java.util.List;

public interface ServicioMeta {
    void guardarMeta(Long idUsuario, DatosMeta datosMeta) throws ExcepcionCamposInvalidos, ExcepcionBaseDeDatos, ExcepcionCategoriaConMetaExistente;

    Meta obtenerMetaPorId(Long idMeta) throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente;

    void eliminarMeta(Long idMeta) throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente;

    void actualizarMeta(DatosEditarMeta datosEditarMeta) throws ExcepcionCamposInvalidos, ExcepcionBaseDeDatos, ExcepcionMetaNoExistente;

    List<Meta> obtenerMetas(Long idUsuario) throws ExcepcionBaseDeDatos;
}
