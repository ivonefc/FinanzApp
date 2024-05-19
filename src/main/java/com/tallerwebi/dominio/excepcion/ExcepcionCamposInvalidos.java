package com.tallerwebi.dominio.excepcion;

import java.util.Map;

public class ExcepcionCamposInvalidos extends Exception {
    private Map<String, String> errores;
    public ExcepcionCamposInvalidos(Map<String, String> errores) {
        this.errores = errores;
    }
    public Map<String, String> getErrores() {
        return this.errores;
    }
}
