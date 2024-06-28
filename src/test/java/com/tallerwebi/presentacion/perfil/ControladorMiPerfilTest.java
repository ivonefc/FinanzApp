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
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    public void queAlQuererIrAEditarPerfilTeLleveAlFormularioDeEditarPerfil() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        //preparacion
        Long idUsuarioMock = 1L;
        Usuario usuarioMock = new Usuario();
        usuarioMock.setId(idUsuarioMock);

        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(servicioUsuarioMock.obtenerUsuarioPorId(idUsuarioMock)).thenReturn(usuarioMock);

        //ejecucion
        ModelAndView modelAndView = controladorMiPerfil.irAEditarPerfil(idUsuarioMock, requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("editar-perfil"));
    }

    @Test
    public void queAlQuererIrAEditarPerfilYNoExistaUsuarioLogueadoMeRedirijaALoguin() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        //preparacion
        Long idUsuarioMock = 1L;
        when(requestMock.getSession(false)).thenReturn(null);

        //ejecucion
        ModelAndView modelAndView = controladorMiPerfil.irAEditarPerfil(idUsuarioMock, requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    @Test
    public void queAlQuererIrAEditarPerfilLanceExcepcionBaseDeDatos() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        //preparacion
        Long idUsuarioMock = 1L;
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(servicioUsuarioMock.obtenerUsuarioPorId(idUsuarioMock)).thenThrow(new ExcepcionBaseDeDatos("Base de datos no disponible"));

        //ejecucion y validacion
        assertThrows(ExcepcionBaseDeDatos.class, () -> {
            controladorMiPerfil.irAEditarPerfil(idUsuarioMock, requestMock);
        });
    }

    @Test
    public void queAlQuererIrAEditarPerfilLanceExcepcionUsuarioInexistente() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        //preparacion
        Long idUsuarioMock = 1L;
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(servicioUsuarioMock.obtenerUsuarioPorId(idUsuarioMock)).thenThrow(new UsuarioInexistente());

        //ejecucion y validacion
        assertThrows(UsuarioInexistente.class, () -> {
            controladorMiPerfil.irAEditarPerfil(idUsuarioMock, requestMock);
        });
    }


}
