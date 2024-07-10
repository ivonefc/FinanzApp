package com.tallerwebi.infraestructura.notificacionHeader;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;
import com.tallerwebi.dominio.notificacion.Notificacion;
import com.tallerwebi.dominio.notificacionHeader.RepositorioNotificacionHeader;
import com.tallerwebi.dominio.usuario.Usuario;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class RepositorioNotificacionHeaderImpl implements RepositorioNotificacionHeader {


    private SessionFactory sessionFactory;

    public RepositorioNotificacionHeaderImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public RepositorioNotificacionHeaderImpl(){

    }

    @Override
    public List<Notificacion> obtenerSolicitudesRecibidas(Long idUsuario) throws ExcepcionBaseDeDatos {
        try{
            Session session = sessionFactory.getCurrentSession();
            return session.createQuery("FROM Notificacion n WHERE n.usuario.id = :idUsuario AND n.tipo = :tipoNotificacion AND n.estado = :estadoNotificacion ORDER BY n.fecha DESC", Notificacion.class)
                    .setParameter("idUsuario", idUsuario)
                    .setParameter("tipoNotificacion", "Solicitud de amistad")
                    .setParameter("estadoNotificacion", "Pendiente")
                    .setMaxResults(10)
                    .getResultList();
        }catch (Exception he){
            throw new ExcepcionBaseDeDatos("Base de datos no disponible");
        }
    }


    @Override
    public List<Notificacion> obtenerSolicitudesAceptadas(Long idUsuario) throws ExcepcionBaseDeDatos, UsuarioInexistente {
        if (idUsuario == null)
            throw new UsuarioInexistente();

        try {
            Session session = sessionFactory.getCurrentSession();
            Usuario usuario = session.get(Usuario.class, idUsuario);
            if (usuario == null)
                throw new UsuarioInexistente();

            return session.createQuery("FROM Notificacion n WHERE n.usuario.id = :idUsuario AND n.tipo = :tipoNotificacion AND n.estado = :estadoNotificacion ORDER BY n.fecha DESC", Notificacion.class)
                    .setParameter("idUsuario", idUsuario)
                    .setParameter("tipoNotificacion", "Solicitud de amistad")
                    .setParameter("estadoNotificacion", "Aceptada")
                    .setMaxResults(10)
                    .getResultList();
        } catch (HibernateException he) {
            throw new ExcepcionBaseDeDatos("Base de datos no disponible");
        }
    }

    @Override
    public List<Notificacion> obtenerMovimientosCompartidos() {
        // obtener movimientos de la tabla notificaciones que me hayan enviado otros usuarios, ordenados por fecha y con un limite de 10
        return List.of();
    }

    @Override
    public List<Notificacion> obtenerMetasAlcanzadas() {
        // obtener meta que haya alcanzado el usuario ordenado por fecha y con un limite de 10
        return List.of();
    }

    @Override
    public List<Notificacion> obtenerMetasNoAlcanzadas() {
        //  obtener meta que no haya alcanzado el usuario ordenado por fecha y con un limite de 10
        return List.of();
    }

    @Override
    public List<Notificacion> obtenerMetasMensajeAlerta() {
        // obtener meta del usuario que tenga un mensaje de alerta ordenado por fecha y con un limite de 10
        return List.of();
    }
}
