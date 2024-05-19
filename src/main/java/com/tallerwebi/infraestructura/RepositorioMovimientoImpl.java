package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionMovimientoNoEncontrado;
import com.tallerwebi.dominio.movimiento.Movimiento;
import com.tallerwebi.dominio.movimiento.RepositorioMovimiento;
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
    public List<Movimiento> obtenerMovimientos(Long idUsuario) throws ExcepcionBaseDeDatos{
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
    public Movimiento obtenerMovimientoPorId(Long id) throws ExcepcionMovimientoNoEncontrado {
        Session session = sessionFactory.getCurrentSession();
        Movimiento movimiento = session.createQuery("FROM Movimiento M WHERE M.id = :idMovimiento", Movimiento.class)
                .setParameter("idMovimiento", id)
                .uniqueResult();
        if(movimiento==null){
            throw new ExcepcionMovimientoNoEncontrado("No se encontro el movimiento");
        }
        return movimiento;
    }

    @Override
    public void actualizarMovimiento(Movimiento movimiento) throws ExcepcionBaseDeDatos {
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
    public void eliminarMovimiento(Movimiento movimiento) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(movimiento);
    }

    @Override
    public List<Movimiento> obtenerMovimientosPorFecha(Long idUsuario, LocalDate fecha) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM Movimiento m WHERE m.usuario.id = :idUsuario AND m.fechayHora = :fecha", Movimiento.class)
                .setParameter("idUsuario", idUsuario)
                .setParameter("fecha", fecha)
                .getResultList();
    }
}
