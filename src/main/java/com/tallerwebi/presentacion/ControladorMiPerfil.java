package com.tallerwebi.presentacion;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ControladorMiPerfil {

    @GetMapping("/mi-perfil")
    public ModelAndView irAMiPerfil(HttpServletRequest request){
        if (request.getSession(false) == null) {
            return new ModelAndView("redirect:/login");
        }
        return new ModelAndView("mi-perfil");
    }

}
