package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionCamposInvalidos;
import com.tallerwebi.dominio.movimiento.*;
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
import java.time.LocalDate;
import java.util.List;


@Controller
public class ControladorMovimiento {

    private ServicioMovimiento servicioMovimiento;

    @Autowired
    public ControladorMovimiento(ServicioMovimiento servicioMovimiento) {
        this.servicioMovimiento = servicioMovimiento;
    }

    @GetMapping("/movimientos")
    public ModelAndView obtenerMovimientos(HttpServletRequest httpServletRequest) throws ExcepcionBaseDeDatos{
        ModelMap model = new ModelMap();
        HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpSession == null)
            return new ModelAndView("redirect:/login");

        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        List<Movimiento> movimientos = servicioMovimiento.obtenerMovimientos(idUsuario);
        model.put("movimientos", movimientos);
        return new ModelAndView("movimientos", model);
    }

    @GetMapping("/movimientos/{fecha}")
    @ResponseBody
    public List<Movimiento> obtenerMovimientosPorFecha(@PathVariable String fecha, HttpServletRequest httpServletRequest){
        HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpSession == null)
            return null;

        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        LocalDate fechaLD = LocalDate.parse(fecha);
        List<Movimiento> movimientos = servicioMovimiento.obtenerMovimientosPorFecha(idUsuario, fechaLD);
        return movimientos;
    }

    @GetMapping("/movimientos/editar/{id}")
    public ModelAndView irAFormularioEditarMovimiento(HttpServletRequest httpServletRequest, @PathVariable Long id) {
        ModelMap modelo = new ModelMap();
        HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpSession == null)
            return new ModelAndView("redirect:/login");
        }
        Movimiento movimiento = servicioMovimiento.obtenerMovimientoPorId(id);
        DatosEditarMovimiento datosEditarMovimiento = DatosEditarMovimiento.contruirDesdeMovimiento(movimiento);
        modelo.put("movimiento", datosEditarMovimiento);
        return new ModelAndView("editar-movimiento", modelo);
    }


    @PostMapping ("/movimientos/editar")
    public ModelAndView editarMovimiento(@ModelAttribute("movimiento") DatosEditarMovimiento datosEditarMovimiento, HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession(false);
        ModelMap modelo = new ModelMap();
        if(httpSession == null){
            return new ModelAndView("redirect:/login");
        }
        try {
            servicioMovimiento.actualizarMovimiento(datosEditarMovimiento);
        } catch (ExcepcionCamposInvalidos e) {
            modelo.put("errores", e.getErrores());
            return new ModelAndView("editar-movimiento", modelo);
        }
        return new ModelAndView("redirect:/movimientos");
    }

    //FUNCION ELIMINAR MOVIMIENTO
    @PostMapping("/movimientos/editar/{idMovimiento}")
    public ModelAndView eliminarMovimiento(@PathVariable Long idMovimiento, HttpServletRequest httpServletRequest) throws ExcepcionBaseDeDatos{
        HttpSession httpSession = httpServletRequest.getSession(false);
        if(httpSession == null)
            return new ModelAndView("redirect:/login");

        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");

        Optional<Movimiento> optionalMovimiento = servicioMovimiento.obtenerMovimientoPorId(idUsuario, idMovimiento);
        if (optionalMovimiento.isEmpty()) {
            ModelAndView modelAndView = new ModelAndView("error");
            modelAndView.addObject("error", "No se encontró el movimiento con el ID proporcionado");
            return modelAndView;
        }

        try {
            servicioMovimiento.eliminarMovimiento(idUsuario, optionalMovimiento.get());
        } catch (ExcepcionBaseDeDatos e) {
            ModelAndView modelAndView = new ModelAndView("error");
            modelAndView.addObject("error", "Error en la base de datos");
            return modelAndView;
        }
        return new ModelAndView("redirect:/movimientos");
    }

    @RequestMapping("/agregar-movimiento")
    public ModelAndView irAAgregarMovimiento(HttpServletRequest httpServletRequest){
        ModelMap modelo = new ModelMap();
        HttpSession httpSession = httpServletRequest.getSession(false);

        if (httpSession == null)
            return new ModelAndView("redirect:/login");

        modelo.put("agregarMovimiento", new DatosAgregarMovimiento());
        return new ModelAndView("agregar-movimiento", modelo);
    }

    @PostMapping("/movimientos/nuevo-movimiento") // La idea es que este método se utilice cuando se manda el form de nuevo movimiento, es decir que nuevo-movimiento sea el action del form
    public ModelAndView ingresarNuevoMovimiento(@ModelAttribute("movimiento") DatosAgregarMovimiento datosAgregarMovimiento, HttpServletRequest httpServletRequest) throws ExcepcionBaseDeDatos{
        HttpSession httpSession = httpServletRequest.getSession(false);
        ModelMap modelo = new ModelMap();
        if(httpSession==null){
            return new ModelAndView("redirect:/login");
        }
        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        try {
            servicioMovimiento.nuevoMovimiento(idUsuario, datosAgregarMovimiento);
        } catch (ExcepcionCamposInvalidos e) {
            modelo.put("errores", e.getErrores());
            modelo.put("agregarMovimiento", new DatosAgregarMovimiento());
            return new ModelAndView("agregar-movimiento", modelo);
        }
        return new ModelAndView("redirect:/movimientos");
    }
}
