package com.tallerwebi.dominio.categoria;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ServicioCategoriaImpl implements ServicioCategoria{

    private RepositorioCategoria repositorioCategoria;

    @Autowired
    public ServicioCategoriaImpl(RepositorioCategoria repositorioCategoria) {
        this.repositorioCategoria = repositorioCategoria;
    }

    @Transactional
    @Override
    public CategoriaMovimiento obtenerCategoriaPorNombre(String nombre) throws ExcepcionBaseDeDatos {
        return repositorioCategoria.obtenerCategoriaPorNombre(nombre);
    }

    @Transactional
    @Override
    public List<CategoriaMovimiento> obtenerCategorias() throws ExcepcionBaseDeDatos {
        return repositorioCategoria.obtenerCategorias();
    }

    @Transactional
    public void actualizarColor(String nombre, String color) throws Exception {
        CategoriaMovimiento categoria = repositorioCategoria.obtenerCategoriaPorNombre(nombre);
        if (categoria == null) {
            throw new Exception("Categoría no encontrada");
        }
        categoria.setColor(color);
        repositorioCategoria.actualizarColor(categoria);
    }

}
