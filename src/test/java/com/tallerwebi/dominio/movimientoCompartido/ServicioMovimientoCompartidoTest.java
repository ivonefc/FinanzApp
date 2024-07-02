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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    public void queAlSolicitarAlServicioObtenerAmigosDevuelvaUnaListaVacíaDeAmigos() throws ExcepcionBaseDeDatos {
        //preparacion
        when(repositorioMovimientoCompartidoMock.obtenerAmigos(anyLong())).thenReturn(Arrays.asList());

        //ejecucion
        List<Usuario> amigos = servicioMovimientoCompartido.obtenerAmigos(1L);

        //validacion
        assertThat(amigos, notNullValue());
        assertThat(amigos, empty());
    }

    @Test
    public void queAlSolicitarAlServicioObtenerAmigosLanceExcepcionBaseDeDatos() throws ExcepcionBaseDeDatos {
        // Preparación
        when(repositorioMovimientoCompartidoMock.obtenerAmigos(anyLong())).thenThrow(ExcepcionBaseDeDatos.class);

        // Ejecución y validación
        ExcepcionBaseDeDatos exception = assertThrows(ExcepcionBaseDeDatos.class, () -> {
            servicioMovimientoCompartido.obtenerAmigos(1L);
        });
        assertNotNull(exception);
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
    public void queAlSolicitarAlServicioObtenerLasSolicitudesEnviadasMeDevuelvaUnaListaVaciaDeSolicitudesEnviadas() throws ExcepcionBaseDeDatos {
        //preparacion
        when(repositorioMovimientoCompartidoMock.obtenerSolicitudesEnviadas(anyLong())).thenReturn(Arrays.asList());

        //ejecucion
        List<Notificacion> solicitudes = servicioMovimientoCompartido.obtenerSolicitudesEnviadas(1L);

        //validacion
        assertThat(solicitudes, notNullValue());
        assertThat(solicitudes, empty());
    }

    @Test
    public void queAlSolicitarAlServicioObtenerLasSolicitudesEnviadasLanceExcepcionBaseDeDatos() throws ExcepcionBaseDeDatos {
        //preparacion
        when(repositorioMovimientoCompartidoMock.obtenerSolicitudesEnviadas(anyLong())).thenThrow(ExcepcionBaseDeDatos.class);

        //ejecucion y validacion
        ExcepcionBaseDeDatos exception = assertThrows(ExcepcionBaseDeDatos.class, () -> {
            servicioMovimientoCompartido.obtenerSolicitudesEnviadas(1L);
        });
        assertNotNull(exception);
    }

    @Test
    public void queAlSolicitarAlServicioAgregarAmigoLanceExcepcionBaseDeDatos() throws ExcepcionBaseDeDatos, UsuarioInexistente, ExcepcionAmigoYaExistente, ExcepcionSolicitudEnviada, ExcepcionAutoAmistad {
        // Preparación
        Usuario amigoMock = mock(Usuario.class);
        when(amigoMock.getEmail()).thenReturn("amigo@ejemplo.com");
        doThrow(ExcepcionBaseDeDatos.class).when(repositorioMovimientoCompartidoMock).agregarNuevoAmigo(anyLong(), anyString());

        // Ejecución y validación
        ExcepcionBaseDeDatos exception = assertThrows(ExcepcionBaseDeDatos.class, () -> {
            servicioMovimientoCompartido.agregarNuevoAmigo(1L, amigoMock.getEmail());
        });
        assertNotNull(exception);
    }

    @Test
    public void queAlSolicitarAlServicioAgregarAmigoLanceExcepcionSoliciutdEnviada() throws ExcepcionBaseDeDatos, UsuarioInexistente, ExcepcionAmigoYaExistente, ExcepcionSolicitudEnviada, ExcepcionAutoAmistad {
        // Preparación
        Usuario amigoMock = mock(Usuario.class);
        when(amigoMock.getEmail()).thenReturn("amigo@ejemplo.com");
        doThrow(ExcepcionSolicitudEnviada.class).when(repositorioMovimientoCompartidoMock).agregarNuevoAmigo(anyLong(), anyString());

        // Ejecución y validación
        ExcepcionSolicitudEnviada exception = assertThrows(ExcepcionSolicitudEnviada.class, () -> {
            servicioMovimientoCompartido.agregarNuevoAmigo(1L, amigoMock.getEmail());
        });
        assertNotNull(exception);
    }

    @Test
    public void queAlSolicitarAlServicioAgregarAmigoLanceExcepcionAutoAmistad() throws ExcepcionBaseDeDatos, UsuarioInexistente, ExcepcionAmigoYaExistente, ExcepcionSolicitudEnviada, ExcepcionAutoAmistad {
        // Preparación
        Usuario amigoMock = mock(Usuario.class);
        when(amigoMock.getEmail()).thenReturn("amigo@ejemplo.com");
        doThrow(ExcepcionAutoAmistad.class).when(repositorioMovimientoCompartidoMock).agregarNuevoAmigo(anyLong(), anyString());

        // Ejecución y validación
        ExcepcionAutoAmistad exception = assertThrows(ExcepcionAutoAmistad.class, () -> {
            servicioMovimientoCompartido.agregarNuevoAmigo(1L, amigoMock.getEmail());
        });
        assertNotNull(exception);
    }

    @Test
    public void queAlSolicitarAlServicioAgregarAmigoLanceExcepcionUsuarioInexistente() throws ExcepcionBaseDeDatos, UsuarioInexistente, ExcepcionAmigoYaExistente, ExcepcionSolicitudEnviada, ExcepcionAutoAmistad {
        // Preparación
        Usuario amigoMock = mock(Usuario.class);
        when(amigoMock.getEmail()).thenReturn("amigo@ejemplo.com");
        doThrow(UsuarioInexistente.class).when(repositorioMovimientoCompartidoMock).agregarNuevoAmigo(anyLong(), anyString());

        // Ejecución y validación
        UsuarioInexistente exception = assertThrows(UsuarioInexistente.class, () -> {
            servicioMovimientoCompartido.agregarNuevoAmigo(1L, amigoMock.getEmail());
        });
        assertNotNull(exception);
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
    public void queAlSolicitarAlServicioEliminarUnaSolicitudLanceExceptionNotificacionInexistente() throws ExcepcionBaseDeDatos, ExcepcionMovimientoNoEncontrado, ExcepcionNotificacionInexistente {
        //preparacion
        when(repositorioMovimientoCompartidoMock.obtenerNotificacionPorId(anyLong())).thenReturn(null);

        //ejecucion y validacion
        ExcepcionNotificacionInexistente exception = assertThrows(ExcepcionNotificacionInexistente.class, () -> {
            servicioMovimientoCompartido.eliminarSolicitud(1L);
        });
        assertNotNull(exception);
    }

    @Test
    public void queAlSolicitarAlServicioEliminarUnaSolicitudLanceExceptionBaseDeDatos() throws ExcepcionBaseDeDatos, ExcepcionMovimientoNoEncontrado, ExcepcionNotificacionInexistente {
        //preparacion
        when(repositorioMovimientoCompartidoMock.obtenerNotificacionPorId(anyLong())).thenThrow(ExcepcionBaseDeDatos.class);

        //ejecucion y validacion
        ExcepcionBaseDeDatos exception = assertThrows(ExcepcionBaseDeDatos.class, () -> {
            servicioMovimientoCompartido.eliminarSolicitud(1L);
        });
        assertNotNull(exception);
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
    public void queAlSolicitarAlServicioObtenerLasSolicitudesRecibidasDevuelvaUnaListaVacíaDeSolicitudesRecibidas() throws ExcepcionBaseDeDatos {
        //preparacion
        when(repositorioMovimientoCompartidoMock.obtenerSolicitudesRecibidas(anyLong())).thenReturn(Arrays.asList());

        //ejecucion
        List<Notificacion> solicitudes = servicioMovimientoCompartido.obtenerSolicitudesRecibidas(1L);

        //validacion
        assertThat(solicitudes, notNullValue());
        assertThat(solicitudes, empty());
    }

    @Test
    public void queAlSolicitarAlServicioObtenerLasSolicitudesRecibidasLanceExcepcionBaseDeDatos() throws ExcepcionBaseDeDatos {
        //preparacion
        when(repositorioMovimientoCompartidoMock.obtenerSolicitudesRecibidas(anyLong())).thenThrow(ExcepcionBaseDeDatos.class);

        //ejecucion y validacion
        ExcepcionBaseDeDatos exception = assertThrows(ExcepcionBaseDeDatos.class, () -> {
            servicioMovimientoCompartido.obtenerSolicitudesRecibidas(1L);
        });
        assertNotNull(exception);
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

    @Test
    public void queAlSolicitarAlServicioAceptarUnaSolicitudLanceExceptionNotificacionInexistente() throws ExcepcionBaseDeDatos, ExcepcionMovimientoNoEncontrado, ExcepcionNotificacionInexistente {
        //preparacion
        when(repositorioMovimientoCompartidoMock.obtenerNotificacionPorId(anyLong())).thenReturn(null);

        //ejecucion y validacion
        ExcepcionNotificacionInexistente exception = assertThrows(ExcepcionNotificacionInexistente.class, () -> {
            servicioMovimientoCompartido.aceptarSolicitud(1L);
        });
        assertNotNull(exception);
    }

    @Test
    public void queAlSolicitarAlServicioAceptarUnaSolicitudLanceExcepcionBaseDeDatos() throws ExcepcionBaseDeDatos, ExcepcionMovimientoNoEncontrado, ExcepcionNotificacionInexistente {
        //preparacion
        when(repositorioMovimientoCompartidoMock.obtenerNotificacionPorId(anyLong())).thenThrow(ExcepcionBaseDeDatos.class);

        //ejecucion y validacion
        ExcepcionBaseDeDatos exception = assertThrows(ExcepcionBaseDeDatos.class, () -> {
            servicioMovimientoCompartido.aceptarSolicitud(1L);
        });
        assertNotNull(exception);
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
    public void queAlSolicitarAlServicioEliminarUnAmigoLanceExceptionUsuarioInexistente() throws ExcepcionBaseDeDatos, ExcepcionMovimientoNoEncontrado, UsuarioInexistente {
        //preparacion
        Usuario amigo = mock(Usuario.class);
        Long idAmigo = 1L;
        Long idUsuario = 2L;
        when(amigo.getId()).thenReturn(idAmigo);
        when(usuarioMock.getId()).thenReturn(idUsuario);
        doThrow(UsuarioInexistente.class).when(repositorioMovimientoCompartidoMock).eliminarAmigo(anyLong(), anyLong());

        //ejecucion y validacion
        UsuarioInexistente exception = assertThrows(UsuarioInexistente.class, () -> {
            servicioMovimientoCompartido.eliminarAmigo(amigo.getId(), usuarioMock.getId());
        });
        assertNotNull(exception);
    }

    @Test
    public void queAlSolicitarAlServicioEliminarUnAmigoLanceExcepcionBaseDeDatos() throws ExcepcionBaseDeDatos, ExcepcionMovimientoNoEncontrado, UsuarioInexistente {
        //preparacion
        Usuario amigo = mock(Usuario.class);
        Long idAmigo = 1L;
        Long idUsuario = 2L;
        when(amigo.getId()).thenReturn(idAmigo);
        when(usuarioMock.getId()).thenReturn(idUsuario);
        doThrow(ExcepcionBaseDeDatos.class).when(repositorioMovimientoCompartidoMock).eliminarAmigo(anyLong(), anyLong());

        //ejecucion y validacion
        ExcepcionBaseDeDatos exception = assertThrows(ExcepcionBaseDeDatos.class, () -> {
            servicioMovimientoCompartido.eliminarAmigo(amigo.getId(), usuarioMock.getId());
        });
        assertNotNull(exception);
    }

    @Test
    public void queAlSolicitarAlServicioObtenerLosMovimientosCompartidosMeDevuelvaUnaListaDeMovimientosCompartidos() throws ExcepcionBaseDeDatos {
        //preparacion
        Movimiento movimientoMock1 = mock(Movimiento.class);
        Movimiento movimientoMock2 = mock(Movimiento.class);
        when(repositorioMovimientoCompartidoMock.obtenerMovimientosCompartidos(anyLong(), anyLong())).thenReturn(Arrays.asList(movimientoMock1, movimientoMock2));

        //ejecucion
        List<Movimiento> movimientos = servicioMovimientoCompartido.obtenerMovimientosCompartidos(1L, 2L);

        //validacion
        assertThat(movimientos, notNullValue());
        assertThat(movimientos, not(empty()));
        assertThat(movimientos, containsInAnyOrder(movimientoMock1, movimientoMock2));
        assertThat(movimientos, hasSize(2));
    }

    @Test
    public void queAlSolicitarAlServicioObtenerLosMovimientosCompartidosMeDevuelvaUnaListaVaciaDeMovimientosCompartidos() throws ExcepcionBaseDeDatos {
        //preparacion
        when(repositorioMovimientoCompartidoMock.obtenerMovimientosCompartidos(anyLong(), anyLong())).thenReturn(Arrays.asList());

        //ejecucion
        List<Movimiento> movimientos = servicioMovimientoCompartido.obtenerMovimientosCompartidos(1L, 2L);

        //validacion
        assertThat(movimientos, notNullValue());
        assertThat(movimientos, empty());
    }

    @Test
    public void queAlSolicitarAlServicioObtenerLosMovimientosCompartidosLanceExcepcionBaseDeDatos() throws ExcepcionBaseDeDatos {
        //preparacion
        when(repositorioMovimientoCompartidoMock.obtenerMovimientosCompartidos(anyLong(), anyLong())).thenThrow(ExcepcionBaseDeDatos.class);

        //ejecucion y validacion
        ExcepcionBaseDeDatos exception = assertThrows(ExcepcionBaseDeDatos.class, () -> {
            servicioMovimientoCompartido.obtenerMovimientosCompartidos(1L, 2L);
        });
        assertNotNull(exception);
    }

    @Test
    public void queAlSolicitarAlServicioObtenerSolicitudesAceptadasMeDevuelvaUnaListaDeSolicitudesAceptadas() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        //preparacion
        Notificacion solicitudMock1 = mock(Notificacion.class);
        Notificacion solicitudMock2 = mock(Notificacion.class);
        when(repositorioMovimientoCompartidoMock.obtenerSolicitudesAceptadas(anyLong())).thenReturn(Arrays.asList(solicitudMock1, solicitudMock2));

        //ejecucion
        List<Notificacion> solicitudes = servicioMovimientoCompartido.obtenerSolicitudesAceptadas(1L);

        //validacion
        assertThat(solicitudes, notNullValue());
        assertThat(solicitudes, not(empty()));
        assertThat(solicitudes, containsInAnyOrder(solicitudMock1, solicitudMock2));
        assertThat(solicitudes, hasSize(2));
    }

    @Test
    public void queAlSolicitarAlServicioObtenerSolicitudesAceptadasMeDevuelvaUnaListaVaciaDeSolicitudesAceptadas() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        //preparacion
        when(repositorioMovimientoCompartidoMock.obtenerSolicitudesAceptadas(anyLong())).thenReturn(Arrays.asList());

        //ejecucion
        List<Notificacion> solicitudes = servicioMovimientoCompartido.obtenerSolicitudesAceptadas(1L);

        //validacion
        assertThat(solicitudes, notNullValue());
        assertThat(solicitudes, empty());
    }

    @Test
    public void queAlSolicitarAlServicioObtenerSolicitudesAceptadasLanceExcepcionBaseDeDatos() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        //preparacion
        when(repositorioMovimientoCompartidoMock.obtenerSolicitudesAceptadas(anyLong())).thenThrow(ExcepcionBaseDeDatos.class);

        //ejecucion y validacion
        ExcepcionBaseDeDatos exception = assertThrows(ExcepcionBaseDeDatos.class, () -> {
            servicioMovimientoCompartido.obtenerSolicitudesAceptadas(1L);
        });
        assertNotNull(exception);
    }

    @Test
    public void queAlSolicitarAlServicioObtenerSolicitudesAceptadasLanceExcepcionUsuarioInexistente() throws ExcepcionBaseDeDatos, UsuarioInexistente {
        //preparacion
        when(repositorioMovimientoCompartidoMock.obtenerSolicitudesAceptadas(anyLong())).thenThrow(UsuarioInexistente.class);

        //ejecucion y validacion
        UsuarioInexistente exception = assertThrows(UsuarioInexistente.class, () -> {
            servicioMovimientoCompartido.obtenerSolicitudesAceptadas(1L);
        });
        assertNotNull(exception);
    }

}
