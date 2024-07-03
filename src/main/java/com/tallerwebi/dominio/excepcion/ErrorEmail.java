package com.tallerwebi.dominio.excepcion;

public class ErrorEmail extends Exception{
    private static final String mensaje = "Error al enviar email";
    public ErrorEmail() {
        super(mensaje);
    }
}
