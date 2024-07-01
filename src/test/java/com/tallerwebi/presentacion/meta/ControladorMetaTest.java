package com.tallerwebi.presentacion.meta;

import com.tallerwebi.dominio.categoria.CategoriaMovimiento;
import com.tallerwebi.dominio.excepcion.*;
import com.tallerwebi.dominio.meta.Meta;
import com.tallerwebi.dominio.meta.ServicioMeta;
import com.tallerwebi.dominio.movimiento.ServicioMovimiento;
import com.tallerwebi.dominio.tipo.TipoMovimiento;
import com.tallerwebi.dominio.usuario.ServicioUsuario;
import com.tallerwebi.dominio.usuario.Usuario;
import org.hamcrest.collection.IsIterableWithSize;
import org.hamcrest.collection.IsMapWithSize;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


public class ControladorMetaTest {
    private ControladorMeta controladorMeta;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;
    private ServicioMeta servicioMetaMock;
    private ServicioMovimiento servicioMovimientoMock;
    private ServicioUsuario servicioUsuarioMock;
    private Usuario usuarioMock;

    @BeforeEach
    public void init(){
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        servicioMetaMock = mock(ServicioMeta.class);
        servicioUsuarioMock = mock(ServicioUsuario.class);
        servicioMovimientoMock =  mock(ServicioMovimiento.class);
        controladorMeta = new ControladorMeta(servicioMetaMock, servicioMovimientoMock, servicioUsuarioMock);
        usuarioMock = mock(Usuario.class);

    }

    //Test Metas (SEGUIMIENTO Y TABLA DE METAS ESTABLECIDAS)
    @Test
    public void deberiaNavegarAVistaMetasYMostrarTablaDeMetasYListaDeSeguimientoCuandoSeClickeaEnOpcionSeguimientoDeMetas() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        //preparacion
        Usuario usuario= new Usuario();
        Long idUsuario = 1L;
        usuario.setId(idUsuario);
        CategoriaMovimiento categoria1 = new CategoriaMovimiento("TRANSPORTE", new TipoMovimiento("EGRESO"));
        CategoriaMovimiento categoria2 = new CategoriaMovimiento("RESTAURANTE", new TipoMovimiento("EGRESO"));
        Meta meta1 = new Meta(usuario, categoria1, 100000.0);
        Meta meta2 = new Meta(usuario, categoria2, 200000.0);
        List<Meta> listaEsperada = new ArrayList<>();
        listaEsperada.add(meta1);
        listaEsperada.add(meta2);
        Map<String, Double> mapEsperado = new HashMap<>();
        mapEsperado.put("TRANSPORTE", 30000.0);
        mapEsperado.put("RESTAURANTE", 20000.0);
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(idUsuario);
        when(servicioMovimientoMock.obtenerTotalGastadoEnCategoriasConMetas(idUsuario)).thenReturn(mapEsperado);
        when(servicioMetaMock.obtenerMetas(idUsuario)).thenReturn(listaEsperada);
        when(servicioUsuarioMock.obtenerUsuarioPorId(idUsuario)).thenReturn(usuarioMock);
        when(usuarioMock.getRol()).thenReturn("PREMIUM");

        //ejecucion
        ModelAndView modelAndView = controladorMeta.irAMetas(requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("metas"));
       assertThat(modelAndView.getModel().get("totales"), is(mapEsperado));
       assertThat(modelAndView.getModel().get("metas"), is(listaEsperada));
       assertThat((Map<String, Double>)modelAndView.getModel().get("totales"), hasEntry("TRANSPORTE", 30000.0));
       assertThat((Map<String, Double>)modelAndView.getModel().get("totales"), hasEntry("RESTAURANTE", 20000.0));
       assertThat((List<Meta>)modelAndView.getModel().get("metas"), hasSize(2));
        assertThat((List<Meta>)modelAndView.getModel().get("metas"), hasItem(meta1));
        assertThat((List<Meta>)modelAndView.getModel().get("metas"), hasItem(meta2));

