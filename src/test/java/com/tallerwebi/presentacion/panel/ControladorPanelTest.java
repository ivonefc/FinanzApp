package com.tallerwebi.presentacion.panel;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.movimiento.Movimiento;
import com.tallerwebi.dominio.panel.ServicioPanel;
import org.junit.jupiter.api.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class ControladorPanelTest {

    private ControladorPanel controladorPanel;
    private ServicioPanel servicioPanelMock;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;

    @BeforeEach
    public void init(){
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        servicioPanelMock = mock(ServicioPanel.class);
        controladorPanel = new ControladorPanel(servicioPanelMock);
    }

    @Test
    public void queAlClickearLaOpcionPanelEnElMenuDirijaALaVistaPanel() {
        //preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("nombreUsuario")).thenReturn("nombreUsuario");

        //ejecucion
        ModelAndView modelAndView = controladorPanel.irAPanel(requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("panel"));
    }

    @Test
    public void queAlQuererIrALaOpcionPanelYNoExistaUsuarioLogueadoMeRedirijaAlLoguin() {
        //preparacion
        when(requestMock.getSession(false)).thenReturn(null);

        //ejecucion
        ModelAndView modelAndView = controladorPanel.irAPanel(requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    @Test
    public void queAlQuererIrALaOpcionPanelMeMuestreDeFormaCorrectaElNombreDeUsuario() {
        //preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("nombreUsuario")).thenReturn("nombreUsuario");

        //ejecucion
        ModelAndView modelAndView = controladorPanel.irAPanel(requestMock);

        //validacion
        assertNotNull(modelAndView);
        assertThat(modelAndView.getModel().get("nombreUsuario").toString(), equalToIgnoringCase("nombreUsuario"));
    }

    @Test
    public void queAlQuererIrALaOpcionPanelYNoExistaNombreDeUsuarioMeRedirijaAlLoguin() {
        //preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("nombreUsuario")).thenReturn(null);

        //ejecucion
        ModelAndView modelAndView = controladorPanel.irAPanel(requestMock);

        //validacion
        assertNotNull(modelAndView);
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    @Test
    public void queAlSolicitarAlServicioObtenerEgresosMeDevuelvaUnaListaDeEgresos() throws ExcepcionBaseDeDatos {
        //preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(1L);

        //ejecucion
        controladorPanel.obtenerEgresos(requestMock);

        //validacion
        verify(servicioPanelMock, times(1)).obtenerMovimientosEgresosPorUsuario(1L);
    }

    @Test
    public void queAlSolicitarAlServicioObtenerEgresosMeLanceExcepcionBaseDeDatos() throws ExcepcionBaseDeDatos {
        //preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(1L);
        doThrow(new ExcepcionBaseDeDatos()).when(servicioPanelMock).obtenerMovimientosEgresosPorUsuario(1L);

        //ejecucion y validacion
        assertThrows(ExcepcionBaseDeDatos.class, () -> controladorPanel.obtenerEgresos(requestMock));
        verify(servicioPanelMock, times(1)).obtenerMovimientosEgresosPorUsuario(1L);
    }

    @Test
    public void queAlSolicitarAlServicioObtenerEgresosYNoHayaSesionMeDevuelvaNull() throws ExcepcionBaseDeDatos {
        //preparacion
        when(requestMock.getSession(false)).thenReturn(null);

        //ejecucion
        controladorPanel.obtenerEgresos(requestMock);

        //validacion
        verify(servicioPanelMock, never()).obtenerMovimientosEgresosPorUsuario(1L);
    }

    @Test
    public void queAlSolicitarAlServicioObtenerEgresosDevuelvaUnaListaVaciaPorNoTenerEgresos() throws ExcepcionBaseDeDatos {
        //preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(1L);
        doReturn(Collections.emptyList()).when(servicioPanelMock).obtenerMovimientosEgresosPorUsuario(1L);

        //ejecucion
        List<Movimiento> result = controladorPanel.obtenerEgresos(requestMock);

        //validacion
        verify(servicioPanelMock, times(1)).obtenerMovimientosEgresosPorUsuario(1L);
        assertTrue(result.isEmpty());
    }

    @Test
    public void queAlSolicitarAlServicioObtenerIngresosMeDevuelvaUnaListaDeIngresos() throws ExcepcionBaseDeDatos {
        //preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(1L);

        //ejecucion
        controladorPanel.obtenerIngresos(requestMock);

        //validacion
        verify(servicioPanelMock, times(1)).obtenerMovimientosIngresosPorUsuario(1L);
    }

    @Test
    public void queAlSolicitarAlServicioObtenerIngresosMeLanceExcepcionBaseDeDatos() throws ExcepcionBaseDeDatos {
        //preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(1L);
        doThrow(new ExcepcionBaseDeDatos()).when(servicioPanelMock).obtenerMovimientosIngresosPorUsuario(1L);

        //ejecucion y validacion
        assertThrows(ExcepcionBaseDeDatos.class, () -> controladorPanel.obtenerIngresos(requestMock));
        verify(servicioPanelMock, times(1)).obtenerMovimientosIngresosPorUsuario(1L);
    }

    @Test
    public void queAlSolicitarAlServicioObtenerIngresosYNoHayaSesionMeDevuelvaNull() throws ExcepcionBaseDeDatos {
        //preparacion
        when(requestMock.getSession(false)).thenReturn(null);

        //ejecucion
        controladorPanel.obtenerIngresos(requestMock);

        //validacion
        verify(servicioPanelMock, never()).obtenerMovimientosIngresosPorUsuario(1L);
    }

    @Test
    public void queAlSolicitarAlServicioObtenerIngresosDevuelvaUnaListaVaciaPorNoTenerIngresos() throws ExcepcionBaseDeDatos {
        //preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(1L);
        doReturn(Collections.emptyList()).when(servicioPanelMock).obtenerMovimientosIngresosPorUsuario(1L);

        //ejecucion
        List<Movimiento> result = controladorPanel.obtenerIngresos(requestMock);

        //validacion
        verify(servicioPanelMock, times(1)).obtenerMovimientosIngresosPorUsuario(1L);
        assertTrue(result.isEmpty());
    }

}
