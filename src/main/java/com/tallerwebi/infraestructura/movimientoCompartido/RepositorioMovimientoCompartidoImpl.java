package com.tallerwebi.infraestructura.movimientoCompartido;

import com.tallerwebi.dominio.excepcion.*;
import com.tallerwebi.dominio.movimiento.Movimiento;
import com.tallerwebi.dominio.movimientoCompartido.RepositorioMovimientoCompartido;
import com.tallerwebi.dominio.notificacion.Notificacion;
import com.tallerwebi.dominio.notificacion.RepositorioNotificacion;
import com.tallerwebi.dominio.usuario.Usuario;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("repositorioMovimientoCompartido")
public class RepositorioMovimientoCompartidoImpl implements RepositorioMovimientoCompartido {

    private SessionFactory sessionFactory;
    private RepositorioNotificacion repositorioNotificacion;

    @Autowired
    public RepositorioMovimientoCompartidoImpl(SessionFactory sessionFactory, RepositorioNotificacion repositorioNotificacion) {
        this.sessionFactory = sessionFactory;
        this.repositorioNotificacion = repositorioNotificacion;
    }

    @Transactional
    @Override
    public List<Usuario> obtenerAmigos(Long idUsuario) throws ExcepcionBaseDeDatos { //BUSCA POR ID DE USUARIO
        try{
            Session session = sessionFactory.getCurrentSession();
            return session.createQuery("SELECT a FROM Usuario u JOIN u.amigos a WHERE u.id = :idUsuario", Usuario.class)
                    .setParameter("idUsuario", idUsuario)
                    .getResultList();
        }catch (HibernateException he){
            throw new ExcepcionBaseDeDatos("Base de datos no disponible");
        }
    }

    @Override
    public void agregarNuevoAmigo(Long idUsuario, String email) throws ExcepcionBaseDeDatos {
        try {
            Session session = sessionFactory.getCurrentSession();

            // Buscar el usuario por email
            Usuario amigo = (Usuario) session.createQuery("FROM Usuario u WHERE u.email = :email")
                    .setParameter("email", email)
                    .uniqueResult();

            if (amigo == null)
                throw new UsuarioInexistente();

            // Buscar el usuario por id
            Usuario usuario = session.get(Usuario.class, idUsuario);

            if (usuario == null)
                throw new UsuarioInexistente();

            // Verificar si el usuario ya es amigo
            if (usuario.existeAmigo(amigo))
                throw new ExcepcionAmigoYaExistente("El usuario ya es tu amigo");

            // Verificar si ya se ha enviado una solicitud de amistad
            Long count = (Long) session.createQuery("SELECT COUNT(n) FROM Notificacion n WHERE n.usuarioSolicitante.id = :idUsuario AND n.usuario.id = :idAmigo AND n.estado = :estado")
                    .setParameter("idUsuario", idUsuario)
                    .setParameter("idAmigo", amigo.getId())
                    .setParameter("estado", "Pendiente")
                    .uniqueResult();

            if (count > 0) {
                throw new ExcepcionSolicitudEnviada("Ya has enviado una solicitud de amistad a este usuario");
            }

            if(email.equals(usuario.getEmail())){
                throw new ExcepcionAutoAmistad("No se puede agregar a si mismo");
            }

            // Crear una nueva notificación
            Notificacion notificacion = new Notificacion();
            notificacion.setUsuario(amigo);
            notificacion.setUsuarioSolicitante(usuario);
            notificacion.setEstado("Pendiente");
            notificacion.setDescripcion("El usuario " + usuario.getNombre() + " quiere ser tu amigo!");
            notificacion.setTipo("Solicitud de amistad");

            // Guardar la notificación en la base de datos
            repositorioNotificacion.guardar(notificacion);

        } catch (Exception e) {
            throw new ExcepcionBaseDeDatos("Base de datos no disponible", e);
        }
    }

    @Override
    public List<Notificacion> obtenerSolicitudesEnviadas(Long idUsuario) throws ExcepcionBaseDeDatos{
        try{
            Session session = sessionFactory.getCurrentSession();
            return session.createQuery("FROM Notificacion n WHERE n.usuarioSolicitante.id = :idUsuario AND n.tipo = :tipoNotificacion AND n.estado = :estadoNotificacion", Notificacion.class)
                    .setParameter("idUsuario", idUsuario)
                    .setParameter("tipoNotificacion", "Solicitud de amistad")
                    .setParameter("estadoNotificacion", "Pendiente")
                    .getResultList();
        }catch (Exception he){
            throw new ExcepcionBaseDeDatos("Base de datos no disponible");
        }
    }