        verify(requestMock, times(1)).getSession(false);
        verify(sessionMock, times(1)).getAttribute("idUsuario");
        verify(servicioMovimientoMock, times(1)).obtenerTotalGastadoEnCategoriasConMetas(idUsuario);
    }


    @Test
    public void  deberiaDevolverListaYMapaVaciosEnElModeloCuandoNoHayMetasEstablecidas() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        //preparacion
        Long idUsuario = 1L;
        List<Meta> listaEsperada = Collections.emptyList();
        Map<String, Double> mapEsperado = Collections.emptyMap();
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(idUsuario);
        when(servicioMovimientoMock.obtenerTotalGastadoEnCategoriasConMetas(idUsuario)).thenReturn(mapEsperado);
        when(servicioMetaMock.obtenerMetas(idUsuario)).thenReturn(listaEsperada);
        when(servicioUsuarioMock.obtenerUsuarioPorId(idUsuario)).thenReturn(usuarioMock);
        when(usuarioMock.getRol()).thenReturn("PREMIUM");


        //ejecucion
        ModelAndView modelAndView = controladorMeta.irAMetas(requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("metas"));
        assertThat(modelAndView.getModel().get("metas"), notNullValue());
        assertThat(modelAndView.getModel().get("totales"), notNullValue());
        assertThat(((List<Meta>)modelAndView.getModel().get("metas")), IsIterableWithSize.iterableWithSize(0));
        assertThat((Map<String, Double>)modelAndView.getModel().get("totales"), IsMapWithSize.aMapWithSize(0));
        assertThat(modelAndView.getModel().get("totales"), is(mapEsperado));
        assertThat(modelAndView.getModel().get("metas"), is(listaEsperada));




        verify(requestMock, times(1)).getSession(false);
        verify(sessionMock, times(1)).getAttribute("idUsuario");
        verify(servicioMovimientoMock, times(1)).obtenerTotalGastadoEnCategoriasConMetas(idUsuario);


    }


    @Test
    public void queAlQuererIrALaOpcionSeguimientoDeMetasYNoExistaUsuarioLogueadoMeRedirijaAlLoguin() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        //preparacion
        when(requestMock.getSession(false)).thenReturn(null);

        //ejecucion
        ModelAndView modelAndView = controladorMeta.irAMetas(requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    @Test
    public void queAlClickearEnLaOpcionAgregarMetaEnElMenuDirijaALaVistaAgregarMeta() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        when(requestMock.getSession(false)).thenReturn(sessionMock);

        ModelAndView modelAndView = controladorMeta.irAAgregarMetas(requestMock);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("agregar-meta"));
    }

    @Test
    public void queAlQuererIrABarraAgregarMetaYNoExistaUsuarioLogueadoMeRedirijaAlLoguin() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        when(requestMock.getSession(false)).thenReturn(null);

        ModelAndView modelAndView = controladorMeta.irAAgregarMetas(requestMock);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    @Test
    public void crearMetaQueAlQuererAgregarUnaMetaAgregueMetaYRedirijaAVistaMetas() throws ExcepcionCamposInvalidos, ExcepcionBaseDeDatos, ExcepcionCategoriaConMetaExistente, UsuarioInexistente {
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        DatosMeta datosMeta = new DatosMeta("categoria", 200.0);
        doNothing().when(servicioMetaMock).guardarMeta(anyLong(), eq(datosMeta));


        ModelAndView modelAndView = controladorMeta.crearMeta(datosMeta, requestMock);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/metas"));
    }

    @Test
    public void crearMetaQueAlQuererAgregarUnaMetaConCamposVaciosRedirijaAlFormularioYMuestreUnErrorEnCadaCampo() throws ExcepcionCamposInvalidos, ExcepcionBaseDeDatos, ExcepcionCategoriaConMetaExistente, UsuarioInexistente {
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(1L);
        Map<String, String> errores = Map.of(
                "categoria", "El campo es requerido",
                "monto", "El campo es requerido"
        );
        ExcepcionCamposInvalidos excepcionCamposInvalidos = new ExcepcionCamposInvalidos(errores);
        DatosMeta datosMeta = new DatosMeta();
        doThrow(excepcionCamposInvalidos).when(servicioMetaMock).guardarMeta(anyLong(), eq(datosMeta));


        ModelAndView modelAndView = controladorMeta.crearMeta(datosMeta, requestMock);
        Map<String, String> erroresObtenidos = (Map<String, String>) modelAndView.getModel().get("errores");

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("agregar-meta"));
        assertThat(erroresObtenidos, IsMapWithSize.aMapWithSize(2));
        assertThat(erroresObtenidos, hasEntry("categoria", "El campo es requerido"));
        assertThat(erroresObtenidos, hasEntry("monto", "El campo es requerido"));
        verify(servicioMetaMock, times(1)).guardarMeta(anyLong(), ArgumentMatchers.any(DatosMeta.class));
    }

    @Test
    public void crearMetaQueAlQuererAgregarUnaMetaConCampoCategoriaVacioRedirijaAlFormularioYMuestreUnErrorEnElCampo() throws ExcepcionCamposInvalidos, ExcepcionBaseDeDatos, ExcepcionCategoriaConMetaExistente, UsuarioInexistente {
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(1L);
        Map<String, String> errores = Map.of(
                "categoria", "El campo es requerido"
        );
        ExcepcionCamposInvalidos excepcionCamposInvalidos = new ExcepcionCamposInvalidos(errores);
        DatosMeta datosMeta = new DatosMeta();
        doThrow(excepcionCamposInvalidos).when(servicioMetaMock).guardarMeta(anyLong(), eq(datosMeta));

        ModelAndView modelAndView = controladorMeta.crearMeta(datosMeta, requestMock);
        Map<String, String> erroresObtenidos = (Map<String, String>) modelAndView.getModel().get("errores");

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("agregar-meta"));
        assertThat(erroresObtenidos, IsMapWithSize.aMapWithSize(1));
        assertThat(erroresObtenidos, hasEntry("categoria", "El campo es requerido"));
        verify(servicioMetaMock, times(1)).guardarMeta(anyLong(), ArgumentMatchers.any(DatosMeta.class));
    }

    @Test
    public void crearMetaQueAlQuererAgregarUnaMetaConCampoMontoVacioRedirijaAlFormularioYMuestreUnErrorEnElCampo() throws ExcepcionCamposInvalidos, ExcepcionBaseDeDatos, ExcepcionCategoriaConMetaExistente, UsuarioInexistente {
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(1L);
        Map<String, String> errores = Map.of(
                "monto", "El campo es requerido"
        );
        ExcepcionCamposInvalidos excepcionCamposInvalidos = new ExcepcionCamposInvalidos(errores);
        DatosMeta datosMeta = new DatosMeta();
        doThrow(excepcionCamposInvalidos).when(servicioMetaMock).guardarMeta(anyLong(), eq(datosMeta));


        ModelAndView modelAndView = controladorMeta.crearMeta(datosMeta, requestMock);
        Map<String, String> erroresObtenidos = (Map<String, String>) modelAndView.getModel().get("errores");

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("agregar-meta"));
        assertThat(erroresObtenidos, IsMapWithSize.aMapWithSize(1));
        assertThat(erroresObtenidos, hasEntry("monto", "El campo es requerido"));
        verify(servicioMetaMock, times(1)).guardarMeta(anyLong(), ArgumentMatchers.any(DatosMeta.class));
    }

    @Test
    public void crearMetaQueAlQuererCrearMetaParaUnaCategoriaQueYaTieneMetaEstablecidaRedirigirAlFormularioYMostrarUnError() throws ExcepcionCamposInvalidos, ExcepcionBaseDeDatos, ExcepcionCategoriaConMetaExistente, UsuarioInexistente {
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(1L);
        ExcepcionCategoriaConMetaExistente excepcionCategoriaConMetaExistente = new ExcepcionCategoriaConMetaExistente();
        DatosMeta datosMeta=  new DatosMeta("categoria", 200.0);
        doThrow(excepcionCategoriaConMetaExistente).when(servicioMetaMock).guardarMeta(anyLong(), eq(datosMeta));


        ModelAndView modelAndView = controladorMeta.crearMeta(datosMeta, requestMock);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("agregar-meta"));
        assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("La categoria que seleccionaste ya tiene una meta establecida."));
        verify(servicioMetaMock, times(1)).guardarMeta(anyLong(), ArgumentMatchers.any(DatosMeta.class));
    }

    @Test
    public void crearMetaQueAlQuererCrearMetaLanceExcepcionBaseDeDatos() throws ExcepcionCamposInvalidos, ExcepcionBaseDeDatos, ExcepcionCategoriaConMetaExistente, UsuarioInexistente {
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(1L);
        ExcepcionBaseDeDatos excepcionBaseDeDatos = new ExcepcionBaseDeDatos();
        DatosMeta datosMeta = new DatosMeta("categoria", 200.0);
        doThrow(excepcionBaseDeDatos).when(servicioMetaMock).guardarMeta(anyLong(), eq(datosMeta));


        ExcepcionBaseDeDatos thrownException = assertThrows(ExcepcionBaseDeDatos.class, () -> {
            controladorMeta.crearMeta(datosMeta, requestMock);
        });

        assertEquals(excepcionBaseDeDatos.getMessage(), thrownException.getMessage());
        verify(servicioMetaMock, times(1)).guardarMeta(anyLong(), ArgumentMatchers.any(DatosMeta.class));
    }

    @Test
    public void editarMetaQueAlClickearEnLaOpcionEditarMetaEnElMenuDirijaAlFormularioEditarMeta() throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente, UsuarioInexistente {
        //preparacion
        Usuario usuario = new Usuario();
        CategoriaMovimiento categoriaMovimiento = new CategoriaMovimiento();
        Meta meta = new Meta(usuario, categoriaMovimiento, 200.0);

        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(1L);
        when(servicioMetaMock.obtenerMetaPorId(1L)).thenReturn(meta);

        //ejecucion
        ModelAndView modelAndView = controladorMeta.irAFormularioEditarMetas(requestMock, 1L);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("editar-meta"));
    }

    @Test
    public void editarMetaQueAlQuererIrABarraEditarMetaYNoExistaUsuarioLogueadoMeRedirijaAlLoguin() throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente, UsuarioInexistente {
        //preparacion
        when(requestMock.getSession(false)).thenReturn(null);

        //ejecucion
        ModelAndView modelAndView = controladorMeta.irAFormularioEditarMetas(requestMock, 1L);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    @Test
    public void editarMetaQueAlClickearEnLaOpcionEditarMetaEnElMenuYNoExistaMetaConEseIdMeLanceExcepcionMetaNoExistente() throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente {
        //preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(1L);
        when(servicioMetaMock.obtenerMetaPorId(1L)).thenThrow(new ExcepcionMetaNoExistente());

        //ejecucion y validacion
        assertThrows(ExcepcionMetaNoExistente.class, () -> {
            controladorMeta.irAFormularioEditarMetas(requestMock, 1L);
        });
    }

    @Test
    public void editarMetaQueAlClickearEnLaOpcionEditarMetaLanceExcepcionBaseDeDatos() throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente {
        //preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(1L);
        when(servicioMetaMock.obtenerMetaPorId(1L)).thenThrow(new ExcepcionBaseDeDatos());

        //ejecucion y validacion
        assertThrows(ExcepcionBaseDeDatos.class, () -> {
            controladorMeta.irAFormularioEditarMetas(requestMock, 1L);
        });
    }

    @Test
    public void editarMetaQueAlQuererEditarUnaMetaEditeLaMetaYRedirijaAVistaMetas() throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente, ExcepcionCamposInvalidos, UsuarioInexistente {
        //preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        CategoriaMovimiento categoriaMovimiento = new CategoriaMovimiento();
        String categoria = categoriaMovimiento.getNombre();
        DatosEditarMeta datosEditarMeta = new DatosEditarMeta(1L, categoria , 200.0);
        doNothing().when(servicioMetaMock).actualizarMeta(datosEditarMeta);

        //ejecucion
        ModelAndView modelAndView = controladorMeta.editarMeta(datosEditarMeta, requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/metas"));
        verify(servicioMetaMock, times(1)).actualizarMeta(datosEditarMeta);
    }

    @Test
    public void editarMetaQueAlQuererEditarYNoExistaUsuarioLogueadoNoSePuedaEditar() throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente, UsuarioInexistente {
        //preparacion
        when(requestMock.getSession(false)).thenReturn(null);

        //ejecucion
        ModelAndView modelAndView = controladorMeta.editarMeta(new DatosEditarMeta(), requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    @Test
    public void editarMetaQueAlQuererEditarUnaMetaConCamposVaciosRedirijaAlFormularioYMuestreUnErrorEnCadaCampo() throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente, ExcepcionCamposInvalidos, UsuarioInexistente {
        //preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        CategoriaMovimiento categoriaMovimiento = new CategoriaMovimiento();
        String categoria = categoriaMovimiento.getNombre();
        DatosEditarMeta datosEditarMeta = new DatosEditarMeta(1L, categoria, 200.0);
        Map<String, String> errores = Map.of(
                "categoria", "El campo es requerido",
                "monto", "El campo es requerido"
        );
        ExcepcionCamposInvalidos excepcionCamposInvalidos = new ExcepcionCamposInvalidos(errores);
        doThrow(excepcionCamposInvalidos).when(servicioMetaMock).actualizarMeta(datosEditarMeta);

        //ejecucion
        ModelAndView modelAndView = controladorMeta.editarMeta(datosEditarMeta, requestMock);
        Map<String, String> erroresObtenidos = (Map<String, String>) modelAndView.getModel().get("errores");

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("editar-meta"));
        assertThat(erroresObtenidos, IsMapWithSize.aMapWithSize(2));
        assertThat(erroresObtenidos, hasEntry("categoria", "El campo es requerido"));
        assertThat(erroresObtenidos, hasEntry("monto", "El campo es requerido"));
        verify(servicioMetaMock, times(1)).actualizarMeta(datosEditarMeta);
    }

    @Test
    public void editarMetaQueAlQuererEditarUnaMetaConCampoCategoriaVacioRedirijaAlFormularioYMuestreUnErrorEnElCampo() throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente, ExcepcionCamposInvalidos, UsuarioInexistente {
        //preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        CategoriaMovimiento categoriaMovimiento = new CategoriaMovimiento();
        String categoria = categoriaMovimiento.getNombre();
        DatosEditarMeta datosEditarMeta = new DatosEditarMeta(1L, categoria, 200.0);
        Map<String, String> errores = Map.of(
                "categoria", "El campo es requerido"
        );
        ExcepcionCamposInvalidos excepcionCamposInvalidos = new ExcepcionCamposInvalidos(errores);
        doThrow(excepcionCamposInvalidos).when(servicioMetaMock).actualizarMeta(datosEditarMeta);

        //ejecucion
        ModelAndView modelAndView = controladorMeta.editarMeta(datosEditarMeta, requestMock);
        Map<String, String> erroresObtenidos = (Map<String, String>) modelAndView.getModel().get("errores");

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("editar-meta"));
        assertThat(erroresObtenidos, IsMapWithSize.aMapWithSize(1));
        assertThat(erroresObtenidos, hasEntry("categoria", "El campo es requerido"));
        verify(servicioMetaMock, times(1)).actualizarMeta(datosEditarMeta);
    }

    @Test
    public void editarMetaQueAlQuererEditarUnaMetaConCampoMontoVacioRedirijaAlFormularioYMuestreUnErrorEnElCampo() throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente, ExcepcionCamposInvalidos, UsuarioInexistente {
        //preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        DatosEditarMeta datosEditarMeta = new DatosEditarMeta(1L, "categoria", null);
        Map<String, String> errores = Map.of(
                "monto", "El campo es requerido"
        );
        ExcepcionCamposInvalidos excepcionCamposInvalidos = new ExcepcionCamposInvalidos(errores);
        doThrow(excepcionCamposInvalidos).when(servicioMetaMock).actualizarMeta(datosEditarMeta);

        //ejecucion
        ModelAndView modelAndView = controladorMeta.editarMeta(datosEditarMeta, requestMock);
        Map<String, String> erroresObtenidos = (Map<String, String>) modelAndView.getModel().get("errores");

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("editar-meta"));
        assertThat(erroresObtenidos, IsMapWithSize.aMapWithSize(1));
        assertThat(erroresObtenidos, hasEntry("monto", "El campo es requerido"));
        verify(servicioMetaMock, times(1)).actualizarMeta(datosEditarMeta);
    }

    @Test
    public void editarMetaQueAlQuererEditarUnaMetaLanceExcepcionBaseDeDatos() throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente, ExcepcionCamposInvalidos {
        //preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        CategoriaMovimiento categoriaMovimiento = new CategoriaMovimiento();
        String categoria = categoriaMovimiento.getNombre();
        DatosEditarMeta datosEditarMeta = new DatosEditarMeta(1L, categoria, 200.0);
        ExcepcionBaseDeDatos excepcionBaseDeDatos = new ExcepcionBaseDeDatos();
        doThrow(excepcionBaseDeDatos).when(servicioMetaMock).actualizarMeta(datosEditarMeta);

        //ejecucion y validacion
        ExcepcionBaseDeDatos thrownException = assertThrows(ExcepcionBaseDeDatos.class, () -> {
            controladorMeta.editarMeta(datosEditarMeta, requestMock);
        });

        assertEquals(excepcionBaseDeDatos.getMessage(), thrownException.getMessage());
        verify(servicioMetaMock, times(1)).actualizarMeta(datosEditarMeta);
    }

    @Test
    public void editarMetaQueAlQuererEditarUnMetaYLaMetaNoExistaLanceExcepcionMetaNoExistente() throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente, ExcepcionCamposInvalidos {
        //preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        CategoriaMovimiento categoriaMovimiento = new CategoriaMovimiento();
        String categoria = categoriaMovimiento.getNombre();
        DatosEditarMeta datosEditarMeta = new DatosEditarMeta(1L, categoria, 200.0);
        ExcepcionMetaNoExistente excepcionMetaNoExistente = new ExcepcionMetaNoExistente();
        doThrow(excepcionMetaNoExistente).when(servicioMetaMock).actualizarMeta(datosEditarMeta);

        //ejecucion y validacion
        ExcepcionMetaNoExistente thrownException = assertThrows(ExcepcionMetaNoExistente.class, () -> {
            controladorMeta.editarMeta(datosEditarMeta, requestMock);
        });

        assertEquals(excepcionMetaNoExistente.getMessage(), thrownException.getMessage());
        verify(servicioMetaMock, times(1)).actualizarMeta(datosEditarMeta);
    }

    //eliminar
    @Test
    public void eliminarMetaQueAlClickearEnLaOpcionEliminarMetaEnElMenuElimineLaMetaYRedirijaAVistaMetas() throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente {
        //preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        doNothing().when(servicioMetaMock).eliminarMeta(1L);

        //ejecucion
        ModelAndView modelAndView = controladorMeta.eliminarMeta(1L, requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/metas"));
        verify(servicioMetaMock, times(1)).eliminarMeta(1L);
    }

    @Test
    public void eliminarMetaQueAlQuererEliminarYNoExistaUsuarioLogueadoNoSePuedaEliminar() throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente {
        //preparacion
        when(requestMock.getSession(false)).thenReturn(null);

        //ejecucion
        ModelAndView modelAndView = controladorMeta.eliminarMeta(1L, requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    @Test
    public void eliminarMetaQueAlQuererEliminarUnaMetaLanceExcepcionBaseDeDatos() throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente {
        //preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        ExcepcionBaseDeDatos excepcionBaseDeDatos = new ExcepcionBaseDeDatos();
        doThrow(excepcionBaseDeDatos).when(servicioMetaMock).eliminarMeta(1L);

        //ejecucion y validacion
        ExcepcionBaseDeDatos thrownException = assertThrows(ExcepcionBaseDeDatos.class, () -> {
            controladorMeta.eliminarMeta(1L, requestMock);
        });

        assertEquals(excepcionBaseDeDatos.getMessage(), thrownException.getMessage());
        verify(servicioMetaMock, times(1)).eliminarMeta(1L);
    }

    @Test
    public void eliminarMetaQueAlQuererEliminarUnMetaYLaMetaNoExistaLanceExcepcionMetaNoExistente() throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente {
        //preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        ExcepcionMetaNoExistente excepcionMetaNoExistente = new ExcepcionMetaNoExistente();
        doThrow(excepcionMetaNoExistente).when(servicioMetaMock).eliminarMeta(1L);

        //ejecucion y validacion
        ExcepcionMetaNoExistente thrownException = assertThrows(ExcepcionMetaNoExistente.class, () -> {
            controladorMeta.eliminarMeta(1L, requestMock);
        });

        assertEquals(excepcionMetaNoExistente.getMessage(), thrownException.getMessage());
        verify(servicioMetaMock, times(1)).eliminarMeta(1L);
    }

}
