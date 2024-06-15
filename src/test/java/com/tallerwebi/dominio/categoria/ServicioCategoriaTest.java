package com.tallerwebi.dominio.categoria;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionCamposInvalidos;
import com.tallerwebi.dominio.excepcion.ExcepcionMovimientoNoEncontrado;
import com.tallerwebi.dominio.usuario.RepositorioUsuario;
import com.tallerwebi.dominio.usuario.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class ServicioCategoriaTest {

    ServicioCategoria servicioCategoriaMock;
    RepositorioCategoria repositorioCategoriaMock;
    RepositorioUsuario repositorioUsuarioMock;
    HttpServletRequest httpServletRequestMock;
    HttpSession httpSessionMock;
    Usuario usuarioMock;
    CategoriaMovimiento categoriaMock;

    @BeforeEach
    public void init(){
        repositorioCategoriaMock = mock(RepositorioCategoria.class);
        repositorioUsuarioMock = mock(RepositorioUsuario.class);
        httpServletRequestMock = mock(HttpServletRequest.class);
        httpSessionMock = mock(HttpSession.class);
        usuarioMock = mock(Usuario.class);
        categoriaMock = mock(CategoriaMovimiento.class);
        servicioCategoriaMock = mock(ServicioCategoria.class);
    }


    @Test
    public void queAlSolicitarAlServicioObtenerCategoriaPorNombreDevuelvaUnaCategoria() throws ExcepcionBaseDeDatos, ExcepcionMovimientoNoEncontrado { //ID DE MOVIMIENTO
        //preparacion
        CategoriaMovimiento categoriaMock = mock(CategoriaMovimiento.class);
        when(servicioCategoriaMock.obtenerCategoriaPorNombre(anyString())).thenReturn(categoriaMock);

        //ejecucion
        CategoriaMovimiento categoria = servicioCategoriaMock.obtenerCategoriaPorNombre("SUPERMERCADO");

        //validacion
        assertThat(categoria, notNullValue());
        assertThat(categoria, is(categoriaMock));
    }

    @Test
    public void queAlSolicitarAlServicioObtenerCategoriaPorNombreLanceExcepcionBaseDeDatos() throws ExcepcionBaseDeDatos {
        //preparacion
        when(servicioCategoriaMock.obtenerCategoriaPorNombre(anyString())).thenThrow(ExcepcionBaseDeDatos.class);

        //ejecucion y validacion
        assertThrows(ExcepcionBaseDeDatos.class, () -> servicioCategoriaMock.obtenerCategoriaPorNombre(anyString()));
    }

    @Test
    public void queAlSolicitarAlServicioObtenerCategoriasDevuelvaUnaListaDeCategorias() throws ExcepcionBaseDeDatos {
        //preparacion
        CategoriaMovimiento categoriaMock1 = mock(CategoriaMovimiento.class);
        CategoriaMovimiento categoriaMock2 = mock(CategoriaMovimiento.class);
        when(servicioCategoriaMock.obtenerCategorias()).thenReturn(Arrays.asList(categoriaMock1, categoriaMock2));

        //ejecucion
        List<CategoriaMovimiento> categorias = servicioCategoriaMock.obtenerCategorias();

        //validacion
        assertThat(categorias, notNullValue());
        assertThat(categorias, not(empty()));
        assertThat(categorias, containsInAnyOrder(categoriaMock1, categoriaMock2));
        assertThat(categorias, hasSize(2));
    }

    @Test
    public void queAlSolicitarAlServicioObtenerCategoriasLanceExcepcionBaseDeDatos() throws ExcepcionBaseDeDatos {
        //preparacion
        when(servicioCategoriaMock.obtenerCategorias()).thenThrow(ExcepcionBaseDeDatos.class);

        //ejecucion y validacion
        assertThrows(ExcepcionBaseDeDatos.class, () -> servicioCategoriaMock.obtenerCategorias());
    }

    @Test
    public void queAlSolicitarAlServicioActualizarColorActualiceElColor() throws Exception {
        //preparacion
        CategoriaMovimiento categoriaMock = mock(CategoriaMovimiento.class);
        when(repositorioCategoriaMock.obtenerCategoriaPorNombre(anyString())).thenReturn(categoriaMock);
        when(categoriaMock.getNombre()).thenReturn("SUPERMERCADO");
        String colorElegido = "#E39E1E";
        String nombreCategoria = categoriaMock.getNombre();
        ServicioCategoria servicioCategoria = new ServicioCategoriaImpl(repositorioCategoriaMock);

        //ejecucion
        servicioCategoria.actualizarColor(nombreCategoria, colorElegido);

        //validacion
        verify(categoriaMock).setColor(colorElegido);
    }

    @Test
    public void queAlSolicitarAlServicioActualizarColorLanceUnaExcepcion() throws Exception{
        //preparacion
        doThrow(new Exception()).when(servicioCategoriaMock).actualizarColor(anyString(), anyString());

        //ejecucion y validacion
        assertThrows(Exception.class, () -> servicioCategoriaMock.actualizarColor(anyString(), anyString()));
    }


}
