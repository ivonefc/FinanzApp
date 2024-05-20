package com.tallerwebi.dominio.usuario;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;

public interface RepositorioUsuario {

    Usuario buscarUsuarioPorEmailYPassword(String email, String password) throws UsuarioInexistente, ExcepcionBaseDeDatos;
    void guardar(Usuario usuario);
    Usuario buscarUsuarioPorEmail(String email) throws UsuarioInexistente, ExcepcionBaseDeDatos;
    void modificar(Usuario usuario);
    Usuario obtenerUsuarioPorId(Long id);
}


