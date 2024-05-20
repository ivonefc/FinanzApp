package com.tallerwebi.dominio.categoria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServicioCategoriaImpl implements ServicioCategoria{

    private RepositorioCategoria repositorioCategoria;

    @Autowired
    public ServicioCategoriaImpl(RepositorioCategoria repositorioCategoria) {
        this.repositorioCategoria = repositorioCategoria;
    }

    @Transactional
    @Override
    public CategoriaMovimiento obtenerCategoriaPorNombre(String nombre) {
        return repositorioCategoria.obtenerCategoriaPorNombre(nombre);
    }
}
