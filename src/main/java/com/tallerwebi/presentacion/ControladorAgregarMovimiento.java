package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.agregarMovimiento.ServicioAgregarMovimiento;
import com.tallerwebi.dominio.movimiento.Movimiento;
import com.tallerwebi.dominio.movimiento.ServicioMovimiento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ControladorAgregarMovimiento {

    private ServicioAgregarMovimiento servicioAgregarMovimiento;

    @Autowired
    public ControladorAgregarMovimiento(ServicioAgregarMovimiento servicioAgregarMovimiento) {
        this.servicioAgregarMovimiento = servicioAgregarMovimiento;
    }

    @RequestMapping("/agregar-movimiento")
    public ModelAndView irAAgregarMovimiento() {

        ModelMap modelo = new ModelMap();
        modelo.put("agregarMovimiento", new DatosAgregarMovimiento());
        return new ModelAndView("agregar-movimiento", modelo);
    }

    @PostMapping("/nuevo-movimiento") // La idea es que este método se utilice cuando se manda el form de nuevo movimiento, es decir que nuevo-movimiento sea el action del form
    public ModelAndView ingresarNuevoMovimiento(@ModelAttribute("movimiento") Movimiento movimiento) {
        ModelMap model = new ModelMap();
        try{
            servicioAgregarMovimiento.nuevoMovimiento(movimiento);
        } catch (Exception e){
            model.put("error", "Error al ingresar un nuevo movimiento");
            return new ModelAndView("redirect:/agregar-movimiento", model); //
        }
        return new ModelAndView("redirect:/agregar-movimiento");
    }
}
