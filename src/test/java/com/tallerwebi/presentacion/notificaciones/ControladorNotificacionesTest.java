package com.tallerwebi.presentacion.notificaciones;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;
import com.tallerwebi.dominio.movimiento.ServicioMovimiento;
import com.tallerwebi.dominio.movimientoCompartido.ServicioMovimientoCompartido;
import com.tallerwebi.dominio.notificacion.Notificacion;
import com.tallerwebi.dominio.usuario.ServicioUsuario;
import com.tallerwebi.dominio.usuario.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControladorNotificacionesTest {

    private ControladorNotificaciones controladorNotificaciones;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;
    private ServicioUsuario servicioUsuarioMock;
    private ServicioMovimientoCompartido servicioMovimientoCompartidoMock;
    private ServicioMovimiento servicioMovimientoMock;

    @BeforeEach
    public void init(){
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        servicioUsuarioMock = mock(ServicioUsuario.class);
        servicioMovimientoCompartidoMock = mock(ServicioMovimientoCompartido.class);
        servicioMovimientoMock = mock(ServicioMovimiento.class);
        controladorNotificaciones = new ControladorNotificaciones(servicioMovimientoCompartidoMock, servicioUsuarioMock, servicioMovimientoMock);
    }

    @Test
    public void queAlQuererIrANotificacionesMeLleveCorrectamente() throws ExcepcionBaseDeDatos, UsuarioInexistente, JsonProcessingException {
        // Preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(1L); // Assuming 1L is a valid user ID in your system
        Usuario usuarioMock = mock(Usuario.class);
        when(servicioUsuarioMock.obtenerUsuarioPorId(anyLong())).thenReturn(usuarioMock);
        List<Notificacion> notificacionesMock = new ArrayList<>();
        when(servicioMovimientoCompartidoMock.obtenerSolicitudesRecibidas(anyLong())).thenReturn(notificacionesMock);
        when(servicioMovimientoCompartidoMock.obtenerSolicitudesAceptadas(anyLong())).thenReturn(notificacionesMock);
        when(servicioMovimientoMock.obtenerMovimientosCompartidos(anyLong())).thenReturn(notificacionesMock);

        // Ejecucion
        ModelAndView modelAndView = controladorNotificaciones.irANotificaciones(requestMock);

        // Validacion
        assertEquals("notificaciones", modelAndView.getViewName());
        assertTrue(modelAndView.getModel().containsKey("usuario"));
        assertTrue(modelAndView.getModel().containsKey("notificaciones"));
        assertTrue(modelAndView.getModel().containsKey("DatosNotificacion"));

    }

    @Test
    public void queAlQuererIrANotificacionesSinSesionMeRedirijaALogin() throws ExcepcionBaseDeDatos, UsuarioInexistente, JsonProcessingException {
        // Preparacion
        when(requestMock.getSession(false)).thenReturn(null);

        // Ejecucion
        ModelAndView modelAndView = controladorNotificaciones.irANotificaciones(requestMock);

        // Validacion
        assertEquals("redirect:/login", modelAndView.getViewName());
    }

    @Test
    public void queAlQuererIrANotificacionesLanceExcepcionUsuarioInexistente() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        // Preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(1L); // Assuming 1L is a valid user ID in your system
        when(servicioUsuarioMock.obtenerUsuarioPorId(anyLong())).thenThrow(UsuarioInexistente.class);

        // Ejecucion y Validacion
        assertThrows(UsuarioInexistente.class, () -> {
            controladorNotificaciones.irANotificaciones(requestMock);
        });
    }

    @Test
    public void queAlQuererIrANotificacionesLanceExcepcionBaseDeDatos() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        // Preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(1L); // Assuming 1L is a valid user ID in your system
        when(servicioUsuarioMock.obtenerUsuarioPorId(anyLong())).thenThrow(ExcepcionBaseDeDatos.class);

        // Ejecucion y Validacion
        assertThrows(ExcepcionBaseDeDatos.class, () -> {
            controladorNotificaciones.irANotificaciones(requestMock);
        });
    }

}
