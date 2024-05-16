package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.movimiento.Movimiento;
import com.tallerwebi.dominio.movimiento.RepositorioMovimiento;
import com.tallerwebi.dominio.movimiento.ServicioMovimiento;
import com.tallerwebi.dominio.movimiento.ServicioMovimientoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServicioMovimientoTest {
    ServicioMovimiento servicioMovimiento;
    RepositorioMovimiento repositorioMovimientoMock;
    HttpServletRequest httpServletRequestMock;

     @BeforeEach
    public void init(){
         repositorioMovimientoMock = mock(RepositorioMovimiento.class);
         servicioMovimiento = new ServicioMovimientoImpl(repositorioMovimientoMock);
         httpServletRequestMock = mock(HttpServletRequest.class);
     }

     @Test
    public void queAlSolicitarAlServicioObtenerMovimientosDevuelvaUnaListaDeMovimientos() throws ExcepcionBaseDeDatos {
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

}
