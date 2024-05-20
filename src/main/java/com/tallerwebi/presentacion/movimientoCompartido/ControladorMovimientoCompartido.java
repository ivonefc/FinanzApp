package com.tallerwebi.presentacion.movimientoCompartido;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ControladorMovimientoCompartido {

    @GetMapping("/movimientos-compartidos")
    public ModelAndView irAMovimientosCompartidos(HttpServletRequest request){
        if (request.getSession(false) == null) {
            return new ModelAndView("redirect:/login");
        }
        return new ModelAndView("movimientos-compartidos");
    }
}
