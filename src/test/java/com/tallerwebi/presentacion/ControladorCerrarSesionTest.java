package com.tallerwebi.presentacion;

import org.junit.jupiter.api.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.mockito.Mockito.*;

public class ControladorCerrarSesionTest {

    private ControladorCerrarSesion controladorCerrarSesion;
    private HttpSession sessionMock;
    private HttpServletRequest requestMock;

    @BeforeEach
    public void init(){
        sessionMock = mock(HttpSession.class);
        requestMock = mock(HttpServletRequest.class);
        controladorCerrarSesion = new ControladorCerrarSesion();
    }

    @Test
    public void cerrarSesionDeberiaInvalidarLaSesionYRedirigirALogin(){
        // preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);

        // ejecucion
        ModelAndView modelAndView = controladorCerrarSesion.cerrarSesion(sessionMock);

        // validacion
        verify(sessionMock, times(1)).invalidate();
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    @Test
    public void queNoSePuedaCerrarSesionSiNoExisteSesionActiva(){
        // preparacion
        when(requestMock.getSession(false)).thenReturn(null);

        // ejecucion
        ModelAndView modelAndView = controladorCerrarSesion.cerrarSesion(sessionMock);

        // validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

}
