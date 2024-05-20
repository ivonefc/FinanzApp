package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionCamposInvalidos;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;
import com.tallerwebi.presentacion.DatosRegistroUsuario;

public interface ServicioLogin {

    Usuario consultarUsuario(String email, String password) throws UsuarioInexistente, ExcepcionBaseDeDatos;
    void registrar(DatosRegistroUsuario datosRegistroUsuario) throws UsuarioExistente, ExcepcionBaseDeDatos, ExcepcionCamposInvalidos;

}
