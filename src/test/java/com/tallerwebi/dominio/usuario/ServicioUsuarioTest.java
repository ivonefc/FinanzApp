package com.tallerwebi.dominio.usuario;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionCamposInvalidos;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ServicioUsuarioTest {
    ServicioUsuario servicioUsuario;
    RepositorioUsuario repositorioUsuarioMock;
    HttpServletRequest httpServletRequestMock;
    HttpSession httpSessionMock;
    Usuario usuarioMock;

    @BeforeEach
    public void init() {
        repositorioUsuarioMock = mock(RepositorioUsuario.class);
        httpServletRequestMock = mock(HttpServletRequest.class);
        httpSessionMock = mock(HttpSession.class);
        usuarioMock = mock(Usuario.class);
        servicioUsuario = new ServicioUsuarioImpl(repositorioUsuarioMock);
    }

    @Test
    public void queAlSolicitarAlServicioBuscarUsuarioPorEmailYPasswordLoBusqueCorrectamente() throws ExcepcionBaseDeDatos, UsuarioInexistente, ExcepcionCamposInvalidos {
        // preparacion
        when(usuarioMock.getEmail()).thenReturn("email@test");
        when(usuarioMock.getPassword()).thenReturn("password");
        when(repositorioUsuarioMock.buscarUsuarioPorEmailYPassword("email@test", "password")).thenReturn(usuarioMock);

        // ejecucion
        Usuario usuario = servicioUsuario.buscarUsuarioPorEmailYPassword("email@test", "password");

        // validacion
        assertEquals(usuarioMock, usuario);
        assertEquals("email@test", usuario.getEmail());
        assertEquals("password", usuario.getPassword());
    }

    @Test
    public void queAlSolicitarAlServicioBuscarUsuarioPorEmailYPasswordLanceExcepcionUsuarioInexistente() throws ExcepcionBaseDeDatos, UsuarioInexistente, ExcepcionCamposInvalidos {
        // preparacion
        when(repositorioUsuarioMock.buscarUsuarioPorEmailYPassword("email@test", "password")).thenThrow(new UsuarioInexistente());

        // ejecucion
        UsuarioInexistente e = assertThrows(UsuarioInexistente.class, () -> {
            servicioUsuario.buscarUsuarioPorEmailYPassword("email@test", "password");
        });

        // validacion
        assertEquals("No se encontro usuario", e.getMessage());
    }

    @Test
    public void queAlSolicitarAlServicioBuscarUsuarioPorEmailYPasswordLanceExcepcionBaseDeDatos() throws ExcepcionBaseDeDatos, UsuarioInexistente, ExcepcionCamposInvalidos {
        // preparacion
        when(repositorioUsuarioMock.buscarUsuarioPorEmailYPassword("email@test", "password")).thenThrow(new ExcepcionBaseDeDatos(new Exception()));

        // ejecucion
        ExcepcionBaseDeDatos excepcion = assertThrows(ExcepcionBaseDeDatos.class, () -> {
            servicioUsuario.buscarUsuarioPorEmailYPassword("email@test", "password");
        });

        // validacion
        assertEquals("Base de datos no disponible", excepcion.getMessage());
    }

    @Test
    public void queAlSolicitarAlServicioGuardarUsuarioLoHagaCorrectamente() throws ExcepcionBaseDeDatos, UsuarioInexistente, UsuarioExistente {
        // preparacion
        when(usuarioMock.getEmail()).thenReturn("email@test");
        when(repositorioUsuarioMock.buscarUsuarioPorEmail("email@test")).thenReturn(usuarioMock);

        // ejecucion
        servicioUsuario.guardar(usuarioMock);

        // validacion
        verify(repositorioUsuarioMock).guardar(usuarioMock);
        assertEquals("email@test", usuarioMock.getEmail());
    }

    @Test
    public void queAlSolicitarAlServicioGuardarUsuarioLanceExcepcionUsuarioExistente() throws ExcepcionBaseDeDatos, UsuarioInexistente, UsuarioExistente {
        // preparacion
        when(usuarioMock.getEmail()).thenReturn("email@test");
        when(repositorioUsuarioMock.buscarUsuarioPorEmail("email@test")).thenReturn(usuarioMock);
        doThrow(new UsuarioExistente()).when(repositorioUsuarioMock).guardar(usuarioMock);

        // ejecucion y validacion
        UsuarioExistente e = assertThrows(UsuarioExistente.class, () -> {
            servicioUsuario.guardar(usuarioMock);
        });

        // validacion
        assertEquals("El usuario ya existe", e.getMessage());
    }

    @Test
    public void queAlSolicitarAlServicioGuardarUsuarioLanceExcepcionBaseDeDatos() throws ExcepcionBaseDeDatos, UsuarioInexistente, UsuarioExistente {
        // preparacion
        when(usuarioMock.getEmail()).thenReturn("email@test");
        when(repositorioUsuarioMock.buscarUsuarioPorEmail("email@test")).thenReturn(usuarioMock);
        doThrow(new ExcepcionBaseDeDatos()).when(repositorioUsuarioMock).guardar(usuarioMock);

        // ejecucion
        ExcepcionBaseDeDatos excepcion = assertThrows(ExcepcionBaseDeDatos.class, () -> {
            servicioUsuario.guardar(usuarioMock);
        });

        // validacion
        assertEquals("Base de datos no disponible", excepcion.getMessage());
    }

    @Test
    public void queAlSolicitarAlServicioBuscarUsuarioPorEmailLoBusqueCorrectamente() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        // preparacion
        when(usuarioMock.getEmail()).thenReturn("email@test");
        when(repositorioUsuarioMock.buscarUsuarioPorEmail("email@test")).thenReturn(usuarioMock);

        // ejecucion
        Usuario usuario = servicioUsuario.buscarUsuarioPorEmail("email@test");

        // validacion
        assertEquals(usuarioMock, usuario);
        verify(repositorioUsuarioMock).buscarUsuarioPorEmail("email@test");
    }

    @Test
    public void queAlSolicitarAlServicioBuscarUsuarioPorEmailLanceExcepcionUsuarioInexistente() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        // preparacion
        when(repositorioUsuarioMock.buscarUsuarioPorEmail("email@test")).thenThrow(new UsuarioInexistente());

        // ejecucion
        UsuarioInexistente e = assertThrows(UsuarioInexistente.class, () -> {
            servicioUsuario.buscarUsuarioPorEmail("email@test");
        });

        // validacion
        assertEquals("No se encontro usuario", e.getMessage());
    }

    @Test
    public void queAlSolicitarAlServicioBuscarUsuarioPorEmailLanceExcepcionBaseDeDatos() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        // preparacion
        when(repositorioUsuarioMock.buscarUsuarioPorEmail("email@test")).thenThrow(new ExcepcionBaseDeDatos(new Exception()));

        // ejecucion
        ExcepcionBaseDeDatos excepcion = assertThrows(ExcepcionBaseDeDatos.class, () -> {
            servicioUsuario.buscarUsuarioPorEmail("email@test");
        });

        // validacion
        assertEquals("Base de datos no disponible", excepcion.getMessage());
    }

