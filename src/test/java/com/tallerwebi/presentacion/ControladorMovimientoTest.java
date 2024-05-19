package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExceptionSinDatos;
import com.tallerwebi.dominio.movimiento.*;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsIterableWithSize;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;

public class ControladorMovimientoTest {
    ControladorMovimiento controladorMovimiento;
    ServicioMovimiento servicioMovimientoMock;
    ServicioCategoria servicioCategoriaMock;
    HttpServletRequest httpServletRequestMock;
    HttpSession httpSessionMock;

    @BeforeEach
    public void init(){
        servicioMovimientoMock = mock(ServicioMovimiento.class);
        servicioCategoriaMock = mock(ServicioCategoria.class);
        controladorMovimiento = new ControladorMovimiento(servicioMovimientoMock, servicioCategoriaMock);
        httpServletRequestMock = mock(HttpServletRequest.class);
        httpSessionMock = mock(HttpSession.class);
    }

    @Test
    public void queAlClickearLaOpcionVerMovimientosEnElMenuDirijaALaVistaMovimientosYMuestreLosMovimientos() throws ExcepcionBaseDeDatos {
        //preparacion
        Movimiento movimientoMock1 = mock(Movimiento.class);
        Movimiento movimientoMock2 = mock(Movimiento.class);
        when(servicioMovimientoMock.obtenerMovimientos(anyLong())).thenReturn(Arrays.asList(movimientoMock1, movimientoMock2));
        when(httpServletRequestMock.getSession(false)).thenReturn(httpSessionMock);
        when(httpSessionMock.getAttribute("idUsuario")).thenReturn(anyLong());

        //ejecucion
        ModelAndView modelAndView = controladorMovimiento.obtenerMovimientos(httpServletRequestMock);
        List<Movimiento> movimientosActual =  (List<Movimiento>) modelAndView.getModel().get("movimientos");

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("movimientos"));
        assertThat(movimientosActual, IsIterableWithSize.iterableWithSize(2));
        assertThat(movimientosActual, Matchers.containsInAnyOrder(movimientoMock1, movimientoMock2));
        verify(httpSessionMock, times(1)).getAttribute("idUsuario");
    }

    @Test
    public void queAlQuererIraBarraMovimientosYNoExistaUsuarioLogueadoMeRedirijaAlLoguin() throws ExcepcionBaseDeDatos {
        //preparacion
        when(httpServletRequestMock.getSession(false)).thenReturn(null);

        //ejecucion
        ModelAndView modelAndView = controladorMovimiento.obtenerMovimientos(httpServletRequestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
        verify(httpSessionMock, times(0)).getAttribute("idUsuario");
    }

    @Test
    public void queAlQuererObtenerMovimientosPorFechaYExistaUsuarioLogueadoRetorneLosMovimientos() {
        //preparacion
        Movimiento movimientoMock1 = mock(Movimiento.class);
        Movimiento movimientoMock2 = mock(Movimiento.class);
        when(servicioMovimientoMock.obtenerMovimientosPorFecha(anyLong(), any())).thenReturn(Arrays.asList(movimientoMock1, movimientoMock2));
        when(httpServletRequestMock.getSession(false)).thenReturn(httpSessionMock);
        when(httpSessionMock.getAttribute("idUsuario")).thenReturn(anyLong());

        //ejecucion
        List<Movimiento> movimientos = controladorMovimiento.obtenerMovimientosPorFecha("2021-06-01", httpServletRequestMock);

        //validacion
        assertThat(movimientos, IsIterableWithSize.iterableWithSize(2));
        assertThat(movimientos, Matchers.containsInAnyOrder(movimientoMock1, movimientoMock2));
        verify(httpSessionMock, times(1)).getAttribute("idUsuario");
    }

    @Test
    public void queAlQuererObtenerMovimientosPorFechaYNoExistaUsuarioLogueadoRetorneNull() {
        //preparacion
        when(httpServletRequestMock.getSession(false)).thenReturn(null);

        //ejecucion
        List<Movimiento> movimientos = controladorMovimiento.obtenerMovimientosPorFecha("2021-06-01", httpServletRequestMock);

        //validacion
        assertThat(movimientos, Matchers.nullValue());
        verify(httpSessionMock, times(0)).getAttribute("idUsuario");
    }

    @Test
    public void queAlQuererIrAVistaEditarUnMovimientoYExistaUsuarioLogueadoMeDirijaAlFormularioDeEdicion() {
        //preparacion
        Movimiento movimientoMock = mock(Movimiento.class);
        when(servicioMovimientoMock.obtenerMovimientoPorId(anyLong(), anyLong())).thenReturn(java.util.Optional.of(movimientoMock));
        when(httpServletRequestMock.getSession(false)).thenReturn(httpSessionMock);
        when(httpSessionMock.getAttribute("idUsuario")).thenReturn(anyLong());

        //ejecucion
        ModelAndView modelAndView = controladorMovimiento.irAFormularioEditarMovimiento(httpServletRequestMock, 1L);
        Movimiento movimientoActual = (Movimiento) modelAndView.getModel().get("movimiento");

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("editar-movimiento"));
        assertThat(movimientoActual, Matchers.is(movimientoMock));
        verify(httpSessionMock, times(1)).getAttribute("idUsuario");
    }

    @Test
    public void queAlQuererIrAVistaEditarUnMovimientoYNoExistaUsuarioLogueadoMeRedirijaAlLoguin() {
        //preparacion
        when(httpServletRequestMock.getSession(false)).thenReturn(null);

        //ejecucion
        ModelAndView modelAndView = controladorMovimiento.irAFormularioEditarMovimiento(httpServletRequestMock, 1L);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
        verify(httpSessionMock, times(0)).getAttribute("idUsuario");
    }

    @Test
    public void queAlQuererEditarUnMovimientoSePuedaEditarMovimiento() throws ExcepcionBaseDeDatos, ExceptionSinDatos {
        //preparacion
        Movimiento movimientoMock = mock(Movimiento.class);
        CategoriaMovimiento categoriaMock = mock(CategoriaMovimiento.class);
        when(httpServletRequestMock.getSession(false)).thenReturn(httpSessionMock);
        when(httpSessionMock.getAttribute("idUsuario")).thenReturn(1L);
        when(movimientoMock.getCategoria()).thenReturn(categoriaMock);
        when(categoriaMock.getNombre()).thenReturn("Categoria Test");
        when(servicioCategoriaMock.obtenerCategoriaPorNombre(anyString())).thenReturn(categoriaMock);

        //ejecucion
        ModelAndView modelAndView = controladorMovimiento.editarMovimiento(movimientoMock, httpServletRequestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/movimientos"));
        verify(servicioMovimientoMock, times(1)).editarMovimiento(anyLong(), any(Movimiento.class));
    }

    @Test
    public void queAlQuererEditarUnMovimientoYNoExistaUsuarioLogueadoNoSePuedaEditarMovimiento() throws ExceptionSinDatos {
        //preparacion
        when(httpServletRequestMock.getSession(false)).thenReturn(null);

        //ejecucion
        ModelAndView modelAndView = controladorMovimiento.editarMovimiento(null, httpServletRequestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
        verify(httpSessionMock, times(0)).getAttribute("idUsuario");
    }

    @Test
    public void queAlQuererEditarUnMovimientoYNoSeIngreseNingunDatoNoSePuedaEditarMovimiento() throws ExcepcionBaseDeDatos, ExceptionSinDatos {
        //preparacion
        when(httpServletRequestMock.getSession(false)).thenReturn(httpSessionMock);
        when(httpSessionMock.getAttribute("idUsuario")).thenReturn(1L);

        //ejecucion
        ExceptionSinDatos exception = assertThrows(ExceptionSinDatos.class, () -> {
            controladorMovimiento.editarMovimiento(null, httpServletRequestMock);
        });

        //validacion
        assertEquals("Error al editar el movimiento", exception.getMessage());
        verify(servicioMovimientoMock, times(0)).editarMovimiento(anyLong(), any(Movimiento.class));
    }

    @Test
    public void queAlQuererEditarUnMovimientoYNoSePuedaEstablecerConexionConLaBaseDeDatosSeMuestreUnMensajeDeError() throws ExcepcionBaseDeDatos, ExceptionSinDatos {
        //preparacion
        Movimiento movimientoMock = mock(Movimiento.class);
        CategoriaMovimiento categoriaMock = mock(CategoriaMovimiento.class);
        when(httpServletRequestMock.getSession(false)).thenReturn(httpSessionMock);
        when(httpSessionMock.getAttribute("idUsuario")).thenReturn(1L);
        when(movimientoMock.getCategoria()).thenReturn(categoriaMock);
        when(categoriaMock.getNombre()).thenReturn("Categoria Test");
        when(servicioCategoriaMock.obtenerCategoriaPorNombre(anyString())).thenReturn(categoriaMock);
        doThrow(ExcepcionBaseDeDatos.class).when(servicioMovimientoMock).editarMovimiento(anyLong(), any(Movimiento.class));

        //ejecucion
        ModelAndView modelAndView = controladorMovimiento.editarMovimiento(movimientoMock, httpServletRequestMock);

        //validacion
        assertEquals("error", modelAndView.getViewName());
        assertEquals("Error en la base de datos", modelAndView.getModel().get("error"));
    }

    @Test
    public void queAlClickearEnLaBarraDeNavegacionEnAgregarMovimientoTeLleveALaPaginaAgregarMovimiento() {
        //preparacion
        when(httpServletRequestMock.getSession(false)).thenReturn(httpSessionMock);
        when(httpSessionMock.getAttribute("idUsuario")).thenReturn(1L);

        //ejecucion
        ModelAndView modelAndView = controladorMovimiento.irAAgregarMovimiento(httpServletRequestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("agregar-movimiento"));
//        verify(httpSessionMock, times(1)).getAttribute("idUsuario");
    }

    @Test
    public void queAlClickearEnLaBarraDeNavegacionEnAgregarMovimientoYNoExistaUsuarioLogueadoMeRedirijaAlLogin()  {
        //preparacion
        when(httpServletRequestMock.getSession(false)).thenReturn(null);

        //ejecucion
        ModelAndView modelAndView = controladorMovimiento.irAAgregarMovimiento(httpServletRequestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
        verify(httpSessionMock, times(0)).getAttribute("idUsuario");
    }

    @Test
    public void queAlQuererAgregarUnMovimientoSePuedaAgregarMovimiento() throws ExcepcionBaseDeDatos, ExceptionSinDatos {
        //preparacion
        Movimiento movimientoMock = mock(Movimiento.class);
        CategoriaMovimiento categoriaMock = mock(CategoriaMovimiento.class);
        when(httpServletRequestMock.getSession(false)).thenReturn(httpSessionMock);
        when(httpSessionMock.getAttribute("idUsuario")).thenReturn(1L);
        when(movimientoMock.getCategoria()).thenReturn(categoriaMock);
        when(categoriaMock.getNombre()).thenReturn("Categoria Test");
        when(servicioCategoriaMock.obtenerCategoriaPorNombre(anyString())).thenReturn(categoriaMock);
        DatosAgregarMovimiento datosAgregarMovimiento = new DatosAgregarMovimiento("descripcion", "tipo", "categoria", 100.0);

        //ejecucion
        ModelAndView modelAndView = controladorMovimiento.ingresarNuevoMovimiento(datosAgregarMovimiento, httpServletRequestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/movimientos"));
        verify(servicioMovimientoMock, times(1)).nuevoMovimiento(anyLong(), any(Movimiento.class), any(CategoriaMovimiento.class));
    }

    @Test
    public void queAlQuererAgregarUnMovimientoYNoExistaUsuarioLogueadoNoSePuedaAgregarMovimiento() throws ExceptionSinDatos {
        //preparacion
        DatosAgregarMovimiento datosAgregarMovimiento = new DatosAgregarMovimiento("descripcion", "tipo", "categoria", 100.0);
        when(httpServletRequestMock.getSession(false)).thenReturn(null);

        //ejecucion
        ModelAndView modelAndView = controladorMovimiento.ingresarNuevoMovimiento(datosAgregarMovimiento, httpServletRequestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
        verify(httpSessionMock, times(0)).getAttribute("idUsuario");
    }

    @Test
    public void queAlQuererAgregarUnMovimientoYNoSePuedaEstablecerConexionConLaBaseDeDatosSeMuestreUnMensajeDeError() throws ExcepcionBaseDeDatos, ExceptionSinDatos {
        //preparacion
        Movimiento movimientoMock = mock(Movimiento.class);
        CategoriaMovimiento categoriaMock = mock(CategoriaMovimiento.class);
        when(httpServletRequestMock.getSession(false)).thenReturn(httpSessionMock);
        when(httpSessionMock.getAttribute("idUsuario")).thenReturn(1L);
        when(movimientoMock.getCategoria()).thenReturn(categoriaMock);
        when(categoriaMock.getNombre()).thenReturn("Categoria Test");
        when(servicioCategoriaMock.obtenerCategoriaPorNombre(anyString())).thenReturn(categoriaMock);
        doThrow(ExcepcionBaseDeDatos.class).when(servicioMovimientoMock).nuevoMovimiento(anyLong(), any(Movimiento.class), any(CategoriaMovimiento.class));
        DatosAgregarMovimiento datosAgregarMovimiento = new DatosAgregarMovimiento("descripcion", "tipo", "categoria", 100.0);

        //ejecucion
        ModelAndView modelAndView = controladorMovimiento.ingresarNuevoMovimiento(datosAgregarMovimiento, httpServletRequestMock);

        //validacion
        assertEquals("error", modelAndView.getViewName());
        assertEquals("Error en la base de datos", modelAndView.getModel().get("error"));
    }

    @Test
    public void queAlQuererAgregarUnMovimientoYNoSeIngreseNingunDatoNoSePuedaAgregarMovimiento() throws ExcepcionBaseDeDatos, ExceptionSinDatos {
        //preparacion
        when(httpServletRequestMock.getSession(false)).thenReturn(httpSessionMock);
        when(httpSessionMock.getAttribute("idUsuario")).thenReturn(1L);

        //ejecucion
        ExceptionSinDatos exception = assertThrows(ExceptionSinDatos.class, () -> {
            controladorMovimiento.ingresarNuevoMovimiento(null, httpServletRequestMock);
        });

        //validacion
        assertEquals("Error al ingresar el movimiento", exception.getMessage());
        verify(servicioMovimientoMock, times(0)).nuevoMovimiento(anyLong(), any(Movimiento.class), any(CategoriaMovimiento.class));
    }

    //TESTE PARA ELIMINAR MOVIMIENTO
    @Test
    public void queAlQuererEliminarUnMovimientoSePuedaEliminarMovimiento() throws ExcepcionBaseDeDatos{
        //preparacion
        Movimiento movimientoMock = mock(Movimiento.class);
        when(httpServletRequestMock.getSession(false)).thenReturn(httpSessionMock);
        when(httpSessionMock.getAttribute("idUsuario")).thenReturn(1L);
        when(servicioMovimientoMock.obtenerMovimientoPorId(anyLong(), anyLong())).thenReturn(Optional.of(movimientoMock));

        //ejecucion
        ModelAndView modelAndView = controladorMovimiento.eliminarMovimiento(movimientoMock.getId(), httpServletRequestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/movimientos"));
        verify(servicioMovimientoMock, times(1)).eliminarMovimiento(anyLong(), any(Movimiento.class));
    }

    @Test
    public void queAlQuererEliminarUnMovimientoYNoExistaUsuarioLogueadoNoSePuedaEliminarMovimiento() throws ExcepcionBaseDeDatos {
        //preparacion
        Movimiento movimientoMock = mock(Movimiento.class);
        when(httpServletRequestMock.getSession(false)).thenReturn(null);

        //ejecucion
        ModelAndView modelAndView = controladorMovimiento.eliminarMovimiento(movimientoMock.getId(), httpServletRequestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
        verify(httpSessionMock, times(0)).getAttribute("idUsuario");
    }

    @Test
    public void queAlQuererEliminarUnMovimientoYNoSePuedaEstablecerConexionConLaBaseDeDatosSeMuestreUnMensajeDeError() throws ExcepcionBaseDeDatos{
        //preparacion
        Movimiento movimientoMock = mock(Movimiento.class);
        when(httpServletRequestMock.getSession(false)).thenReturn(httpSessionMock);
        when(httpSessionMock.getAttribute("idUsuario")).thenReturn(1L);
        when(servicioMovimientoMock.obtenerMovimientoPorId(anyLong(), anyLong())).thenReturn(Optional.of(movimientoMock));
        doThrow(ExcepcionBaseDeDatos.class).when(servicioMovimientoMock).eliminarMovimiento(anyLong(), any(Movimiento.class));

        //ejecucion
        ModelAndView modelAndView = controladorMovimiento.eliminarMovimiento(movimientoMock.getId(), httpServletRequestMock);

        //validacion
        assertEquals("error", modelAndView.getViewName());
        assertEquals("Error en la base de datos", modelAndView.getModel().get("error"));
    }


}
