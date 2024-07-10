package com.tallerwebi.infraestructura.notificaciones;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.notificacion.Notificacion;
import com.tallerwebi.dominio.notificacion.RepositorioNotificacion;
import com.tallerwebi.dominio.usuario.Usuario;
import com.tallerwebi.infraestructura.RepositorioNotificacionImpl;
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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateTestInfraestructuraConfig.class})
public class RepositorioNotificacionTest {
    @Autowired
    private SessionFactory sessionFactory;

    private RepositorioNotificacion repositorioNotificacion;

//    @Test
//    @Transactional
//    @Rollback
//    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//    public void queAlGuardarNotificacionSeGuardeCorrectamente() throws ExcepcionBaseDeDatos {
//        // preparacion
//        repositorioNotificacion = new RepositorioNotificacionImpl(sessionFactory);
//        Notificacion notificacion = new Notificacion();
//        Usuario usuario = new Usuario();
//        Usuario usuarioSolicitante = new Usuario();
//
//        // Configurar los atributos de notificacion
//        notificacion.setDescripcion("Descripcion de prueba");
//        notificacion.setEstado("Estado de prueba");
//        notificacion.setTipo("Tipo de prueba");
//        notificacion.setUsuario(usuario);
//        notificacion.setUsuarioSolicitante(usuarioSolicitante);
//
//        // ejecucion
//        repositorioNotificacion.guardar(notificacion);
//
//        // Recuperar la notificacion de la base de datos
//        Session session = sessionFactory.getCurrentSession();
//        Notificacion notificacionGuardada = session.get(Notificacion.class, notificacion.getId());
//
//        // validacion
//        assertNotNull(notificacionGuardada);
//        assertEquals("Descripcion de prueba", notificacionGuardada.getDescripcion());
//        assertEquals("Estado de prueba", notificacionGuardada.getEstado());
//        assertEquals("Tipo de prueba", notificacionGuardada.getTipo());
//        assertEquals(usuario, notificacionGuardada.getUsuario());
//        assertEquals(usuarioSolicitante, notificacionGuardada.getUsuarioSolicitante());
//    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlGuardarNotificacionLanceExcepcionDeBDD() {
        // preparacion
        SessionFactory sessionFactoryMock = Mockito.mock(SessionFactory.class);
        Session sessionMock = Mockito.mock(Session.class);
        Mockito.when(sessionFactoryMock.getCurrentSession()).thenReturn(sessionMock);
        Mockito.doThrow(HibernateException.class).when(sessionMock).save(Mockito.any(Notificacion.class));

        repositorioNotificacion = new RepositorioNotificacionImpl(sessionFactoryMock);
        Notificacion notificacion = new Notificacion();
        Usuario usuario = new Usuario();
        Usuario usuarioSolicitante = new Usuario();

        // Configurar los atributos de notificacion
        notificacion.setDescripcion("Descripcion de prueba");
        notificacion.setEstado("Estado de prueba");
        notificacion.setTipo("Tipo de prueba");
        notificacion.setUsuario(usuario);
        notificacion.setUsuarioSolicitante(usuarioSolicitante);

        // ejecucion y validacion
        assertThrows(ExcepcionBaseDeDatos.class, () -> {
            repositorioNotificacion.guardar(notificacion);
        });
    }

}
