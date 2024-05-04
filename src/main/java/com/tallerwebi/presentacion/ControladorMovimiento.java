package com.tallerwebi.presentacion;


import com.tallerwebi.dominio.movimiento.Movimiento;
import com.tallerwebi.dominio.movimiento.ServicioMovimiento;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ControladorMovimiento{

    private ServicioMovimiento servicioMovimiento;

    public ControladorMovimiento(ServicioMovimiento servicioMovimiento) {
        this.servicioMovimiento = servicioMovimiento;
    }

    @GetMapping("/movimientos")
    public ModelAndView obtenerMovimientos (HttpServletRequest httpServletRequest){
        ModelMap model = new ModelMap();
        HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpSession == null) {
            return new ModelAndView("redirect:/login");
        }
        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        List<Movimiento> movimientos = servicioMovimiento.obtenerMovimientos(idUsuario);
        model.put("movimientos", movimientos);
        return new ModelAndView("movimientos", model);
    }
}
