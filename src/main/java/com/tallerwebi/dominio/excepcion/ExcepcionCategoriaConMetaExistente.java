package com.tallerwebi.dominio.excepcion;

public class ExcepcionCategoriaConMetaExistente extends Exception {
    private static final String mensaje = "La categoria que seleccionaste ya tiene una meta establecida.";
    public ExcepcionCategoriaConMetaExistente() {
        super(mensaje);
    }
}
