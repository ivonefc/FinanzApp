package com.tallerwebi.presentacion.movimientoCompartido;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionCamposInvalidos;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;
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
        servicioMovimientoCompartidoMock = mock(ServicioMovimientoCompartido.class);
        controladorMovimientoCompartido = new ControladorMovimientoCompartido(servicioMovimientoCompartidoMock, servicioUsuarioMock);
    }

    @Test
    public void queAlClickearLaOpcionMovimientosCompartidosEnElMenuDirijaALaVistaMovimientosCompartidos() throws ExcepcionBaseDeDatos {
        //preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);

        //ejecucion
        ModelAndView modelAndView = controladorMovimientoCompartido.irAMovimientosCompartidos(requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("movimientos-compartidos"));
    }

    @Test
    public void queAlQuererIrALaOpcionCuentasCompartidasYNoExistaUsuarioLogueadoMeRedirijaAlLoguin() throws ExcepcionBaseDeDatos{
        //preparacion
        when(requestMock.getSession(false)).thenReturn(null);

        //ejecucion
        ModelAndView modelAndView = controladorMovimientoCompartido.irAMovimientosCompartidos(requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    @Test
    public void queAlClickearVolverAPaginaDeInicioMeRedirijaAPanel(){
        //preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);

        //ejecucion
        ModelAndView modelAndView = controladorMovimientoCompartido.volverAPanel(requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/panel"));
    }

    @Test
    public void queAlQuererVolverAPanelYNoExistaUsuarioLogueadoMeRedirijaAlLoguin(){
        //preparacion
        when(requestMock.getSession(false)).thenReturn(null);

        //ejecucion
        ModelAndView modelAndView = controladorMovimientoCompartido.volverAPanel(requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    @Test
    public void queAlClickearAgregarAmigoMeRedirijaALaVistaDeAgregarAmigo() throws ExcepcionBaseDeDatos {
        //preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);

        //ejecucion
        ModelAndView modelAndView = controladorMovimientoCompartido.irAAgregarAmigo(requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("agregar-amigo"));
    }

    @Test
    public void queAlQuererAgregarUnAmigoSePuedaAgregarAmigo() throws ExcepcionBaseDeDatos{
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
    public void queNoSePuedaAgregarUnAmigoQueYaEsAmigo() throws ExcepcionBaseDeDatos {
        // Preparación
        Long idUsuario = 1L;
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(idUsuario);
        Usuario amigo = new Usuario();
        doThrow(new ExcepcionBaseDeDatos("El usuario ya es tu amigo")).when(servicioMovimientoCompartidoMock).agregarNuevoAmigo(idUsuario, amigo.getEmail());

        // Ejecución
        ModelAndView modelAndView = controladorMovimientoCompartido.agregarNuevoAmigo(amigo, requestMock);

        // Validación
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("agregar-amigo"));
        verify(servicioMovimientoCompartidoMock, times(1)).agregarNuevoAmigo(idUsuario, amigo.getEmail());
    }

    @Test
    public void queLanceUnaExepcionCuandoAgregoDeAmigoAUnUsuarioQueNoExiste() throws ExcepcionBaseDeDatos {
        // Preparación
        Long idUsuario = 1L;
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(idUsuario);
        Usuario amigo = new Usuario();
        doThrow(new ExcepcionBaseDeDatos("No se encontró un usuario con el email proporcionado")).when(servicioMovimientoCompartidoMock).agregarNuevoAmigo(idUsuario, amigo.getEmail());

        // Ejecución
        ModelAndView modelAndView = controladorMovimientoCompartido.agregarNuevoAmigo(amigo, requestMock);

        // Validación
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("agregar-amigo"));
        verify(servicioMovimientoCompartidoMock, times(1)).agregarNuevoAmigo(idUsuario, amigo.getEmail());
    }

    @Test
    public void queNoSePuedaAgregarDosVecesDeAmigoAlMismoUsuario() throws ExcepcionBaseDeDatos {
        // Preparación
        Long idUsuario = 1L;
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(idUsuario);
        Usuario amigo = new Usuario();
        doThrow(new ExcepcionBaseDeDatos("Ya has enviado una solicitud de amistad a este usuario")).when(servicioMovimientoCompartidoMock).agregarNuevoAmigo(idUsuario, amigo.getEmail());

        // Ejecución
        ModelAndView modelAndView = controladorMovimientoCompartido.agregarNuevoAmigo(amigo, requestMock);

        // Validación
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("agregar-amigo"));
        verify(servicioMovimientoCompartidoMock, times(1)).agregarNuevoAmigo(idUsuario, amigo.getEmail());
    }

    @Test
    public void queSePuedaEliminarUnaSolicitudDeAmistadEnviada() throws ExcepcionBaseDeDatos {
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
    public void queSePuedaAceptarUnaSolicitudDeAmistad() throws ExcepcionBaseDeDatos {
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
    public void queSePuedaEliminarAmigo() throws ExcepcionBaseDeDatos {
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
}
