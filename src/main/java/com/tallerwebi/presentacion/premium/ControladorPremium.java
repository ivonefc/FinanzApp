package com.tallerwebi.presentacion.premium;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class ControladorPremium {

    @GetMapping("/metodo-pago")
    public ModelAndView irAMetodoPagoPremium(HttpServletRequest request) throws ExcepcionBaseDeDatos {
        HttpSession session = request.getSession(false);
        if (session == null)
            return new ModelAndView("redirect:/login");

        return new ModelAndView("metodo-pago");
    }
}
