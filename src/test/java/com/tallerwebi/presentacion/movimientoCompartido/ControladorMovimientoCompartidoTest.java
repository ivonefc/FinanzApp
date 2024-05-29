package com.tallerwebi.presentacion.movimientoCompartido;

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

    @BeforeEach
    public void init(){
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        controladorMovimientoCompartido = new ControladorMovimientoCompartido();
    }

    @Test
    public void queAlClickearLaOpcionMovimientosCompartidosEnElMenuDirijaALaVistaMovimientosCompartidos(){
        //preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);

        //ejecucion
        ModelAndView modelAndView = controladorMovimientoCompartido.irAMovimientosCompartidos(requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("movimientos-compartidos"));
    }

    @Test
    public void queAlQuererIrALaOpcionCuentasCompartidasYNoExistaUsuarioLogueadoMeRedirijaAlLoguin(){
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
