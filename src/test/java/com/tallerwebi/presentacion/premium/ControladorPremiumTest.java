package com.tallerwebi.presentacion.premium;

import com.tallerwebi.dominio.email.Response;
import com.tallerwebi.dominio.email.ServicioEmail;
import com.tallerwebi.dominio.excepcion.ErrorEmail;
import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;
import com.tallerwebi.dominio.usuario.ServicioUsuario;
import com.tallerwebi.dominio.usuario.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.mockito.Mockito.*;

public class ControladorPremiumTest {

    private ControladorPremium controladorPremium;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;
    private ServicioUsuario servicioUsuarioMock;
    ServicioEmail servicioEmailMock;

    @BeforeEach
    public void init(){
        servicioUsuarioMock = mock(ServicioUsuario.class);
        servicioEmailMock = mock(ServicioEmail.class);
        controladorPremium = new ControladorPremium(servicioUsuarioMock, servicioEmailMock);
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
    }

    @Test
    public void queAlClickearLaOpcionPremiumEnElHeaderDirijaALaVistaPremium() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        //preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);

        //ejecucion
        ModelAndView modelAndView = controladorPremium.irAPremium(requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("premium"));
    }

    @Test
    public void queAlQuererIrALaOpcionPremiumYNoExistaUsuarioLogueadoMeRedirijaAlLoguin() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        //preparacion
        when(requestMock.getSession(false)).thenReturn(null);

        //ejecucion
        ModelAndView modelAndView = controladorPremium.irAPremium(requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    @Test
    public void queAlClickearLaOpcioAdquirirPlanEnLaVistaPremiumDirijaAVistaMetodoDePago() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        //preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);

        //ejecucion
        ModelAndView modelAndView = controladorPremium.irAMetodoPagoPremium(requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("metodo-pago"));
    }

    @Test
    public void queAlQuererIrALaOpcionAdquirirPlanYNoExistaUsuarioLogueadoMeRedirijaAlLoguin() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        //preparacion
        when(requestMock.getSession(false)).thenReturn(null);

        //ejecucion
        ModelAndView modelAndView = controladorPremium.irAMetodoPagoPremium(requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    @Test
    public void queAlCompletarLosDatosYPresionarPagarRedirijaALaVistaDeEmailYMandeUnMailAlUsuario() throws ExcepcionBaseDeDatos, UsuarioInexistente, ErrorEmail {
        //preparacion
        Long idUsuario = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(idUsuario);
        usuario.setNombre("Maria");
        usuario.setEmail("maria@gmail.com");

        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(idUsuario);
        when(servicioUsuarioMock.obtenerUsuarioPorId(idUsuario)).thenReturn(usuario);
        doNothing().when(servicioUsuarioMock).actualizarPlan(usuario);
        when(servicioEmailMock.enviarEmailPremium(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(new Response("En breve recibirás un email con todos los detalles de tu suscripción.\n" +
                        "\n" +
                        "¡Gracias por elegirnos para gestionar tus finanzas!"));


        //ejecucion
        ModelAndView modelAndView = controladorPremium.pagar(requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("email-premium-enviado"));
        ModelMap modelMap = modelAndView.getModelMap();
        assertThat( modelMap.get("usuario"), equalTo(usuario));
        assertThat(modelMap.get("mensajePremium").toString(), equalTo("En breve recibirás un email con todos los detalles de tu suscripción.\n" +
                "\n" +
                "¡Gracias por elegirnos para gestionar tus finanzas!"));

        verify(servicioUsuarioMock, times(1)).obtenerUsuarioPorId(idUsuario);
        verify(servicioUsuarioMock, times(1)).actualizarPlan(usuario);
        verify(servicioEmailMock, times(1)).enviarEmailPremium(anyString(), anyString(), anyString(), anyString(), anyString());
    }


}
