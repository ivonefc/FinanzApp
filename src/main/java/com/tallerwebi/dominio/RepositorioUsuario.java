package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;

public interface RepositorioUsuario {

    Usuario buscarUsuario(String email, String password);
    void guardar(Usuario usuario);
    Usuario buscar(String email);
    void modificar(Usuario usuario);
    Usuario obtenerUsuarioPorId(Long id) throws ExcepcionBaseDeDatos;
}

