package com.tallerwebi.presentacion.panel;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ControladorPanel {
    @GetMapping("/panel")
    public ModelAndView irAPanel(HttpServletRequest request){
        if (request.getSession(false) == null) {
            return new ModelAndView("redirect:/login");
        }
        return new ModelAndView("panel");
    }
}
