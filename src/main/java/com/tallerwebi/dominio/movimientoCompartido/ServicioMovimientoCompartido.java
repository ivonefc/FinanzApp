package com.tallerwebi.dominio.movimientoCompartido;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.notificacion.Notificacion;
import com.tallerwebi.dominio.usuario.Usuario;

import java.util.List;

public interface ServicioMovimientoCompartido {

    public List<Usuario> obtenerAmigos(Long idUsuario) throws ExcepcionBaseDeDatos;

    void agregarNuevoAmigo(Long idUsuario, String email) throws ExcepcionBaseDeDatos;

    public List<Notificacion> obtenerSolicitudesEnviadas(Long idUsuario) throws ExcepcionBaseDeDatos;

    void eliminarSolicitud(Long id) throws ExcepcionBaseDeDatos;

    List<Notificacion> obtenerSolicitudesRecibidas(Long idUsuario) throws ExcepcionBaseDeDatos;

    void aceptarSolicitud(Long id) throws ExcepcionBaseDeDatos;

    void eliminarAmigo(Long idAmigo, Long idUsuario) throws ExcepcionBaseDeDatos;
}
