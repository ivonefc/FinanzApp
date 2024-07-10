package com.tallerwebi.dominio.notificacionHeader;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;
import com.tallerwebi.dominio.notificacion.Notificacion;

import java.util.List;

public interface ServicioNotificacionHeader {

    List<Notificacion> solicitarSolicitudesRecibidas(Long idUsuario) throws ExcepcionBaseDeDatos;

    List<Notificacion> solicitarSolicitudesAceptadas(Long idUsuario) throws ExcepcionBaseDeDatos, UsuarioInexistente;

    List<Notificacion> solicitarMovimientosCompartidos();

    List<Notificacion> solicitarMetasAlcanzadas();

    List<Notificacion> solicitarMetasNoAlcanzadas();

    List<Notificacion> solicitarMetasMensajeAlerta();

}
