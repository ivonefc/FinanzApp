package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
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
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class ControladorMovimiento {

    private ServicioMovimiento servicioMovimiento;
    private ServicioCategoria servicioCategoria;

    @Autowired
    public ControladorMovimiento(ServicioMovimiento servicioMovimiento, ServicioCategoria servicioCategoria) {
        this.servicioMovimiento = servicioMovimiento;
        this.servicioCategoria = servicioCategoria;
    }

    @GetMapping("/movimientos")
    public ModelAndView obtenerMovimientos(HttpServletRequest httpServletRequest) throws ExcepcionBaseDeDatos{
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

    @GetMapping("/movimientos/{fecha}")
    @ResponseBody
    public List<Movimiento> obtenerMovimientosPorFecha(@PathVariable String fecha, HttpServletRequest httpServletRequest){
        HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpSession == null) {
            return null;
        }
        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        LocalDate fechaLD = LocalDate.parse(fecha);
        List<Movimiento> movimientos = servicioMovimiento.obtenerMovimientosPorFecha(idUsuario, fechaLD);
        return movimientos;
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
        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        CategoriaMovimiento categoriaMovimiento = servicioCategoria.obtenerCategoriaPorNombre(movimiento.getCategoria().getNombre());
        movimiento.setCategoria(categoriaMovimiento);
        servicioMovimiento.editarMovimiento(idUsuario, movimiento);
        return new ModelAndView("redirect:/movimientos");
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

    @PostMapping("/movimientos/nuevo-movimiento") // La idea es que este método se utilice cuando se manda el form de nuevo movimiento, es decir que nuevo-movimiento sea el action del form
    public ModelAndView ingresarNuevoMovimiento(@ModelAttribute("movimiento") DatosAgregarMovimiento datosAgregarMovimiento, HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession(false);

        if(httpSession==null){
            return new ModelAndView("redirect:/login");
        }

        System.out.println(datosAgregarMovimiento);

        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        String descripcion = datosAgregarMovimiento.getDescripcion();
        Double monto = datosAgregarMovimiento.getMonto();
        CategoriaMovimiento categoriaMovimiento = servicioCategoria.obtenerCategoriaPorNombre(datosAgregarMovimiento.getCategoria());
        Movimiento movimiento = new Movimiento(descripcion, monto, LocalDate.now());

        servicioMovimiento.nuevoMovimiento(idUsuario, movimiento, categoriaMovimiento);

        return new ModelAndView("redirect:/movimientos");
    }
}
