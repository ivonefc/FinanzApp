package com.tallerwebi.dominio.excepcion;

public class ExcepcionBaseDeDatos extends Exception{
    private static final String mensaje = "Usuario no encontrado o usuario inválido";
    public ExcepcionBaseDeDatos() {
         super(mensaje);
     }
    public ExcepcionBaseDeDatos(String mensaje) {
        super(mensaje);
    }

    public ExcepcionBaseDeDatos(Throwable cause) {
        super(mensaje, cause);
    }
    public ExcepcionBaseDeDatos(String mensaje, Throwable cause) {
        super(mensaje, cause);
    }

    public static String getMensaje() {
        return mensaje;
    }
}
