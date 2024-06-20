package com.tallerwebi.presentacion.ajustes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.mockito.Mockito.*;

public class ControladorAjustesTest {

    private ControladorAjustes controladorAjustes;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;

    @BeforeEach
    public void init(){
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        controladorAjustes = new ControladorAjustes();
    }

    @Test
    public void queAlClickearLaOpcionAjustesEnElMenuDirijaALaVistaAjustes(){
        //preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);

        //ejecucion
        ModelAndView modelAndView = controladorAjustes.irAAjustes(requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("ajustes"));
    }

    @Test
    public void queAlQuererIrALaOpcionAjustesYNoExistaUsuarioLogueadoMeRedirijaAlLoguin(){
        //preparacion
        when(requestMock.getSession(false)).thenReturn(null);

        //ejecucion
        ModelAndView modelAndView = controladorAjustes.irAAjustes(requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    @Test
    public void queAlClickearVolverAPaginaDeInicioMeRedirijaAPanel(){
        //preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);

        //ejecucion
        ModelAndView modelAndView = controladorAjustes.volverAPanel(requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/panel"));
    }

    @Test
    public void queAlQuererVolverAPanelYNoExistaUsuarioLogueadoMeRedirijaAlLoguin(){
        //preparacion
        when(requestMock.getSession(false)).thenReturn(null);

        //ejecucion
        ModelAndView modelAndView = controladorAjustes.volverAPanel(requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    @Test
    public void queAlClickearIrAMiPerfilMeRedirijaAMiPerfil(){
        //preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);

        //ejecucion
        ModelAndView modelAndView = controladorAjustes.irAMiPerfil(requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/mi-perfil"));
    }

    @Test
    public void queAlQuererIrAMiPerfilYNoExistaUsuarioLogueadoMeRedirijaAlLoguin(){
        //preparacion
        when(requestMock.getSession(false)).thenReturn(null);

        //ejecucion
        ModelAndView modelAndView = controladorAjustes.irAMiPerfil(requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    @Test
    public void queAlClickearEditarColoresDeCategoriaMeRedirijaAEditarColores(){
        //preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);

        //ejecucion
        ModelAndView modelAndView = controladorAjustes.irAEditarColores(requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/categorias/editar-colores"));
    }

    @Test
    public void queAlQuererIrAEditarColoresDeCategoriaYNoExistaUsuarioLogueadoMeRedirijaAlLoguin(){
        //preparacion
        when(requestMock.getSession(false)).thenReturn(null);

        //ejecucion
        ModelAndView modelAndView = controladorAjustes.irAEditarColores(requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

}
