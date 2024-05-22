package com.tallerwebi.presentacion.autenticacion;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class ControladorCerrarSesion {

    @GetMapping(path = "/cerrar-sesion")
    public ModelAndView cerrarSesion(HttpSession session) {
        if (session != null)
            session.invalidate(); // Invalida la sesión actual

        return new ModelAndView("redirect:/login");
    }

}
