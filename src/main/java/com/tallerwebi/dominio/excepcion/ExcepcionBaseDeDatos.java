package com.tallerwebi.dominio.excepcion;

public class ExcepcionBaseDeDatos extends Exception{
     public ExcepcionBaseDeDatos(String mensaje) {
         super(mensaje);
     }

    public ExcepcionBaseDeDatos(String message, Throwable cause) {
        super(message, cause);
    }
}
