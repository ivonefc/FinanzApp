package com.tallerwebi.presentacion.notificaciones;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;
import com.tallerwebi.dominio.movimientoCompartido.ServicioMovimientoCompartido;
import com.tallerwebi.dominio.notificacion.Notificacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControladorNotificacionHeaderTest {

    private ControladorNotificacionHeader controladorNotificacionHeader;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;
    private ServicioMovimientoCompartido servicioMovimientoCompartidoMock;

    @BeforeEach
    public void init(){
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        servicioMovimientoCompartidoMock = mock(ServicioMovimientoCompartido.class);
        controladorNotificacionHeader = new ControladorNotificacionHeader(servicioMovimientoCompartidoMock);
    }

    @Test
    public void queAlQuererObtenerNotificacionesRecibidasLasObtengaCorrectamente() throws ExcepcionBaseDeDatos {
        // Preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(1L);
        List<Notificacion> notificacionesMock = new ArrayList<>();
        when(servicioMovimientoCompartidoMock.obtenerSolicitudesRecibidas(anyLong())).thenReturn(notificacionesMock);

        // Ejecucion
        List<Notificacion> notificacionesRecibidas = controladorNotificacionHeader.obtenerNotificaciones(requestMock);

        // Validacion
        assertEquals(notificacionesMock, notificacionesRecibidas);
    }

    @Test
    public void queAlQuererObtenerNotificacionesRecibidasDevuelvaUnaListaVaciaAlNoTenerNotificaciones() throws ExcepcionBaseDeDatos {
        // Preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(1L);
        when(servicioMovimientoCompartidoMock.obtenerSolicitudesRecibidas(anyLong())).thenReturn(new ArrayList<>());

        // Ejecucion
        List<Notificacion> notificacionesRecibidas = controladorNotificacionHeader.obtenerNotificaciones(requestMock);

        // Validacion
        assertEquals(0, notificacionesRecibidas.size());
    }

    @Test
    public void queAlQuererObtenerNotificacionesRecibidasDevuelvaUnaListaVaciaAlNoTenerSesion() throws ExcepcionBaseDeDatos {
        // Preparacion
        when(requestMock.getSession(false)).thenReturn(null);

        // Ejecucion
        List<Notificacion> notificacionesRecibidas = controladorNotificacionHeader.obtenerNotificaciones(requestMock);

        // Validacion
        assertEquals(0, notificacionesRecibidas.size());
    }

    @Test
    public void queAlQuererObtenerNotificacionesRecibidasLanceExcepcionBaseDeDatos() throws ExcepcionBaseDeDatos {
        // Preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(1L);
        when(servicioMovimientoCompartidoMock.obtenerSolicitudesRecibidas(anyLong())).thenThrow(ExcepcionBaseDeDatos.class);

        // Ejecucion y validacion
        assertThrows(ExcepcionBaseDeDatos.class, () -> {
            controladorNotificacionHeader.obtenerNotificaciones(requestMock);
        });
    }

    @Test
    public void queAlQuererObtenerNotificacionesAceptadasLasObtengaCorrectamente() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        // Preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(1L);
        List<Notificacion> notificacionesMock = new ArrayList<>();
        when(servicioMovimientoCompartidoMock.obtenerSolicitudesAceptadas(anyLong())).thenReturn(notificacionesMock);

        // Ejecucion
        List<Notificacion> notificacionesAceptadas = controladorNotificacionHeader.obtenerNotificacionesAceptadas(requestMock);

        // Validacion
        assertEquals(notificacionesMock, notificacionesAceptadas);
    }

    @Test
    public void queAlQuererObtenerNotificacionesAceptadasDevuelvaUnaListaVaciaAlNoTenerNotificacionesAcepatadas() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        // Preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(1L);
        when(servicioMovimientoCompartidoMock.obtenerSolicitudesAceptadas(anyLong())).thenReturn(new ArrayList<>());

        // Ejecucion
        List<Notificacion> notificacionesAceptadas = controladorNotificacionHeader.obtenerNotificacionesAceptadas(requestMock);

        // Validacion
        assertEquals(0, notificacionesAceptadas.size());
    }

    @Test
    public void queAlQuererObtenerNotificacionesAceptadasDevuelvaUnaListaVaciaAlNoTenerSesion() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        // Preparacion
        when(requestMock.getSession(false)).thenReturn(null);

        // Ejecucion
        List<Notificacion> notificacionesAceptadas = controladorNotificacionHeader.obtenerNotificacionesAceptadas(requestMock);

        // Validacion
        assertEquals(0, notificacionesAceptadas.size());
    }

    @Test
    public void queAlQuererObtenerNotificacionesAceptadasLanceExcepcionBaseDeDatos() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        // Preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(1L);
        when(servicioMovimientoCompartidoMock.obtenerSolicitudesAceptadas(anyLong())).thenThrow(ExcepcionBaseDeDatos.class);

        // Ejecucion y validacion
        assertThrows(ExcepcionBaseDeDatos.class, () -> {
            controladorNotificacionHeader.obtenerNotificacionesAceptadas(requestMock);
        });
    }


}
