package com.tallerwebi.presentacion.excepcion;

import com.tallerwebi.dominio.excepcion.*;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ControladorDeExcepciones {
    @ExceptionHandler({ExcepcionBaseDeDatos.class, ExcepcionMovimientoNoEncontrado.class, PaginaInexistente.class, ExcepcionMetaNoExistente.class})
    public ModelAndView excepcion(Exception ex){
        ModelMap modelo = new ModelMap();
        modelo.put("error", ex.getMessage());
        return new ModelAndView("error", modelo);
    }

    @ExceptionHandler(ExcepcionExportacionDeArchivo.class)
    @ResponseBody
    public String excepcionExportacion(Exception ex){
        return ex.getMessage();
    }

    @ExceptionHandler(JDBCConnectionException.class)
    public ModelAndView excepcionDeConexion(JDBCConnectionException ex){
        ModelMap modelo = new ModelMap();
        modelo.put("error", "No se pudo establecer la conexión con la base de datos.");
        return new ModelAndView("error", modelo);
    }
}
