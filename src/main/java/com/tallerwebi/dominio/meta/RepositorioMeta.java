package com.tallerwebi.dominio.meta;

import com.tallerwebi.dominio.categoria.CategoriaMovimiento;
import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionCategoriaConMetaExistente;
import com.tallerwebi.dominio.excepcion.ExcepcionMetaNoExistente;
import com.tallerwebi.dominio.usuario.Usuario;
import com.tallerwebi.presentacion.meta.DatosEditarMeta;
import com.tallerwebi.presentacion.meta.DatosMeta;

import java.time.LocalDate;
import java.util.List;

public interface RepositorioMeta {
    void guardar(Meta meta) throws ExcepcionBaseDeDatos;

    void existeMetaConUsuarioYCategoria(Usuario usuario, CategoriaMovimiento categoria) throws ExcepcionCategoriaConMetaExistente, ExcepcionBaseDeDatos;

    Meta obtenerMetaPorId(Long id) throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente;

    void eliminarMeta(Meta meta) throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente;

    void actualizarMeta(Meta meta) throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente;

    List<Meta> obtenerMetas(Long idUsuario) throws ExcepcionBaseDeDatos;

    Meta obtenerMetaPorCategoria(String Categoria) throws ExcepcionBaseDeDatos;
}