//    @Test
//    public void queAlSolicitarAlServicioModificarUsuarioLoHagaCorrectamente() {
//        // ejecucion
//        servicioUsuario.modificar(usuarioMock);
//
//        // validacion
//        verify(repositorioUsuarioMock).modificar(usuarioMock);
//    }

    @Test
    public void queAlSolicitarAlServicioObtenerUsuarioPorIdLoHagaCorrectamente() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        // preparacion
        when(repositorioUsuarioMock.obtenerUsuarioPorId(1L)).thenReturn(usuarioMock);

        // ejecucion
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(1L);

        // validacion
        assertEquals(usuarioMock, usuario);
        verify(repositorioUsuarioMock).obtenerUsuarioPorId(1L);
    }

    @Test
    public void queAlSolicitarAlServicioObtenerUsuarioPorIdLanceExcepcionUsuarioInexistente() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        // preparacion
        when(repositorioUsuarioMock.obtenerUsuarioPorId(1L)).thenThrow(new UsuarioInexistente());

        // ejecucion
        UsuarioInexistente e = assertThrows(UsuarioInexistente.class, () -> {
            servicioUsuario.obtenerUsuarioPorId(1L);
        });

        // validacion
        assertEquals("No se encontro usuario", e.getMessage());
    }

    @Test
    public void queAlSolicitarAlServicioObtenerUsuarioPorIdLanceExcepcionBaseDeDatos() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        // preparacion
        when(repositorioUsuarioMock.obtenerUsuarioPorId(1L)).thenThrow(new ExcepcionBaseDeDatos(new Exception()));

        // ejecucion
        ExcepcionBaseDeDatos excepcion = assertThrows(ExcepcionBaseDeDatos.class, () -> {
            servicioUsuario.obtenerUsuarioPorId(1L);
        });

        // validacion
        assertEquals("Base de datos no disponible", excepcion.getMessage());
    }

}
