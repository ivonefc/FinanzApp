package com.tallerwebi.presentacion.movimientoCompartido;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.movimientoCompartido.ServicioMovimientoCompartido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.mockito.Mockito.*;

public class ControladorMovimientoCompartidoTest {

    private ControladorMovimientoCompartido controladorMovimientoCompartido;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;
    private ServicioMovimientoCompartido servicioMovimientoCompartidoMock;

    @BeforeEach
    public void init(){
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        servicioMovimientoCompartidoMock = mock(ServicioMovimientoCompartido.class);
        controladorMovimientoCompartido = new ControladorMovimientoCompartido(servicioMovimientoCompartidoMock);
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

}
