package com.tallerwebi.dominio.meta;

import com.tallerwebi.dominio.excepcion.*;
import com.tallerwebi.presentacion.meta.DatosEditarMeta;
import com.tallerwebi.presentacion.meta.DatosMeta;

import java.time.LocalDate;
import java.util.List;

public interface ServicioMeta {
    void guardarMeta(Long idUsuario, DatosMeta datosMeta) throws ExcepcionCamposInvalidos, ExcepcionBaseDeDatos, ExcepcionCategoriaConMetaExistente, UsuarioInexistente;

    Meta obtenerMetaPorId(Long idMeta) throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente;

    void eliminarMeta(Long idMeta) throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente;

    void actualizarMeta(DatosEditarMeta datosEditarMeta) throws ExcepcionCamposInvalidos, ExcepcionBaseDeDatos, ExcepcionMetaNoExistente;

    List<Meta> obtenerMetas(Long idUsuario) throws ExcepcionBaseDeDatos;

}
