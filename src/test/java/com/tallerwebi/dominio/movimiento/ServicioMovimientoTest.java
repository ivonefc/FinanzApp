package com.tallerwebi.dominio.movimiento;

import com.tallerwebi.dominio.categoria.CategoriaMovimiento;
import com.tallerwebi.dominio.categoria.RepositorioCategoria;
import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionCamposInvalidos;
import com.tallerwebi.dominio.excepcion.ExcepcionMovimientoNoEncontrado;
import com.tallerwebi.dominio.meta.Meta;
import com.tallerwebi.dominio.meta.RepositorioMeta;
import com.tallerwebi.dominio.tipo.TipoMovimiento;
import com.tallerwebi.dominio.usuario.RepositorioUsuario;
import com.tallerwebi.dominio.usuario.Usuario;
import com.tallerwebi.presentacion.movimiento.DatosAgregarMovimiento;
import com.tallerwebi.presentacion.movimiento.DatosEditarMovimiento;
import org.hamcrest.collection.IsMapWithSize;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

public class ServicioMovimientoTest {
    ServicioMovimiento servicioMovimiento;
    RepositorioMovimiento repositorioMovimientoMock;
    RepositorioCategoria repositorioCategoriaMock;
    RepositorioUsuario repositorioUsuarioMock;
    HttpServletRequest httpServletRequestMock;
    RepositorioMeta repositorioMetaMock;
    HttpSession httpSessionMock;
    Usuario usuarioMock;
    CategoriaMovimiento categoriaMock;

