package com.tallerwebi.presentacion;


import com.tallerwebi.dominio.agregarMovimiento.ServicioAgregarMovimiento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControladorAgregarMovimientoTest {

    ServicioAgregarMovimiento servicioAgregarMovimientoMock;
    ControladorAgregarMovimiento controladorAgregarMovimiento;
    HttpServletRequest httpServletRequestMock;
    HttpSession httpSessionMock;


    @BeforeEach
    public void init(){
        servicioAgregarMovimientoMock = mock(ServicioAgregarMovimiento.class);
        controladorAgregarMovimiento = new ControladorAgregarMovimiento(servicioAgregarMovimientoMock);
        httpServletRequestMock = mock(HttpServletRequest.class);
        httpSessionMock = mock(HttpSession.class);
    }
    @Test
    public void queAlClickearEnLaBarraDeNavegacionEnAgregarMovimientoYNoExistaUsuarioLogueadoMeRedirijaAlLogin() {

        //Preparacion
        when(httpServletRequestMock.getSession(false)).thenReturn(null);

        //Ejecucion

        ModelAndView modelAndView = controladorAgregarMovimiento.irAAgregarMovimiento(httpServletRequestMock);

        //Validacion

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    @Test
    public void queAlClickearEnLaBarraDeNavegacionEnAgregarMovimientoTeLleveALaPaginaAgregarMovimiento() {

        //Preparacion
        when(httpSessionMock.getAttribute("idUsuario")).thenReturn(1);
        when(httpServletRequestMock.getSession(false)).thenReturn(httpSessionMock);

        //Ejecucion

        ModelAndView modelAndView = controladorAgregarMovimiento.irAAgregarMovimiento(httpServletRequestMock);

        //Validacion

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("agregar-movimiento"));


    }



}
