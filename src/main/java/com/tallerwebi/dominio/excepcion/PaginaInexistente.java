package com.tallerwebi.dominio.excepcion;

public class PaginaInexistente extends Exception {
    private static String mensaje = "La pagina no existe";
    public PaginaInexistente() {
        super(mensaje);
    }
}
