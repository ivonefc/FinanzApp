package com.tallerwebi.presentacion.meta;

import com.tallerwebi.dominio.categoria.CategoriaMovimiento;
import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionCamposInvalidos;
import com.tallerwebi.dominio.excepcion.ExcepcionCategoriaConMetaExistente;
import com.tallerwebi.dominio.excepcion.ExcepcionMetaNoExistente;
import com.tallerwebi.dominio.meta.Meta;
import com.tallerwebi.dominio.meta.ServicioMeta;
import com.tallerwebi.dominio.usuario.Usuario;
import org.hamcrest.collection.IsMapWithSize;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


public class ControladorMetaTest {
    private ControladorMeta controladorMeta;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;
    private ServicioMeta servicioMetaMock;

    @BeforeEach
    public void init(){
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        servicioMetaMock = mock(ServicioMeta.class);
        controladorMeta = new ControladorMeta(servicioMetaMock);
    }

    @Test
    public void queAlClickearLaOpcionSeguimientoDeMetasEnElMenuDirijaALaVistaMetas(){
        //preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);

        //ejecucion
        ModelAndView modelAndView = controladorMeta.irAMetas(requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("metas"));
    }

    @Test
    public void queAlQuererIrALaOpcionSeguimientoDeMetasYNoExistaUsuarioLogueadoMeRedirijaAlLoguin(){
        //preparacion
        when(requestMock.getSession(false)).thenReturn(null);

        //ejecucion
        ModelAndView modelAndView = controladorMeta.irAMetas(requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    @Test
    public void queAlClickearEnLaOpcionAgregarMetaEnElMenuDirijaALaVistaAgregarMeta(){
        when(requestMock.getSession(false)).thenReturn(sessionMock);

        ModelAndView modelAndView = controladorMeta.irAAgregarMetas(requestMock);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("agregar-meta"));
    }

    @Test
    public void queAlQuererIrABarraAgregarMetaYNoExistaUsuarioLogueadoMeRedirijaAlLoguin(){
        when(requestMock.getSession(false)).thenReturn(null);

        ModelAndView modelAndView = controladorMeta.irAAgregarMetas(requestMock);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    @Test
    public void crearMetaQueAlQuererAgregarUnaMetaAgregueMetaYRedirijaAVistaMetas() throws ExcepcionCamposInvalidos, ExcepcionBaseDeDatos, ExcepcionCategoriaConMetaExistente {
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        doNothing().when(servicioMetaMock).guardarMeta(anyLong(), any(DatosMeta.class));
        DatosMeta datosMeta = new DatosMeta("categoria", 200.0);

        ModelAndView modelAndView = controladorMeta.crearMeta(datosMeta, requestMock);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/metas"));
    }

    @Test
    public void crearMetaQueAlQuererAgregarUnaMetaConCamposVaciosRedirijaAlFormularioYMuestreUnErrorEnCadaCampo() throws ExcepcionCamposInvalidos, ExcepcionBaseDeDatos, ExcepcionCategoriaConMetaExistente {
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(1L);
        Map<String, String> errores = Map.of(
                "categoria", "El campo es requerido",
                "monto", "El campo es requerido"
        );
        ExcepcionCamposInvalidos excepcionCamposInvalidos = new ExcepcionCamposInvalidos(errores);
        doThrow(excepcionCamposInvalidos).when(servicioMetaMock).guardarMeta(anyLong(), any(DatosMeta.class));
        DatosMeta datosMeta = new DatosMeta();

        ModelAndView modelAndView = controladorMeta.crearMeta(datosMeta, requestMock);
        Map<String, String> erroresObtenidos = (Map<String, String>) modelAndView.getModel().get("errores");

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("agregar-meta"));
        assertThat(erroresObtenidos, IsMapWithSize.aMapWithSize(2));
        assertThat(erroresObtenidos, hasEntry("categoria", "El campo es requerido"));
        assertThat(erroresObtenidos, hasEntry("monto", "El campo es requerido"));
        verify(servicioMetaMock, times(1)).guardarMeta(anyLong(), ArgumentMatchers.any(DatosMeta.class));
    }

    @Test
    public void crearMetaQueAlQuererAgregarUnaMetaConCampoCategoriaVacioRedirijaAlFormularioYMuestreUnErrorEnElCampo() throws ExcepcionCamposInvalidos, ExcepcionBaseDeDatos, ExcepcionCategoriaConMetaExistente {
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(1L);
        Map<String, String> errores = Map.of(
                "categoria", "El campo es requerido"
        );
        ExcepcionCamposInvalidos excepcionCamposInvalidos = new ExcepcionCamposInvalidos(errores);
        doThrow(excepcionCamposInvalidos).when(servicioMetaMock).guardarMeta(anyLong(), any(DatosMeta.class));
        DatosMeta datosMeta = new DatosMeta();

        ModelAndView modelAndView = controladorMeta.crearMeta(datosMeta, requestMock);
        Map<String, String> erroresObtenidos = (Map<String, String>) modelAndView.getModel().get("errores");

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("agregar-meta"));
        assertThat(erroresObtenidos, IsMapWithSize.aMapWithSize(1));
        assertThat(erroresObtenidos, hasEntry("categoria", "El campo es requerido"));
        verify(servicioMetaMock, times(1)).guardarMeta(anyLong(), ArgumentMatchers.any(DatosMeta.class));
    }

