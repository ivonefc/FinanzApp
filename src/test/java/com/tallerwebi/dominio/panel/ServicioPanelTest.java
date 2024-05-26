package com.tallerwebi.dominio.panel;

import com.tallerwebi.dominio.categoria.CategoriaMovimiento;
import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.movimiento.Movimiento;
import com.tallerwebi.dominio.movimiento.ServicioMovimiento;
import com.tallerwebi.dominio.tipo.TipoMovimiento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServicioPanelTest {

    ServicioPanel servicioPanel;
    HttpServletRequest httpServletRequestMock;
    HttpSession httpSessionMock;

    @BeforeEach
    public void init() {
        servicioPanel = new ServicioPanelImpl();
        httpServletRequestMock = mock(HttpServletRequest.class);
        httpSessionMock = mock(HttpSession.class);
    }

    @Test
    public void queAlSolicitarObtenerMovimientosEgresosPorUsuarioLosDevuelva() throws ExcepcionBaseDeDatos {
        // preparacion
        Long idUsuario = 1L;
        ServicioMovimiento servicioMovimientoMock = mock(ServicioMovimiento.class);
        List<Movimiento> movimientosEgresos = new ArrayList<>();
        Movimiento movimiento1 = new Movimiento();
        movimiento1.setCategoria(new CategoriaMovimiento());
        movimiento1.getCategoria().setTipo(new TipoMovimiento());
        movimiento1.getCategoria().getTipo().setNombre("EGRESO");
        movimientosEgresos.add(movimiento1);

        Movimiento movimiento2 = new Movimiento();
        movimiento2.setCategoria(new CategoriaMovimiento());
        movimiento2.getCategoria().setTipo(new TipoMovimiento());
        movimiento2.getCategoria().getTipo().setNombre("EGRESO");
        movimientosEgresos.add(movimiento2);
        when(servicioMovimientoMock.obtenerMovimientos(idUsuario)).thenReturn(movimientosEgresos);
        ServicioPanel servicioPanel = new ServicioPanelImpl(servicioMovimientoMock);

        // ejecucion
        List<Movimiento> result = servicioPanel.obtenerMovimientosEgresosPorUsuario(idUsuario);

        // validacion
        assertEquals(movimientosEgresos, result);
        assertEquals(2, result.size());
        assertFalse(result.isEmpty());

        // Verificar que cada movimiento en la lista resultante es un movimiento de egreso
        for (Movimiento movimiento : result) {
            assertEquals("EGRESO", movimiento.getCategoria().getTipo().getNombre());
        }
    }

    @Test
    public void queAlSolicitarObtenerMovimientosEgresosPorUsuarioConMovimientosDeDiferentesTiposDevuelvaSoloLosDeTipoEgreso() throws ExcepcionBaseDeDatos {
        // preparacion
        Long idUsuario = 1L;
        ServicioMovimiento servicioMovimientoMock = mock(ServicioMovimiento.class);
        List<Movimiento> movimientos = new ArrayList<>();
        Movimiento movimiento1 = new Movimiento();
        movimiento1.setCategoria(new CategoriaMovimiento());
        movimiento1.getCategoria().setTipo(new TipoMovimiento());
        movimiento1.getCategoria().getTipo().setNombre("EGRESO");
        movimientos.add(movimiento1);

        Movimiento movimiento2 = new Movimiento();
        movimiento2.setCategoria(new CategoriaMovimiento());
        movimiento2.getCategoria().setTipo(new TipoMovimiento());
        movimiento2.getCategoria().getTipo().setNombre("INGRESO");
        movimientos.add(movimiento2);
        when(servicioMovimientoMock.obtenerMovimientos(idUsuario)).thenReturn(movimientos);
        ServicioPanel servicioPanel = new ServicioPanelImpl(servicioMovimientoMock);

        // ejecucion
        List<Movimiento> result = servicioPanel.obtenerMovimientosEgresosPorUsuario(idUsuario);

        // validacion
        assertEquals(1, result.size());
        assertFalse(result.isEmpty());

        // Verificar que cada movimiento en la lista resultante es un movimiento de egreso
        for (Movimiento movimiento : result) {
            assertEquals("EGRESO", movimiento.getCategoria().getTipo().getNombre());
        }
    }

    @Test
    public void queAlSolicitarObtenerMovimientosEgresosDevuelvaUnaListaVacia() throws ExcepcionBaseDeDatos {
        // preparacion
        Long idUsuario = 1L;
        ServicioMovimiento servicioMovimientoMock = mock(ServicioMovimiento.class);
        List<Movimiento> movimientosEgresos = new ArrayList<>();
        when(servicioMovimientoMock.obtenerMovimientos(idUsuario)).thenReturn(movimientosEgresos);
        ServicioPanel servicioPanel = new ServicioPanelImpl(servicioMovimientoMock);

        // ejecucion
        List<Movimiento> result = servicioPanel.obtenerMovimientosEgresosPorUsuario(idUsuario);

        // validacion
        assertEquals(movimientosEgresos, result);
        assertEquals(0, result.size());
        assertTrue(result.isEmpty());
    }

    @Test
    public void queAlSolicitarObtenerMovimientosEgresosLanceExcepcionBaseDeDatos() throws ExcepcionBaseDeDatos {
        // preparacion
        Long idUsuario = 1L;
        ServicioMovimiento servicioMovimientoMock = mock(ServicioMovimiento.class);
        when(servicioMovimientoMock.obtenerMovimientos(idUsuario)).thenThrow(new ExcepcionBaseDeDatos("Error en la base de datos"));
        ServicioPanel servicioPanel = new ServicioPanelImpl(servicioMovimientoMock);

        // ejecucion
        try {
            servicioPanel.obtenerMovimientosEgresosPorUsuario(idUsuario);
        } catch (ExcepcionBaseDeDatos e) {
            // validacion
            assertEquals("Error en la base de datos", e.getMessage());
        }
    }

    @Test
    public void queAlSolicitarObtenerMovimientosIngresosPorUsuarioLosDevuelva() throws ExcepcionBaseDeDatos {
        // preparacion
        Long idUsuario = 1L;
        ServicioMovimiento servicioMovimientoMock = mock(ServicioMovimiento.class);
        List<Movimiento> movimientosIngresos = new ArrayList<>();
        Movimiento movimiento1 = new Movimiento();
        movimiento1.setCategoria(new CategoriaMovimiento());
        movimiento1.getCategoria().setTipo(new TipoMovimiento());
        movimiento1.getCategoria().getTipo().setNombre("INGRESO");
        movimientosIngresos.add(movimiento1);

        Movimiento movimiento2 = new Movimiento();
        movimiento2.setCategoria(new CategoriaMovimiento());
        movimiento2.getCategoria().setTipo(new TipoMovimiento());
        movimiento2.getCategoria().getTipo().setNombre("INGRESO");
        movimientosIngresos.add(movimiento2);
        when(servicioMovimientoMock.obtenerMovimientos(idUsuario)).thenReturn(movimientosIngresos);
        ServicioPanel servicioPanel = new ServicioPanelImpl(servicioMovimientoMock);

        // ejecucion
        List<Movimiento> result = servicioPanel.obtenerMovimientosIngresosPorUsuario(idUsuario);

        // validacion
        assertEquals(movimientosIngresos, result);
        assertEquals(2, result.size());
        assertFalse(result.isEmpty());

        // Verificar que cada movimiento en la lista resultante es un movimiento de ingreso
        for (Movimiento movimiento : result) {
            assertEquals("INGRESO", movimiento.getCategoria().getTipo().getNombre());
        }
    }

    @Test
    public void queAlSolicitarObtenerMovimientosIngresosDevuelvaUnaListaVacia() throws ExcepcionBaseDeDatos {
        // preparacion
        Long idUsuario = 1L;
        ServicioMovimiento servicioMovimientoMock = mock(ServicioMovimiento.class);
        List<Movimiento> movimientosIngresos = new ArrayList<>();
        when(servicioMovimientoMock.obtenerMovimientos(idUsuario)).thenReturn(movimientosIngresos);
        ServicioPanel servicioPanel = new ServicioPanelImpl(servicioMovimientoMock);

        // ejecucion
        List<Movimiento> result = servicioPanel.obtenerMovimientosIngresosPorUsuario(idUsuario);

        // validacion
        assertEquals(movimientosIngresos, result);
        assertEquals(0, result.size());
        assertTrue(result.isEmpty());
    }

    @Test
    public void queAlSolicitarObtenerMovimientosIngresosLanceExcepcionBaseDeDatos() throws ExcepcionBaseDeDatos {
        // preparacion
        Long idUsuario = 1L;
        ServicioMovimiento servicioMovimientoMock = mock(ServicioMovimiento.class);
        when(servicioMovimientoMock.obtenerMovimientos(idUsuario)).thenThrow(new ExcepcionBaseDeDatos("Error en la base de datos"));
        ServicioPanel servicioPanel = new ServicioPanelImpl(servicioMovimientoMock);

        // ejecucion
        try {
            servicioPanel.obtenerMovimientosIngresosPorUsuario(idUsuario);
        } catch (ExcepcionBaseDeDatos e) {
            // validacion
            assertEquals("Error en la base de datos", e.getMessage());
        }
    }

    @Test
    public void queAlSolicitarObtenerMovimientosIngresosPorUsuarioConMovimientosDeDiferentesTiposDevuelvaSoloLosDeTipoIngreso() throws ExcepcionBaseDeDatos {
        // preparacion
        Long idUsuario = 1L;
        ServicioMovimiento servicioMovimientoMock = mock(ServicioMovimiento.class);
        List<Movimiento> movimientos = new ArrayList<>();
        Movimiento movimiento1 = new Movimiento();
        movimiento1.setCategoria(new CategoriaMovimiento());
        movimiento1.getCategoria().setTipo(new TipoMovimiento());
        movimiento1.getCategoria().getTipo().setNombre("EGRESO");
        movimientos.add(movimiento1);

        Movimiento movimiento2 = new Movimiento();
        movimiento2.setCategoria(new CategoriaMovimiento());
        movimiento2.getCategoria().setTipo(new TipoMovimiento());
        movimiento2.getCategoria().getTipo().setNombre("INGRESO");
        movimientos.add(movimiento2);
        when(servicioMovimientoMock.obtenerMovimientos(idUsuario)).thenReturn(movimientos);
        ServicioPanel servicioPanel = new ServicioPanelImpl(servicioMovimientoMock);

        // ejecucion
        List<Movimiento> result = servicioPanel.obtenerMovimientosIngresosPorUsuario(idUsuario);

        // validacion
        assertEquals(1, result.size());
        assertFalse(result.isEmpty());

        // Verificar que cada movimiento en la lista resultante es un movimiento de ingreso
        for (Movimiento movimiento : result) {
            assertEquals("INGRESO", movimiento.getCategoria().getTipo().getNombre());
        }
    }

}
