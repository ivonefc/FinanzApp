package com.tallerwebi.infraestructura.movimientoCompartido;

import com.tallerwebi.dominio.categoria.CategoriaMovimiento;
import com.tallerwebi.dominio.excepcion.*;
import com.tallerwebi.dominio.movimiento.Movimiento;
import com.tallerwebi.dominio.movimientoCompartido.RepositorioMovimientoCompartido;
import com.tallerwebi.dominio.notificacion.Notificacion;
import com.tallerwebi.dominio.notificacion.RepositorioNotificacion;
import com.tallerwebi.dominio.usuario.Usuario;
import com.tallerwebi.infraestructura.config.HibernateTestInfraestructuraConfig;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateTestInfraestructuraConfig.class})
public class RepositorioMovimientoCompartidoTest {
    @Autowired
    private SessionFactory sessionFactory;

    private RepositorioMovimientoCompartido repositorioMovimientoCompartido;
    private RepositorioNotificacion repositorioNotificacion;

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioObtenerAmigosDevuelvaUnaListaDeAmigos() throws ExcepcionBaseDeDatos {
        //preparacion
        repositorioMovimientoCompartido = new RepositorioMovimientoCompartidoImpl(sessionFactory, repositorioNotificacion);
        // Crear un usuario y algunos amigos
        Usuario usuario = new Usuario();
        usuario.setNombre("Usuario de prueba");

        Usuario amigo1 = new Usuario();
        amigo1.setNombre("Amigo 1");

        Usuario amigo2 = new Usuario();
        amigo2.setNombre("Amigo 2");

        // Guardar el usuario y sus amigos en la base de datos
        Session session = sessionFactory.getCurrentSession();
        session.save(usuario);
        session.save(amigo1);
        session.save(amigo2);

        // Agregar una entrada en la tabla de unión para cada amigo
        session.createNativeQuery("INSERT INTO amigos (usuario_id, amigo_id) VALUES (:usuarioId, :amigoId)")
                .setParameter("usuarioId", usuario.getId())
                .setParameter("amigoId", amigo1.getId())
                .executeUpdate();

        session.createNativeQuery("INSERT INTO amigos (usuario_id, amigo_id) VALUES (:usuarioId, :amigoId)")
                .setParameter("usuarioId", usuario.getId())
                .setParameter("amigoId", amigo2.getId())
                .executeUpdate();

        //ejecucion
        List<Usuario> amigos = repositorioMovimientoCompartido.obtenerAmigos(usuario.getId());

        //verificacion
        assertEquals(2, amigos.size());
        assertTrue(amigos.stream().anyMatch(amigo -> amigo.getNombre().equals(amigo1.getNombre())));
        assertTrue(amigos.stream().anyMatch(amigo -> amigo.getNombre().equals(amigo2.getNombre())));
        assertNotNull(usuario.getId());
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioObtenerAmigosDevuelvaUnaListaVaciaYaQueNoTieneAmigos() throws ExcepcionBaseDeDatos {
        //preparacion
        repositorioMovimientoCompartido = new RepositorioMovimientoCompartidoImpl(sessionFactory, repositorioNotificacion);
        // Crear un usuario sin amigos
        Usuario usuario = new Usuario();
        usuario.setNombre("Usuario de prueba");

        // Guardar el usuario en la base de datos
        Session session = sessionFactory.getCurrentSession();
        session.save(usuario);

        //ejecucion
        List<Usuario> amigos = repositorioMovimientoCompartido.obtenerAmigos(usuario.getId());

        //verificacion
        assertTrue(amigos.isEmpty());
        assertNotNull(usuario.getId());
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioObtenerAmigosLanceExcepcionBaseDeDatos() {
        //preparacion
        SessionFactory sessionFactoryMock = Mockito.mock(SessionFactory.class);
        Session sessionMock = Mockito.mock(Session.class);
        when(sessionFactoryMock.getCurrentSession()).thenReturn(sessionMock);
        when(sessionMock.createQuery(Mockito.anyString(), Mockito.eq(Usuario.class))).thenThrow(new HibernateException("Base de datos no disponible"));

        repositorioMovimientoCompartido = new RepositorioMovimientoCompartidoImpl(sessionFactoryMock, repositorioNotificacion);

        //ejecucion y verificacion
        assertThrows(ExcepcionBaseDeDatos.class, () ->
                repositorioMovimientoCompartido.obtenerAmigos(1L)
        );
    }

//    @Test
//    @Transactional
//    @Rollback
//    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//    public void queAlSolicitarAlRepositorioAgregarNuevoAmigoSeAgregueUnNuevoAmigo() throws ExcepcionBaseDeDatos, ExcepcionAmigoYaExistente, ExcepcionSolicitudEnviada, UsuarioInexistente, ExcepcionAutoAmistad {
//        //preparacion
//        repositorioMovimientoCompartido = new RepositorioMovimientoCompartidoImpl(sessionFactory, repositorioNotificacion);
//        // Crear un usuario y un amigo
//        Usuario usuario = new Usuario();
//        usuario.setId(1L);
//        usuario.setNombre("Usuario de prueba");
//        usuario.setEmail("usuario@prueba.com");
//
//        Usuario amigo = new Usuario();
//        amigo.setId(2L);
//        amigo.setNombre("Amigo de prueba");
//        amigo.setEmail("amigo@prueba.com");
//
//        // Guardar el usuario y el amigo en la base de datos
//        Session session = sessionFactory.getCurrentSession();
//        session.save(usuario);
//        session.save(amigo);
//
//        // Forzar la sincronización para obtener los IDs
//        session.flush();
//
//        // Ahora los IDs deberían estar disponibles
//        assertNotNull(usuario.getId());
//        assertNotNull(amigo.getId());
//
//        //ejecucion
//        repositorioMovimientoCompartido.agregarNuevoAmigo(usuario.getId(), amigo.getEmail());
//
//        //verificacion
//        List<Usuario> amigos = repositorioMovimientoCompartido.obtenerAmigos(usuario.getId());
//        assertEquals(1, amigos.size());
//        assertTrue(amigos.stream().anyMatch(amigoGuardado -> amigoGuardado.getEmail().equals(amigo.getEmail())));
//    }

//    @Test
//    @Transactional
//    @Rollback
//    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//    public void queAlSolicitarAlRepositorioAgregarNuevoAmigoLanceExcepcionBaseDeDatos() throws ExcepcionBaseDeDatos {
//        //preparacion
//        SessionFactory sessionFactoryMock = Mockito.mock(SessionFactory.class);
//        Session sessionMock = Mockito.mock(Session.class);
//        RepositorioNotificacion repositorioNotificacionMock = Mockito.mock(RepositorioNotificacion.class);
//        when(sessionFactoryMock.getCurrentSession()).thenReturn(sessionMock);
//        when(sessionMock.createQuery(Mockito.anyString(), Mockito.eq(Usuario.class))).thenThrow(new HibernateException("Base de datos no disponible"));
//
//        // Aquí configuramos el comportamiento de repositorioNotificacionMock
//        doNothing().when(repositorioNotificacionMock).guardar(any(Notificacion.class));
//
//        repositorioMovimientoCompartido = new RepositorioMovimientoCompartidoImpl(sessionFactoryMock, repositorioNotificacionMock);
//
//        //ejecucion y verificacion
//        assertThrows(ExcepcionBaseDeDatos.class, () ->
//                repositorioMovimientoCompartido.agregarNuevoAmigo(1L, "amigo@prueba")
//        );
//    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioObtenerSolicitudesEnviadasSeObtenganCorrectamente() throws ExcepcionBaseDeDatos {
        //preparacion
        repositorioMovimientoCompartido = new RepositorioMovimientoCompartidoImpl(sessionFactory, repositorioNotificacion);
        // Crear un usuario y un amigo
        Usuario usuario = new Usuario();
        usuario.setNombre("Usuario de prueba");

        Usuario amigo = new Usuario();
        amigo.setNombre("Amigo de prueba");

        // Guardar el usuario y el amigo en la base de datos
        Session session = sessionFactory.getCurrentSession();
        session.save(usuario);
        session.save(amigo);

        // Agregar una solicitud de amistad enviada
        Notificacion notificacion = new Notificacion();
        notificacion.setUsuario(usuario); // El usuario al que se le envía la solicitud
        notificacion.setUsuarioSolicitante(amigo); // El usuario que envía la solicitud
        notificacion.setEstado("Pendiente");
        notificacion.setTipo("Solicitud de amistad");

        // Guardar la notificación en la base de datos
        session.save(notificacion);

        //ejecucion
        List<Notificacion> solicitudesEnviadas = repositorioMovimientoCompartido.obtenerSolicitudesEnviadas(amigo.getId());

        //verificacion
        assertEquals(1, solicitudesEnviadas.size());
        assertTrue(solicitudesEnviadas.stream().anyMatch(notificacionGuardada -> notificacionGuardada.getUsuarioSolicitante().getNombre().equals(amigo.getNombre())));
        assertNotNull(usuario.getId());
        assertNotNull(amigo.getId());
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioObtenerSolicitudesEnviadasSeObtengaUnaListaVaciaYaQueNoHaySolicitudesEnviadas() throws ExcepcionBaseDeDatos {
        //preparacion
        repositorioMovimientoCompartido = new RepositorioMovimientoCompartidoImpl(sessionFactory, repositorioNotificacion);
        // Crear un usuario sin solicitudes enviadas
        Usuario usuario = new Usuario();
        usuario.setNombre("Usuario de prueba");

        // Guardar el usuario en la base de datos
        Session session = sessionFactory.getCurrentSession();
        session.save(usuario);

        //ejecucion
        List<Notificacion> solicitudesEnviadas = repositorioMovimientoCompartido.obtenerSolicitudesEnviadas(usuario.getId());

        //verificacion
        assertTrue(solicitudesEnviadas.isEmpty());
        assertNotNull(usuario.getId());
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioObtenerSolicitudesEnviadasLanceExcepcionBaseDeDatos() {
        //preparacion
        SessionFactory sessionFactoryMock = Mockito.mock(SessionFactory.class);
        Session sessionMock = Mockito.mock(Session.class);
        when(sessionFactoryMock.getCurrentSession()).thenReturn(sessionMock);
        when(sessionMock.createQuery(Mockito.anyString(), Mockito.eq(Notificacion.class))).thenThrow(new HibernateException("Base de datos no disponible"));

        repositorioMovimientoCompartido = new RepositorioMovimientoCompartidoImpl(sessionFactoryMock, repositorioNotificacion);

        //ejecucion y verificacion
        assertThrows(ExcepcionBaseDeDatos.class, () ->
                repositorioMovimientoCompartido.obtenerSolicitudesEnviadas(1L)
        );
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioObtenerNotificacionPorIdLaObtengaCorrectamente() throws ExcepcionBaseDeDatos, ExcepcionNotificacionInexistente {
        //preparacion
        repositorioMovimientoCompartido = new RepositorioMovimientoCompartidoImpl(sessionFactory, repositorioNotificacion);
        // Crear un usuario y un amigo
        Usuario usuario = new Usuario();
        usuario.setNombre("Usuario de prueba");

        Usuario amigo = new Usuario();
        amigo.setNombre("Amigo de prueba");

        // Guardar el usuario y el amigo en la base de datos
        Session session = sessionFactory.getCurrentSession();
        session.save(usuario);
        session.save(amigo);

        // Agregar una solicitud de amistad enviada
        Notificacion notificacion = new Notificacion();
        notificacion.setUsuario(usuario); // El usuario al que se le envía la solicitud
        notificacion.setUsuarioSolicitante(amigo); // El usuario que envía la solicitud
        notificacion.setEstado("Pendiente");
        notificacion.setTipo("Solicitud de amistad");

        // Guardar la notificación en la base de datos
        session.save(notificacion);

        //ejecucion
        Notificacion notificacionObtenida = repositorioMovimientoCompartido.obtenerNotificacionPorId(notificacion.getId());

        //verificacion
        assertNotNull(notificacionObtenida);
        assertEquals(notificacion.getId(), notificacionObtenida.getId());
        assertEquals(notificacion.getUsuario().getId(), notificacionObtenida.getUsuario().getId());
        assertEquals(notificacion.getUsuarioSolicitante().getId(), notificacionObtenida.getUsuarioSolicitante().getId());
        assertEquals(notificacion.getEstado(), notificacionObtenida.getEstado());
        assertEquals(notificacion.getTipo(), notificacionObtenida.getTipo());
        assertNotNull(usuario.getId());
        assertNotNull(amigo.getId());
        assertNotNull(notificacion.getId());
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioObtenerNotificacionPorIdLanceExcepcionNotificacionInexistente() {
        //preparacion
        repositorioMovimientoCompartido = new RepositorioMovimientoCompartidoImpl(sessionFactory, repositorioNotificacion);

        //ejecucion y verificacion
        assertThrows(ExcepcionNotificacionInexistente.class, () ->
                repositorioMovimientoCompartido.obtenerNotificacionPorId(1L)
        );
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioObtenerNotificacionPorIdLanceExcepcionBaseDeDatos() {
        //preparacion
        SessionFactory sessionFactoryMock = Mockito.mock(SessionFactory.class);
        Session sessionMock = Mockito.mock(Session.class);
        when(sessionFactoryMock.getCurrentSession()).thenReturn(sessionMock);
        when(sessionMock.createQuery(Mockito.anyString(), Mockito.eq(Notificacion.class))).thenThrow(new HibernateException("Base de datos no disponible"));

        repositorioMovimientoCompartido = new RepositorioMovimientoCompartidoImpl(sessionFactoryMock, repositorioNotificacion);

        //ejecucion y verificacion
        assertThrows(ExcepcionBaseDeDatos.class, () ->
                repositorioMovimientoCompartido.obtenerNotificacionPorId(1L)
        );
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioEliminarNotificacionDeSolicitudSeElimineCorrectamente() throws ExcepcionBaseDeDatos, ExcepcionNotificacionInexistente {
        //preparacion
        repositorioMovimientoCompartido = new RepositorioMovimientoCompartidoImpl(sessionFactory, repositorioNotificacion);
        // Crear un usuario y un amigo
        Usuario usuario = new Usuario();
        usuario.setNombre("Usuario de prueba");

        Usuario amigo = new Usuario();
        amigo.setNombre("Amigo de prueba");

        // Guardar el usuario y el amigo en la base de datos
        Session session = sessionFactory.getCurrentSession();
        session.save(usuario);
        session.save(amigo);

        // Agregar una solicitud de amistad enviada
        Notificacion notificacion = new Notificacion();
        notificacion.setUsuario(usuario); // El usuario al que se le envía la solicitud
        notificacion.setUsuarioSolicitante(amigo); // El usuario que envía la solicitud
        notificacion.setEstado("Pendiente");
        notificacion.setTipo("Solicitud de amistad");

        // Guardar la notificación en la base de datos
        session.save(notificacion);

        //ejecucion
        repositorioMovimientoCompartido.eliminarSolicitud(notificacion);

        //verificacion
        List<Notificacion> solicitudesEnviadas = repositorioMovimientoCompartido.obtenerSolicitudesEnviadas(amigo.getId());
        assertTrue(solicitudesEnviadas.isEmpty());
        assertNotNull(usuario.getId());
        assertNotNull(amigo.getId());
        assertNotNull(notificacion.getId());
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioEliminarNotificacionDeSolicitudLanceExcepcionNotificacionInexistente() {
        //preparacion
        SessionFactory sessionFactoryMock = Mockito.mock(SessionFactory.class);
        Session sessionMock = Mockito.mock(Session.class);
        when(sessionFactoryMock.getCurrentSession()).thenReturn(sessionMock);

        repositorioMovimientoCompartido = new RepositorioMovimientoCompartidoImpl(sessionFactoryMock, repositorioNotificacion);

        // Crear una notificacion
        Notificacion notificacion = new Notificacion();
        notificacion.setId(1L); // Establecer un ID para la notificación

        // Configurar el mock para que devuelva null cuando se llama al método get
        when(sessionMock.get(Notificacion.class, notificacion.getId())).thenReturn(null);

        //ejecucion y verificacion
        assertThrows(ExcepcionNotificacionInexistente.class, () ->
                repositorioMovimientoCompartido.eliminarSolicitud(notificacion)
        );
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioEliminarNotificacionDeSolicitudLanceExcepcionBaseDeDatos() {
        //preparacion
        SessionFactory sessionFactoryMock = Mockito.mock(SessionFactory.class);
        Session sessionMock = Mockito.mock(Session.class);
        when(sessionFactoryMock.getCurrentSession()).thenReturn(sessionMock);
        when(sessionMock.createQuery(Mockito.anyString(), Mockito.eq(Notificacion.class))).thenThrow(new HibernateException("Base de datos no disponible"));
        doThrow(new HibernateException("Base de datos no disponible")).when(sessionMock).delete(Mockito.any());

        repositorioMovimientoCompartido = new RepositorioMovimientoCompartidoImpl(sessionFactoryMock, repositorioNotificacion);

        // Crear un usuario y un amigo
        Usuario usuario = new Usuario();
        usuario.setNombre("Usuario de prueba");

        Usuario amigo = new Usuario();
        amigo.setNombre("Amigo de prueba");

        // Agregar una solicitud de amistad enviada
        Notificacion notificacion = new Notificacion();
        notificacion.setId(1L);
        notificacion.setUsuario(usuario); // El usuario al que se le envía la solicitud
        notificacion.setUsuarioSolicitante(amigo); // El usuario que envía la solicitud
        notificacion.setEstado("Pendiente");
        notificacion.setTipo("Solicitud de amistad");

        // Configurar el mock para que devuelva la notificación cuando se llama al método get
        when(sessionMock.get(Notificacion.class, notificacion.getId())).thenReturn(notificacion);

        //ejecucion y verificacion
        assertThrows(ExcepcionBaseDeDatos.class, () ->
                repositorioMovimientoCompartido.eliminarSolicitud(notificacion)
        );
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioObtenerSolicitudesRecibidasSeObtengaCorrectamente() throws ExcepcionBaseDeDatos {
        //preparacion
        repositorioMovimientoCompartido = new RepositorioMovimientoCompartidoImpl(sessionFactory, repositorioNotificacion);
        // Crear un usuario y un amigo
        Usuario usuario = new Usuario();
        usuario.setNombre("Usuario de prueba");

        Usuario amigo = new Usuario();
        amigo.setNombre("Amigo de prueba");

        // Guardar el usuario y el amigo en la base de datos
        Session session = sessionFactory.getCurrentSession();
        session.save(usuario);
        session.save(amigo);

        // Agregar una solicitud de amistad recibida
        Notificacion notificacion = new Notificacion();
        notificacion.setUsuario(amigo); // El usuario al que se le envía la solicitud
        notificacion.setUsuarioSolicitante(usuario); // El usuario que envía la solicitud
        notificacion.setEstado("Pendiente");
        notificacion.setTipo("Solicitud de amistad");

        // Guardar la notificación en la base de datos
        session.save(notificacion);

        //ejecucion
        List<Notificacion> solicitudesRecibidas = repositorioMovimientoCompartido.obtenerSolicitudesRecibidas(amigo.getId());

        //verificacion
        assertEquals(1, solicitudesRecibidas.size());
        assertTrue(solicitudesRecibidas.stream().anyMatch(notificacionGuardada -> notificacionGuardada.getUsuario().getNombre().equals(amigo.getNombre())));
        assertNotNull(usuario.getId());
        assertNotNull(amigo.getId());
        assertNotNull(notificacion.getId());
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioObtenerSolicitudesRecibidasSeObtengaUnaListaVaciaYaQueNoHaySolicitudesRecibidas() throws ExcepcionBaseDeDatos {
        //preparacion
        repositorioMovimientoCompartido = new RepositorioMovimientoCompartidoImpl(sessionFactory, repositorioNotificacion);
        // Crear un usuario sin solicitudes recibidas
        Usuario usuario = new Usuario();
        usuario.setNombre("Usuario de prueba");

        // Guardar el usuario en la base de datos
        Session session = sessionFactory.getCurrentSession();
        session.save(usuario);

        //ejecucion
        List<Notificacion> solicitudesRecibidas = repositorioMovimientoCompartido.obtenerSolicitudesRecibidas(usuario.getId());

        //verificacion
        assertTrue(solicitudesRecibidas.isEmpty());
        assertNotNull(usuario.getId());
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioObtenerSolicitudesRecibidasLanceExcepcionBaseDeDatos() {
        //preparacion
        SessionFactory sessionFactoryMock = Mockito.mock(SessionFactory.class);
        Session sessionMock = Mockito.mock(Session.class);
        when(sessionFactoryMock.getCurrentSession()).thenReturn(sessionMock);
        when(sessionMock.createQuery(Mockito.anyString(), Mockito.eq(Notificacion.class))).thenThrow(new HibernateException("Base de datos no disponible"));

        repositorioMovimientoCompartido = new RepositorioMovimientoCompartidoImpl(sessionFactoryMock, repositorioNotificacion);

        //ejecucion y verificacion
        assertThrows(ExcepcionBaseDeDatos.class, () ->
                repositorioMovimientoCompartido.obtenerSolicitudesRecibidas(1L)
        );
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioAceptarSolicitudSeAcepteCorrectamente() throws ExcepcionBaseDeDatos, ExcepcionNotificacionInexistente {
        //preparacion
        repositorioMovimientoCompartido = new RepositorioMovimientoCompartidoImpl(sessionFactory, repositorioNotificacion);
        // Crear un usuario y un amigo
        Usuario usuario = new Usuario();
        usuario.setNombre("Usuario de prueba");

        Usuario amigo = new Usuario();
        amigo.setNombre("Amigo de prueba");

        // Guardar el usuario y el amigo en la base de datos
        Session session = sessionFactory.getCurrentSession();
        session.save(usuario);
        session.save(amigo);

        // Agregar una solicitud de amistad recibida
        Notificacion notificacion = new Notificacion();
        notificacion.setUsuario(amigo); // El usuario al que se le envía la solicitud
        notificacion.setUsuarioSolicitante(usuario); // El usuario que envía la solicitud
        notificacion.setEstado("Pendiente");
        notificacion.setTipo("Solicitud de amistad");

        // Guardar la notificación en la base de datos
        session.save(notificacion);

        //ejecucion
        repositorioMovimientoCompartido.aceptarSolicitud(notificacion);

        //verificacion
        List<Notificacion> solicitudesRecibidas = repositorioMovimientoCompartido.obtenerSolicitudesRecibidas(amigo.getId());
        assertTrue(solicitudesRecibidas.isEmpty());

        List<Usuario> amigos = repositorioMovimientoCompartido.obtenerAmigos(usuario.getId());
        assertEquals(1, amigos.size());
        assertTrue(amigos.stream().anyMatch(amigoGuardado -> amigoGuardado.getNombre().equals(amigo.getNombre())));
        assertNotNull(usuario.getId());
        assertNotNull(amigo.getId());
        assertNotNull(notificacion.getId());
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioAceptarSolicitudLanceExcepcionNotificacionInexistente() {
        //preparacion
        repositorioMovimientoCompartido = new RepositorioMovimientoCompartidoImpl(sessionFactory, repositorioNotificacion);

        //ejecucion y verificacion
        assertThrows(ExcepcionNotificacionInexistente.class, () ->
                repositorioMovimientoCompartido.aceptarSolicitud(new Notificacion())
        );
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioAceptarSolicitudLanceExcepcionBaseDeDatos() {
        //preparacion
        SessionFactory sessionFactoryMock = Mockito.mock(SessionFactory.class);
        Session sessionMock = Mockito.mock(Session.class);
        when(sessionFactoryMock.getCurrentSession()).thenReturn(sessionMock);
        doThrow(new HibernateException("Base de datos no disponible")).when(sessionMock).update(Mockito.any());

        repositorioMovimientoCompartido = new RepositorioMovimientoCompartidoImpl(sessionFactoryMock, repositorioNotificacion);

        // Crear un usuario y un amigo
        Usuario usuario = new Usuario();
        usuario.setNombre("Usuario de prueba");

        Usuario amigo = new Usuario();
        amigo.setNombre("Amigo de prueba");

        // Agregar una solicitud de amistad recibida
        Notificacion notificacion = new Notificacion();
        notificacion.setId(1L);
        notificacion.setUsuario(amigo); // El usuario al que se le envía la solicitud
        notificacion.setUsuarioSolicitante(usuario); // El usuario que envía la solicitud
        notificacion.setEstado("Pendiente");
        notificacion.setTipo("Solicitud de amistad");

        // Crear un mock de Notificacion
        Notificacion notificacionMock = Mockito.mock(Notificacion.class);

        // Configurar el mock de Session para que devuelva la notificación real cuando se llama al método get
        when(sessionMock.get(Notificacion.class, notificacion.getId())).thenReturn(notificacion);

        //ejecucion y verificacion
        assertThrows(ExcepcionBaseDeDatos.class, () ->
                repositorioMovimientoCompartido.aceptarSolicitud(notificacion)
        );
    }
/*
    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioliminarAmigoSeElimineCorrectamente() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        //preparacion
        repositorioMovimientoCompartido = new RepositorioMovimientoCompartidoImpl(sessionFactory, repositorioNotificacion);
        // Crear un usuario y un amigo
        Usuario usuario = new Usuario();
        usuario.setNombre("Usuario de prueba");

        Usuario amigo = new Usuario();
        amigo.setNombre("Amigo de prueba");

        // Guardar el usuario y el amigo en la base de datos
        Session session = sessionFactory.getCurrentSession();
        session.save(usuario);
        session.save(amigo);

        // Agregar el amigo al conjunto de amigos del usuario
        usuario.getAmigos().add(amigo);
        amigo.getAmigos().add(usuario);

        // Guardar el usuario y el amigo en la base de datos
        session.save(usuario);
        session.save(amigo);

        //ejecucion
        repositorioMovimientoCompartido.eliminarAmigo(amigo.getId(), usuario.getId());

        //verificacion
        List<Usuario> amigos = repositorioMovimientoCompartido.obtenerAmigos(usuario.getId());
        assertTrue(amigos.isEmpty());
        assertNotNull(usuario.getId());
        assertNotNull(amigo.getId());
    }*/

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioliminarAmigoLanceExcepcionUsuarioInexistente() {
        //preparacion
        repositorioMovimientoCompartido = new RepositorioMovimientoCompartidoImpl(sessionFactory, repositorioNotificacion);

        //ejecucion y verificacion
        assertThrows(UsuarioInexistente.class, () ->
                repositorioMovimientoCompartido.eliminarAmigo(1L, 2L)
        );
    }

//    @Test
//    @Transactional
//    @Rollback
//    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//    public void queAlSolicitarAlRepositorioliminarAmigoLanceExcepcionBaseDeDatos() {
//        //preparacion
//        SessionFactory sessionFactoryMock = Mockito.mock(SessionFactory.class);
//        Session sessionMock = Mockito.mock(Session.class);
//        NativeQuery nativeQueryMock = Mockito.mock(NativeQuery.class);
//        when(sessionFactoryMock.getCurrentSession()).thenReturn(sessionMock);
//        when(sessionMock.createNativeQuery(Mockito.anyString())).thenReturn(nativeQueryMock);
//        doThrow(new HibernateException("Base de datos no disponible")).when(nativeQueryMock).executeUpdate();
//
//        repositorioMovimientoCompartido = new RepositorioMovimientoCompartidoImpl(sessionFactoryMock, repositorioNotificacion);
//
//        // Crear un usuario y un amigo
//        Usuario usuario = new Usuario();
//        usuario.setNombre("Usuario de prueba");
//        usuario.setId(1L);
//
//        Usuario amigo = new Usuario();
//        amigo.setNombre("Amigo de prueba");
//        amigo.setId(2L);
//
//        // Configurar el mock de Session para que devuelva el objeto que se pasa como argumento cuando se llama a save
//        when(sessionMock.save(Mockito.any(Usuario.class))).thenReturn(null);
//
//        // Guardar el usuario y el amigo en la base de datos
//        Session session = sessionFactoryMock.getCurrentSession();
//        session.save(usuario);
//        session.save(amigo);
//
//        // Agregar una entrada en la tabla de unión para cada amigo
//        session.createNativeQuery("INSERT INTO amigos (usuario_id, amigo_id) VALUES (:usuarioId, :amigoId)")
//                .setParameter("usuarioId", usuario.getId())
//                .setParameter("amigoId", amigo.getId())
//                .executeUpdate();
//
//        //ejecucion y verificacion
//        assertThrows(ExcepcionBaseDeDatos.class, () ->
//                repositorioMovimientoCompartido.eliminarAmigo(amigo.getId(), usuario.getId())
//        );
//    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioObtenerMovimientosCompartidosLosObtengaCorrectamente() throws ExcepcionBaseDeDatos {
        //preparacion
        repositorioMovimientoCompartido = new RepositorioMovimientoCompartidoImpl(sessionFactory, repositorioNotificacion);
        // Crear un usuario y un amigo
        Usuario usuario = new Usuario();
        usuario.setNombre("Usuario de prueba");

        Usuario amigo = new Usuario();
        amigo.setNombre("Amigo de prueba");

        // Guardar el usuario y el amigo en la base de datos
        Session session = sessionFactory.getCurrentSession();
        session.save(usuario);
        session.save(amigo);

        LocalDate fechayhora = LocalDate.now();
        CategoriaMovimiento categoriaMovimiento = new CategoriaMovimiento();
        // Guardar la categoriaMovimiento en la base de datos
        session.save(categoriaMovimiento);

        // Crear un movimiento compartido
        //(String descripcion, Double monto, LocalDate fechayHora, CategoriaMovimiento categoria, Usuario usuario, Usuario amigo, Double montoAmigo)
        Movimiento movimiento = new Movimiento("Movimiento de prueba", 100.0, fechayhora, categoriaMovimiento, usuario, amigo, 150.0);

        // Guardar el movimiento en la base de datos
        session.save(movimiento);

        //ejecucion
        List<Movimiento> movimientosCompartidos = repositorioMovimientoCompartido.obtenerMovimientosCompartidos(amigo.getId(), usuario.getId());

        //verificacion
        assertEquals(1, movimientosCompartidos.size());
        assertTrue(movimientosCompartidos.stream().anyMatch(movimientoGuardado -> movimientoGuardado.getDescripcion().equals(movimiento.getDescripcion())));
        assertNotNull(usuario.getId());
        assertNotNull(amigo.getId());
        assertNotNull(movimiento.getId());
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioObtenerMovimientosCompartidosObtengaUnaListaVacíaYaQueNoHayMovimientosCompartidos() throws ExcepcionBaseDeDatos {
        //preparacion
        repositorioMovimientoCompartido = new RepositorioMovimientoCompartidoImpl(sessionFactory, repositorioNotificacion);
        // Crear un usuario y un amigo
        Usuario usuario = new Usuario();
        usuario.setNombre("Usuario de prueba");

        Usuario amigo = new Usuario();
        amigo.setNombre("Amigo de prueba");

        // Guardar el usuario y el amigo en la base de datos
        Session session = sessionFactory.getCurrentSession();
        session.save(usuario);
        session.save(amigo);

        //ejecucion
        List<Movimiento> movimientosCompartidos = repositorioMovimientoCompartido.obtenerMovimientosCompartidos(amigo.getId(), usuario.getId());

        //verificacion
        assertTrue(movimientosCompartidos.isEmpty());
        assertNotNull(usuario.getId());
        assertNotNull(amigo.getId());
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioObtenerMovimientosCompartidosLanceExcepcionBaseDeDatos() {
        //preparacion
        SessionFactory sessionFactoryMock = Mockito.mock(SessionFactory.class);
        Session sessionMock = Mockito.mock(Session.class);
        when(sessionFactoryMock.getCurrentSession()).thenReturn(sessionMock);
        when(sessionMock.createQuery(Mockito.anyString(), Mockito.eq(Movimiento.class))).thenThrow(new HibernateException("Base de datos no disponible"));

        repositorioMovimientoCompartido = new RepositorioMovimientoCompartidoImpl(sessionFactoryMock, repositorioNotificacion);

        //ejecucion y verificacion
        assertThrows(ExcepcionBaseDeDatos.class, () ->
                repositorioMovimientoCompartido.obtenerMovimientosCompartidos(1L, 2L)
        );
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioObtenerSolicitudesAceptadasLasObtengaCorrectamente() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        //preparacion
        repositorioMovimientoCompartido = new RepositorioMovimientoCompartidoImpl(sessionFactory, repositorioNotificacion);
        // Crear un usuario y un amigo
        Usuario usuario = new Usuario();
        usuario.setNombre("Usuario de prueba");

        Usuario amigo = new Usuario();
        amigo.setNombre("Amigo de prueba");

        // Guardar el usuario y el amigo en la base de datos
        Session session = sessionFactory.getCurrentSession();
        session.save(usuario);
        session.save(amigo);

        // Agregar una solicitud de amistad recibida
        Notificacion notificacion = new Notificacion();
        notificacion.setUsuario(amigo); // El usuario al que se le envía la solicitud
        notificacion.setUsuarioSolicitante(usuario); // El usuario que envía la solicitud
        notificacion.setEstado("Aceptada");
        notificacion.setTipo("Solicitud de amistad");

        // Guardar la notificación en la base de datos
        session.save(notificacion);

        //ejecucion
        List<Notificacion> solicitudesAceptadas = repositorioMovimientoCompartido.obtenerSolicitudesAceptadas(amigo.getId());

        //verificacion
        assertEquals(1, solicitudesAceptadas.size());
        assertTrue(solicitudesAceptadas.stream().anyMatch(notificacionGuardada -> notificacionGuardada.getUsuario().getNombre().equals(amigo.getNombre())));
        assertNotNull(usuario.getId());
        assertNotNull(amigo.getId());
        assertNotNull(notificacion.getId());
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioObtenerSolicitudesAceptadasObtengaUnaListaVaciaYaQueNoHaySolicitudesAceptadas() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        //preparacion
        repositorioMovimientoCompartido = new RepositorioMovimientoCompartidoImpl(sessionFactory, repositorioNotificacion);
        // Crear un usuario sin solicitudes aceptadas
        Usuario usuario = new Usuario();
        usuario.setNombre("Usuario de prueba");

        // Guardar el usuario en la base de datos
        Session session = sessionFactory.getCurrentSession();
        session.save(usuario);

        //ejecucion
        List<Notificacion> solicitudesAceptadas = repositorioMovimientoCompartido.obtenerSolicitudesAceptadas(usuario.getId());

        //verificacion
        assertTrue(solicitudesAceptadas.isEmpty());
        assertNotNull(usuario.getId());
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioObtenerSolicitudesAceptadasLanceExcepcionBaseDeDatos() {
        //preparacion
        SessionFactory sessionFactoryMock = Mockito.mock(SessionFactory.class);
        Session sessionMock = Mockito.mock(Session.class);
        when(sessionFactoryMock.getCurrentSession()).thenReturn(sessionMock);
        when(sessionMock.createQuery(Mockito.anyString(), Mockito.eq(Notificacion.class))).thenThrow(new HibernateException("Base de datos no disponible"));
        when(sessionMock.get(Usuario.class, 1L)).thenThrow(new HibernateException("Base de datos no disponible"));

        repositorioMovimientoCompartido = new RepositorioMovimientoCompartidoImpl(sessionFactoryMock, repositorioNotificacion);

        //ejecucion y verificacion
        assertThrows(ExcepcionBaseDeDatos.class, () ->
                repositorioMovimientoCompartido.obtenerSolicitudesAceptadas(1L)
        );
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioObtenerSolicitudesAceptadasLanceExcepcionUsuarioInexistente() {
        //preparacion
        repositorioMovimientoCompartido = new RepositorioMovimientoCompartidoImpl(sessionFactory, repositorioNotificacion);

        //ejecucion y verificacion
        assertThrows(UsuarioInexistente.class, () ->
                repositorioMovimientoCompartido.obtenerSolicitudesAceptadas(1L)
        );
    }

}
