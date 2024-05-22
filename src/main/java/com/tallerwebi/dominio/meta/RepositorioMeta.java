package com.tallerwebi.dominio.meta;

import com.tallerwebi.dominio.categoria.CategoriaMovimiento;
import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionCategoriaConMetaExistente;
import com.tallerwebi.dominio.usuario.Usuario;

public interface RepositorioMeta {
    void guardar(Meta meta) throws ExcepcionBaseDeDatos;

    void existeMetaConUsuarioYCategoria(Usuario usuario, CategoriaMovimiento categoria) throws ExcepcionCategoriaConMetaExistente, ExcepcionBaseDeDatos;

    Meta obtenerMetaPorId(Long idMeta) throws ExcepcionBaseDeDatos;
}
