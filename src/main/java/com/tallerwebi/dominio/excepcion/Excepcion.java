package com.tallerwebi.dominio.excepcion;

public class Excepcion extends Exception{
    private static final String mensaje = "Excepción general";
     public Excepcion() {
         super(mensaje);
     }
    public Excepcion(String mensaje) {
        super(mensaje);
    }
}
