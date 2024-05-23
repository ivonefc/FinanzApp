package com.tallerwebi.presentacion.panel;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import org.junit.jupiter.api.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.mockito.Mockito.*;

public class ControladorPanelTest {

    private ControladorPanel controladorPanel;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;

    @BeforeEach
    public void init(){
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        controladorPanel = new ControladorPanel();
    }

    @Test
    public void queAlClickearLaOpcionPanelEnElMenuDirijaALaVistaPanel() throws ExcepcionBaseDeDatos {
        //preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);

        //ejecucion
        ModelAndView modelAndView = controladorPanel.irAPanel(requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("panel"));
    }

    @Test
    public void queAlQuererIrALaOpcionPanelYNoExistaUsuarioLogueadoMeRedirijaAlLoguin() throws ExcepcionBaseDeDatos {
        //preparacion
        when(requestMock.getSession(false)).thenReturn(null);

        //ejecucion
        ModelAndView modelAndView = controladorPanel.irAPanel(requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

}
