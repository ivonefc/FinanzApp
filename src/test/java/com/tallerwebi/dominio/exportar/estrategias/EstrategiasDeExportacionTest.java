package com.tallerwebi.dominio.exportar.estrategias;

import com.itextpdf.text.DocumentException;
import com.tallerwebi.dominio.categoria.CategoriaMovimiento;
import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionExportacionDeArchivo;
import com.tallerwebi.dominio.movimiento.Movimiento;
import com.tallerwebi.dominio.movimiento.ServicioMovimiento;
import com.tallerwebi.dominio.tipo.TipoMovimiento;
import com.tallerwebi.dominio.usuario.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class EstrategiasDeExportacionTest {
    ServicioMovimiento servicioMovimientoMock;
    EstrategiaDeExportacion estrategiaDeExportacion;

    @BeforeEach
    public void init(){
        servicioMovimientoMock = mock(ServicioMovimiento.class);
        estrategiaDeExportacion = new EstrategiaDeExportacionPDF(servicioMovimientoMock);
    }

    @Test
    public void queAlSolicitarGenerarArchivoPDFDevuelvaUnArrayDeBytes() throws ExcepcionBaseDeDatos, ExcepcionExportacionDeArchivo, DocumentException {
        //preparacion
        Long idUsuario = 1L;
        Usuario usuario = new Usuario();
        CategoriaMovimiento categoria = new CategoriaMovimiento("RESTAURANTE", new TipoMovimiento("EGRESO"));
        List<Movimiento> movimientos = List.of(
                new Movimiento("Descripcion", 100.0, LocalDate.now(), categoria, usuario)
        );
        when(servicioMovimientoMock.obtenerMovimientos(idUsuario)).thenReturn(movimientos);

        //ejecucion
        byte[] byteArray = estrategiaDeExportacion.generarArchivo(idUsuario);

        //validacion
        verify(servicioMovimientoMock, times(1)).obtenerMovimientos(idUsuario);
        assertThat(byteArray, notNullValue());
        assertThat(byteArray.length, greaterThan(0));
    }


    @Test
    public void queAlSolicitarGenerarArchivoPDFYNoExistanMovimientosLanceExcepcionDeExportacionDeArchivo() throws ExcepcionBaseDeDatos, ExcepcionExportacionDeArchivo, DocumentException {
        //preparacion
        Long idUsuario = 1L;
        Usuario usuario = new Usuario();
        CategoriaMovimiento categoria = new CategoriaMovimiento("RESTAURANTE", new TipoMovimiento("EGRESO"));
        List<Movimiento> movimientos = Collections.emptyList();
        when(servicioMovimientoMock.obtenerMovimientos(idUsuario)).thenReturn(movimientos);

        //ejecucion y validacion
        assertThrows(ExcepcionExportacionDeArchivo.class, () ->{
            estrategiaDeExportacion.generarArchivo(idUsuario);
        }, "No se pudo exportar archivo");
    }

}