    @Test
    public void crearMetaQueAlQuererAgregarUnaMetaConCampoMontoVacioRedirijaAlFormularioYMuestreUnErrorEnElCampo() throws ExcepcionCamposInvalidos, ExcepcionBaseDeDatos, ExcepcionCategoriaConMetaExistente {
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(1L);
        Map<String, String> errores = Map.of(
                "monto", "El campo es requerido"
        );
        ExcepcionCamposInvalidos excepcionCamposInvalidos = new ExcepcionCamposInvalidos(errores);
        doThrow(excepcionCamposInvalidos).when(servicioMetaMock).guardarMeta(anyLong(), any(DatosMeta.class));
        DatosMeta datosMeta = new DatosMeta();

        ModelAndView modelAndView = controladorMeta.crearMeta(datosMeta, requestMock);
        Map<String, String> erroresObtenidos = (Map<String, String>) modelAndView.getModel().get("errores");

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("agregar-meta"));
        assertThat(erroresObtenidos, IsMapWithSize.aMapWithSize(1));
        assertThat(erroresObtenidos, hasEntry("monto", "El campo es requerido"));
        verify(servicioMetaMock, times(1)).guardarMeta(anyLong(), ArgumentMatchers.any(DatosMeta.class));
    }

    @Test
    public void crearMetaQueAlQuererCrearMetaParaUnaCategoriaQueYaTieneMetaEstablecidaRedirigirAlFormularioYMostrarUnError() throws ExcepcionCamposInvalidos, ExcepcionBaseDeDatos, ExcepcionCategoriaConMetaExistente {
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(1L);
        ExcepcionCategoriaConMetaExistente excepcionCategoriaConMetaExistente = new ExcepcionCategoriaConMetaExistente();
        doThrow(excepcionCategoriaConMetaExistente).when(servicioMetaMock).guardarMeta(anyLong(), any(DatosMeta.class));
        DatosMeta datosMeta=  new DatosMeta("categoria", 200.0);

        ModelAndView modelAndView = controladorMeta.crearMeta(datosMeta, requestMock);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("agregar-meta"));
        assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("La categoria que seleccionaste ya tiene una meta establecida."));
        verify(servicioMetaMock, times(1)).guardarMeta(anyLong(), ArgumentMatchers.any(DatosMeta.class));
    }

    @Test
    public void crearMetaQueAlQuererCrearMetaLanceExcepcionBaseDeDatos() throws ExcepcionCamposInvalidos, ExcepcionBaseDeDatos, ExcepcionCategoriaConMetaExistente {
        when(requestMock.getSession(false)).thenReturn(sessionMock);
        when(sessionMock.getAttribute("idUsuario")).thenReturn(1L);
        ExcepcionBaseDeDatos excepcionBaseDeDatos = new ExcepcionBaseDeDatos();
        doThrow(excepcionBaseDeDatos).when(servicioMetaMock).guardarMeta(anyLong(), any(DatosMeta.class));
        DatosMeta datosMeta = new DatosMeta("categoria", 200.0);

        ExcepcionBaseDeDatos thrownException = assertThrows(ExcepcionBaseDeDatos.class, () -> {
            controladorMeta.crearMeta(datosMeta, requestMock);
        });

        assertEquals(excepcionBaseDeDatos.getMessage(), thrownException.getMessage());
        verify(servicioMetaMock, times(1)).guardarMeta(anyLong(), ArgumentMatchers.any(DatosMeta.class));
    }

    @Test
    public void editarMetaQueAlClickearEnLaOpcionEditarMetaEnElMenuDirijaALaVistaEditarMeta() throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente {
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
    public void editarMetaQueAlQuererIrABarraEditarMetaYNoExistaUsuarioLogueadoMeRedirijaAlLoguin() throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente {
        //preparacion
        when(requestMock.getSession(false)).thenReturn(null);

        //ejecucion
        ModelAndView modelAndView = controladorMeta.irAFormularioEditarMetas(requestMock, 1L);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    @Test
    public void editarMetaQueAlClickearEnLaOpcionEditarMetaEnElMenuYNoExistaMetaConEseIdMeRedirijaALaVistaMetas() throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente {
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

    //eliminar



}
