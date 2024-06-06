package com.tallerwebi.presentacion.perfil;

import com.tallerwebi.dominio.usuario.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ControladorMiPerfil {

    @GetMapping("/mi-perfil")
    public ModelAndView irAMiPerfil(HttpServletRequest request){
        if (request.getSession(false) == null)
            return new ModelAndView("redirect:/login");

        return new ModelAndView("mi-perfil");
    }

//    @GetMapping("/mi-perfil/{id}")
//    public ModelAndView obtenerDatosDelUsuario(HttpServletRequest request, @PathVariable Long idUsuario){
//        if (request.getSession(false) == null)
//            return new ModelAndView("redirect:/login");
//
//
//
//    }

    @GetMapping("/perfil/panel")
    public ModelAndView volverAPanel(HttpServletRequest request){
        if (request.getSession(false) == null)
            return new ModelAndView("redirect:/login");

        return new ModelAndView("redirect:/panel");
    }

}
