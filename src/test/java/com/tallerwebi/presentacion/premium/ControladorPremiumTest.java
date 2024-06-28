package com.tallerwebi.presentacion.premium;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControladorPremiumTest {

    private ControladorPremium controladorPremium;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;

    @BeforeEach
    public void init(){
        controladorPremium = new ControladorPremium();
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
    }

    @Test
    public void queAlClickearLaOpcionPremiumEnElHeaderDirijaALaVistaPremium(){
        //preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);

        //ejecucion
        ModelAndView modelAndView = controladorPremium.irAPremium(requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("premium"));
    }

    @Test
    public void queAlQuererIrALaOpcionPremiumYNoExistaUsuarioLogueadoMeRedirijaAlLoguin(){
        //preparacion
        when(requestMock.getSession(false)).thenReturn(null);

        //ejecucion
        ModelAndView modelAndView = controladorPremium.irAPremium(requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    @Test
    public void queAlClickearLaOpcioAdquirirPlanEnLaVistaPremiumDirijaAVistaMetodoDePago(){
        //preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);

        //ejecucion
        ModelAndView modelAndView = controladorPremium.irAMetodoPagoPremium(requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("metodo-pago"));
    }

    @Test
    public void queAlQuererIrALaOpcionAdquirirPlanYNoExistaUsuarioLogueadoMeRedirijaAlLoguin(){
        //preparacion
        when(requestMock.getSession(false)).thenReturn(null);

        //ejecucion
        ModelAndView modelAndView = controladorPremium.irAMetodoPagoPremium(requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
    }


}
