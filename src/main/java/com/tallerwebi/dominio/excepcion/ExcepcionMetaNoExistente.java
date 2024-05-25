package com.tallerwebi.dominio.excepcion;

public class ExcepcionMetaNoExistente extends Throwable{
    private static final String mensaje = "La meta buscada no existe.";
    public ExcepcionMetaNoExistente() {
        super(mensaje);
    }
}
