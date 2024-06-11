package com.tallerwebi.presentacion.perfil;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;
import com.tallerwebi.dominio.usuario.ServicioUsuario;
import com.tallerwebi.dominio.usuario.Usuario;
import org.junit.jupiter.api.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.mockito.Mockito.*;

public class ControladorMiPerfilTest {

    private ControladorMiPerfil controladorMiPerfil;
    private ServicioUsuario servicioUsuarioMock;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;

    @BeforeEach
    public void init(){
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        servicioUsuarioMock = mock(ServicioUsuario.class);
        controladorMiPerfil = new ControladorMiPerfil(servicioUsuarioMock);
    }

    @Test
    public void queAlClickearLaOpcionMiPerfilEnNavDeUsuarioDirijaALaVistaMiPerfil() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        //preparacion
        Long idUsuarioMock = 1L;
        Usuario usuarioMock = new Usuario();
        usuarioMock.setId(idUsuarioMock);

        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(idUsuarioMock);
        when(servicioUsuarioMock.obtenerUsuarioPorId(idUsuarioMock)).thenReturn(usuarioMock);

        //ejecucion
        ModelAndView modelAndView = controladorMiPerfil.irAMiPerfil(requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("mi-perfil"));
    }

    @Test
    public void queAlQuererIrALaOpcionMiPerfilYNoExistaUsuarioLogueadoMeRedirijaAlLoguin() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        //preparacion
        when(requestMock.getSession(false)).thenReturn(null);

        //ejecucion
        ModelAndView modelAndView = controladorMiPerfil.irAMiPerfil(requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    @Test
    public void queAlClickearVolverAPaginaDeInicioMeRedirijaAPanel(){
        //preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);

        //ejecucion
        ModelAndView modelAndView = controladorMiPerfil.volverAPanel(requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/panel"));
    }

    @Test
    public void queAlClickearVolverAPaginaDeInicioYNoExistaUsuarioLogueadoMeRedirijaALogin(){
        //preparacion
        when(requestMock.getSession(false)).thenReturn(null);

        //ejecucion
        ModelAndView modelAndView = controladorMiPerfil.volverAPanel(requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

}
