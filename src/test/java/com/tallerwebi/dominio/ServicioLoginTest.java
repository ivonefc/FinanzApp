package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionCamposInvalidos;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;
import com.tallerwebi.presentacion.DatosRegistroUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.MatcherAssert.assertThat;
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
}
