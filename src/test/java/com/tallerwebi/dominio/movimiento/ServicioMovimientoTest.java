package com.tallerwebi.dominio.movimiento;

import com.tallerwebi.dominio.usuario.RepositorioUsuario;
import com.tallerwebi.dominio.categoria.CategoriaMovimiento;
import com.tallerwebi.dominio.categoria.RepositorioCategoria;
import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionCamposInvalidos;
import com.tallerwebi.dominio.excepcion.ExcepcionMovimientoNoEncontrado;
import com.tallerwebi.dominio.usuario.Usuario;
import com.tallerwebi.presentacion.movimiento.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ServicioMovimientoTest {
    ServicioMovimiento servicioMovimiento;
    RepositorioMovimiento repositorioMovimientoMock;
    RepositorioCategoria repositorioCategoriaMock;
    RepositorioUsuario repositorioUsuarioMock;
    HttpServletRequest httpServletRequestMock;

     @BeforeEach
    public void init(){
         repositorioMovimientoMock = mock(RepositorioMovimiento.class);
         repositorioCategoriaMock = mock(RepositorioCategoria.class);
         repositorioUsuarioMock = mock(RepositorioUsuario.class);
         servicioMovimiento = new ServicioMovimientoImpl(repositorioMovimientoMock, repositorioCategoriaMock, repositorioUsuarioMock);
         httpServletRequestMock = mock(HttpServletRequest.class);
    }

    @Test
    public void queAlSolicitarAlServicioObtenerMovimientosDevuelvaUnaListaDeMovimientos() throws ExcepcionBaseDeDatos { //ID DE USUARIO
         //preparacion
         Movimiento movimientoMock1 = mock(Movimiento.class);
         Movimiento movimientoMock2 = mock(Movimiento.class);
         when(repositorioMovimientoMock.obtenerMovimientos(anyLong())).thenReturn(Arrays.asList(movimientoMock1, movimientoMock2));

         //ejecucion
         List<Movimiento> movimientos = servicioMovimiento.obtenerMovimientos(1L);

         //validacion
         assertThat(movimientos, notNullValue());
         assertThat(movimientos, not(empty()));
         assertThat(movimientos, containsInAnyOrder(movimientoMock1, movimientoMock2));
         assertThat(movimientos, hasSize(2));
    }

    @Test
    public void queAlSolicitarAlServicioUnaListaDeMovimientosDevuelvaUnaListaVacia() throws ExcepcionBaseDeDatos {
         //preparacion
         when(repositorioMovimientoMock.obtenerMovimientos(anyLong())).thenReturn(Collections.emptyList());

         //ejecucion
         List<Movimiento> movimientos = servicioMovimiento.obtenerMovimientos(1L);

         //validacion
         assertThat(movimientos, notNullValue());
         assertThat(movimientos, empty());
         assertThat(movimientos, hasSize(0));
    }

    @Test
    public void queAlSolicitarAlServicioObtenerMovimientosLanceExcepcionBaseDeDatos() throws ExcepcionBaseDeDatos {
         //preparacion
         when(repositorioMovimientoMock.obtenerMovimientos(anyLong())).thenThrow(ExcepcionBaseDeDatos.class);

         //ejecucion y validacion
        assertThrows(ExcepcionBaseDeDatos.class, () -> servicioMovimiento.obtenerMovimientos(1L));
    }

    @Test
    public void queAlSolicitarAlServicioObtenerMovimientoPorIdDevuelvaUnMovimiento() throws ExcepcionBaseDeDatos, ExcepcionMovimientoNoEncontrado { //ID DE MOVIMIENTO
         //preparacion
         Movimiento movimientoMock = mock(Movimiento.class);
         when(repositorioMovimientoMock.obtenerMovimientoPorId(anyLong())).thenReturn(movimientoMock);

         //ejecucion
         Movimiento movimiento = servicioMovimiento.obtenerMovimientoPorId(1L);

         //validacion
         assertThat(movimiento, notNullValue());
         assertThat(movimiento, is(movimientoMock));
    }

    @Test
    public void queAlSolicitarAlServicioObtenerMovimientoPorIdLanceExcepcionBaseDeDatos() throws ExcepcionBaseDeDatos, ExcepcionMovimientoNoEncontrado {
        //preparacion
        when(repositorioMovimientoMock.obtenerMovimientoPorId(anyLong())).thenThrow(ExcepcionBaseDeDatos.class);

        //ejecucion y validacion
        assertThrows(ExcepcionBaseDeDatos.class, () -> servicioMovimiento.obtenerMovimientoPorId(1L));
    }

    @Test
    public void queAlSolicitarAlServicioObtenerMovimientoPorIdLanceExcepcionMovimientoNoEncontrado() throws ExcepcionBaseDeDatos, ExcepcionMovimientoNoEncontrado {
        //preparacion
        when(repositorioMovimientoMock.obtenerMovimientoPorId(anyLong())).thenThrow(ExcepcionMovimientoNoEncontrado.class);

        //ejecucion y validacion
        assertThrows(ExcepcionMovimientoNoEncontrado.class, () -> servicioMovimiento.obtenerMovimientoPorId(1L));
    }

    @Test
    public void queAlSolicitarAlServicioActualizarMovimientoActualiceElMovimiento() throws ExcepcionBaseDeDatos, ExcepcionMovimientoNoEncontrado, ExcepcionCamposInvalidos {
        //preparacion
        DatosEditarMovimiento datosEditarMovimiento = mock(DatosEditarMovimiento.class);
        when(datosEditarMovimiento.getId()).thenReturn(1L);
        when(datosEditarMovimiento.getCategoria()).thenReturn("SUPERMERCADO");
        when(datosEditarMovimiento.getDescripcion()).thenReturn("Compras en el supermercado");
        when(datosEditarMovimiento.getMonto()).thenReturn(10000.0);
        CategoriaMovimiento categoriaMovimientoMock = mock(CategoriaMovimiento.class);
        Movimiento movimientoMock = mock(Movimiento.class);
        when(repositorioCategoriaMock.obtenerCategoriaPorNombre(anyString())).thenReturn(categoriaMovimientoMock);
        when(repositorioMovimientoMock.obtenerMovimientoPorId(anyLong())).thenReturn(movimientoMock);

        //ejecucion
        servicioMovimiento.actualizarMovimiento(datosEditarMovimiento);

        //validacion
        verify(movimientoMock).setCategoria(categoriaMovimientoMock);
        verify(movimientoMock).setDescripcion("Compras en el supermercado");
        verify(movimientoMock).setMonto(10000.0);
    }

    @Test
    public void queAlSolicitarAlServicioActualizarMovimientoLanceExcepcionBaseDeDatos() throws ExcepcionBaseDeDatos{
        //preparacion
        DatosEditarMovimiento datosEditarMovimiento = mock(DatosEditarMovimiento.class);
        when(datosEditarMovimiento.getId()).thenReturn(1L);
        when(datosEditarMovimiento.getCategoria()).thenReturn("SUPERMERCADO");
        when(datosEditarMovimiento.getDescripcion()).thenReturn("Compras en el supermercado");
        when(datosEditarMovimiento.getMonto()).thenReturn(10000.0);
        when(repositorioCategoriaMock.obtenerCategoriaPorNombre(anyString())).thenThrow(ExcepcionBaseDeDatos.class);

        //ejecucion y validacion
        assertThrows(ExcepcionBaseDeDatos.class, () -> servicioMovimiento.actualizarMovimiento(datosEditarMovimiento));
    }

    @Test
    public void queAlSolicitarAlServicioActualizarMovimientoLanceExcepcionMovimientoNoEncontrado() throws ExcepcionBaseDeDatos, ExcepcionMovimientoNoEncontrado, ExcepcionCamposInvalidos {
        //preparacion
        DatosEditarMovimiento datosEditarMovimiento = mock(DatosEditarMovimiento.class);
        when(datosEditarMovimiento.getId()).thenReturn(1L);
        when(datosEditarMovimiento.getCategoria()).thenReturn("SUPERMERCADO");
        when(datosEditarMovimiento.getDescripcion()).thenReturn("Compras en el supermercado");
        when(datosEditarMovimiento.getMonto()).thenReturn(10000.0);
        when(repositorioMovimientoMock.obtenerMovimientoPorId(anyLong())).thenReturn(null);
        doNothing().when(datosEditarMovimiento).validarCampos();
        CategoriaMovimiento categoriaMovimiento = mock(CategoriaMovimiento.class);
        when(repositorioCategoriaMock.obtenerCategoriaPorNombre(anyString())).thenReturn(categoriaMovimiento);

        //ejecucion y validacion
        assertThrows(ExcepcionMovimientoNoEncontrado.class, () -> servicioMovimiento.actualizarMovimiento(datosEditarMovimiento));
    }

    @Test
    public void queAlSolicitarAlServicioActualizarMovimientoLanceExcepcionCamposInvalidos()  {
         //preparacion
         DatosEditarMovimiento datosEditarMovimiento = mock(DatosEditarMovimiento.class);
         when(datosEditarMovimiento.getId()).thenReturn(1L);
         when(datosEditarMovimiento.getCategoria()).thenReturn(null);

         //ejecucion y validacion
         assertThrows(ExcepcionCamposInvalidos.class, () -> servicioMovimiento.actualizarMovimiento(datosEditarMovimiento));
    }

    @Test
    public void queAlSolicitarAlServicioEliminarUnMovimientoElimineElMovimiento() throws ExcepcionBaseDeDatos, ExcepcionMovimientoNoEncontrado {
        //preparacion
        Movimiento movimientoMock = mock(Movimiento.class);
        when(repositorioMovimientoMock.obtenerMovimientoPorId(anyLong())).thenReturn(movimientoMock);

        //ejecucion
        servicioMovimiento.eliminarMovimiento(1L);

        //validacion
        verify(repositorioMovimientoMock).eliminarMovimiento(movimientoMock);
    }

    @Test
    public void queAlSolicitarAlServicioEliminarUnMovimientoLanceExcepcionBaseDeDatos() throws ExcepcionBaseDeDatos, ExcepcionMovimientoNoEncontrado {
        //preparacion
        when(repositorioMovimientoMock.obtenerMovimientoPorId(anyLong())).thenThrow(ExcepcionBaseDeDatos.class);

        //ejecucion y validacion
        assertThrows(ExcepcionBaseDeDatos.class, () -> servicioMovimiento.eliminarMovimiento(1L));
    }

    @Test
    public void queAlSolicitarAlServicioEliminarUnMovimientoLanceExcepcionMovimientoNoEncontrado() throws ExcepcionBaseDeDatos, ExcepcionMovimientoNoEncontrado {
        //preparacion
        when(repositorioMovimientoMock.obtenerMovimientoPorId(anyLong())).thenReturn(null);

        //ejecucion y validacion
        assertThrows(ExcepcionMovimientoNoEncontrado.class, () -> servicioMovimiento.eliminarMovimiento(1L));
    }

    @Test
    public void queAlSolicitarAlServicioObtenerMovimientosPorFechaDevuelvaUnaListaDeMovimientos() throws ExcepcionBaseDeDatos {
         //preparacion
         Movimiento movimientoMock1 = mock(Movimiento.class);
         Movimiento movimientoMock2 = mock(Movimiento.class);
         when(repositorioMovimientoMock.obtenerMovimientosPorFecha(anyLong(), any())).thenReturn(Arrays.asList(movimientoMock1, movimientoMock2));

         //ejecucion
         List<Movimiento> movimientos = servicioMovimiento.obtenerMovimientosPorFecha(1L, LocalDate.now());

         //validacion
         assertThat(movimientos, notNullValue());
         assertThat(movimientos, not(empty()));
         assertThat(movimientos, containsInAnyOrder(movimientoMock1, movimientoMock2));
         assertThat(movimientos, hasSize(2));
    }

    @Test
    public void queAlSolicitarAlServicioObtenerMovimientosPorFechaDevuelvaUnaListaVacia() throws ExcepcionBaseDeDatos {
         //preparacion
         when(repositorioMovimientoMock.obtenerMovimientosPorFecha(anyLong(), any())).thenReturn(Collections.emptyList());

         //ejecucion
         List<Movimiento> movimientos = servicioMovimiento.obtenerMovimientosPorFecha(1L, null);

         //validacion
         assertThat(movimientos, notNullValue());
         assertThat(movimientos, empty());
         assertThat(movimientos, hasSize(0));
    }

    @Test
    public void queAlSolicitarAlServicioObtenerMovimientosPorFechaLanceExcepcionBaseDeDatos() throws ExcepcionBaseDeDatos {
        //preparacion
        when(repositorioMovimientoMock.obtenerMovimientosPorFecha(anyLong(), any())).thenThrow(ExcepcionBaseDeDatos.class);

        //ejecucion y validacion
        assertThrows(ExcepcionBaseDeDatos.class, () -> servicioMovimiento.obtenerMovimientosPorFecha(1L, null));
    }

    @Test
    public void queAlSolicitarAlServicioNuevoMovimientoSeGuardeElMovimiento() throws ExcepcionBaseDeDatos, ExcepcionCamposInvalidos {
        //preparacion
        DatosAgregarMovimiento datosAgregarMovimiento = mock(DatosAgregarMovimiento.class);
        when(datosAgregarMovimiento.getDescripcion()).thenReturn("descripcion");
        when(datosAgregarMovimiento.getMonto()).thenReturn(1.0);
        when(datosAgregarMovimiento.getCategoria()).thenReturn("categoria");
        Usuario usuarioMock = mock(Usuario.class);
        when(repositorioUsuarioMock.obtenerUsuarioPorId(anyLong())).thenReturn(usuarioMock);
        CategoriaMovimiento categoriaMovimientoMock = mock(CategoriaMovimiento.class);
        when(repositorioCategoriaMock.obtenerCategoriaPorNombre(anyString())).thenReturn(categoriaMovimientoMock);

        //ejecucion
        servicioMovimiento.nuevoMovimiento(1L, datosAgregarMovimiento);

        //validacion
        verify(repositorioMovimientoMock).guardarMovimiento(any());
    }

    @Test
    public void queAlSolicitarAlServicioNuevoMovimientoLanceExcepcionBaseDeDatos() throws ExcepcionBaseDeDatos {
        //preparacion
        DatosAgregarMovimiento datosAgregarMovimiento = mock(DatosAgregarMovimiento.class);
        when(datosAgregarMovimiento.getDescripcion()).thenReturn("descripcion");
        when(datosAgregarMovimiento.getMonto()).thenReturn(1.0);
        when(datosAgregarMovimiento.getCategoria()).thenReturn("categoria");
        when(repositorioUsuarioMock.obtenerUsuarioPorId(anyLong())).thenThrow(ExcepcionBaseDeDatos.class);

        //ejecucion y validacion
        assertThrows(ExcepcionBaseDeDatos.class, () -> servicioMovimiento.nuevoMovimiento(1L, datosAgregarMovimiento));
    }

    @Test
    public void queAlSolicitarAlServicioNuevoMovimientoLanceExcepcionCamposInvalidos() throws ExcepcionCamposInvalidos {
        //preparacion
        DatosAgregarMovimiento datosAgregarMovimiento = mock(DatosAgregarMovimiento.class);
        when(datosAgregarMovimiento.getDescripcion()).thenReturn("");
        doThrow(new ExcepcionCamposInvalidos(new HashMap<>())).when(datosAgregarMovimiento).validarCampos();

        //ejecucion y validacion
        assertThrows(ExcepcionCamposInvalidos.class, () -> servicioMovimiento.nuevoMovimiento(1L, datosAgregarMovimiento));
    }

}
