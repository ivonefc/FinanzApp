package com.tallerwebi.dominio.excepcion;

public class UsuarioExistente extends Exception {
    private static final String mensaje = "El usuario ya existe";
    public UsuarioExistente(){
        super(mensaje);
    }
}

