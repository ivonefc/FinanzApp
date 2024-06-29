package com.tallerwebi.dominio.movimientoCompartido;

import com.tallerwebi.dominio.categoria.CategoriaMovimiento;
import com.tallerwebi.dominio.categoria.RepositorioCategoria;
import com.tallerwebi.dominio.excepcion.*;
import com.tallerwebi.dominio.meta.RepositorioMeta;
import com.tallerwebi.dominio.movimiento.Movimiento;
import com.tallerwebi.dominio.movimiento.RepositorioMovimiento;
import com.tallerwebi.dominio.movimiento.ServicioMovimiento;
import com.tallerwebi.dominio.movimiento.ServicioMovimientoImpl;
import com.tallerwebi.dominio.notificacion.Notificacion;
import com.tallerwebi.dominio.usuario.RepositorioUsuario;
import com.tallerwebi.dominio.usuario.Usuario;
import com.tallerwebi.presentacion.movimiento.DatosAgregarMovimiento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ServicioMovimientoCompartidoTest {
    ServicioMovimientoCompartido servicioMovimientoCompartido;
    RepositorioMovimientoCompartido repositorioMovimientoCompartidoMock;
    RepositorioCategoria repositorioCategoriaMock;
    RepositorioUsuario repositorioUsuarioMock;
    RepositorioMeta repositorioMetaMock;
    HttpServletRequest httpServletRequestMock;
    HttpSession httpSessionMock;
    Usuario usuarioMock;

    @BeforeEach
    public void init(){
        repositorioMovimientoCompartidoMock = mock(RepositorioMovimientoCompartido.class);
        repositorioCategoriaMock = mock(RepositorioCategoria.class);
        repositorioUsuarioMock = mock(RepositorioUsuario.class);
        servicioMovimientoCompartido = new ServicioMovimientoCompartidoImpl(repositorioMovimientoCompartidoMock, repositorioCategoriaMock, repositorioUsuarioMock, repositorioMetaMock);
        httpServletRequestMock = mock(HttpServletRequest.class);
        httpSessionMock = mock(HttpSession.class);
        usuarioMock = mock(Usuario.class);
    }


    @Test
    public void queAlSolicitarAlServicioObtenerAmigosDevuelvaUnaListaDeAmigos() throws ExcepcionBaseDeDatos {
        //preparacion
        Usuario amigoMock1 = mock(Usuario.class);
        Usuario amigoMock2 = mock(Usuario.class);

        when(repositorioMovimientoCompartidoMock.obtenerAmigos(anyLong())).thenReturn(Arrays.asList(amigoMock1, amigoMock2));

        //ejecucion
        List<Usuario> amigos = servicioMovimientoCompartido.obtenerAmigos(1L);

        //validacion
        assertThat(amigos, notNullValue());
        assertThat(amigos, not(empty()));
        assertThat(amigos, containsInAnyOrder(amigoMock1, amigoMock2));
        assertThat(amigos, hasSize(2));
    }

    @Test
    public void queAlSolicitarAlServicioObtenerLasSolicitudesEnviadasMeDevuelvaUnaListaDeSolicitudesEnviadas() throws ExcepcionBaseDeDatos {
        //preparacion
        Notificacion solicitudMock1 = mock(Notificacion.class);
        Notificacion solicitudMock2 = mock(Notificacion.class);

        when(repositorioMovimientoCompartidoMock.obtenerSolicitudesEnviadas(anyLong())).thenReturn(Arrays.asList(solicitudMock1, solicitudMock2));

        //ejecucion
        List<Notificacion> solicitudes = servicioMovimientoCompartido.obtenerSolicitudesEnviadas(1L);

        //validacion
        assertThat(solicitudes, notNullValue());
        assertThat(solicitudes, not(empty()));
        assertThat(solicitudes, containsInAnyOrder(solicitudMock1, solicitudMock2));
        assertThat(solicitudes, hasSize(2));
    }

    @Test
    public void queAlSolicitarAlServicioObtenerLasSolicitudesRecibidasMeDevuelvaUnaListaDeSolicitudesRecibidas() throws ExcepcionBaseDeDatos {
        //preparacion
        Notificacion solicitudMock1 = mock(Notificacion.class);
        Notificacion solicitudMock2 = mock(Notificacion.class);

        when(repositorioMovimientoCompartidoMock.obtenerSolicitudesRecibidas(anyLong())).thenReturn(Arrays.asList(solicitudMock1, solicitudMock2));

        //ejecucion
        List<Notificacion> solicitudes = servicioMovimientoCompartido.obtenerSolicitudesRecibidas(1L);

        //validacion
        assertThat(solicitudes, notNullValue());
        assertThat(solicitudes, not(empty()));
        assertThat(solicitudes, containsInAnyOrder(solicitudMock1, solicitudMock2));
        assertThat(solicitudes, hasSize(2));
    }

    @Test
    public void queAlSolicitarAlServicioAgregarAmigoSeGuardeElAmigo() throws ExcepcionBaseDeDatos, ExcepcionCamposInvalidos, UsuarioInexistente, ExcepcionAmigoYaExistente, ExcepcionSolicitudEnviada, ExcepcionAutoAmistad {
        //preparacion
        Long idUsuario = 1L;
        Usuario amigoMock = mock(Usuario.class);
        when(amigoMock.getEmail()).thenReturn("amigo@ejemplo.com");

        //ejecucion
        servicioMovimientoCompartido.agregarNuevoAmigo(1L, amigoMock.getEmail());

        //validacion
        verify(repositorioMovimientoCompartidoMock).agregarNuevoAmigo(1L, amigoMock.getEmail());
    }

    @Test
    public void queAlSolicitarAlServicioEliminarUnaSolicitudElimineLaSolicitud() throws ExcepcionBaseDeDatos, ExcepcionMovimientoNoEncontrado, ExcepcionNotificacionInexistente {
        //preparacion
        Notificacion solicitud = mock(Notificacion.class);
        when(repositorioMovimientoCompartidoMock.obtenerNotificacionPorId(anyLong())).thenReturn(solicitud);

        //ejecucion
        servicioMovimientoCompartido.eliminarSolicitud(1L);

        //validacion
        verify(repositorioMovimientoCompartidoMock).eliminarSolicitud(solicitud);
    }

    @Test
    public void queAlSolicitarAlServicioEliminarUnAmigoElimineElAmigo() throws ExcepcionBaseDeDatos, ExcepcionMovimientoNoEncontrado, UsuarioInexistente {
        //preparacion
        Usuario amigo = mock(Usuario.class);
        Long idAmigo = 1L;
        Long idUsuario = 2L;
        when(amigo.getId()).thenReturn(idAmigo);
        when(usuarioMock.getId()).thenReturn(idUsuario);

        //ejecucion
        servicioMovimientoCompartido.eliminarAmigo(amigo.getId(), usuarioMock.getId());

        //validacion
        verify(repositorioMovimientoCompartidoMock).eliminarAmigo(idAmigo, idUsuario);
    }

    @Test
    public void queAlSolicitarAlServicioAceptarUnaSolicitudAcepteLaSolicitud() throws ExcepcionBaseDeDatos, ExcepcionMovimientoNoEncontrado, ExcepcionNotificacionInexistente {
        //preparacion
        Notificacion solicitud = mock(Notificacion.class);
        when(repositorioMovimientoCompartidoMock.obtenerNotificacionPorId(anyLong())).thenReturn(solicitud);

        //ejecucion
        servicioMovimientoCompartido.aceptarSolicitud(1L);

        //validacion
        verify(repositorioMovimientoCompartidoMock).aceptarSolicitud(solicitud);
    }
}
