package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.notificacion.Notificacion;
import com.tallerwebi.dominio.notificacion.RepositorioNotificacion;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("repositorioNotificacion")
public class RepositorioNotificacionImpl implements RepositorioNotificacion {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioNotificacionImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public void guardar(Notificacion notificacion) throws ExcepcionBaseDeDatos {
        try {
            Session session = sessionFactory.getCurrentSession();
            session.save(notificacion);
        } catch (HibernateException e) {
            throw new ExcepcionBaseDeDatos("Base de datos no disponible", e);
        }
    }

    @Override
    public void actualizar(Long idNotificacion, String estado) throws ExcepcionBaseDeDatos {
        try {
            Session session = sessionFactory.getCurrentSession();
            Notificacion notificacion = session.get(Notificacion.class, idNotificacion);
            if (notificacion != null) {
                notificacion.setEstado(estado);
                session.update(notificacion);
            } else {
                throw new ExcepcionBaseDeDatos("No se encontró la notificación con el ID: " + idNotificacion);
            }
        } catch (HibernateException e) {
            throw new ExcepcionBaseDeDatos("Error al actualizar el estado de la notificación", e);
        }
    }
}
