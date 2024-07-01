package com.tallerwebi.presentacion.movimiento;

import com.itextpdf.text.DocumentException;
import com.tallerwebi.dominio.categoria.CategoriaMovimiento;
import com.tallerwebi.dominio.excepcion.*;
import com.tallerwebi.dominio.exportar.ServicioDeExportacion;
import com.tallerwebi.dominio.exportar.TipoDeArchivo;
import com.tallerwebi.dominio.movimiento.Movimiento;
import com.tallerwebi.dominio.movimiento.ServicioMovimiento;
import com.tallerwebi.dominio.movimientoCompartido.ServicioMovimientoCompartido;
import com.tallerwebi.dominio.tipo.TipoMovimiento;
import com.tallerwebi.dominio.usuario.ServicioUsuario;
import com.tallerwebi.dominio.usuario.Usuario;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsIterableWithSize;
import org.hamcrest.collection.IsMapWithSize;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

public class ControladorMovimientoTest {
    ControladorMovimiento controladorMovimiento;
    ServicioMovimiento servicioMovimientoMock;
    HttpServletRequest httpServletRequestMock;
    HttpSession httpSessionMock;
    DatosEditarMovimiento datosEditarMovimientoMock;
    DatosAgregarMovimiento datosAgregarMovimientoMock;
    ServicioDeExportacion servicioDeExportacionMock;
    ServicioMovimientoCompartido servicioMovimientoCompartidoMock;
    ServicioUsuario servicioUsuarioMock;

    @BeforeEach
    public void init(){
        servicioMovimientoCompartidoMock = mock(ServicioMovimientoCompartido.class);
        servicioMovimientoMock = mock(ServicioMovimiento.class);
        servicioDeExportacionMock = mock(ServicioDeExportacion.class);
        servicioUsuarioMock = mock(ServicioUsuario.class);
        controladorMovimiento = new ControladorMovimiento(servicioMovimientoMock, servicioDeExportacionMock,servicioUsuarioMock, servicioMovimientoCompartidoMock);
        httpServletRequestMock = mock(HttpServletRequest.class);
        httpSessionMock = mock(HttpSession.class);
        datosEditarMovimientoMock = mock(DatosEditarMovimiento.class);
        datosAgregarMovimientoMock = mock(DatosAgregarMovimiento.class);

    }

