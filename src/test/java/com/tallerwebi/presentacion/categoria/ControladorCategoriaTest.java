package com.tallerwebi.presentacion.categoria;

import com.tallerwebi.dominio.categoria.CategoriaMovimiento;
import com.tallerwebi.dominio.categoria.ServicioCategoria;
import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionCamposInvalidos;
import com.tallerwebi.dominio.excepcion.ExcepcionMovimientoNoEncontrado;
import com.tallerwebi.dominio.movimiento.Movimiento;
import com.tallerwebi.dominio.tipo.TipoMovimiento;
import com.tallerwebi.presentacion.movimiento.DatosEditarMovimiento;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ControladorCategoriaTest {

    ControladorCategoria controladorCategoria;
    ServicioCategoria servicioCategoriaMock;
    HttpServletRequest httpServletRequestMock;
    HttpSession httpSessionMock;


    @BeforeEach
    public void init() {
        servicioCategoriaMock = mock(ServicioCategoria.class);
        controladorCategoria = new ControladorCategoria(servicioCategoriaMock);
        httpServletRequestMock = mock(HttpServletRequest.class);
        httpSessionMock = mock(HttpSession.class);
     }


    @Test
    public void queAlQuererIrAVistaEditarColorCategoriaYNoExistaUsuarioLogueadoMeRedirijaAlLoguin() throws ExcepcionBaseDeDatos {
        //preparacion
        when(httpServletRequestMock.getSession(false)).thenReturn(null);

        //ejecucion
        ModelAndView modelAndView = controladorCategoria.irAVistaEditarColores(httpServletRequestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
        verify(httpSessionMock, times(0)).getAttribute("idUsuario");
    }


    @Test
    public void queAlQuererIrAVistaEditarColorYExistaUsuarioLogueadoMeDirijaALaVistaDeEdicion() throws ExcepcionBaseDeDatos {
        //preparacion
        when(httpServletRequestMock.getSession(false)).thenReturn(httpSessionMock);

        //ejecucion
        ModelAndView modelAndView = controladorCategoria.irAVistaEditarColores(httpServletRequestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("editar-colores"));
    }

    @Test
    public void queAlQuererEditarUnColorSePuedaEditarElColor() throws ExcepcionBaseDeDatos {
        //preparacion
        CategoriaMovimiento categoriaMock = mock(CategoriaMovimiento.class);
        when(categoriaMock.getNombre()).thenReturn("SUPERMERCADO");

        String nombreCategoria = categoriaMock.getNombre();
        String colorElegido = "#E39E1E";
        Model model = mock(Model.class);

        //ejecucion
        String viewName = controladorCategoria.actualizarColor(nombreCategoria, colorElegido, model);

        //validacion
        assertThat(viewName, equalToIgnoringCase("redirect:/categorias/editar-colores"));
    }
}
