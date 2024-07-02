package com.tallerwebi.dominio.movimientoCompartido;

import com.tallerwebi.dominio.excepcion.*;
import com.tallerwebi.dominio.movimiento.Movimiento;
import com.tallerwebi.dominio.notificacion.Notificacion;
import com.tallerwebi.dominio.usuario.Usuario;

import java.util.List;

public interface RepositorioMovimientoCompartido {

    public List<Usuario> obtenerAmigos(Long idUsuario) throws ExcepcionBaseDeDatos;

    void agregarNuevoAmigo(Long idUsuario, String email) throws Excepcion, ExcepcionBaseDeDatos, ExcepcionUsuarioNoPremium, UsuarioInexistente, ExcepcionAmigoYaExistente, ExcepcionSolicitudEnviada, ExcepcionAutoAmistad;

    public List<Notificacion> obtenerSolicitudesEnviadas(Long idUsuario) throws ExcepcionBaseDeDatos;

    Notificacion obtenerNotificacionPorId(Long idNotificacion) throws ExcepcionBaseDeDatos, ExcepcionNotificacionInexistente;

    void eliminarSolicitud(Notificacion notificacion) throws ExcepcionBaseDeDatos, ExcepcionNotificacionInexistente;

    List<Notificacion> obtenerSolicitudesRecibidas(Long idUsuario) throws ExcepcionBaseDeDatos;

    void aceptarSolicitud(Notificacion notificacion) throws ExcepcionBaseDeDatos, ExcepcionNotificacionInexistente;

    void eliminarAmigo(Long idAmigo, Long idUsuario) throws ExcepcionBaseDeDatos, UsuarioInexistente;

    List<Movimiento> obtenerMovimientosCompartidos(Long idAmigo, Long idUsuario) throws ExcepcionBaseDeDatos;

    List<Notificacion> obtenerSolicitudesAceptadas(Long idUsuario) throws ExcepcionBaseDeDatos, UsuarioInexistente;
}
