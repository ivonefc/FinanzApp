package com.tallerwebi.dominio.notificacionHeader;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;
import com.tallerwebi.dominio.notificacion.Notificacion;

import java.util.List;

public class ServicioNotificacionHeaderImpl implements ServicioNotificacionHeader {

    private RepositorioNotificacionHeader repositorioNotificacionHeader;

    public ServicioNotificacionHeaderImpl() {
    }

    public ServicioNotificacionHeaderImpl(RepositorioNotificacionHeader repositorioNotificacionHeader) {
        this.repositorioNotificacionHeader = repositorioNotificacionHeader;
    }


    @Override
    public List<Notificacion> solicitarSolicitudesRecibidas(Long idUsuario) throws ExcepcionBaseDeDatos {
        return repositorioNotificacionHeader.obtenerSolicitudesRecibidas(idUsuario);
    }

    @Override
    public List<Notificacion> solicitarSolicitudesAceptadas(Long idUsuario) throws ExcepcionBaseDeDatos, UsuarioInexistente {
        return repositorioNotificacionHeader.obtenerSolicitudesAceptadas(idUsuario);
    }

    @Override
    public List<Notificacion> solicitarMovimientosCompartidos() {
        return repositorioNotificacionHeader.obtenerMovimientosCompartidos();
    }

    @Override
    public List<Notificacion> solicitarMetasAlcanzadas() {
        return repositorioNotificacionHeader.obtenerMetasAlcanzadas();
    }

    @Override
    public List<Notificacion> solicitarMetasNoAlcanzadas() {
        return repositorioNotificacionHeader.obtenerMetasNoAlcanzadas();
    }

    @Override
    public List<Notificacion> solicitarMetasMensajeAlerta() {
        return repositorioNotificacionHeader.obtenerMetasMensajeAlerta();
    }
}
