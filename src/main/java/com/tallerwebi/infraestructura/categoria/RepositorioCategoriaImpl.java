package com.tallerwebi.infraestructura.categoria;

import com.tallerwebi.dominio.categoria.CategoriaMovimiento;
import com.tallerwebi.dominio.categoria.RepositorioCategoria;
import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import org.hibernate.HibernateException;
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
    public CategoriaMovimiento obtenerCategoriaPorNombre(String nombre) throws ExcepcionBaseDeDatos {
        try{
            Session session = sessionFactory.getCurrentSession();
            return session.createQuery("FROM CategoriaMovimiento c WHERE c.nombre = :nombre", CategoriaMovimiento.class)
                    .setParameter("nombre", nombre)
                    .uniqueResult();
        }catch(HibernateException e){
            throw new ExcepcionBaseDeDatos();
        }


    }
}
