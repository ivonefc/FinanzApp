package com.tallerwebi.dominio.usuario;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;

public interface RepositorioUsuario {

    void guardar(Usuario usuario) throws ExcepcionBaseDeDatos, UsuarioExistente; //CREAR USUARIO Y GUARDARLO EN LA BDD

    void modificar(Usuario usuario); //DATOS EDITAR USUARIO

    Usuario obtenerUsuarioPorId(Long id) throws ExcepcionBaseDeDatos, UsuarioInexistente;

    Usuario buscarUsuarioPorEmailYPassword(String email, String password) throws UsuarioInexistente, ExcepcionBaseDeDatos;

    Usuario buscarUsuarioPorEmail(String email) throws UsuarioInexistente, ExcepcionBaseDeDatos;
}

