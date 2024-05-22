package com.tallerwebi.dominio.autenticacion;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionCamposInvalidos;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;
import com.tallerwebi.dominio.usuario.RepositorioUsuario;
import com.tallerwebi.dominio.usuario.Usuario;
import com.tallerwebi.presentacion.autenticacion.DatosRegistroUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ServicioLoginTest {
    ServicioLogin servicioLogin;
    RepositorioUsuario repositorioUsuarioMock;
    Usuario usuarioMock;

    @BeforeEach
    public void init() {
        repositorioUsuarioMock = mock(RepositorioUsuario.class);
        servicioLogin = new ServicioLoginImpl(repositorioUsuarioMock);
        usuarioMock = mock(Usuario.class);
    }

    //Crear usuario de forma exitosa
    @Test
    public void registrarmeQueAlQuererRegistarUnUsuarioPermitaRegistrar() throws ExcepcionCamposInvalidos, ExcepcionBaseDeDatos, UsuarioExistente, UsuarioInexistente {
        //preparacion
        DatosRegistroUsuario datosRegistroUsuario = new DatosRegistroUsuario("nombre", "email@test", "password");
        UsuarioInexistente usuarioInexistente = new UsuarioInexistente();
        doThrow(usuarioInexistente).when(repositorioUsuarioMock).buscarUsuarioPorEmail(anyString());
        //ejecucion
        servicioLogin.registrar(datosRegistroUsuario);

        // validación
        verify(repositorioUsuarioMock, times(1)).guardar(any(Usuario.class));
    }

    //Error al crear un usuario con datos existentes en bdd
    @Test
    public void registrarmeQueAlQuererRegistrarUnUsuarioConDatosExistentesRedirigirAlFormularioYMostrarError() throws ExcepcionBaseDeDatos, UsuarioInexistente, ExcepcionCamposInvalidos, UsuarioExistente {
        //preparacion
        when(repositorioUsuarioMock.buscarUsuarioPorEmail(anyString())).thenReturn(usuarioMock);
        DatosRegistroUsuario datosRegistroUsuario = new DatosRegistroUsuario("nombre", "email@test", "password");

        //ejecucion
        UsuarioExistente usuarioExistente = assertThrows(UsuarioExistente.class, ()->{
            servicioLogin.registrar(datosRegistroUsuario);
        });

        //validacion
        assertThat(usuarioExistente.getMessage(), equalToIgnoringCase("El usuario ya existe"));
        verify(repositorioUsuarioMock, times(0)).guardar(any(Usuario.class));
    }

    //Error al crear un usuario por caida de la base de datos
    @Test
    public void registrarmeQueAlQuererRegistrarUnUsuarioConErrorEnLaBaseDeDatosLanceError() throws ExcepcionBaseDeDatos, UsuarioInexistente, ExcepcionCamposInvalidos, UsuarioExistente {
        //preparacion
        when(repositorioUsuarioMock.buscarUsuarioPorEmail(anyString())).thenThrow(new ExcepcionBaseDeDatos("Error en la base de datos"));
        DatosRegistroUsuario datosRegistroUsuario = new DatosRegistroUsuario("nombre", "email@test", "password");

        //ejecucion
        ExcepcionBaseDeDatos excepcionBaseDeDatos = assertThrows(ExcepcionBaseDeDatos.class, ()->{
            servicioLogin.registrar(datosRegistroUsuario);
        });

        //validacion
        assertThat(excepcionBaseDeDatos.getMessage(), equalToIgnoringCase("Error en la base de datos"));
        verify(repositorioUsuarioMock, times(0)).guardar(any(Usuario.class));
    }

    //Error al crear un usuario por campos invalidos
    @Test
    public void registrarmeQueAlQuererRegistrarUnUsuarioConCamposInvalidosLanceError() throws ExcepcionBaseDeDatos, UsuarioInexistente, ExcepcionCamposInvalidos, UsuarioExistente {
        //preparacion
        DatosRegistroUsuario datosRegistroUsuario = new DatosRegistroUsuario("", "email@test", "password");

        //ejecucion
        ExcepcionCamposInvalidos excepcionCamposInvalidos = assertThrows(ExcepcionCamposInvalidos.class, ()->{
            servicioLogin.registrar(datosRegistroUsuario);
        });

        //validacion
        assertNotNull(excepcionCamposInvalidos);
        verify(repositorioUsuarioMock, times(0)).guardar(any(Usuario.class));
    }

    //Consultar usuario de forma exitosa
    @Test
    public void consultarUsuarioQueAlConsultarUnUsuarioExistenteRetorneElUsuario() throws UsuarioInexistente, ExcepcionBaseDeDatos {
        //preparacion
        when(repositorioUsuarioMock.buscarUsuarioPorEmailYPassword(anyString(), anyString())).thenReturn(usuarioMock);
        when(usuarioMock.getEmail()).thenReturn("email@test");
        when(usuarioMock.getPassword()).thenReturn("password");

        //ejecucion
        Usuario usuario = servicioLogin.consultarUsuario("email@test", "password");

        //validacion
        assertThat(usuario.getEmail(), equalToIgnoringCase("email@test"));
        assertThat(usuario.getPassword(), equalToIgnoringCase("password"));
        verify(repositorioUsuarioMock, times(1)).buscarUsuarioPorEmailYPassword("email@test", "password");
    }

    //Error al consultar un usuario inexistente
    @Test
    public void consultarUsuarioQueAlConsultarUnUsuarioInexistenteLanceError() throws UsuarioInexistente, ExcepcionBaseDeDatos {
        //preparacion
        when(repositorioUsuarioMock.buscarUsuarioPorEmailYPassword(anyString(), anyString())).thenThrow(new UsuarioInexistente());

        //ejecucion
        UsuarioInexistente usuarioInexistente = assertThrows(UsuarioInexistente.class, ()->{
            servicioLogin.consultarUsuario("email@test", "password");
        });

        //validacion
        assertThat(usuarioInexistente.getMessage(), equalToIgnoringCase("No se encontro usuario"));
    }

    //Error al consultar un usuario por caida de la base de datos
    @Test
    public void consultarUsuarioQueAlConsultarUnUsuarioConErrorEnLaBaseDeDatosLanceError() throws UsuarioInexistente, ExcepcionBaseDeDatos {
        //preparacion
        when(repositorioUsuarioMock.buscarUsuarioPorEmailYPassword(anyString(), anyString())).thenThrow(new ExcepcionBaseDeDatos("Error en la base de datos"));

        //ejecucion
        ExcepcionBaseDeDatos excepcionBaseDeDatos = assertThrows(ExcepcionBaseDeDatos.class, ()->{
            servicioLogin.consultarUsuario("email@test", "password");
        });

        //validacion
        assertThat(excepcionBaseDeDatos.getMessage(), equalToIgnoringCase("Error en la base de datos"));
    }

}