    @Test
    public void queAlQuererObtenerMovimientosPorPaginaYExistaUsuarioLogueadoRetorneLosMovimientosPorPagina() throws ExcepcionBaseDeDatos, PaginaInexistente {
        //preparacion
        when(httpServletRequestMock.getSession(false)).thenReturn(httpSessionMock);
        when(httpSessionMock.getAttribute("idUsuario")).thenReturn(1L);
        List<Movimiento> movimientos = generarMovimientos(10);
        when(servicioMovimientoMock.obtenerMovimientosPorPagina(anyLong(), anyInt(), anyInt())).thenReturn(movimientos);
        when(servicioMovimientoMock.calcularCantidadDePaginas(anyLong(), anyInt())).thenReturn(2);

        //ejecucion
        ModelAndView modelAndView = controladorMovimiento.obtenerMovimientosPorPagina(httpServletRequestMock, 1);
        List<Movimiento> movimientosObtenidos = (List<Movimiento>)modelAndView.getModel().get("movimientos");

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("movimientos"));
        assertThat(movimientosObtenidos, contains(movimientos.toArray()));
        assertThat(modelAndView.getModel().get("cantidadDePaginas"), equalTo(2));
    }

    @Test
    public void queAlQuererObtenerMovimientosPorPaginaYNoExistaUsuarioLogueadoMeRedirijaAlLogin() throws ExcepcionBaseDeDatos, PaginaInexistente {
        //preparacion
        when(httpServletRequestMock.getSession(false)).thenReturn(null);

        //ejecucion
        ModelAndView modelAndView = controladorMovimiento.obtenerMovimientosPorPagina(httpServletRequestMock, 1);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
        verify(httpSessionMock, times(0)).getAttribute("idUsuario");
    }

    @Test
    public void queAlQuererObtenerMovimientosPorPaginaLanceUnaExcepcionSiLaPaginaNoExiste() throws ExcepcionBaseDeDatos, PaginaInexistente {
        //preparacion
        when(httpServletRequestMock.getSession(false)).thenReturn(httpSessionMock);
        when(httpSessionMock.getAttribute("idUsuario")).thenReturn(1L);
        List<Movimiento> movimientos = generarMovimientos(10);
        when(servicioMovimientoMock.obtenerMovimientosPorPagina(anyLong(), anyInt(), anyInt())).thenReturn(movimientos);
        when(servicioMovimientoMock.calcularCantidadDePaginas(anyLong(), anyInt())).thenReturn(2);

        //ejecucion y validacion
        Assertions.assertThrows(PaginaInexistente.class, ()->{
            controladorMovimiento.obtenerMovimientosPorPagina(httpServletRequestMock, 3);
        }, "La pagina no existe");
    }

    @Test
    public void queAlQuererObtenerMovimientosPorPaginaLanceExceptionSiNoSePuedeEstablecerConexionConLaBaseDeDatos() throws ExcepcionBaseDeDatos {
        //preparacion
        when(httpServletRequestMock.getSession(false)).thenReturn(httpSessionMock);
        when(httpSessionMock.getAttribute("idUsuario")).thenReturn(1L);
        ExcepcionBaseDeDatos excepcion = new ExcepcionBaseDeDatos("Base de datos no disponible");
        doThrow(excepcion).when(servicioMovimientoMock).obtenerMovimientosPorFecha(anyLong(), any());

        //ejecucion y validacion
        ExcepcionBaseDeDatos excepcionBaseDeDatos = assertThrows(ExcepcionBaseDeDatos.class, ()->{
            controladorMovimiento.obtenerMovimientosPorFecha("2021-06-01", httpServletRequestMock);
        });
        assertThat(excepcionBaseDeDatos.getMessage(), equalToIgnoringCase("Base de datos no disponible"));
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
    public void queAlQuererObtenerMovimientosPorFechaYNoTengaMovimientosDevuelvaListaVaciaYMensaje() throws ExcepcionBaseDeDatos {
        //preparacion
        when(httpServletRequestMock.getSession(false)).thenReturn(httpSessionMock);
        when(httpSessionMock.getAttribute("idUsuario")).thenReturn(1L);
        when(servicioMovimientoMock.obtenerMovimientosPorFecha(anyLong(), any())).thenReturn(Collections.emptyList());

        //ejecucion
        List<Movimiento> movimientos = controladorMovimiento.obtenerMovimientosPorFecha("2021-06-01", httpServletRequestMock);

        //validacion
        assertThat(movimientos, IsIterableWithSize.iterableWithSize(0));
        verify(httpSessionMock, times(1)).getAttribute("idUsuario");
    }

    @Test
    public void queAlQuererObtenerMovimientosPorFechaLanceExceptionSiNoSePuedeEstablecerConexionConLaBaseDeDatos() throws ExcepcionBaseDeDatos {
        //preparacion
        when(httpServletRequestMock.getSession(false)).thenReturn(httpSessionMock);
        when(httpSessionMock.getAttribute("idUsuario")).thenReturn(1L);
        ExcepcionBaseDeDatos excepcion = new ExcepcionBaseDeDatos("Base de datos no disponible");
        doThrow(excepcion).when(servicioMovimientoMock).obtenerMovimientosPorFecha(anyLong(), any());

        //ejecucion y validacion
        ExcepcionBaseDeDatos excepcionBaseDeDatos = assertThrows(ExcepcionBaseDeDatos.class, ()->{
            controladorMovimiento.obtenerMovimientosPorFecha("2021-06-01", httpServletRequestMock);
        });
        assertThat(excepcionBaseDeDatos.getMessage(), equalToIgnoringCase("Base de datos no disponible"));
    }

    @Test
    public void queAlQuererIrAVistaEditarUnMovimientoYExistaUsuarioLogueadoMeDirijaAlFormularioDeEdicion() throws ExcepcionMovimientoNoEncontrado, ExcepcionBaseDeDatos, UsuarioInexistente {
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
    public void queAlQuererIrAVistaEditarUnMovimientoYNoExistaUsuarioLogueadoMeRedirijaAlLoguin() throws ExcepcionMovimientoNoEncontrado, ExcepcionBaseDeDatos, UsuarioInexistente {
        //preparacion
        when(httpServletRequestMock.getSession(false)).thenReturn(null);

        //ejecucion
        ModelAndView modelAndView = controladorMovimiento.irAFormularioEditarMovimiento(httpServletRequestMock, 1L);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
        verify(httpSessionMock, times(0)).getAttribute("idUsuario");
    }

    @Test
    public void queAlQuererIrAVistaEditarUnMovimientoLanceExceptionSiNoSePuedeEstablecerConexionConLaBaseDeDatos() throws ExcepcionMovimientoNoEncontrado, ExcepcionBaseDeDatos {
        //preparacion
        when(httpServletRequestMock.getSession(false)).thenReturn(httpSessionMock);
        ExcepcionBaseDeDatos excepcion = new ExcepcionBaseDeDatos("Base de datos no disponible");
        doThrow(excepcion).when(servicioMovimientoMock).obtenerMovimientoPorId(anyLong());

        //ejecucion y validacion
        ExcepcionBaseDeDatos excepcionBaseDeDatos = assertThrows(ExcepcionBaseDeDatos.class, ()->{
            controladorMovimiento.irAFormularioEditarMovimiento(httpServletRequestMock, 1L);
        });
        assertThat(excepcionBaseDeDatos.getMessage(), equalToIgnoringCase("Base de datos no disponible"));
    }

    @Test
    public void queAlQuererIrAVistaEditarUnMovimientoLanceExcepcionMovimientoNoEncontradoSiNoExisteMovimiento() throws ExcepcionMovimientoNoEncontrado, ExcepcionBaseDeDatos {
        //preparacion
        when(httpServletRequestMock.getSession(false)).thenReturn(httpSessionMock);
        ExcepcionMovimientoNoEncontrado excepcion = new ExcepcionMovimientoNoEncontrado("Movimiento no encontrado");
        doThrow(excepcion).when(servicioMovimientoMock).obtenerMovimientoPorId(anyLong());

        //ejecucion y validacion
        ExcepcionMovimientoNoEncontrado excepcionMovimientoNoEncontrado = assertThrows(ExcepcionMovimientoNoEncontrado.class, ()->{
            controladorMovimiento.irAFormularioEditarMovimiento(httpServletRequestMock, 1L);
        });
        assertThat(excepcionMovimientoNoEncontrado.getMessage(), equalToIgnoringCase("Movimiento no encontrado"));
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
    public void queAlQuererEditarUnMovimientoLanceExcepcionMovimientoNoEncontradoSiNoExisteMovimiento() throws ExcepcionBaseDeDatos, ExcepcionCamposInvalidos, ExcepcionMovimientoNoEncontrado {
        //preparacion
        when(httpServletRequestMock.getSession(false)).thenReturn(httpSessionMock);
        when(httpSessionMock.getAttribute("idUsuario")).thenReturn(1L);
        ExcepcionMovimientoNoEncontrado excepcion = new ExcepcionMovimientoNoEncontrado("Movimiento no encontrado");
        doThrow(excepcion).when(servicioMovimientoMock).actualizarMovimiento(datosEditarMovimientoMock);

        //ejecucion y validacion
        ExcepcionMovimientoNoEncontrado excepcionMovimientoNoEncontrado = assertThrows(ExcepcionMovimientoNoEncontrado.class, ()->{
            controladorMovimiento.editarMovimiento(datosEditarMovimientoMock, httpServletRequestMock);
        });
        assertThat(excepcionMovimientoNoEncontrado.getMessage(), equalToIgnoringCase("Movimiento no encontrado"));
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

    @Test
    public void queAlQuererEliminarUnMovimientoLanceExcepcionMovimientoNoEncontradoSiNoExisteMovimiento() throws ExcepcionBaseDeDatos, ExcepcionMovimientoNoEncontrado {
        //preparacion
        when(httpServletRequestMock.getSession(false)).thenReturn(httpSessionMock);
        when(httpSessionMock.getAttribute("idUsuario")).thenReturn(1L);
        ExcepcionMovimientoNoEncontrado excepcion = new ExcepcionMovimientoNoEncontrado("Movimiento no encontrado");
        doThrow(excepcion).when(servicioMovimientoMock).eliminarMovimiento(anyLong());

        //ejecucion y validacion
        ExcepcionMovimientoNoEncontrado excepcionMovimientoNoEncontrado = assertThrows(ExcepcionMovimientoNoEncontrado.class, ()->{
            controladorMovimiento.eliminarMovimiento(1L, httpServletRequestMock);
        });
        assertThat(excepcionMovimientoNoEncontrado.getMessage(), equalToIgnoringCase("Movimiento no encontrado"));
    }

    @Test
    public void queAlClickearEnLaBarraDeNavegacionEnAgregarMovimientoTeLleveALaPaginaAgregarMovimiento() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        //preparacion
        when(httpServletRequestMock.getSession(false)).thenReturn(httpSessionMock);
        when(httpSessionMock.getAttribute("idUsuario")).thenReturn(1L);
        when(servicioMovimientoCompartidoMock.obtenerAmigos(anyLong())).thenReturn(Collections.emptyList());

        //ejecucion
        ModelAndView modelAndView = controladorMovimiento.irAAgregarMovimiento(httpServletRequestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("agregar-movimiento"));
    }

    @Test
    public void queAlClickearEnLaBarraDeNavegacionEnAgregarMovimientoYNoExistaUsuarioLogueadoMeRedirijaAlLogin() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        //preparacion
        when(httpServletRequestMock.getSession(false)).thenReturn(null);

        //ejecucion
        ModelAndView modelAndView = controladorMovimiento.irAAgregarMovimiento(httpServletRequestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
        verify(httpSessionMock, times(0)).getAttribute("idUsuario");
    }

    @Test
    public void queAlClickearEnLaBarraDeNavegacionEnAgregarMovimientoLanceExceptionSiNoSePuedeEstablecerConexionConLaBaseDeDatos() throws ExcepcionBaseDeDatos {
        //preparacion
        when(httpServletRequestMock.getSession(false)).thenReturn(httpSessionMock);
        when(httpSessionMock.getAttribute("idUsuario")).thenReturn(1L);
        ExcepcionBaseDeDatos excepcion = new ExcepcionBaseDeDatos("Base de datos no disponible");
        doThrow(excepcion).when(servicioMovimientoCompartidoMock).obtenerAmigos(anyLong());

        //ejecucion y validacion
        ExcepcionBaseDeDatos excepcionBaseDeDatos = assertThrows(ExcepcionBaseDeDatos.class, ()->{
            controladorMovimiento.irAAgregarMovimiento(httpServletRequestMock);
        });
        assertThat(excepcionBaseDeDatos.getMessage(), equalToIgnoringCase("Base de datos no disponible"));
    }

    @Test
    public void queAlQuererAgregarUnMovimientoSePuedaAgregarMovimiento() throws ExcepcionBaseDeDatos, ExcepcionCamposInvalidos, UsuarioInexistente {
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
    public void queAlQuererAgregarUnMovimientoYNoSePuedaEstablecerConexionConLaBaseDeDatosSeMuestreUnMensajeDeError() throws ExcepcionBaseDeDatos, ExcepcionCamposInvalidos, UsuarioInexistente {
        //preparacion
        when(httpServletRequestMock.getSession(false)).thenReturn(httpSessionMock);
        when(httpSessionMock.getAttribute("idUsuario")).thenReturn(1L);
        doThrow(ExcepcionBaseDeDatos.class).when(servicioMovimientoMock).nuevoMovimiento(anyLong(), any());
        DatosAgregarMovimiento datosAgregarMovimiento = new DatosAgregarMovimiento("descripcion", "tipo", "categoria", 100.0);

        //ejecucion y validacion
        assertThrows(ExcepcionBaseDeDatos.class, () -> controladorMovimiento.ingresarNuevoMovimiento(datosAgregarMovimiento, httpServletRequestMock));
    }

    @Test
    public void queAlQuererAgregarUnMovimientoYNoSeIngreseNingunDatoNoSePuedaAgregarMovimiento() throws ExcepcionBaseDeDatos, ExcepcionCamposInvalidos, UsuarioInexistente {
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

    //Testando método de paginación
    @Test
    public void queAlQuererIraVerMovimientosYNoExistaUsuarioLogueadoMeRedirijaAlLoguin() throws ExcepcionBaseDeDatos, PaginaInexistente, UsuarioInexistente {
        //preparacion
        when(httpServletRequestMock.getSession(false)).thenReturn(null);

        //ejecucion
        ModelAndView modelAndView = controladorMovimiento.obtenerMovimientosPorPagina(httpServletRequestMock, 1);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
        verify(httpSessionMock, times(0)).getAttribute("idUsuario");
    }

    @Test
    public void queAlIrAVerMovimientosMuestreLosMovimientosDeLaPaginaUno() throws ExcepcionBaseDeDatos, PaginaInexistente, UsuarioInexistente {
        //preparación
        when(httpServletRequestMock.getSession(false)).thenReturn(httpSessionMock);
        when(httpSessionMock.getAttribute("idUsuario")).thenReturn(1L);
        List<Movimiento> movimientos = generarMovimientos(10);
        when(servicioMovimientoMock.obtenerMovimientosPorPagina(anyLong(), anyInt(), anyInt())).thenReturn(movimientos);
        when(servicioMovimientoMock.calcularCantidadDePaginas(anyLong(), anyInt())).thenReturn(2);

        //ejecución
        ModelAndView modelAndView = controladorMovimiento.obtenerMovimientosPorPagina(httpServletRequestMock, 1);
        List<Movimiento> movimientosObtenidos = (List<Movimiento>)modelAndView.getModel().get("movimientos");
        //validación
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("movimientos"));
        assertThat(movimientosObtenidos, contains(movimientos.toArray()));
        assertThat(modelAndView.getModel().get("cantidadDePaginas"), equalTo(2));
    }

    @Test
    public void queAlClickearEnUnNumeroDePaginaMuestreLosMovimientosEnLaPaginaSeleccionada() throws ExcepcionBaseDeDatos, PaginaInexistente, UsuarioInexistente {
        //preparación
        when(httpServletRequestMock.getSession(false)).thenReturn(httpSessionMock);
        when(httpSessionMock.getAttribute("idUsuario")).thenReturn(1L);
        List<Movimiento> movimientos = generarMovimientos(10);
        when(servicioMovimientoMock.obtenerMovimientosPorPagina(anyLong(), anyInt(), anyInt())).thenReturn(movimientos);
        when(servicioMovimientoMock.calcularCantidadDePaginas(anyLong(), anyInt())).thenReturn(2);

        //ejecución
        ModelAndView modelAndView = controladorMovimiento.obtenerMovimientosPorPagina(httpServletRequestMock, 2);
        List<Movimiento> movimientosObtenidos = (List<Movimiento>)modelAndView.getModel().get("movimientos");
        //validación
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("movimientos"));
        assertThat(movimientosObtenidos, contains(movimientos.toArray()));
        assertThat(modelAndView.getModel().get("cantidadDePaginas"), equalTo(2));
    }

    @Test
    public void queAlIndicarUnNumeroDePaginaInexistenteLanceUnaExcepcion() throws ExcepcionBaseDeDatos, PaginaInexistente {
        //preparación
        when(httpServletRequestMock.getSession(false)).thenReturn(httpSessionMock);
        when(httpSessionMock.getAttribute("idUsuario")).thenReturn(1L);
        List<Movimiento> movimientos = generarMovimientos(10);
        when(servicioMovimientoMock.obtenerMovimientosPorPagina(anyLong(), anyInt(), anyInt())).thenReturn(movimientos);
        when(servicioMovimientoMock.calcularCantidadDePaginas(anyLong(), anyInt())).thenReturn(2);

        //ejecución y validación
        Assertions.assertThrows(PaginaInexistente.class, ()->{
            controladorMovimiento.obtenerMovimientosPorPagina(httpServletRequestMock, 3);
        }, "La pagina no existe");
    }

    //DESCARGA DE ARCHIVO PDF
    @Test
    public void queAlHacerClickEnElBotonExportarPDFDebeDescargarArchivoPDFConTodosLosMovimientos() throws ExcepcionExportacionDeArchivo, ExcepcionBaseDeDatos, DocumentException {
        //preparacion
        Long idUsuario = 1L;
        byte[] bytesDelArchivo = "contenido del archivo".getBytes();

        when(httpServletRequestMock.getSession(false)).thenReturn(httpSessionMock);
        when(httpSessionMock.getAttribute("idUsuario")).thenReturn(1L);
        TipoDeArchivo tipoDeArchivo = TipoDeArchivo.PDF;
        when(servicioDeExportacionMock.generarArchivo(idUsuario, tipoDeArchivo)).thenReturn(bytesDelArchivo);

        //ejecucion
        ResponseEntity<byte[]> respuesta = controladorMovimiento.descargarDocumentoDeMovimentos(tipoDeArchivo, httpServletRequestMock);

        //validacion
        assertThat(respuesta.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(respuesta.getHeaders().getContentType(), equalTo(MediaType.APPLICATION_OCTET_STREAM));
        assertThat(respuesta.getBody(), equalTo(bytesDelArchivo));
    }

    //DESCARGA DE ARCHIVO XLSX
    @Test
    public void queAlHacerClickEnElBotonExportarXLSXDebeDescargarArchivoXLSXConTodosLosMovimientos() throws ExcepcionExportacionDeArchivo, ExcepcionBaseDeDatos, DocumentException {
        //preparacion
        Long idUsuario = 1L;
        byte[] bytesDelArchivo = "contenido del archivo".getBytes();

        when(httpServletRequestMock.getSession(false)).thenReturn(httpSessionMock);
        when(httpSessionMock.getAttribute("idUsuario")).thenReturn(1L);
        TipoDeArchivo tipoDeArchivo = TipoDeArchivo.XLSX;
        when(servicioDeExportacionMock.generarArchivo(idUsuario, tipoDeArchivo)).thenReturn(bytesDelArchivo);

        //ejecucion
        ResponseEntity<byte[]> respuesta = controladorMovimiento.descargarDocumentoDeMovimentos(tipoDeArchivo, httpServletRequestMock);

        //validacion
        assertThat(respuesta.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(respuesta.getHeaders().getContentType(), equalTo(MediaType.APPLICATION_OCTET_STREAM));
        assertThat(respuesta.getBody(), equalTo(bytesDelArchivo));
    }

    @Test
    public void queAlQuererObtenerAmigosObtengaTodosLosAmigos() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        //preparacion
        when(httpServletRequestMock.getSession(false)).thenReturn(httpSessionMock);
        when(httpSessionMock.getAttribute("idUsuario")).thenReturn(1L);
        List<Usuario> amigos = new ArrayList<>();
        amigos.add(new Usuario("amigo1", "1234", "usuario", true));
        amigos.add(new Usuario("amigo2", "1234", "usuario", true));
        when(servicioUsuario.obtenerAmigosDeUnUsuario(anyLong())).thenReturn(amigos);

        //ejecucion
        List<Usuario> amigosObtenidos = controladorMovimiento.obtenerAmigos(httpServletRequestMock);

        //validacion
        assertThat(amigosObtenidos, contains(amigos.toArray()));
    }

    @Test
    public void queAlQuererObtenerAmigosObtengaUnaListaVaciaAlNoTenerAmigos() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        //preparacion
        when(httpServletRequestMock.getSession(false)).thenReturn(httpSessionMock);
        when(httpSessionMock.getAttribute("idUsuario")).thenReturn(1L);
        when(servicioUsuario.obtenerAmigosDeUnUsuario(anyLong())).thenReturn(Collections.emptyList());

        //ejecucion
        List<Usuario> amigosObtenidos = controladorMovimiento.obtenerAmigos(httpServletRequestMock);

        //validacion
        assertThat(amigosObtenidos, IsIterableWithSize.iterableWithSize(0));
    }

    @Test
    public void queAlQuererObtenerAmigosLanceExcepcionSiNoSePuedeEstablecerConexionConLaBaseDeDatos() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        //preparacion
        when(httpServletRequestMock.getSession(false)).thenReturn(httpSessionMock);
        when(httpSessionMock.getAttribute("idUsuario")).thenReturn(1L); // Añade un usuario ficticio
        ExcepcionBaseDeDatos excepcion = new ExcepcionBaseDeDatos("Base de datos no disponible");
        doThrow(excepcion).when(servicioUsuario).obtenerAmigosDeUnUsuario(anyLong());

        //ejecucion y validacion
        ExcepcionBaseDeDatos excepcionBaseDeDatos = assertThrows(ExcepcionBaseDeDatos.class, ()->{
            controladorMovimiento.obtenerAmigos(httpServletRequestMock);
        });
        assertThat(excepcionBaseDeDatos.getMessage(), equalToIgnoringCase("Base de datos no disponible"));
    }

    @Test
    public void queAlQuererObtenerAmigosLanceExcepcionUsuarioInexistenteSiNoExisteUsuario() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        //preparacion
        when(httpServletRequestMock.getSession(false)).thenReturn(httpSessionMock);
        UsuarioInexistente excepcion = new UsuarioInexistente();
        doThrow(excepcion).when(servicioUsuario).obtenerAmigosDeUnUsuario(anyLong());

        //ejecucion y validacion
        assertThrows(UsuarioInexistente.class, ()->{
            controladorMovimiento.obtenerAmigos(httpServletRequestMock);
        });
    }

    //METODOS PRIVADOS
    private List<Movimiento> generarMovimientos(int cantidadDeMovimientos) {
        List<Movimiento> movimientos = new ArrayList<>();
        Usuario usuarioMock = mock(Usuario.class);
        CategoriaMovimiento categoriaMock = mock(CategoriaMovimiento.class);
        for(int i = 0; i < cantidadDeMovimientos; i++) {
            movimientos.add(new Movimiento("Descripcion" + i, i + 0.0, LocalDate.now(), categoriaMock, usuarioMock));
        }
        return movimientos;
    }

}

