package com.tallerwebi.dominio.movimientoCompartido;

import com.tallerwebi.dominio.categoria.RepositorioCategoria;
import com.tallerwebi.dominio.excepcion.*;
import com.tallerwebi.dominio.meta.RepositorioMeta;
import com.tallerwebi.dominio.movimiento.Movimiento;
import com.tallerwebi.dominio.notificacion.Notificacion;
import com.tallerwebi.dominio.usuario.RepositorioUsuario;
import com.tallerwebi.dominio.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service("servicioMovimientoCompartido")
@Transactional
public class ServicioMovimientoCompartidoImpl implements ServicioMovimientoCompartido {
    private RepositorioMovimientoCompartido repositorioMovimientoCompartido;
    private RepositorioCategoria repositorioCategoria;
    private RepositorioUsuario repositorioUsuario;
    private RepositorioMeta repositorioMeta;

    @Autowired
    public ServicioMovimientoCompartidoImpl(RepositorioMovimientoCompartido repositorioMovimientoCompartido, RepositorioCategoria repositorioCategoria, RepositorioUsuario repositorioUsuario, RepositorioMeta repositorioMeta) {
        this.repositorioMovimientoCompartido = repositorioMovimientoCompartido;
        this.repositorioCategoria = repositorioCategoria;
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioMeta = repositorioMeta;
    }

    @Transactional
    @Override
    public List<Usuario> obtenerAmigos(Long idUsuario) throws ExcepcionBaseDeDatos { //ID DE USUARIO
        return repositorioMovimientoCompartido.obtenerAmigos(idUsuario);
    }

    @Transactional
    @Override
    public List<Notificacion> obtenerSolicitudesEnviadas(Long idUsuario) throws ExcepcionBaseDeDatos {
        return repositorioMovimientoCompartido.obtenerSolicitudesEnviadas(idUsuario);
    }

    @Transactional
    @Override
    public void agregarNuevoAmigo(Long idUsuario, String email) throws ExcepcionBaseDeDatos, ExcepcionAmigoYaExistente, ExcepcionSolicitudEnviada, UsuarioInexistente, ExcepcionAutoAmistad {
        repositorioMovimientoCompartido.agregarNuevoAmigo(idUsuario, email);
    }

    @Transactional
    @Override
    public void eliminarSolicitud(Long idNotificacion) throws ExcepcionBaseDeDatos, ExcepcionNotificacionInexistente {
        Notificacion notificacion = repositorioMovimientoCompartido.obtenerNotificacionPorId(idNotificacion);
        if (notificacion == null)
            throw new ExcepcionBaseDeDatos("No se encontro la notificacion");
        repositorioMovimientoCompartido.eliminarSolicitud(notificacion);
    }

    @Override
    public List<Notificacion> obtenerSolicitudesRecibidas(Long idUsuario) throws ExcepcionBaseDeDatos {
        return repositorioMovimientoCompartido.obtenerSolicitudesRecibidas(idUsuario);
    }

    @Override
    public void aceptarSolicitud(Long id) throws ExcepcionBaseDeDatos, ExcepcionNotificacionInexistente {
        Notificacion notificacion = repositorioMovimientoCompartido.obtenerNotificacionPorId(id);
        if (notificacion == null)
            throw new ExcepcionBaseDeDatos("No se encontro la notificacion");
        repositorioMovimientoCompartido.aceptarSolicitud(notificacion);
    }

    @Override
    public void eliminarAmigo(Long idAmigo, Long idUsuario) throws ExcepcionBaseDeDatos, UsuarioInexistente {
        repositorioMovimientoCompartido.eliminarAmigo(idAmigo, idUsuario);
    }

    @Override
    public List<Movimiento> obtenerMovimientosCompartidos(Long idAmigo, Long idUsuario) throws ExcepcionBaseDeDatos {
        return repositorioMovimientoCompartido.obtenerMovimientosCompartidos(idAmigo, idUsuario);
    }

    @Override
    public List<Notificacion> obtenerSolicitudesAceptadas(Long idUsuario) throws ExcepcionBaseDeDatos, UsuarioInexistente {
        return repositorioMovimientoCompartido.obtenerSolicitudesAceptadas(idUsuario);
    }

}
