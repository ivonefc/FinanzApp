package com.tallerwebi.presentacion.movimientoCompartido;

import com.tallerwebi.dominio.excepcion.*;
import com.tallerwebi.dominio.movimiento.Movimiento;
import com.tallerwebi.dominio.movimientoCompartido.ServicioMovimientoCompartido;
import com.tallerwebi.dominio.notificacion.Notificacion;
import com.tallerwebi.dominio.usuario.ServicioUsuario;
import com.tallerwebi.dominio.usuario.Usuario;
import com.tallerwebi.presentacion.movimiento.DatosAgregarMovimiento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ControladorMovimientoCompartidoTest {

    private ControladorMovimientoCompartido controladorMovimientoCompartido;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;
    private ServicioMovimientoCompartido servicioMovimientoCompartidoMock;
    private ServicioUsuario servicioUsuarioMock;

    @BeforeEach
    public void init(){
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        servicioUsuarioMock = mock(ServicioUsuario.class);
        servicioMovimientoCompartidoMock = mock(ServicioMovimientoCompartido.class);
        controladorMovimientoCompartido = new ControladorMovimientoCompartido(servicioMovimientoCompartidoMock, servicioUsuarioMock);
    }

    @Test
    public void queAlClickearLaOpcionMovimientosCompartidosEnElMenuDirijaALaVistaMovimientosCompartidos() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        //preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);

        //ejecucion
        ModelAndView modelAndView = controladorMovimientoCompartido.irAMovimientosCompartidos(requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("movimientos-compartidos"));
    }

    @Test
    public void queAlQuererIrALaOpcionMovimientosCompartidosEnElMenuYNoExistaUsuarioLogueadoMeRedirijaAlLoguin() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        //preparacion
        when(requestMock.getSession(false)).thenReturn(null);

        //ejecucion
        ModelAndView modelAndView = controladorMovimientoCompartido.irAMovimientosCompartidos(requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    @Test
    public void queAlQuererIrALaOpcionMovimientosCompartidosEnElMenuLanceUnaExcepcionSiNoSeEncuentraElUsuario() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        // Preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(1L);
        when(servicioUsuarioMock.obtenerUsuarioPorId(1L)).thenThrow(new UsuarioInexistente());

        // Ejecucion y validacion
        assertThrows(UsuarioInexistente.class, () -> {
            controladorMovimientoCompartido.irAMovimientosCompartidos(requestMock);
        });
    }

    @Test
    public void queAlClickearAgregarAmigoMeRedirijaALaVistaDeAgregarAmigo() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        //preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);

        //ejecucion
        ModelAndView modelAndView = controladorMovimientoCompartido.irAAgregarAmigo(requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("agregar-amigo"));
    }

    @Test
    public void queAlClickearAgregarAmigoYNoExistaUsuarioLogueadoMeRedirijaAlLoguin() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        //preparacion
        when(requestMock.getSession(false)).thenReturn(null);

        //ejecucion
        ModelAndView modelAndView = controladorMovimientoCompartido.irAAgregarAmigo(requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    @Test
    public void queAlClickearAgregarAmigoLanceExcepcionUsuarioInexistenteSiNoSeEncuentraUsuario() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        // Preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(1L);
        when(servicioUsuarioMock.obtenerUsuarioPorId(1L)).thenThrow(new UsuarioInexistente());

        // Ejecucion y validacion
        assertThrows(UsuarioInexistente.class, () -> {
            controladorMovimientoCompartido.irAAgregarAmigo(requestMock);
        });
    }

    @Test
    public void queAlQuererAgregarUnAmigoSePuedaAgregarAmigo() throws ExcepcionBaseDeDatos, Excepcion {
        //preparacion
        Long idUsuario = 1L;
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(1L);
        Usuario amigo = new Usuario();

        //ejecucion
        ModelAndView modelAndView = controladorMovimientoCompartido.agregarNuevoAmigo(amigo, requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/movimientos-compartidos"));
        verify(servicioMovimientoCompartidoMock, times(1)).agregarNuevoAmigo(idUsuario, amigo.getEmail());
    }

    @Test
    public void queNoSePuedaAgregarUnAmigoQueYaEsAmigo() throws Excepcion, ExcepcionBaseDeDatos, ExcepcionAmigoYaExistente, ExcepcionSolicitudEnviada, UsuarioInexistente, ExcepcionAutoAmistad, ExcepcionUsuarioNoPremium, Excepcion {
        // Preparación
        Long idUsuario = 1L;
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(idUsuario);
        Usuario amigo = new Usuario();
        doThrow(new Excepcion("El usuario ya es tu amigo")).when(servicioMovimientoCompartidoMock).agregarNuevoAmigo(idUsuario, amigo.getEmail());

        // Ejecución
        ModelAndView modelAndView = controladorMovimientoCompartido.agregarNuevoAmigo(amigo, requestMock);

        // Validación
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("agregar-amigo"));
        verify(servicioMovimientoCompartidoMock, times(1)).agregarNuevoAmigo(idUsuario, amigo.getEmail());
    }

    @Test
    public void queLanceUnaExepcionCuandoAgregoDeAmigoAUnUsuarioQueNoExiste() throws Excepcion, ExcepcionBaseDeDatos, ExcepcionAmigoYaExistente, ExcepcionSolicitudEnviada, UsuarioInexistente, ExcepcionAutoAmistad, ExcepcionUsuarioNoPremium, Excepcion {
        // Preparación
        Long idUsuario = 1L;
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(idUsuario);
        Usuario amigo = new Usuario();
        doThrow(new Excepcion("No se encontró un usuario con el email proporcionado")).when(servicioMovimientoCompartidoMock).agregarNuevoAmigo(idUsuario, amigo.getEmail());

        // Ejecución
        ModelAndView modelAndView = controladorMovimientoCompartido.agregarNuevoAmigo(amigo, requestMock);

        // Validación
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("agregar-amigo"));
        verify(servicioMovimientoCompartidoMock, times(1)).agregarNuevoAmigo(idUsuario, amigo.getEmail());
    }

    @Test
    public void queNoSePuedaAgregarDosVecesDeAmigoAlMismoUsuario() throws Excepcion, ExcepcionBaseDeDatos, ExcepcionAmigoYaExistente, ExcepcionSolicitudEnviada, UsuarioInexistente, ExcepcionAutoAmistad, ExcepcionUsuarioNoPremium, Excepcion {
        // Preparación
        Long idUsuario = 1L;
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(idUsuario);
        Usuario amigo = new Usuario();
        doThrow(new Excepcion("Ya has enviado una solicitud de amistad a este usuario")).when(servicioMovimientoCompartidoMock).agregarNuevoAmigo(idUsuario, amigo.getEmail());

        // Ejecución
        ModelAndView modelAndView = controladorMovimientoCompartido.agregarNuevoAmigo(amigo, requestMock);

        // Validación
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("agregar-amigo"));
        verify(servicioMovimientoCompartidoMock, times(1)).agregarNuevoAmigo(idUsuario, amigo.getEmail());
    }

    @Test
    public void queSePuedaEliminarUnaSolicitudDeAmistadEnviada() throws ExcepcionBaseDeDatos, ExcepcionNotificacionInexistente {
        // Preparación
        Long id = 1L;
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(1L);

        // Ejecución
        ModelAndView modelAndView = controladorMovimientoCompartido.eliminarSolicitud(id, requestMock);

        // Validación
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/movimientos-compartidos"));
        verify(servicioMovimientoCompartidoMock, times(1)).eliminarSolicitud(id);
    }

    @Test
    public void queAlQuererEliminarUnaSolicitudDeAmistadLanceExcepcionNotificacionInexistente() throws ExcepcionBaseDeDatos, ExcepcionNotificacionInexistente {
        // Preparación
        Long id = 1L;
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(1L);
        doThrow(new ExcepcionNotificacionInexistente("No se encontró la notificación")).when(servicioMovimientoCompartidoMock).eliminarSolicitud(1L);

        // Ejecución y validación
        Exception exception = assertThrows(ExcepcionNotificacionInexistente.class, () -> {
            controladorMovimientoCompartido.eliminarSolicitud(id, requestMock);
        });
        String expectedMessage = "No se encontró la notificación";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void queAlQuererEliminarUnaSolicitudDeAmistadLanceExcepcionBaseDeDatos() throws ExcepcionBaseDeDatos, ExcepcionNotificacionInexistente {
        // Preparación
        Long id = 1L;
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(1L);
        doThrow(new ExcepcionBaseDeDatos("No se encontró la notificación")).when(servicioMovimientoCompartidoMock).eliminarSolicitud(id);

        // Ejecución y validación
        assertThrows(ExcepcionBaseDeDatos.class, () -> {
            controladorMovimientoCompartido.eliminarSolicitud(id, requestMock);
        });
    }

    @Test
    public void queSePuedaAceptarUnaSolicitudDeAmistad() throws ExcepcionBaseDeDatos, ExcepcionNotificacionInexistente {
        // Preparación
        Long id = 1L;
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(1L);

        // Ejecución
        ModelAndView modelAndView = controladorMovimientoCompartido.aceptarSolicitud(id, requestMock);

        // Validación
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/notificaciones"));
        verify(servicioMovimientoCompartidoMock, times(1)).aceptarSolicitud(id);
    }

    @Test
    public void queAlQuererAceptarUnaSolicitudYNoHayaUsuarioLogueadoMeRedirijaAlLoguin() throws ExcepcionBaseDeDatos, ExcepcionNotificacionInexistente {
        // Preparación
        when(requestMock.getSession(false)).thenReturn(null);

        // Ejecución
        ModelAndView modelAndView = controladorMovimientoCompartido.aceptarSolicitud(1L, requestMock);

        // Validación
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    @Test
    public void queAlQuererAceptarUnaSolicitudLanceExcepcionUsuarioInexistente() throws ExcepcionBaseDeDatos, ExcepcionNotificacionInexistente {
        // Preparación
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(1L);
        doAnswer(invocation -> {
            throw new UsuarioInexistente();
        }).when(servicioMovimientoCompartidoMock).aceptarSolicitud(1L);

        // Ejecución y validación
        Exception exception = assertThrows(UsuarioInexistente.class, () -> {
            controladorMovimientoCompartido.aceptarSolicitud(1L, requestMock);
        });
    }

    @Test
    public void queAlQuererAceptarUnaSolicitudLanceExcepcionBaseDeDatos() throws ExcepcionBaseDeDatos, ExcepcionNotificacionInexistente {
        // Preparación
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(1L);
        doThrow(new ExcepcionBaseDeDatos("No se encontró la notificación")).when(servicioMovimientoCompartidoMock).aceptarSolicitud(1L);

        // Ejecución y validación
        assertThrows(ExcepcionBaseDeDatos.class, () -> {
            controladorMovimientoCompartido.aceptarSolicitud(1L, requestMock);
        });
    }

    @Test
    public void queSePuedaEliminarAmigo() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        // Preparación
        Long idAmigo = 1L;
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(1L);

        // Ejecución
        ModelAndView modelAndView = controladorMovimientoCompartido.eliminarAmigo(idAmigo, requestMock);

        // Validación
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/movimientos-compartidos"));
        verify(servicioMovimientoCompartidoMock, times(1)).eliminarAmigo(idAmigo, 1L);
    }

    @Test
    public void queAlQuererEliminarUnAmigoYNoHayaUsuarioLogueadoMeRedirijaAlLoguin() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        // Preparación
        when(requestMock.getSession(false)).thenReturn(null);

        // Ejecución
        ModelAndView modelAndView = controladorMovimientoCompartido.eliminarAmigo(1L, requestMock);

        // Validación
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    @Test
    public void queAlQuererEliminarUnAmigoLanceExcepcionUsuarioInexistente() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        // Preparación
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(1L);
        doThrow(new UsuarioInexistente()).when(servicioMovimientoCompartidoMock).eliminarAmigo(1L, 1L);

        // Ejecución y validación
        assertThrows(UsuarioInexistente.class, () -> {
            controladorMovimientoCompartido.eliminarAmigo(1L, requestMock);
        });
    }

    @Test
    public void queAlQuererEliminarUnAmigoLanceExcepcionBaseDeDatos() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        // Preparación
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(1L);
        doThrow(new ExcepcionBaseDeDatos("No se encontró el amigo")).when(servicioMovimientoCompartidoMock).eliminarAmigo(1L, 1L);

        // Ejecución y validación
        assertThrows(ExcepcionBaseDeDatos.class, () -> {
            controladorMovimientoCompartido.eliminarAmigo(1L, requestMock);
        });
    }

    @Test
    public void queSePuedaVerLosMovimientosCompartidos() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        // Preparación
        Long idAmigo = 1L;
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(1L);
        List<Movimiento> movimientos = new ArrayList<>();
        when(servicioMovimientoCompartidoMock.obtenerMovimientosCompartidos(idAmigo, 1L)).thenReturn(movimientos);

        // Ejecución
        ModelAndView modelAndView = controladorMovimientoCompartido.verMovimientosCompartidos(idAmigo, requestMock);

        // Validación
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("movimientos-compartidos-amigo"));
        verify(servicioMovimientoCompartidoMock, times(1)).obtenerMovimientosCompartidos(idAmigo, 1L);
    }

    @Test
    public void queAlQuererVerLosMovimientosCompartidosYNoHayaUsuarioLogueadoMeRedirijaAlLoguin() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        // Preparación
        when(requestMock.getSession(false)).thenReturn(null);

        // Ejecución
        ModelAndView modelAndView = controladorMovimientoCompartido.verMovimientosCompartidos(1L, requestMock);

        // Validación
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    @Test
    public void queAlQuererVerLosMovimientosCompartidosLanceExcepcionUsuarioInexistente() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        // Preparación
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(1L);
        doAnswer(invocation -> {
            throw new UsuarioInexistente();
        }).when(servicioMovimientoCompartidoMock).obtenerMovimientosCompartidos(1L, 1L);

        // Ejecución y validación
        Exception exception = assertThrows(UsuarioInexistente.class, () -> {
            controladorMovimientoCompartido.verMovimientosCompartidos(1L, requestMock);
        });
    }

    @Test
    public void queAlQuererVerLosMovimientosCompartidosLanceExcepcionBaseDeDatos() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        // Preparación
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(1L);
        doThrow(new ExcepcionBaseDeDatos("No se encontraron los movimientos compartidos")).when(servicioMovimientoCompartidoMock).obtenerMovimientosCompartidos(1L, 1L);

        // Ejecución y validación
        assertThrows(ExcepcionBaseDeDatos.class, () -> {
            controladorMovimientoCompartido.verMovimientosCompartidos(1L, requestMock);
        });
    }

    @Test
    public void queAlClickearVolverAlInicioMeRedirijaAlPanel() {
        // preparación
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(1L);

        // ejecución
        ModelAndView modelAndView = controladorMovimientoCompartido.volverAPanel(requestMock);

        // validación
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/panel"));
    }

    @Test
    public void queAlClickearVolverAlInicioYNoHayaUsuarioLogueadoMeRedirijaAlLoguin() {
        // preparación
        when(requestMock.getSession(false)).thenReturn(null);

        // ejecución
        ModelAndView modelAndView = controladorMovimientoCompartido.volverAPanel(requestMock);

        // validación
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
    }
}
