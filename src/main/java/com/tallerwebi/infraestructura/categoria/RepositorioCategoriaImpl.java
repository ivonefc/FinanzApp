package com.tallerwebi.infraestructura.categoria;

import com.tallerwebi.dominio.categoria.CategoriaMovimiento;
import com.tallerwebi.dominio.categoria.RepositorioCategoria;
import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionMovimientoNoEncontrado;
import com.tallerwebi.dominio.movimiento.Movimiento;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RepositorioCategoriaImpl implements RepositorioCategoria {

    @PersistenceContext
    private EntityManager entityManager;

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioCategoriaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
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

    @Override
    public List<CategoriaMovimiento> obtenerCategorias() throws ExcepcionBaseDeDatos {
        try{
            Session session = sessionFactory.getCurrentSession();
            return session.createQuery("FROM CategoriaMovimiento", CategoriaMovimiento.class)
                    .getResultList();
        }catch(HibernateException e){
            throw new ExcepcionBaseDeDatos();
        }
    }

    public void actualizarColor(CategoriaMovimiento categoria) throws ExcepcionBaseDeDatos {

        try {
            entityManager.merge(categoria);
        } catch (Exception e) {
            throw new ExcepcionBaseDeDatos("Base de datos no disponible");
        }
    }

}
