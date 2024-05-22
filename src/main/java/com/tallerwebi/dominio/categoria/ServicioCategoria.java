package com.tallerwebi.dominio.categoria;


import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;

public interface ServicioCategoria {
    CategoriaMovimiento obtenerCategoriaPorNombre(String nombre) throws ExcepcionBaseDeDatos;
}
