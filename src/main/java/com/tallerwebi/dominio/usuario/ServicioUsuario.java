package com.tallerwebi.dominio.usuario;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionCamposInvalidos;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;
import com.tallerwebi.presentacion.perfil.DatosEditarPerfil;

import java.util.List;

public interface ServicioUsuario {
    Usuario buscarUsuarioPorEmailYPassword(String email, String password) throws UsuarioInexistente, ExcepcionBaseDeDatos, ExcepcionCamposInvalidos;

    void guardar(Usuario usuario) throws ExcepcionBaseDeDatos, UsuarioInexistente, UsuarioExistente;

    Usuario buscarUsuarioPorEmail(String email) throws UsuarioInexistente, ExcepcionBaseDeDatos;

    void modificar(DatosEditarPerfil datosEditarPerfil) throws ExcepcionBaseDeDatos, UsuarioInexistente, ExcepcionCamposInvalidos; //DATOS EDITAR USUARIO

    Usuario obtenerUsuarioPorId(Long id) throws ExcepcionBaseDeDatos, UsuarioInexistente;

    List<Usuario> obtenerAmigosDeUnUsuario(Long idUsuario) throws ExcepcionBaseDeDatos, UsuarioInexistente;

    void actualizarPlan(Usuario usuario) throws ExcepcionBaseDeDatos;
}
