package com.tallerwebi.dominio.excepcion;

public class ExcepcionMovimientoNoEncontrado extends Throwable {
    private static final String mensaje = "No se encontro el movimiento";
    public ExcepcionMovimientoNoEncontrado(){super(mensaje);}
    public ExcepcionMovimientoNoEncontrado(String mensaje) {
        super(mensaje);
    }
}
