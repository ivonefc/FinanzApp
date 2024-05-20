package com.tallerwebi.infraestructura.categoria;

import com.tallerwebi.dominio.categoria.CategoriaMovimiento;
import com.tallerwebi.dominio.categoria.RepositorioCategoria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RepositorioCategoriaImpl implements RepositorioCategoria {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioCategoriaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public CategoriaMovimiento obtenerCategoriaPorNombre(String nombre) {
        Session session = sessionFactory.getCurrentSession();
        CategoriaMovimiento categoriaMovimiento = session.createQuery("FROM CategoriaMovimiento c WHERE c.nombre = :nombre", CategoriaMovimiento.class)
                .setParameter("nombre", nombre)
                .uniqueResult();
        return categoriaMovimiento;
    }
}
