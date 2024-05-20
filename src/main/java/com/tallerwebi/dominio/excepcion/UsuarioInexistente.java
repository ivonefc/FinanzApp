package com.tallerwebi.dominio.excepcion;

public class UsuarioInexistente extends Exception {
    private static final String mensaje = "No se encontro usuario";
    public UsuarioInexistente() {
        super(mensaje);
    }
}
