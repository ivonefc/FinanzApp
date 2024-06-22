package com.tallerwebi.dominio.categoria;


import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;

import java.util.List;

public interface ServicioCategoria {
    CategoriaMovimiento obtenerCategoriaPorNombre(String nombre) throws ExcepcionBaseDeDatos;
}
