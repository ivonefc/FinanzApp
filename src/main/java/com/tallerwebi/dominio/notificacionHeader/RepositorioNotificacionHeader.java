package com.tallerwebi.dominio.notificacionHeader;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;
import com.tallerwebi.dominio.notificacion.Notificacion;

import java.util.List;

public interface RepositorioNotificacionHeader {

    List<Notificacion> obtenerSolicitudesRecibidas(Long idUsuario) throws ExcepcionBaseDeDatos;

    List<Notificacion> obtenerSolicitudesAceptadas(Long idUsuario) throws ExcepcionBaseDeDatos, UsuarioInexistente;

    List<Notificacion> obtenerMovimientosCompartidos();

    List<Notificacion> obtenerMetasAlcanzadas();

    List<Notificacion> obtenerMetasNoAlcanzadas();

    List<Notificacion> obtenerMetasMensajeAlerta();

}
