package com.tallerwebi.dominio.excepcion;

public class UsuarioExistente extends Excepcion {
    private static final String mensaje = "El usuario ya existe";
    public UsuarioExistente(){
        super(mensaje);
    }
}

