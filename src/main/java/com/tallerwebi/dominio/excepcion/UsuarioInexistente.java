package com.tallerwebi.dominio.excepcion;

public class UsuarioInexistente extends Excepcion {
    private static final String mensaje = "No se encontro usuario";
    public UsuarioInexistente() {
        super(mensaje);
    }
    public UsuarioInexistente(String mensaje) {
        super(mensaje);
    }

}
