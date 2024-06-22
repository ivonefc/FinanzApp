package com.tallerwebi.dominio.exportar;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionExportacionDeArchivo;
import com.tallerwebi.dominio.exportar.estrategias.EstrategiaDeExportacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

public class ServicioDeExportacionTest {
    ServicioDeExportacion servicioDeExportacion;
    EstrategiaDeExportacion estrategiaDeExportacionMock;

    @BeforeEach
    public void init(){
        estrategiaDeExportacionMock =  mock(EstrategiaDeExportacion.class);
        List<EstrategiaDeExportacion> estrategiaDeExportacionList = List.of(estrategiaDeExportacionMock);
        servicioDeExportacion = new ServicioDeExportacionImpl(estrategiaDeExportacionList);
    }

    @Test
    public void queAlSolicitarGenerarArchivoRetorneUnArrayDeBytes() throws ExcepcionExportacionDeArchivo, ExcepcionBaseDeDatos{
        //preparacion
        Long idUsuario = 1L;
        byte[] archivoEnBytes = "contenido".getBytes();
        Map<TipoDeArchivo, EstrategiaDeExportacion> estrategiaDeExportacionMap = servicioDeExportacion.obtenerEstrategiasDeExportacion();
        TipoDeArchivo tipoDeArchivo = TipoDeArchivo.PDF;
        estrategiaDeExportacionMap.put(tipoDeArchivo, estrategiaDeExportacionMock);
        when(estrategiaDeExportacionMap.get(TipoDeArchivo.PDF).generarArchivo(idUsuario)).thenReturn(archivoEnBytes);

        //ejecucion
        byte[] resultado = servicioDeExportacion.generarArchivo(idUsuario, tipoDeArchivo);

        //validacion
        assertThat(resultado, equalTo(archivoEnBytes));
        verify(estrategiaDeExportacionMock, times(1)).generarArchivo(idUsuario);
    }
}
