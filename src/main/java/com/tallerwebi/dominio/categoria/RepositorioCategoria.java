package com.tallerwebi.dominio.categoria;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;

public interface RepositorioCategoria {
    CategoriaMovimiento obtenerCategoriaPorNombre(String nombre) throws ExcepcionBaseDeDatos;
}
