package com.tallerwebi.presentacion.panel;

import com.tallerwebi.dominio.categoria.CategoriaMovimiento;
import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.movimiento.Movimiento;
import com.tallerwebi.dominio.movimiento.ServicioMovimiento;
import com.tallerwebi.dominio.panel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class ControladorPanel {

    private ServicioPanel servicioPanel;

    @Autowired
    public ControladorPanel(ServicioPanel servicioPanel) {
        this.servicioPanel = servicioPanel;
    }
    public ControladorPanel(){}


    @GetMapping("/panel")
    public ModelAndView irAPanel(HttpServletRequest request){
        if (request.getSession(false) == null) {
            return new ModelAndView("redirect:/login");
        }
        return new ModelAndView("panel");
    }

    @GetMapping("/panel/egresos")
    @ResponseBody
    public List<Movimiento> obtenerEgresos(HttpServletRequest httpServletRequest) throws ExcepcionBaseDeDatos {
        HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpSession == null)
            return null;
        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        List<Movimiento> egresos = servicioPanel.obtenerMovimientosEgresosPorUsuario(idUsuario);
        return egresos;
    }

    @GetMapping("/panel/ingresos")
    @ResponseBody
    public List<Movimiento> obtenerIngresos(HttpServletRequest httpServletRequest) throws ExcepcionBaseDeDatos {
        HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpSession == null)
            return null;
        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        List<Movimiento> ingresos = servicioPanel.obtenerMovimientosIngresosPorUsuario(idUsuario);
        return ingresos;
    }

    /*@GetMapping("/panel/montosPorCategoria")
    @ResponseBody
    public Map<String, Double> obtenerMontosPorCategoria(HttpServletRequest httpServletRequest) throws ExcepcionBaseDeDatos {
        HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpSession == null)
            return null;
        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        Map<String, Double> montos = servicioPanel.obtenerMontosPorCategoria(idUsuario);
        return montos;
    }*/
}
