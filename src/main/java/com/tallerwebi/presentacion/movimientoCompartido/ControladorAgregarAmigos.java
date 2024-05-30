package com.tallerwebi.presentacion.movimientoCompartido;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ControladorAgregarAmigos {

    @GetMapping("/agregar-amigos")
    public ModelAndView irAAgregarAmigos(HttpServletRequest request){
        if (request.getSession(false) == null) {
            return new ModelAndView("redirect:/login");
        }
        return new ModelAndView("agregar-amigos");
    }

    @GetMapping("/amigos/panel")
    public ModelAndView volverAPanel(HttpServletRequest request){
        if (request.getSession(false) == null) {
            return new ModelAndView("redirect:/login");
        }
        return new ModelAndView("redirect:/panel");
    }
}
