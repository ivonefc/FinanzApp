package com.tallerwebi.dominio.meta;

import com.tallerwebi.dominio.categoria.CategoriaMovimiento;
import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionCamposInvalidos;
import com.tallerwebi.dominio.excepcion.ExcepcionCategoriaConMetaExistente;
import com.tallerwebi.dominio.usuario.RepositorioUsuario;
import com.tallerwebi.dominio.categoria.RepositorioCategoria;
import com.tallerwebi.dominio.usuario.Usuario;
import com.tallerwebi.presentacion.meta.DatosMeta;
import org.hamcrest.collection.IsMapWithSize;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    public void queAlSolicitarAlServicioGuardarMetaGuardeLaMetaCorrectamente() throws ExcepcionCamposInvalidos, ExcepcionBaseDeDatos, ExcepcionCategoriaConMetaExistente {
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
    public void queAlSolicitarAlServicioGuardarMetaLanceExcepcionCamposInvalidosSiNoSeEnviaCategoria() throws ExcepcionBaseDeDatos, ExcepcionCategoriaConMetaExistente, ExcepcionCamposInvalidos {
        // preparacion
        DatosMeta datosMeta = new DatosMeta();
        Long idUsuario = 1L;
        ServicioMeta servicioMetaMock = mock(ServicioMeta.class);
        Map<String, String> errores = new HashMap<>();
        errores.put("categoria", "El campo es requerido");
        errores.put("monto", "El campo es requerido");
        ExcepcionCamposInvalidos excepcion = new ExcepcionCamposInvalidos(errores);
        doThrow(excepcion).when(servicioMetaMock).guardarMeta(anyLong(), eq(datosMeta));

        // ejecucion y validacion
        ExcepcionCamposInvalidos thrown = assertThrows(ExcepcionCamposInvalidos.class, () -> servicioMetaMock.guardarMeta(idUsuario, datosMeta));
        assertThat(thrown.getErrores(), IsMapWithSize.aMapWithSize(2));
        assertThat(thrown.getErrores(), hasEntry("categoria", "El campo es requerido"));
        assertThat(thrown.getErrores(), hasEntry("monto", "El campo es requerido"));
        verify(repositorioMetaMock, times(0)).guardar(any(Meta.class));
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
    public void queAlSolicitarAlServicioGuardarMetaLanceExcepcionBaseDeDatosSiOcurreUnErrorAlGuardarLaMeta() throws ExcepcionCamposInvalidos, ExcepcionBaseDeDatos {
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
    public void queAlSolicitarAlServicioGuardarMetaLanceExcepcionBaseDeDatosSiOcurreUnErrorAlConsultarCategoriaPorNombre() throws ExcepcionBaseDeDatos, ExcepcionCategoriaConMetaExistente {
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
    public void queAlSolicitarAlServicioGuardarMetaLanceExcepcionBaseDeDatosSiOcurreUnErrorAlConsultarUsuarioPorId() throws ExcepcionBaseDeDatos, ExcepcionCategoriaConMetaExistente {
        // preparacion
        DatosMeta datosMeta = new DatosMeta();
        datosMeta.setCategoria("Comida");
        datosMeta.setMonto(1000.0);
        Long idUsuario = 1L;

        doThrow(ExcepcionBaseDeDatos.class).when(repositorioUsuarioMock).obtenerUsuarioPorId(any());

        // ejecucion y validacion
        assertThrows(ExcepcionBaseDeDatos.class, () -> servicioMeta.guardarMeta(idUsuario, datosMeta));
    }

    //Testeando el método obtenerMetas()
    @Test
    public void obtenerMetasQueAlSolicitarObtenerMetasDevuelvaUnaListaDeMetas() throws ExcepcionBaseDeDatos {
        //preparacion
        List<Meta> metas = List.of(
                new Meta(usuarioMock, categoriaMock, 300.0),
                new Meta(usuarioMock, categoriaMock, 200.0)
        );
        when(repositorioMetaMock.obtenerMetas(anyLong())).thenReturn(metas);

        //ejecución
        List<Meta> metasObtenidas = servicioMeta.obtenerMetas(1L);

        //validación
        assertThat(metasObtenidas, notNullValue());
        assertThat(metasObtenidas, not(empty()));
        assertThat(metasObtenidas, hasSize(2));
    }

    @Test
    public void obtenerMetasQueAlSolicitarObtenerMetasDevuelvaUnaListaVaciaSiNoSeEstablecieronMetas() throws ExcepcionBaseDeDatos {
        //preparacion
        when(repositorioMetaMock.obtenerMetas(anyLong())).thenReturn(Collections.emptyList());

        //ejecución
        List<Meta> metasObtenidas = servicioMeta.obtenerMetas(1L);

        //validación
        assertThat(metasObtenidas, notNullValue());
        assertThat(metasObtenidas, empty());
        assertThat(metasObtenidas, hasSize(0));
    }

    @Test
    public void obtenerMetasQueAlSolicitarObtenerMetasLanceUnaExcepcionDeBDDSiEstaNoEstaDisponible() throws ExcepcionBaseDeDatos {
        //preparacion
        when(repositorioMetaMock.obtenerMetas(anyLong())).thenThrow(ExcepcionBaseDeDatos.class);

        //ejecución y validación
        Assertions.assertThrows(ExcepcionBaseDeDatos.class, ()->{
            servicioMeta.obtenerMetas(1L);
        }, "Base de datos no disponible");

    }

}
