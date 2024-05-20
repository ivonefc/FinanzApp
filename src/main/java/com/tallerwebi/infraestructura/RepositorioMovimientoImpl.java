package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionMovimientoNoEncontrado;
import com.tallerwebi.dominio.movimiento.Movimiento;
import com.tallerwebi.dominio.movimiento.RepositorioMovimiento;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository("repositorioMovimiento")
public class RepositorioMovimientoImpl implements RepositorioMovimiento {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioMovimientoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Movimiento> obtenerMovimientos(Long idUsuario) throws ExcepcionBaseDeDatos{ //BUSCA POR ID DE USUARIO
        try{
            Session session = sessionFactory.getCurrentSession();
            return session.createQuery("FROM Movimiento M WHERE M.usuario.id = :idUsuario", Movimiento.class)
                    .setParameter("idUsuario", idUsuario)
                    .getResultList();
        }catch (Exception he){
            throw new ExcepcionBaseDeDatos("Base de datos no disponible");
        }
    }

    @Override
    public Movimiento obtenerMovimientoPorId(Long id) throws ExcepcionMovimientoNoEncontrado, ExcepcionBaseDeDatos { //BUSCA POR ID DE MOVIMIENTO
        try {
            Session session = sessionFactory.getCurrentSession();
            Movimiento movimiento = session.createQuery("FROM Movimiento M WHERE M.id = :idMovimiento", Movimiento.class)
                    .setParameter("idMovimiento", id)
                    .uniqueResult();
            if(movimiento==null)
                throw new ExcepcionMovimientoNoEncontrado("No se encontro el movimiento");

            return movimiento;
        } catch (HibernateException he) {
            throw new ExcepcionBaseDeDatos("Base de datos no disponible", he);
        }
    }

    @Override
    public void actualizarMovimiento(Movimiento movimiento) throws ExcepcionBaseDeDatos, ExcepcionMovimientoNoEncontrado {
        if(movimiento==null)
            throw new ExcepcionMovimientoNoEncontrado("No se encontro el movimiento");

        try{
            Session session = sessionFactory.getCurrentSession();
            session.update(movimiento);
        }catch (Exception he){
            throw new ExcepcionBaseDeDatos("Base de datos no disponible");
        }
    }

    @Override
    public void guardarMovimiento(Movimiento movimiento) throws ExcepcionBaseDeDatos {
        try {
            Session session = sessionFactory.getCurrentSession();
            session.save(movimiento);
        }catch (Exception he){
            throw new ExcepcionBaseDeDatos("Base de datos no disponible");
        }
    }

    @Override
    public void eliminarMovimiento(Movimiento movimiento) throws ExcepcionBaseDeDatos, ExcepcionMovimientoNoEncontrado {
        if (movimiento == null || movimiento.getId() == null)
            throw new ExcepcionMovimientoNoEncontrado("No se encontro el movimiento");

        try {
            Session session = sessionFactory.getCurrentSession();
            Movimiento movimientoExistente = session.get(Movimiento.class, movimiento.getId());
            if (movimientoExistente == null)
                throw new ExcepcionMovimientoNoEncontrado("No se encontro el movimiento");

            session.delete(movimiento);
        } catch (HibernateException he) {
            throw new ExcepcionBaseDeDatos("Base de datos no disponible", he);
        } catch (ExcepcionMovimientoNoEncontrado e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    @Override
    public List<Movimiento> obtenerMovimientosPorFecha(Long idUsuario, LocalDate fecha) throws ExcepcionBaseDeDatos {
        try{
            Session session = sessionFactory.getCurrentSession();
            return session.createQuery("FROM Movimiento m WHERE m.usuario.id = :idUsuario AND m.fechayHora = :fecha", Movimiento.class)
                    .setParameter("idUsuario", idUsuario)
                    .setParameter("fecha", fecha)
                    .getResultList();
        }catch (HibernateException he) {
            throw new ExcepcionBaseDeDatos("Base de datos no disponible", he);
        }
    }
}