     @BeforeEach
    public void init(){
         repositorioMovimientoMock = mock(RepositorioMovimiento.class);
         repositorioCategoriaMock = mock(RepositorioCategoria.class);
         repositorioUsuarioMock = mock(RepositorioUsuario.class);
         repositorioMetaMock = mock(RepositorioMeta.class);
         servicioMovimiento = new ServicioMovimientoImpl(repositorioMovimientoMock, repositorioCategoriaMock, repositorioUsuarioMock, repositorioMetaMock);
         httpServletRequestMock = mock(HttpServletRequest.class);
         httpSessionMock = mock(HttpSession.class);
         usuarioMock = mock(Usuario.class);
         categoriaMock = mock(CategoriaMovimiento.class);
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
    public void queAlSolicitarAlServicioActualizarYNoSeIngreseNingunDatoNoSePuedaActualizar() throws ExcepcionBaseDeDatos, ExcepcionCamposInvalidos, ExcepcionMovimientoNoEncontrado {
        // Preparación
        DatosEditarMovimiento datosActualizar = new DatosEditarMovimiento();
        datosActualizar.setDescripcion(null);
        datosActualizar.setTipo("");
        datosActualizar.setCategoria(null);
        datosActualizar.setMonto(null);

        // Mocks para los repositorios
        RepositorioMovimiento repositorioMovimientoMock = mock(RepositorioMovimiento.class);
        RepositorioCategoria repositorioCategoriaMock = mock(RepositorioCategoria.class);
        RepositorioUsuario repositorioUsuarioMock = mock(RepositorioUsuario.class);

        // Usamos la implementación real del servicio con repositorios mock
        ServicioMovimiento servicioMovimientoReal = new ServicioMovimientoImpl(repositorioMovimientoMock, repositorioCategoriaMock, repositorioUsuarioMock);

        // Ejecución y validación
        ExcepcionCamposInvalidos thrown = assertThrows(ExcepcionCamposInvalidos.class, () -> servicioMovimientoReal.actualizarMovimiento(datosActualizar));
        assertThat(thrown.getErrores(), IsMapWithSize.aMapWithSize(4));
        assertThat(thrown.getErrores(), hasEntry("descripcion", "El campo es requerido"));
        assertThat(thrown.getErrores(), hasEntry("tipo", "El campo es requerido"));
        assertThat(thrown.getErrores(), hasEntry("categoria", "El campo es requerido"));
        assertThat(thrown.getErrores(), hasEntry("monto", "El campo es requerido"));
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
        Movimiento movimientoMock = mock(Movimiento.class);
        when(repositorioMovimientoMock.obtenerMovimientoPorId(anyLong())).thenReturn(movimientoMock);
        doThrow(ExcepcionBaseDeDatos.class).when(repositorioMovimientoMock).eliminarMovimiento(movimientoMock);

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
    public void queAlSolicitarAlServicioNuevoMovimientoLanceExcepcionCamposInvalidos() throws ExcepcionCamposInvalidos, ExcepcionBaseDeDatos {
        // Preparación
        DatosAgregarMovimiento datosAgregarMovimiento = new DatosAgregarMovimiento();
        datosAgregarMovimiento.setDescripcion("");
        datosAgregarMovimiento.setMonto(0.0);
        datosAgregarMovimiento.setCategoria("");
        datosAgregarMovimiento.setTipo("");

        // Mocks para los repositorios
        RepositorioMovimiento repositorioMovimientoMock = mock(RepositorioMovimiento.class);
        RepositorioCategoria repositorioCategoriaMock = mock(RepositorioCategoria.class);
        RepositorioUsuario repositorioUsuarioMock = mock(RepositorioUsuario.class);

        // Usamos la implementación real del servicio con repositorios mock
        ServicioMovimiento servicioMovimientoReal = new ServicioMovimientoImpl(repositorioMovimientoMock, repositorioCategoriaMock, repositorioUsuarioMock);

        // Ejecución y validación
        ExcepcionCamposInvalidos thrown = assertThrows(ExcepcionCamposInvalidos.class, () -> servicioMovimientoReal.nuevoMovimiento(1L, datosAgregarMovimiento));
        assertThat(thrown.getErrores(), IsMapWithSize.aMapWithSize(4));
        assertThat(thrown.getErrores(), hasEntry("descripcion", "El campo es requerido"));
        assertThat(thrown.getErrores(), hasEntry("monto", "El campo es requerido"));
        assertThat(thrown.getErrores(), hasEntry("categoria", "El campo es requerido"));
        assertThat(thrown.getErrores(), hasEntry("tipo", "El campo es requerido"));
    }

    //Testeando el método calcularCantidadDePaginas para la paginación.
    @Test
    public void queAlSolicitarLaCantidadDePaginasDevuelvaLaDivisionEnteraEntreLaCantidadDeMovimientosYElTamanioDePagina() throws ExcepcionBaseDeDatos{
        //preparacion
        when(repositorioMovimientoMock.obtenerCantidadDeMovimientosPorId(anyLong())).thenReturn(20L);
        when(repositorioUsuarioMock.obtenerUsuarioPorId(anyLong())).thenReturn(usuarioMock);
        //ejecucion
        int cantidadDePaginas = servicioMovimiento.calcularCantidadDePaginas(1L,5);

        //validacion
        assertThat(cantidadDePaginas, equalTo(4));
    }

    @Test
    public void queAlSolicitarLaCantidadDePaginasCuandoLaCantidadDeMovimientosSeaCeroDevuelvaCero() throws ExcepcionBaseDeDatos {
        //preparacion
        when(repositorioMovimientoMock.obtenerCantidadDeMovimientosPorId(anyLong())).thenReturn(0L);
        when(repositorioUsuarioMock.obtenerUsuarioPorId(anyLong())).thenReturn(usuarioMock);
        //ejecucion
        int cantidadDePaginas = servicioMovimiento.calcularCantidadDePaginas(1L,5);

        //validacion
        assertThat(cantidadDePaginas, equalTo(0));
    }

    //Testeando método obtenerMovimientosPorPagina() (paginación)
    @Test
    public void queAlSolicitarMovimientosDeUnaPaginaDevuelvaUnaListaDeMovimientos() throws ExcepcionBaseDeDatos {
         //preparación
        List<Movimiento> movimientos = generarMovimientos(10);
        when(repositorioMovimientoMock.obtenerMovimientosPorPagina(anyLong(), anyInt(), anyInt())).thenReturn(movimientos);
        when(repositorioUsuarioMock.obtenerUsuarioPorId(anyLong())).thenReturn(usuarioMock);


        //ejecución
        List <Movimiento> movimientosObtenidos = servicioMovimiento.obtenerMovimientosPorPagina(1L, 1, 10);

        //validacion
        assertThat(movimientosObtenidos, not(empty()));
        assertThat(movimientosObtenidos, hasSize(10));
    }
    @Test
    public void queAlSolicitarAlServicioUnaListaDeMovimientosPorPaginaDevuelvaUnaListaVacia() throws ExcepcionBaseDeDatos {
        //preparacion
        when(repositorioMovimientoMock.obtenerMovimientosPorPagina(anyLong(), anyInt(), anyInt())).thenReturn(Collections.emptyList());

        //ejecucion
        List<Movimiento> movimientos = servicioMovimiento.obtenerMovimientosPorPagina(1L, 1, 5);

        //validacion
        assertThat(movimientos, notNullValue());
        assertThat(movimientos, empty());
        assertThat(movimientos, hasSize(0));
    }

    @Test
    public void queAlSolicitarAlServicioObtenerMovimientosPorPaginaLanceExcepcionBaseDeDatos() throws ExcepcionBaseDeDatos {
        //preparacion
        when(repositorioMovimientoMock.obtenerMovimientosPorPagina(anyLong(), anyInt(), anyInt())).thenThrow(ExcepcionBaseDeDatos.class);

        //ejecucion y validacion
        assertThrows(ExcepcionBaseDeDatos.class, () -> servicioMovimiento.obtenerMovimientosPorPagina(1L, 1, 5));
    }

    //Testeando metodo para obtener un Map con las categorias y los montos totales gastados para la misma en el mes y año actual.
    @Test
    public void queAlSolicitarElTotalGastadoEnCategoriasConMetasDevuelvaUnMapaConLosDatos() throws ExcepcionBaseDeDatos {
         //preparacion
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        CategoriaMovimiento categoria1 = new CategoriaMovimiento("Categoria 1", new TipoMovimiento("Egreso"));
        categoria1.setId(1L);
        CategoriaMovimiento categoria2 = new CategoriaMovimiento("Categoria 2", new TipoMovimiento("Egreso"));
        categoria2.setId(2L);

        Meta meta1 = new Meta(usuario, categoria1, 100.0);
        Meta meta2 = new Meta(usuario, categoria2, 200.0);

        List<Meta> metas = Arrays.asList(meta1, meta2);

        LocalDate fechaActual = LocalDate.now();
        int mes = fechaActual.getMonthValue();
        int anio = fechaActual.getYear();

        when(repositorioMetaMock.obtenerMetas(usuario.getId())).thenReturn(metas);
        when(repositorioMovimientoMock.obtenerTotalPorCategoriaEnMesYAnioActual(categoria1.getId(), mes, anio)).thenReturn(50.0);
        when(repositorioMovimientoMock.obtenerTotalPorCategoriaEnMesYAnioActual(categoria2.getId(), mes, anio)).thenReturn(60.0);

        //ejecucion
        Map<String, Double> totalesGastadosPorCategoria = servicioMovimiento.obtenerTotalGastadoEnCategoriasConMetas(usuario.getId());

        //validacion
        assertThat(totalesGastadosPorCategoria, is(notNullValue()));
        assertThat(totalesGastadosPorCategoria.size(), is(2));
        assertThat(totalesGastadosPorCategoria, hasEntry("Categoria 1", 50.0));
        assertThat(totalesGastadosPorCategoria, hasEntry("Categoria 2", 60.0));
     }


    //METODOS PRIVADOS


    private List<Movimiento> generarMovimientos(int cantidadDeMovimientos) {
         List<Movimiento> movimientos = new ArrayList<>();
         for(int i = 0; i < cantidadDeMovimientos; i++) {
             movimientos.add(new Movimiento("Descripcion" + i, i + 0.0, LocalDate.now(), categoriaMock, usuarioMock));
         }
         return movimientos;
    }
}
