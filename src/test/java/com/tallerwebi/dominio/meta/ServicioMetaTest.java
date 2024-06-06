package com.tallerwebi.dominio.meta;

import com.tallerwebi.dominio.categoria.CategoriaMovimiento;
import com.tallerwebi.dominio.excepcion.*;
import com.tallerwebi.dominio.usuario.RepositorioUsuario;
import com.tallerwebi.dominio.categoria.RepositorioCategoria;
import com.tallerwebi.dominio.usuario.Usuario;
import com.tallerwebi.presentacion.meta.DatosEditarMeta;
import com.tallerwebi.presentacion.meta.DatosMeta;
import org.hamcrest.collection.IsMapWithSize;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ServicioMetaTest {

    ServicioMeta servicioMeta;
    RepositorioMeta repositorioMetaMock;
    RepositorioCategoria repositorioCategoriaMock;
    RepositorioUsuario repositorioUsuarioMock;
    Usuario usuarioMock;
    CategoriaMovimiento categoriaMock;


    @BeforeEach
    public void init() {
        repositorioMetaMock = mock(RepositorioMeta.class);
        repositorioCategoriaMock = mock(RepositorioCategoria.class);
        repositorioUsuarioMock = mock(RepositorioUsuario.class);
        servicioMeta = new ServicioMetaImpl(repositorioMetaMock, repositorioCategoriaMock, repositorioUsuarioMock);
        usuarioMock = mock(Usuario.class);
        categoriaMock = mock(CategoriaMovimiento.class);
    }

    @Test
    public void queAlSolicitarAlServicioGuardarMetaGuardeLaMetaCorrectamente() throws ExcepcionCamposInvalidos, ExcepcionBaseDeDatos, ExcepcionCategoriaConMetaExistente, UsuarioInexistente {
        // preparacion
        DatosMeta datosMeta = new DatosMeta();
        datosMeta.setCategoria("Comida");
        datosMeta.setMonto(1000.0);
        Long idUsuario = 1L;

        // ejecucion
        servicioMeta.guardarMeta(idUsuario, datosMeta);

        // validacion
        verify(repositorioMetaMock, times(1)).guardar(any(Meta.class));
    }

    @Test
    public void queAlSolicitarAlServicioGuardarMetaLanceExcepcionCamposInvalidosSiNoSeEnviaCategoria() throws ExcepcionBaseDeDatos, ExcepcionCategoriaConMetaExistente, ExcepcionCamposInvalidos, UsuarioInexistente {
        // preparacion
        DatosMeta datosMeta = new DatosMeta();
        Long idUsuario = 1L;

        Map<String, String> errores = new HashMap<>();
        errores.put("categoria", "El campo es requerido");
        errores.put("monto", "El campo es requerido");
        ExcepcionCamposInvalidos excepcion = new ExcepcionCamposInvalidos(errores);

        when(repositorioCategoriaMock.obtenerCategoriaPorNombre(anyString())).thenReturn(null);
        when(repositorioUsuarioMock.obtenerUsuarioPorId(anyLong())).thenReturn(null);

        // ejecucion y validacion
        ExcepcionCamposInvalidos thrown = assertThrows(ExcepcionCamposInvalidos.class, () -> servicioMeta.guardarMeta(idUsuario, datosMeta));
        assertThat(thrown.getErrores(), IsMapWithSize.aMapWithSize(2));
        assertThat(thrown.getErrores(), hasEntry("categoria", "El campo es requerido"));
        assertThat(thrown.getErrores(), hasEntry("monto", "El campo es requerido"));
    }

    @Test
    public void queAlSolicitarAlServicioGuardarMetaLanceExcepcionCategoriaConMetaExistenteSiYaExisteUnaMetaConEsaCategoria() throws ExcepcionBaseDeDatos, ExcepcionCategoriaConMetaExistente {
        // preparacion
        DatosMeta datosMeta = new DatosMeta();
        datosMeta.setCategoria("Comida");
        datosMeta.setMonto(1000.0);
        Long idUsuario = 1L;

        doThrow(ExcepcionCategoriaConMetaExistente.class).when(repositorioMetaMock).existeMetaConUsuarioYCategoria(any(), any());

        // ejecucion y validacion
        assertThrows(ExcepcionCategoriaConMetaExistente.class, () -> servicioMeta.guardarMeta(idUsuario, datosMeta));
    }

    @Test
    public void queAlSolicitarAlServicioGuardarMetaLanceExcepcionBaseDeDatosSiOcurreUnErrorAlGuardarLaMeta() throws ExcepcionBaseDeDatos {
        // preparacion
        DatosMeta datosMeta = new DatosMeta();
        datosMeta.setCategoria("Comida");
        datosMeta.setMonto(1000.0);
        Long idUsuario = 1L;

        doThrow(ExcepcionBaseDeDatos.class).when(repositorioMetaMock).guardar(any());

        // ejecucion y validacion
        assertThrows(ExcepcionBaseDeDatos.class, () -> servicioMeta.guardarMeta(idUsuario, datosMeta));
    }

    @Test
    public void queAlSolicitarAlServicioGuardarMetaLanceExcepcionBaseDeDatosSiOcurreUnErrorAlConsultarSiExisteMetaConUsuarioYCategoria() throws ExcepcionBaseDeDatos, ExcepcionCategoriaConMetaExistente {
        // preparacion
        DatosMeta datosMeta = new DatosMeta();
        datosMeta.setCategoria("Comida");
        datosMeta.setMonto(1000.0);
        Long idUsuario = 1L;

        doThrow(ExcepcionBaseDeDatos.class).when(repositorioMetaMock).existeMetaConUsuarioYCategoria(any(), any());

        // ejecucion y validacion
        assertThrows(ExcepcionBaseDeDatos.class, () -> servicioMeta.guardarMeta(idUsuario, datosMeta));
    }

    @Test
    public void queAlSolicitarAlServicioGuardarMetaLanceExcepcionBaseDeDatosSiOcurreUnErrorAlConsultarCategoriaPorNombre() throws ExcepcionBaseDeDatos {
        // preparacion
        DatosMeta datosMeta = new DatosMeta();
        datosMeta.setCategoria("Comida");
        datosMeta.setMonto(1000.0);
        Long idUsuario = 1L;

        doThrow(ExcepcionBaseDeDatos.class).when(repositorioCategoriaMock).obtenerCategoriaPorNombre(any());

        // ejecucion y validacion
        assertThrows(ExcepcionBaseDeDatos.class, () -> servicioMeta.guardarMeta(idUsuario, datosMeta));
    }

    @Test
    public void queAlSolicitarAlServicioGuardarMetaLanceExcepcionBaseDeDatosSiOcurreUnErrorAlConsultarUsuarioPorId() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        // preparacion
        DatosMeta datosMeta = new DatosMeta();
        datosMeta.setCategoria("Comida");
        datosMeta.setMonto(1000.0);
        Long idUsuario = 1L;

        doThrow(ExcepcionBaseDeDatos.class).when(repositorioUsuarioMock).obtenerUsuarioPorId(any());

        // ejecucion y validacion
        assertThrows(ExcepcionBaseDeDatos.class, () -> servicioMeta.guardarMeta(idUsuario, datosMeta));
    }

    @Test
    public void queAlSolicitarAlServicioObtenerMetaPorIdObtengaLaMetaCorrectamente() throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente {
        // preparacion
        Long idMeta = 1L;

        // ejecucion
        servicioMeta.obtenerMetaPorId(idMeta);

        // validacion
        verify(repositorioMetaMock, times(1)).obtenerMetaPorId(idMeta);
    }

    @Test
    public void queAlSolicitarAlServicioActualizarMetaActualiceLaMetaCorrectamente() throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente, ExcepcionCamposInvalidos {
        // preparacion
        Meta metaMock = mock(Meta.class);
        when(metaMock.getCategoriaMovimiento()).thenReturn(categoriaMock);
        when(metaMock.getMontoMeta()).thenReturn(1000.0);
        when(metaMock.getId()).thenReturn(1L);
        when(repositorioMetaMock.obtenerMetaPorId(anyLong())).thenReturn(metaMock);

        // Asegúrate de que la categoría exista en la base de datos
        when(repositorioCategoriaMock.obtenerCategoriaPorNombre(anyString())).thenReturn(categoriaMock);

        DatosEditarMeta datosEditarMeta = new DatosEditarMeta();
        datosEditarMeta.setId(1L);
        datosEditarMeta.setCategoria("RESTAURANTE");
        datosEditarMeta.setMontoMeta(10000.0);

        // ejecucion
        servicioMeta.actualizarMeta(datosEditarMeta);

        // validacion
        verify(metaMock, times(1)).setCategoriaMovimiento(any());
        verify(metaMock, times(1)).setMontoMeta(anyDouble());
        verify(repositorioMetaMock, times(1)).actualizarMeta(metaMock);
    }

    @Test
    public void queAlSolicitarAlServicioActualizarMetaLanceExcepcionCamposInvalidosSiNoSeEnvianDatos() {
        // preparacion
        DatosEditarMeta datosEditarMeta = new DatosEditarMeta();
        Map<String, String> erroresEsperados = new HashMap<>();
        erroresEsperados.put("categoria", "El campo es requerido");
        erroresEsperados.put("monto", "El campo es requerido");

        // ejecucion y validacion
        ExcepcionCamposInvalidos excepcion = assertThrows(ExcepcionCamposInvalidos.class, () -> servicioMeta.actualizarMeta(datosEditarMeta));
        assertEquals(erroresEsperados, excepcion.getErrores());
    }

    @Test
    public void queAlSolicitarAlServicioActualizarMetaLanceExcepcionMetaNoExistenteSiNoExisteLaMeta() throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente {
        // preparacion
        Meta metaMock = mock(Meta.class);
        when(metaMock.getCategoriaMovimiento()).thenReturn(categoriaMock);
        when(metaMock.getMontoMeta()).thenReturn(1000.0);
        when(metaMock.getId()).thenReturn(1L);
        when(repositorioMetaMock.obtenerMetaPorId(anyLong())).thenReturn(metaMock);

        // Asegúrate de que la categoría exista en la base de datos
        when(repositorioCategoriaMock.obtenerCategoriaPorNombre(anyString())).thenReturn(categoriaMock);

        DatosEditarMeta datosEditarMeta = new DatosEditarMeta();
        datosEditarMeta.setId(1L);
        datosEditarMeta.setCategoria("RESTAURANTE");
        datosEditarMeta.setMontoMeta(10000.0);

        // Aquí hacemos que obtenerMetaPorId lance ExcepcionMetaNoExistente
        when(repositorioMetaMock.obtenerMetaPorId(anyLong())).thenThrow(new ExcepcionMetaNoExistente());

        // ejecucion y validacion
        assertThrows(ExcepcionMetaNoExistente.class, () -> servicioMeta.actualizarMeta(datosEditarMeta));
    }

    @Test
    public void queAlSolicitarAlServicioActualizarMetaLanceExcepcionBaseDeDatosSiOcurreUnErrorAlConsultarCategoriaPorNombre() throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente {
        // preparacion
        Meta metaMock = mock(Meta.class);
        when(metaMock.getCategoriaMovimiento()).thenReturn(categoriaMock);
        when(metaMock.getMontoMeta()).thenReturn(1000.0);
        when(metaMock.getId()).thenReturn(1L);
        when(repositorioMetaMock.obtenerMetaPorId(anyLong())).thenReturn(metaMock);

        // Aquí hacemos que obtenerMetaPorId lance ExcepcionMetaNoExistente
        when(repositorioCategoriaMock.obtenerCategoriaPorNombre(anyString())).thenThrow(new ExcepcionBaseDeDatos());

        DatosEditarMeta datosEditarMeta = new DatosEditarMeta();
        datosEditarMeta.setId(1L);
        datosEditarMeta.setCategoria("RESTAURANTE");
        datosEditarMeta.setMontoMeta(10000.0);

        // ejecucion y validacion
        assertThrows(ExcepcionBaseDeDatos.class, () -> servicioMeta.actualizarMeta(datosEditarMeta));
    }

    @Test
    public void queAlSolicitarAlServicioActualizarMetaLanceExcepcionBaseDeDatosSiOcurreUnErrorAlActualizarLaMeta() throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente{
        // preparacion
        Meta metaMock = mock(Meta.class);
        when(metaMock.getCategoriaMovimiento()).thenReturn(categoriaMock);
        when(metaMock.getMontoMeta()).thenReturn(1000.0);
        when(metaMock.getId()).thenReturn(1L);
        when(repositorioMetaMock.obtenerMetaPorId(anyLong())).thenReturn(metaMock);

        // Asegúrate de que la categoría exista en la base de datos
        when(repositorioCategoriaMock.obtenerCategoriaPorNombre(anyString())).thenReturn(categoriaMock);

        DatosEditarMeta datosEditarMeta = new DatosEditarMeta();
        datosEditarMeta.setId(1L);
        datosEditarMeta.setCategoria("RESTAURANTE");
        datosEditarMeta.setMontoMeta(10000.0);

        doThrow(ExcepcionBaseDeDatos.class).when(repositorioMetaMock).actualizarMeta(any());

        // ejecucion y validacion
        assertThrows(ExcepcionBaseDeDatos.class, () -> servicioMeta.actualizarMeta(datosEditarMeta));
    }

    @Test
    public void queAlSolicitarAlServicioEliminarMetaElimineLaMetaCorrectamente() throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente {
        // preparacion
        Long idMeta = 1L;
        Meta metaMock = mock(Meta.class);
        when(repositorioMetaMock.obtenerMetaPorId(anyLong())).thenReturn(metaMock);

        // ejecucion
        servicioMeta.eliminarMeta(idMeta);

        // validacion
        verify(repositorioMetaMock).eliminarMeta(metaMock);
    }

    @Test
    public void queAlSolicitarAlServicioEliminarMetaLanceExcepcionMetaNoExistenteSiNoExisteLaMeta() throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente {
        // preparacion
        Long idMeta = 1L;

        // Aquí hacemos que obtenerMetaPorId lance ExcepcionMetaNoExistente
        when(repositorioMetaMock.obtenerMetaPorId(anyLong())).thenThrow(new ExcepcionMetaNoExistente());

        // ejecucion y validacion
        assertThrows(ExcepcionMetaNoExistente.class, () -> servicioMeta.eliminarMeta(idMeta));
        verify(repositorioMetaMock, never()).eliminarMeta(any());
    }

    @Test
    public void queAlSolicitarAlServicioEliminarMetaLanceExcepcionBaseDeDatosSiOcurreUnErrorAlEliminarLaMeta() throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente {
        // preparacion
        Long idMeta = 1L;
        Meta metaMock = mock(Meta.class);
        when(repositorioMetaMock.obtenerMetaPorId(anyLong())).thenReturn(metaMock);
        doThrow(ExcepcionBaseDeDatos.class).when(repositorioMetaMock).eliminarMeta(metaMock);

        // ejecucion y validacion
        assertThrows(ExcepcionBaseDeDatos.class, () -> servicioMeta.eliminarMeta(idMeta));
    }

    //Testeando el método obtenerMetas()

    @Test
    public void obtenerMetasQueAlSolicitarObtenerMetasDevuelvaUnaListaDeMetas() throws ExcepcionBaseDeDatos {
        // preparacion
        List<Meta> expectedMetas = new ArrayList<>();
        when(repositorioMetaMock.obtenerMetas(anyLong())).thenReturn(expectedMetas);

        // ejecucion
        List<Meta> actualMetas = servicioMeta.obtenerMetas(1L);

        // validacion
        verify(repositorioMetaMock, times(1)).obtenerMetas(1L);
        assertEquals(expectedMetas, actualMetas);
    }

    @Test
    public void obtenerMetasQueAlSolicitarObtenerMetasLanceUnaExcepcionDeBDDSiEstaNoEstaDisponible() throws ExcepcionBaseDeDatos {
        //preparacion
        when(repositorioMetaMock.obtenerMetas(anyLong())).thenThrow(ExcepcionBaseDeDatos.class);

        //ejecución y validación
        assertThrows(ExcepcionBaseDeDatos.class, () -> servicioMeta.obtenerMetas(1L));
        verify(repositorioMetaMock, times(1)).obtenerMetas(1L);
    }

    @Test
    public void obtenerMetasQueAlSolicitarObtenerMetasDevuelvaUnaListaVaciaSiNoSeEstablecieronMetas() throws ExcepcionBaseDeDatos {
        //preparacion
        when(repositorioMetaMock.obtenerMetas(anyLong())).thenReturn(new ArrayList<>());

        //ejecución
        List<Meta> actualMetas = servicioMeta.obtenerMetas(1L);

        //validación
        verify(repositorioMetaMock, times(1)).obtenerMetas(1L);
        assertTrue(actualMetas.isEmpty());
    }

}
