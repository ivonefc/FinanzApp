package com.tallerwebi.presentacion.ajustes;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ControladorAjustes {

    @GetMapping("/ajustes")
    public ModelAndView irAAjustes(HttpServletRequest request){
        if (request.getSession(false) == null)
            return new ModelAndView("redirect:/login");

        return new ModelAndView("ajustes");
    }

    @GetMapping("/ajustes/panel")
    public ModelAndView volverAPanel(HttpServletRequest request){
        if (request.getSession(false) == null)
            return new ModelAndView("redirect:/login");

        return new ModelAndView("redirect:/panel");
    }

    @GetMapping("/mi-perfil")
    public ModelAndView irAMiPerfil(HttpServletRequest request){
        if (request.getSession(false) == null)
            return new ModelAndView("redirect:/login");

        return new ModelAndView("redirect:/mi-perfil");
    }

    @GetMapping("/categorias/editar-colores")
    public ModelAndView irAEditarColores(HttpServletRequest request){
        if (request.getSession(false) == null)
            return new ModelAndView("redirect:/login");

        return new ModelAndView("redirect:/categorias/editar-colores");
    }

}
