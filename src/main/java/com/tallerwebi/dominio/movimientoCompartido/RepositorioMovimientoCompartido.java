package com.tallerwebi.dominio.movimientoCompartido;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.notificacion.Notificacion;
import com.tallerwebi.dominio.usuario.Usuario;

import java.util.List;

public interface RepositorioMovimientoCompartido {

    public List<Usuario> obtenerAmigos(Long idUsuario) throws ExcepcionBaseDeDatos;

    void agregarNuevoAmigo(Long idUsuario, String email) throws ExcepcionBaseDeDatos;

    public List<Notificacion> obtenerNotificacionesEnviadas(Long idUsuario) throws ExcepcionBaseDeDatos;
}
