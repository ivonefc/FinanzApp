package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.RepositorioUsuario;
import com.tallerwebi.dominio.ServicioLogin;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.agregarMovimiento.RepositorioAgregarMovimiento;
import com.tallerwebi.dominio.agregarMovimiento.ServicioAgregarMovimiento;
import com.tallerwebi.dominio.movimiento.Movimiento;
import com.tallerwebi.dominio.movimiento.ServicioMovimiento;
import com.tallerwebi.infraestructura.RepositorioAgregarMovimientoImpl;
import com.tallerwebi.infraestructura.RepositorioUsuarioImpl;
import org.hibernate.SessionFactory;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Controller
public class ControladorAgregarMovimiento {

    private ServicioAgregarMovimiento servicioAgregarMovimiento;
    private ServicioMovimiento servicioMovimiento;


    @Autowired
    public ControladorAgregarMovimiento(ServicioAgregarMovimiento servicioAgregarMovimiento) {
        this.servicioAgregarMovimiento = servicioAgregarMovimiento;
        this.servicioMovimiento = servicioMovimiento;
    }

    @RequestMapping("/agregar-movimiento")
    public ModelAndView irAAgregarMovimiento(HttpServletRequest httpServletRequest){
        ModelMap modelo = new ModelMap();
        HttpSession httpSession = httpServletRequest.getSession(false);

        if (httpSession == null) {
            return new ModelAndView("redirect:/login");
        }

        modelo.put("agregarMovimiento", new DatosAgregarMovimiento());
        return new ModelAndView("agregar-movimiento", modelo);
    }

    @PostMapping("/nuevo-movimiento") // La idea es que este método se utilice cuando se manda el form de nuevo movimiento, es decir que nuevo-movimiento sea el action del form
    public ModelAndView ingresarNuevoMovimiento(@ModelAttribute("movimiento") Movimiento movimiento, HttpServletRequest httpServletRequest) {
        ModelMap model = new ModelMap();

        try{
            movimiento.setFechayHora(LocalDateTime.now());
            servicioAgregarMovimiento.nuevoMovimiento(movimiento);

        } catch (Exception e){
            model.put("error", "Error al ingresar un nuevo movimiento");
            return new ModelAndView("redirect:/agregar-movimiento", model); //
        }
        return new ModelAndView("redirect:/agregar-movimiento");
    }
}
