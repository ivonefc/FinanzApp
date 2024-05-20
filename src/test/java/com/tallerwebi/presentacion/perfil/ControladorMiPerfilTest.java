package com.tallerwebi.presentacion.perfil;

import org.junit.jupiter.api.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.mockito.Mockito.*;

public class ControladorMiPerfilTest {

    private ControladorMiPerfil controladorMiPerfil;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;

    @BeforeEach
    public void init(){
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        controladorMiPerfil = new ControladorMiPerfil();
    }

    @Test
    public void queAlClickearLaOpcionMiPerfilEnNavDeUsuarioDirijaALaVistaMiPerfil(){
        //preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);

        //ejecucion
        ModelAndView modelAndView = controladorMiPerfil.irAMiPerfil(requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("mi-perfil"));
    }

    @Test
    public void queAlQuererIrALaOpcionMiPerfilYNoExistaUsuarioLogueadoMeRedirijaAlLoguin(){
        //preparacion
        when(requestMock.getSession(false)).thenReturn(null);

        //ejecucion
        ModelAndView modelAndView = controladorMiPerfil.irAMiPerfil(requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

}
