package com.tallerwebi.presentacion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.mockito.Mockito.*;

public class ControladorCuentasCompartidasTest {

    private ControladorCuentasCompartidas controladorCuentasCompartidas;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;

    @BeforeEach
    public void init(){
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        controladorCuentasCompartidas = new ControladorCuentasCompartidas();
    }

    @Test
    public void queAlClickearLaOpcionCuentasCompartidasEnElMenuDirijaALaVistaCuentasCompartidas(){
        //preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);

        //ejecucion
        ModelAndView modelAndView = controladorCuentasCompartidas.irACuentasCompartidas(requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("cuentas-compartidas"));
    }

    @Test
    public void queAlQuererIrALaOpcionCuentasCompartidasYNoExistaUsuarioLogueadoMeRedirijaAlLoguin(){
        //preparacion
        when(requestMock.getSession(false)).thenReturn(null);

        //ejecucion
        ModelAndView modelAndView = controladorCuentasCompartidas.irACuentasCompartidas(requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

}
