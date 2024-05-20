package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionCamposInvalidos;
import com.tallerwebi.dominio.excepcion.ExcepcionMovimientoNoEncontrado;
import com.tallerwebi.dominio.movimiento.CategoriaMovimiento;
import com.tallerwebi.dominio.movimiento.Movimiento;
import com.tallerwebi.dominio.movimiento.ServicioMovimiento;
import com.tallerwebi.dominio.movimiento.TipoMovimiento;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsIterableWithSize;
import org.hamcrest.collection.IsMapWithSize;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ControladorMovimientoTest {
    ControladorMovimiento controladorMovimiento;
    ServicioMovimiento servicioMovimientoMock;
    HttpServletRequest httpServletRequestMock;
    HttpSession httpSessionMock;
    DatosEditarMovimiento datosEditarMovimientoMock;
    DatosAgregarMovimiento datosAgregarMovimientoMock;

    @BeforeEach
    public void init(){
        servicioMovimientoMock = mock(ServicioMovimiento.class);
        controladorMovimiento = new ControladorMovimiento(servicioMovimientoMock);
        httpServletRequestMock = mock(HttpServletRequest.class);
        httpSessionMock = mock(HttpSession.class);
        datosEditarMovimientoMock = mock(DatosEditarMovimiento.class);
        datosAgregarMovimientoMock = mock(DatosAgregarMovimiento.class);
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
    public void queAlQuererObtenerMovimientosPorFechaYExistaUsuarioLogueadoRetorneLosMovimientos() throws ExcepcionBaseDeDatos {
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
    public void queAlQuererObtenerMovimientosPorFechaYNoExistaUsuarioLogueadoRetorneNull() throws ExcepcionBaseDeDatos {
        //preparacion
        when(httpServletRequestMock.getSession(false)).thenReturn(null);

        //ejecucion
        List<Movimiento> movimientos = controladorMovimiento.obtenerMovimientosPorFecha("2021-06-01", httpServletRequestMock);

        //validacion
        assertThat(movimientos, Matchers.nullValue());
        verify(httpSessionMock, times(0)).getAttribute("idUsuario");
    }

    @Test
    public void queAlQuererIrAVistaEditarUnMovimientoYExistaUsuarioLogueadoMeDirijaAlFormularioDeEdicion() throws ExcepcionMovimientoNoEncontrado, ExcepcionBaseDeDatos {
        //preparacion
        Movimiento movimientoMock = mock(Movimiento.class);
        when(servicioMovimientoMock.obtenerMovimientoPorId(anyLong())).thenReturn(movimientoMock);
        when(httpServletRequestMock.getSession(false)).thenReturn(httpSessionMock);
        when(movimientoMock.getDescripcion()).thenReturn("descripcion");
        when(movimientoMock.getMonto()).thenReturn(22.0);
        when(movimientoMock.getCategoria()).thenReturn(new CategoriaMovimiento("categoria", new TipoMovimiento("tipo")));
        when(movimientoMock.getId()).thenReturn(1L);
        when(movimientoMock.getFechayHora()).thenReturn(LocalDate.now());

        // Configuración de método estático
        Mockito.mockStatic(DatosEditarMovimiento.class);
        when(DatosEditarMovimiento.contruirDesdeMovimiento(movimientoMock)).thenReturn(datosEditarMovimientoMock);


        //ejecucion
        ModelAndView modelAndView = controladorMovimiento.irAFormularioEditarMovimiento(httpServletRequestMock, 1L);
        DatosEditarMovimiento movimientoActual = (DatosEditarMovimiento) modelAndView.getModel().get("movimiento");

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("editar-movimiento"));
        assertThat(movimientoActual, Matchers.is(datosEditarMovimientoMock));
    }

    @Test
    public void queAlQuererIrAVistaEditarUnMovimientoYNoExistaUsuarioLogueadoMeRedirijaAlLoguin() throws ExcepcionMovimientoNoEncontrado, ExcepcionBaseDeDatos {
        //preparacion
        when(httpServletRequestMock.getSession(false)).thenReturn(null);

        //ejecucion
        ModelAndView modelAndView = controladorMovimiento.irAFormularioEditarMovimiento(httpServletRequestMock, 1L);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
        verify(httpSessionMock, times(0)).getAttribute("idUsuario");
    }

    @Test
    public void queAlQuererEditarUnMovimientoSePuedaEditarMovimiento() throws ExcepcionMovimientoNoEncontrado, ExcepcionCamposInvalidos, ExcepcionBaseDeDatos {
        //preparacion
        Movimiento movimientoMock = mock(Movimiento.class);
        CategoriaMovimiento categoriaMock = mock(CategoriaMovimiento.class);
        when(httpServletRequestMock.getSession(false)).thenReturn(httpSessionMock);
        when(httpSessionMock.getAttribute("idUsuario")).thenReturn(1L);
        when(movimientoMock.getCategoria()).thenReturn(categoriaMock);
        when(categoriaMock.getNombre()).thenReturn("Categoria Test");

        //ejecucion
        ModelAndView modelAndView = controladorMovimiento.editarMovimiento(datosEditarMovimientoMock, httpServletRequestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/movimientos"));
        verify(servicioMovimientoMock, times(1)).actualizarMovimiento(datosEditarMovimientoMock);
    }

    @Test
    public void queAlQuererEditarUnMovimientoYNoExistaUsuarioLogueadoNoSePuedaEditarMovimiento() throws ExcepcionMovimientoNoEncontrado, ExcepcionBaseDeDatos {
        //preparacion
        when(httpServletRequestMock.getSession(false)).thenReturn(null);

        //ejecucion
        ModelAndView modelAndView = controladorMovimiento.editarMovimiento(null, httpServletRequestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
        verify(httpSessionMock, times(0)).getAttribute("idUsuario");
    }

    @Test
    public void queAlQuererEditarUnMovimientoYNoSeIngreseNingunDatoNoSePuedaEditarMovimiento() throws ExcepcionBaseDeDatos, ExcepcionCamposInvalidos, ExcepcionMovimientoNoEncontrado {
        //preparacion
        when(httpServletRequestMock.getSession(false)).thenReturn(httpSessionMock);
        when(httpSessionMock.getAttribute("idUsuario")).thenReturn(1L);
        doThrow(ExcepcionCamposInvalidos.class).when(servicioMovimientoMock).actualizarMovimiento(datosEditarMovimientoMock);
        Map<String, String> errores = new HashMap<>();
        errores.put("descripcion", "El campo es requerido");
        errores.put("tipo", "El campo es requerido");
        errores.put("categoria", "El campo es requerido");
        errores.put("monto", "El campo es requerido");
        ExcepcionCamposInvalidos excepcion = new ExcepcionCamposInvalidos(errores);
        doThrow(excepcion).when(servicioMovimientoMock).actualizarMovimiento(datosEditarMovimientoMock);

        //ejecucion
        ModelAndView modelAndView = controladorMovimiento.editarMovimiento(datosEditarMovimientoMock, httpServletRequestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("editar-movimiento"));
        assertThat((Map<String, String>)modelAndView.getModel().get("errores"), IsMapWithSize.aMapWithSize(4));
        assertThat((Map<String, String>)modelAndView.getModel().get("errores"), hasEntry("descripcion", "El campo es requerido"));
        assertThat((Map<String, String>)modelAndView.getModel().get("errores"), hasEntry("tipo", "El campo es requerido"));
        assertThat((Map<String, String>)modelAndView.getModel().get("errores"), hasEntry("monto", "El campo es requerido"));
        assertThat((Map<String, String>)modelAndView.getModel().get("errores"), hasEntry("categoria", "El campo es requerido"));
        verify(servicioMovimientoMock, times(1)).actualizarMovimiento(datosEditarMovimientoMock);
    }

    @Test
    public void queAlQuererEditarUnMovimientoYNoSePuedaEstablecerConexionConLaBaseDeDatosSeMuestreUnMensajeDeError() throws ExcepcionCamposInvalidos, ExcepcionMovimientoNoEncontrado, ExcepcionBaseDeDatos {
        //preparacion
        Movimiento movimientoMock = mock(Movimiento.class);
        CategoriaMovimiento categoriaMock = mock(CategoriaMovimiento.class);
        when(httpServletRequestMock.getSession(false)).thenReturn(httpSessionMock);
        when(httpSessionMock.getAttribute("idUsuario")).thenReturn(1L);
        ExcepcionBaseDeDatos excepcion = new ExcepcionBaseDeDatos("Base de datos no disponible");
        doThrow(excepcion).when(servicioMovimientoMock).actualizarMovimiento(datosEditarMovimientoMock);

        //ejecucion
        ExcepcionBaseDeDatos exceptionObtenida = assertThrows(ExcepcionBaseDeDatos.class, () -> {
            controladorMovimiento.editarMovimiento(datosEditarMovimientoMock, httpServletRequestMock);
        });

        //validacion
        assertEquals("Base de datos no disponible", exceptionObtenida.getMessage());
        verify(servicioMovimientoMock, times(1)).actualizarMovimiento(datosEditarMovimientoMock);
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
    public void queAlQuererAgregarUnMovimientoSePuedaAgregarMovimiento() throws ExcepcionBaseDeDatos, ExcepcionCamposInvalidos {
        //preparacion
        when(httpServletRequestMock.getSession(false)).thenReturn(httpSessionMock);
        when(httpSessionMock.getAttribute("idUsuario")).thenReturn(1L);
        DatosAgregarMovimiento datosAgregarMovimiento = new DatosAgregarMovimiento("descripcion", "tipo", "categoria", 100.0);

        //ejecucion
        ModelAndView modelAndView = controladorMovimiento.ingresarNuevoMovimiento(datosAgregarMovimiento, httpServletRequestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/movimientos"));
        verify(servicioMovimientoMock, times(1)).nuevoMovimiento(anyLong(), any());
    }

    @Test
    public void queAlQuererAgregarUnMovimientoYNoExistaUsuarioLogueadoNoSePuedaAgregarMovimiento() throws ExcepcionBaseDeDatos {
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
    public void queAlQuererAgregarUnMovimientoYNoSePuedaEstablecerConexionConLaBaseDeDatosSeMuestreUnMensajeDeError() throws ExcepcionBaseDeDatos, ExcepcionCamposInvalidos {
        //preparacion
        when(httpServletRequestMock.getSession(false)).thenReturn(httpSessionMock);
        when(httpSessionMock.getAttribute("idUsuario")).thenReturn(1L);
        doThrow(ExcepcionBaseDeDatos.class).when(servicioMovimientoMock).nuevoMovimiento(anyLong(), any());
        DatosAgregarMovimiento datosAgregarMovimiento = new DatosAgregarMovimiento("descripcion", "tipo", "categoria", 100.0);

        //ejecucion y validacion
        assertThrows(ExcepcionBaseDeDatos.class, () -> controladorMovimiento.ingresarNuevoMovimiento(datosAgregarMovimiento, httpServletRequestMock));
    }

    @Test
    public void queAlQuererAgregarUnMovimientoYNoSeIngreseNingunDatoNoSePuedaAgregarMovimiento() throws ExcepcionBaseDeDatos, ExcepcionCamposInvalidos {
        //preparacion
        when(httpServletRequestMock.getSession(false)).thenReturn(httpSessionMock);
        when(httpSessionMock.getAttribute("idUsuario")).thenReturn(1L);
        Map<String, String> errores = new HashMap<>();
        errores.put("descripcion", "El campo es requerido");
        errores.put("tipo", "El campo es requerido");
        errores.put("categoria", "El campo es requerido");
        errores.put("monto", "El campo es requerido");
        ExcepcionCamposInvalidos excepcion = new ExcepcionCamposInvalidos(errores);
        doThrow(excepcion).when(servicioMovimientoMock).nuevoMovimiento(anyLong(), ArgumentMatchers.any(DatosAgregarMovimiento.class));

        //ejecucion
        ModelAndView modelAndView = controladorMovimiento.ingresarNuevoMovimiento(new DatosAgregarMovimiento(), httpServletRequestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("agregar-movimiento"));
        assertThat((Map<String, String>)modelAndView.getModel().get("errores"), IsMapWithSize.aMapWithSize(4));
        assertThat((Map<String, String>)modelAndView.getModel().get("errores"), hasEntry("descripcion", "El campo es requerido"));
        assertThat((Map<String, String>)modelAndView.getModel().get("errores"), hasEntry("tipo", "El campo es requerido"));
        assertThat((Map<String, String>)modelAndView.getModel().get("errores"), hasEntry("monto", "El campo es requerido"));
        assertThat((Map<String, String>)modelAndView.getModel().get("errores"), hasEntry("categoria", "El campo es requerido"));
        verify(servicioMovimientoMock, times(1)).nuevoMovimiento(anyLong(), ArgumentMatchers.any(DatosAgregarMovimiento.class));
    }

    @Test
    public void queAlQuererEliminarUnMovimientoSePuedaEliminarMovimiento() throws ExcepcionBaseDeDatos, ExcepcionMovimientoNoEncontrado {
        //preparacion
        Movimiento movimientoMock = mock(Movimiento.class);
        when(httpServletRequestMock.getSession(false)).thenReturn(httpSessionMock);
        when(httpSessionMock.getAttribute("idUsuario")).thenReturn(1L);
        when(servicioMovimientoMock.obtenerMovimientoPorId(anyLong())).thenReturn(movimientoMock);

        //ejecucion
        ModelAndView modelAndView = controladorMovimiento.eliminarMovimiento(movimientoMock.getId(), httpServletRequestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/movimientos"));
        verify(servicioMovimientoMock, times(1)).eliminarMovimiento(anyLong());
    }

    @Test
    public void queAlQuererEliminarUnMovimientoYNoExistaUsuarioLogueadoNoSePuedaEliminarMovimiento() throws ExcepcionBaseDeDatos, ExcepcionMovimientoNoEncontrado {
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
    public void queAlQuererEliminarUnMovimientoYNoSePuedaEstablecerConexionConLaBaseDeDatosSeMuestreUnMensajeDeError() throws ExcepcionBaseDeDatos, ExcepcionMovimientoNoEncontrado {
        //preparacion
        Movimiento movimientoMock = mock(Movimiento.class);
        when(httpServletRequestMock.getSession(false)).thenReturn(httpSessionMock);
        when(httpSessionMock.getAttribute("idUsuario")).thenReturn(1L);
        when(servicioMovimientoMock.obtenerMovimientoPorId(anyLong())).thenReturn(movimientoMock);
        ExcepcionBaseDeDatos excepcion = new ExcepcionBaseDeDatos("Base de datos no disponible");
        doThrow(excepcion).when(servicioMovimientoMock).eliminarMovimiento(anyLong());

        //ejecucion y validacion
        ExcepcionBaseDeDatos excepcionBaseDeDatos = assertThrows(ExcepcionBaseDeDatos.class, ()->{
            controladorMovimiento.eliminarMovimiento(1L, httpServletRequestMock);
        });
        assertThat(excepcionBaseDeDatos.getMessage(), equalToIgnoringCase("Base de datos no disponible"));
    }
}

