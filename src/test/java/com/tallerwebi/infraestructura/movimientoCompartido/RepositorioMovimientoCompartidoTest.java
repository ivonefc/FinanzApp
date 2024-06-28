package com.tallerwebi.infraestructura.movimientoCompartido;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.movimientoCompartido.RepositorioMovimientoCompartido;
import com.tallerwebi.dominio.notificacion.RepositorioNotificacion;
import com.tallerwebi.dominio.usuario.Usuario;
import com.tallerwebi.infraestructura.config.HibernateTestInfraestructuraConfig;
import com.tallerwebi.infraestructura.movimiento.RepositorioMovimientoImpl;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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
//    public void queAlSolicitarAlRepositorioAgregarNuevoAmigoSeAgregueUnNuevoAmigo() throws ExcepcionBaseDeDatos {
//        //preparacion
//        repositorioMovimientoCompartido = new RepositorioMovimientoCompartidoImpl(sessionFactory, repositorioNotificacion);
//        // Crear un usuario y un amigo
//        Usuario usuario = new Usuario();
//        usuario.setNombre("Usuario de prueba");
//
//        Usuario amigo = new Usuario();
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
//        assertTrue(amigos.stream().anyMatch(amigoGuardado -> amigoGuardado.getNombre().equals(amigo.getNombre())));
//        assertNotNull(usuario.getId());
//        assertNotNull(amigo.getId());
//    }

}
