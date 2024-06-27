package com.tallerwebi.presentacion.movimientoCompartido;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;
import com.tallerwebi.dominio.movimiento.Movimiento;
import com.tallerwebi.dominio.movimientoCompartido.ServicioMovimientoCompartido;
import com.tallerwebi.dominio.notificacion.Notificacion;
import com.tallerwebi.dominio.usuario.ServicioUsuario;
import com.tallerwebi.dominio.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ControladorMovimientoCompartido {

    private ServicioMovimientoCompartido servicioMovimientoCompartido;
    private ServicioUsuario servicioUsuario;

    @Autowired
    public ControladorMovimientoCompartido(ServicioMovimientoCompartido servicioMovimientoCompartido, ServicioUsuario servicioUsuario) {
        this.servicioMovimientoCompartido = servicioMovimientoCompartido;
        this.servicioUsuario = servicioUsuario;
    }

    @GetMapping("/movimientos-compartidos")
    public ModelAndView irAMovimientosCompartidos(HttpServletRequest request) throws ExcepcionBaseDeDatos {
        ModelMap modelo = new ModelMap();
        HttpSession httpSession = request.getSession(false);
        if (request.getSession(false) == null) {
            return new ModelAndView("redirect:/login");
        }
        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        List<Notificacion> solicitudesEnviadas = servicioMovimientoCompartido.obtenerSolicitudesEnviadas(idUsuario);
        modelo.put("solicitudesEnviadas", solicitudesEnviadas);
        List<Usuario> amigos = servicioMovimientoCompartido.obtenerAmigos(idUsuario);
        modelo.put("amigos", amigos);
        return new ModelAndView("movimientos-compartidos", modelo);
    }

    @GetMapping("/movimiento/panel")
    public ModelAndView volverAPanel(HttpServletRequest request) {
        if (request.getSession(false) == null) {
            return new ModelAndView("redirect:/login");
        }
        return new ModelAndView("redirect:/panel");
    }

    @GetMapping("/movimientos-compartidos/agregar-amigo")
    public ModelAndView irAAgregarAmigo(HttpServletRequest request) throws ExcepcionBaseDeDatos {
        HttpSession httpSession = request.getSession(false);
        ModelMap modelo = new ModelMap();
        if (request.getSession(false) == null) {
            return new ModelAndView("redirect:/login");
        }
        modelo.put("nuevoAmigo", new Usuario());
        return new ModelAndView("agregar-amigo", modelo);
    }

    @PostMapping("/movimientos-compartidos/agregar-nuevo-amigo")
    public ModelAndView agregarNuevoAmigo(@ModelAttribute("nuevoAmigo") Usuario nuevoAmigo, HttpServletRequest httpServletRequest) throws ExcepcionBaseDeDatos {
        HttpSession httpSession = httpServletRequest.getSession(false);
        ModelMap modelo = new ModelMap();
        if (httpSession == null)
            return new ModelAndView("redirect:/login");

        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        try {
            servicioMovimientoCompartido.agregarNuevoAmigo(idUsuario, nuevoAmigo.getEmail());
        } catch (ExcepcionBaseDeDatos e) {
            modelo.put("errores", ExcepcionBaseDeDatos.getMensaje());
            return new ModelAndView("agregar-amigo", modelo);
        }
        return new ModelAndView("redirect:/movimientos-compartidos");
    }

    @PostMapping("/movimientos-compartidos/eliminarSolicitud/{id}")
    public ModelAndView eliminarSolicitud(@PathVariable Long id, HttpServletRequest httpServletRequest) throws ExcepcionBaseDeDatos{
        HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpSession == null)
            return new ModelAndView("redirect:/login");

        servicioMovimientoCompartido.eliminarSolicitud(id);
        return new ModelAndView("redirect:/movimientos-compartidos");
    }

    @PostMapping("/movimientos-compartidos/aceptarSolicitud/{id}")
    public ModelAndView aceptarSolicitud(@PathVariable Long id, HttpServletRequest httpServletRequest) throws ExcepcionBaseDeDatos{
        HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpSession == null)
            return new ModelAndView("redirect:/login");

        servicioMovimientoCompartido.aceptarSolicitud(id);
        return new ModelAndView("redirect:/notificaciones");
    }

    @PostMapping("/movimientos-compartidos/eliminarAmigo/{idAmigo}")
    public ModelAndView eliminarAmigo(@PathVariable Long idAmigo, HttpServletRequest httpServletRequest) throws ExcepcionBaseDeDatos{
        HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpSession == null)
            return new ModelAndView("redirect:/login");
        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");

        servicioMovimientoCompartido.eliminarAmigo(idAmigo, idUsuario);
        return new ModelAndView("redirect:/movimientos-compartidos");
    }

    @PostMapping("/movimientos-compartidos/verMovimientosCompartidos/{idAmigo}")
    public ModelAndView verMovimientosCompartidos(@PathVariable Long idAmigo, HttpServletRequest httpServletRequest) throws ExcepcionBaseDeDatos, UsuarioInexistente {
        HttpSession httpSession = httpServletRequest.getSession(false);
        ModelMap modelo = new ModelMap();
        if (httpSession == null)
            return new ModelAndView("redirect:/login");
        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        List<Movimiento> movimientosCompartidos = servicioMovimientoCompartido.obtenerMovimientosCompartidos(idAmigo, idUsuario);
        Usuario amigo = servicioUsuario.obtenerUsuarioPorId(idAmigo);
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario);
        modelo.put("amigo", amigo);
        modelo.put("usuario", usuario);
        modelo.put("movimientosCompartidos", movimientosCompartidos);
        return new ModelAndView("movimientos-compartidos-amigo", modelo);
    }

}
