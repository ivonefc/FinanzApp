package com.tallerwebi.dominio.autenticacion;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionCamposInvalidos;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;
import com.tallerwebi.dominio.usuario.Usuario;
import com.tallerwebi.presentacion.autenticacion.DatosRegistroUsuario;

public interface ServicioLogin {

    Usuario consultarUsuario(String email, String password) throws UsuarioInexistente, ExcepcionBaseDeDatos, ExcepcionCamposInvalidos;
    void registrar(DatosRegistroUsuario datosRegistroUsuario) throws UsuarioExistente, ExcepcionBaseDeDatos, ExcepcionCamposInvalidos, UsuarioInexistente;

}