    @Override
    public Notificacion obtenerNotificacionPorId(Long id) throws ExcepcionBaseDeDatos {
        try {
            Session session = sessionFactory.getCurrentSession();
            Notificacion notificacion = session.createQuery("FROM Notificacion N WHERE N.id = :idNotificacion", Notificacion.class)
                    .setParameter("idNotificacion", id)
                    .uniqueResult();
            if(notificacion==null)
                throw new ExcepcionBaseDeDatos("No se encontro la notificacion");
            return notificacion;
        } catch (HibernateException he) {
            throw new ExcepcionBaseDeDatos("Base de datos no disponible", he);
        }
    }

    @Override
    public void eliminarSolicitud(Notificacion notificacion) throws ExcepcionBaseDeDatos{
        if (notificacion == null || notificacion.getId() == null)
            throw new ExcepcionBaseDeDatos("No se encontro la notificacion");
        try {
            Session session = sessionFactory.getCurrentSession();
            Notificacion notificacionExistente = session.get(Notificacion.class, notificacion.getId());

            if (notificacionExistente == null)
                throw new ExcepcionBaseDeDatos("No se encontro la notificacion");

            // Obtener la referencia al usuario y al amigo
            Usuario usuario = notificacionExistente.getUsuarioSolicitante();
            Usuario amigo = notificacionExistente.getUsuario();

            // Eliminar la relación de amistad
            usuario.eliminarAmigo(amigo);

            session.delete(notificacion);
        } catch (HibernateException he) {
            throw new ExcepcionBaseDeDatos("Base de datos no disponible", he);
        }
    }

    @Override
    public List<Notificacion> obtenerSolicitudesRecibidas(Long idUsuario) throws ExcepcionBaseDeDatos {
        try{
            Session session = sessionFactory.getCurrentSession();
            return session.createQuery("FROM Notificacion n WHERE n.usuario.id = :idUsuario AND n.tipo = :tipoNotificacion AND n.estado = :estadoNotificacion", Notificacion.class)
                    .setParameter("idUsuario", idUsuario)
                    .setParameter("tipoNotificacion", "Solicitud de amistad")
                    .setParameter("estadoNotificacion", "Pendiente")
                    .getResultList();
        }catch (Exception he){
            throw new ExcepcionBaseDeDatos("Base de datos no disponible");
        }
    }

    @Override
    public void aceptarSolicitud(Notificacion notificacion) throws ExcepcionBaseDeDatos {
        if (notificacion == null || notificacion.getId() == null)
            throw new ExcepcionBaseDeDatos("No se encontro la notificacion");
        try {
            Session session = sessionFactory.getCurrentSession();
            Notificacion notificacionExistente = session.get(Notificacion.class, notificacion.getId());

            if (notificacionExistente == null)
                throw new ExcepcionBaseDeDatos("No se encontro la notificacion");

            // Obtener la referencia al usuario y al amigo
            Usuario usuario = notificacionExistente.getUsuario();
            Usuario amigo = notificacionExistente.getUsuarioSolicitante();

            // Agregar la relación de amistad
            usuario.agregarAmigo(amigo);
            amigo.agregarAmigo(usuario);

            // Cambiar el estado de la notificación
            notificacionExistente.setEstado("Aceptada");

            session.update(notificacionExistente);
        } catch (HibernateException he) {
            throw new ExcepcionBaseDeDatos("Base de datos no disponible", he);
        }
    }

    @Override
    public void eliminarAmigo(Long idAmigo, Long idUsuario) throws ExcepcionBaseDeDatos {
        try {
            Session session = sessionFactory.getCurrentSession();

            // Buscar el usuario y el amigo por id
            Usuario usuario = session.get(Usuario.class, idUsuario);
            Usuario amigo = session.get(Usuario.class, idAmigo);

            // Si el usuario o el amigo no existen, lanzar una excepción
            if (usuario == null || amigo == null) {
                throw new ExcepcionBaseDeDatos("No se encontró el usuario o el amigo");
            }

            // Eliminar la relación de amistad
            usuario.eliminarAmigo(amigo);
            amigo.eliminarAmigo(usuario);

            // Actualizar los usuarios en la base de datos
            session.update(usuario);
            session.update(amigo);
        } catch (Exception e) {
            throw new ExcepcionBaseDeDatos("Base de datos no disponible", e);
        }
    }

    @Override
    public List<Movimiento> obtenerMovimientosCompartidos(Long idAmigo, Long idUsuario) throws ExcepcionBaseDeDatos {
        try{
            Session session = sessionFactory.getCurrentSession();
            return session.createQuery("FROM Movimiento m WHERE m.usuario.id = :idUsuario AND m.amigo.id = :idAmigo", Movimiento.class)
                    .setParameter("idUsuario", idUsuario)
                    .setParameter("idAmigo", idAmigo)
                    .getResultList();
        }catch (Exception he){
            throw new ExcepcionBaseDeDatos("Base de datos no disponible");
        }
    }

}
