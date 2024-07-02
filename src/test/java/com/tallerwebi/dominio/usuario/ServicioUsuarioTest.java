package com.tallerwebi.dominio.usuario;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionCamposInvalidos;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;
import com.tallerwebi.presentacion.perfil.DatosEditarPerfil;
import org.hamcrest.collection.IsMapWithSize;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
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
        when(repositorioUsuarioMock.buscarUsuarioPorEmailYPassword("email@test", "password")).thenThrow(new ExcepcionBaseDeDatos("Base de datos no disponible"));

        // ejecucion
        ExcepcionBaseDeDatos excepcion = assertThrows(ExcepcionBaseDeDatos.class, () -> {
            servicioUsuario.buscarUsuarioPorEmailYPassword("email@test", "password");
        });

        // validacion
        assertEquals("Base de datos no disponible", excepcion.getMessage());
        verify(repositorioUsuarioMock).buscarUsuarioPorEmailYPassword("email@test", "password");
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
        doThrow(new ExcepcionBaseDeDatos("Base de datos no disponible")).when(repositorioUsuarioMock).guardar(usuarioMock);

        // ejecucion
        ExcepcionBaseDeDatos excepcion = assertThrows(ExcepcionBaseDeDatos.class, () -> {
            servicioUsuario.guardar(usuarioMock);
        });

        // validacion
        assertEquals("Base de datos no disponible", excepcion.getMessage());
        verify(repositorioUsuarioMock).guardar(usuarioMock);
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
        when(repositorioUsuarioMock.buscarUsuarioPorEmail("email@test")).thenThrow(new ExcepcionBaseDeDatos("Base de datos no disponible"));

        // ejecucion
        ExcepcionBaseDeDatos excepcion = assertThrows(ExcepcionBaseDeDatos.class, () -> {
            servicioUsuario.buscarUsuarioPorEmail("email@test");
        });

        // validacion
        assertEquals("Base de datos no disponible", excepcion.getMessage());
        verify(repositorioUsuarioMock).buscarUsuarioPorEmail("email@test");
    }

    @Test
    public void queAlSolicitarAlServicioModificarUsuarioLoHagaCorrectamente() throws ExcepcionBaseDeDatos, UsuarioInexistente, ExcepcionCamposInvalidos {
        // preparacion
        DatosEditarPerfil datosEditarPerfil = mock(DatosEditarPerfil.class);
        when(datosEditarPerfil.getNombre()).thenReturn("nombre");
        when(datosEditarPerfil.getApellido()).thenReturn("apellido");
        when(datosEditarPerfil.getNombreUsuario()).thenReturn("nombreUsuario");
        when(datosEditarPerfil.getEmail()).thenReturn("email@test");
        when(datosEditarPerfil.getPais()).thenReturn("pais");
        when(datosEditarPerfil.getTelefono()).thenReturn(1234567890L);
        when(datosEditarPerfil.getFechaNacimiento()).thenReturn(null);
        when(datosEditarPerfil.getId()).thenReturn(1L);

        Usuario usuarioMock = mock(Usuario.class);
        when(repositorioUsuarioMock.obtenerUsuarioPorId(1L)).thenReturn(usuarioMock);
        when(usuarioMock.getNombre()).thenAnswer(invocation -> datosEditarPerfil.getNombre());
        when(usuarioMock.getApellido()).thenAnswer(invocation -> datosEditarPerfil.getApellido());
        when(usuarioMock.getNombreUsuario()).thenAnswer(invocation -> datosEditarPerfil.getNombreUsuario());
        when(usuarioMock.getEmail()).thenAnswer(invocation -> datosEditarPerfil.getEmail());
        when(usuarioMock.getPais()).thenAnswer(invocation -> datosEditarPerfil.getPais());
        when(usuarioMock.getTelefono()).thenAnswer(invocation -> datosEditarPerfil.getTelefono());
        when(usuarioMock.getFechaNacimiento()).thenAnswer(invocation -> datosEditarPerfil.getFechaNacimiento());

        // ejecucion
        servicioUsuario.modificar(datosEditarPerfil);

        // validacion
        verify(repositorioUsuarioMock).modificar(usuarioMock);
        assertEquals(datosEditarPerfil.getNombre(), usuarioMock.getNombre());
        assertEquals(datosEditarPerfil.getApellido(), usuarioMock.getApellido());
        assertEquals(datosEditarPerfil.getNombreUsuario(), usuarioMock.getNombreUsuario());
        assertEquals(datosEditarPerfil.getEmail(), usuarioMock.getEmail());
        assertEquals(datosEditarPerfil.getPais(), usuarioMock.getPais());
        assertEquals(datosEditarPerfil.getTelefono(), usuarioMock.getTelefono());
        assertEquals(datosEditarPerfil.getFechaNacimiento(), usuarioMock.getFechaNacimiento());
    }

    @Test
    public void queAlSolicitarAlServicioModificarUsuarioLanceExcepcionUsuarioInexistente() throws ExcepcionBaseDeDatos, UsuarioInexistente, ExcepcionCamposInvalidos {
        // preparacion
        DatosEditarPerfil datosEditarPerfil = mock(DatosEditarPerfil.class);
        when(datosEditarPerfil.getId()).thenReturn(1L);
        when(repositorioUsuarioMock.obtenerUsuarioPorId(1L)).thenReturn(null);

        // ejecucion
        UsuarioInexistente e = assertThrows(UsuarioInexistente.class, () -> {
            servicioUsuario.modificar(datosEditarPerfil);
        });

        // validacion
        assertEquals("No se encontro usuario", e.getMessage());
        verify(repositorioUsuarioMock).obtenerUsuarioPorId(1L);
    }

    @Test
    public void queAlSolicitarAlServicioModificarUsuarioLanceExcepcionBaseDeDatos() throws ExcepcionBaseDeDatos, UsuarioInexistente, ExcepcionCamposInvalidos {
        // preparacion
        DatosEditarPerfil datosEditarPerfil = mock(DatosEditarPerfil.class);
        when(datosEditarPerfil.getId()).thenReturn(1L);
        when(repositorioUsuarioMock.obtenerUsuarioPorId(1L)).thenThrow(new ExcepcionBaseDeDatos("Base de datos no disponible"));

        // ejecucion
        ExcepcionBaseDeDatos excepcion = assertThrows(ExcepcionBaseDeDatos.class, () -> {
            servicioUsuario.modificar(datosEditarPerfil);
        });

        // validacion
        assertEquals("Base de datos no disponible", excepcion.getMessage());
        verify(repositorioUsuarioMock).obtenerUsuarioPorId(1L);
    }

    @Test
    public void queAlSolicitarAlServicioModificarUsuarioLanceExcepcionCamposInvalidos() throws ExcepcionBaseDeDatos, UsuarioInexistente, ExcepcionCamposInvalidos {
        // preparacion
        DatosEditarPerfil datosEditarPerfil = mock(DatosEditarPerfil.class);
        Map<String, String> erroresEsperados = new HashMap<>();
        erroresEsperados.put("nombre", "El campo es requerido");
        erroresEsperados.put("apellido", "El campo es requerido");
        erroresEsperados.put("email", "El campo es requerido");
        erroresEsperados.put("nombreUsuario", "El campo es requerido");
        erroresEsperados.put("pais", "El campo es requerido");
        erroresEsperados.put("telefono", "El campo es requerido");
        erroresEsperados.put("fechaNacimiento", "El campo es requerido");

        doThrow(new ExcepcionCamposInvalidos(erroresEsperados)).when(datosEditarPerfil).validarCampos();

        // ejecucion
        ExcepcionCamposInvalidos excepcion = assertThrows(ExcepcionCamposInvalidos.class, () -> {
            servicioUsuario.modificar(datosEditarPerfil);
        });

        // validacion
        assertThat(excepcion.getErrores(), IsMapWithSize.aMapWithSize(7));
        assertThat(excepcion.getErrores(), hasEntry("nombre", "El campo es requerido"));
        assertThat(excepcion.getErrores(), hasEntry("apellido", "El campo es requerido"));
        assertThat(excepcion.getErrores(), hasEntry("email", "El campo es requerido"));
        assertThat(excepcion.getErrores(), hasEntry("nombreUsuario", "El campo es requerido"));
        assertThat(excepcion.getErrores(), hasEntry("pais", "El campo es requerido"));
        assertThat(excepcion.getErrores(), hasEntry("telefono", "El campo es requerido"));
        assertThat(excepcion.getErrores(), hasEntry("fechaNacimiento", "El campo es requerido"));
    }

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
        when(repositorioUsuarioMock.obtenerUsuarioPorId(1L)).thenThrow(new ExcepcionBaseDeDatos("Base de datos no disponible"));

        // ejecucion
        ExcepcionBaseDeDatos excepcion = assertThrows(ExcepcionBaseDeDatos.class, () -> {
            servicioUsuario.obtenerUsuarioPorId(1L);
        });

        // validacion
        assertEquals("Base de datos no disponible", excepcion.getMessage());
        verify(repositorioUsuarioMock).obtenerUsuarioPorId(1L);
    }

//    @Test
//    public void queAlSolicitarAlServicioActualizarPlanPremiumLoHagaCorrectamente() throws ExcepcionBaseDeDatos, UsuarioInexistente {
//        // preparacion
//        when(repositorioUsuarioMock.obtenerUsuarioPorId(1L)).thenReturn(usuarioMock);
//        Usuario usuario = new Usuario();
//        usuario.setPlan("free");
//        when(usuarioMock.getPlan()).thenReturn(usuario.getPlan());
//
//        // ejecucion
//        servicioUsuario.actualizarPlan(usuario);
//
//        // validacion
//        verify(repositorioUsuarioMock).modificar(usuarioMock);
//        assertEquals("premium", usuarioMock.getPlan());
//    }

}
