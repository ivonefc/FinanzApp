package com.tallerwebi.dominio.movimientoCompartido;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.usuario.Usuario;

import java.util.List;

public interface ServicioMovimientoCompartido {

    public List<Usuario> obtenerAmigos(Long idUsuario) throws ExcepcionBaseDeDatos;

    void agregarNuevoAmigo(Long idUsuario, String email) throws ExcepcionBaseDeDatos;
}
