package com.tallerwebi.presentacion;


import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.movimiento.Movimiento;
import com.tallerwebi.dominio.movimiento.ServicioMovimiento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
public class ControladorMovimiento {

    private ServicioMovimiento servicioMovimiento;

    @Autowired
    public ControladorMovimiento(ServicioMovimiento servicioMovimiento) {
        this.servicioMovimiento = servicioMovimiento;
    }

    @GetMapping("/movimientos")
    public ModelAndView obtenerMovimientos(HttpServletRequest httpServletRequest) {
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

    @GetMapping("/movimientos/editar/{id}")
    public ModelAndView irAFormularioEditarMovimiento(HttpServletRequest httpServletRequest, @PathVariable Long id) {
        ModelMap modelo = new ModelMap();
        HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpSession == null) {
            return new ModelAndView("redirect:/login");
        }
        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        Optional<Movimiento> movimiento = servicioMovimiento.obtenerMovimientoPorId(idUsuario, id);
        modelo.put("movimiento", movimiento.get());
        return new ModelAndView("editar-movimiento", modelo);
    }


    @PostMapping ("/movimientos/editar")
    public ModelAndView editarMovimiento(@ModelAttribute("movimiento") Movimiento movimiento, HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession(false);
        if(httpSession == null){
            return new ModelAndView("redirect:/login");
        }
        servicioMovimiento.editarMovimiento(movimiento);
        return new ModelAndView("redirect:/movimientos");
    }
}
