package com.tallerwebi.presentacion.movimientoCompartido;

import com.tallerwebi.dominio.excepcion.*;
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
    public ModelAndView irAMovimientosCompartidos(HttpServletRequest request) throws ExcepcionBaseDeDatos, UsuarioInexistente {
        ModelMap modelo = new ModelMap();
        HttpSession httpSession = request.getSession(false);
        if (request.getSession(false) == null)
            return new ModelAndView("redirect:/login");

        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario);
        if (usuario.getRol().equals("FREE"))
            return new ModelAndView("redirect:/panel");

        modelo.put("usuario", usuario);
        List<Notificacion> solicitudesEnviadas = servicioMovimientoCompartido.obtenerSolicitudesEnviadas(idUsuario);
        modelo.put("solicitudesEnviadas", solicitudesEnviadas);
        List<Usuario> amigos = servicioMovimientoCompartido.obtenerAmigos(idUsuario);
        modelo.put("amigos", amigos);
        return new ModelAndView("movimientos-compartidos", modelo);
    }

    @GetMapping("/movimientos-compartidos/panel")
    public ModelAndView volverAPanel(HttpServletRequest request){
        if (request.getSession(false) == null)
            return new ModelAndView("redirect:/login");

        return new ModelAndView("redirect:/panel");
    }

    @GetMapping("/movimientos-compartidos/agregar-amigo")
    public ModelAndView irAAgregarAmigo(HttpServletRequest request) throws ExcepcionBaseDeDatos, UsuarioInexistente {
        HttpSession httpSession = request.getSession(false);
        if (httpSession == null)
            return new ModelAndView("redirect:/login");

        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario);
        if (usuario.getRol().equals("FREE"))
            return new ModelAndView("redirect:/panel");

        ModelMap modelo = new ModelMap();
        modelo.put("usuario", usuario);
        modelo.put("nuevoAmigo", new Usuario());
        return new ModelAndView("agregar-amigo", modelo);
    }

    @PostMapping("/movimientos-compartidos/agregar-nuevo-amigo")
    public ModelAndView agregarNuevoAmigo(@ModelAttribute("nuevoAmigo") Usuario nuevoAmigo, HttpServletRequest httpServletRequest) throws Excepcion, ExcepcionBaseDeDatos {
        HttpSession httpSession = httpServletRequest.getSession(false);
        ModelMap modelo = new ModelMap();
        if (httpSession == null)
            return new ModelAndView("redirect:/login");

        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        Usuario usuario1 = servicioUsuario.obtenerUsuarioPorId(idUsuario);
        if (usuario1.getRol().equals("FREE"))
            return new ModelAndView("redirect:/panel");

        try {
            servicioMovimientoCompartido.agregarNuevoAmigo(idUsuario, nuevoAmigo.getEmail());
        } catch (Excepcion e) {
            modelo.put("errores", e.getMessage());
            Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario);
            modelo.put("usuario", usuario);
            modelo.put("nuevoAmigo", new Usuario());
            return new ModelAndView("agregar-amigo", modelo);
        }
        return new ModelAndView("redirect:/movimientos-compartidos");
    }

    @PostMapping("/movimientos-compartidos/eliminarSolicitud/{id}")
    public ModelAndView eliminarSolicitud(@PathVariable Long id, HttpServletRequest httpServletRequest) throws ExcepcionBaseDeDatos, ExcepcionNotificacionInexistente {
        HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpSession == null)
            return new ModelAndView("redirect:/login");

        servicioMovimientoCompartido.eliminarSolicitud(id);
        return new ModelAndView("redirect:/movimientos-compartidos");
    }

    @PostMapping("/movimientos-compartidos/aceptarSolicitud/{id}")
    public ModelAndView aceptarSolicitud(@PathVariable Long id, HttpServletRequest httpServletRequest) throws ExcepcionBaseDeDatos, ExcepcionNotificacionInexistente {
        HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpSession == null)
            return new ModelAndView("redirect:/login");

        servicioMovimientoCompartido.aceptarSolicitud(id);
        return new ModelAndView("redirect:/notificaciones");
    }

    @PostMapping("/movimientos-compartidos/eliminarAmigo/{idAmigo}")
    public ModelAndView eliminarAmigo(@PathVariable Long idAmigo, HttpServletRequest httpServletRequest) throws ExcepcionBaseDeDatos, UsuarioInexistente {
        HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpSession == null)
            return new ModelAndView("redirect:/login");

        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario);
        if (usuario.getRol().equals("FREE"))
            return new ModelAndView("redirect:/panel");

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
        if (amigo.getRol().equals("FREE"))
            return new ModelAndView("redirect:/panel");

        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario);
        if (usuario.getRol().equals("FREE"))
            return new ModelAndView("redirect:/panel");
        modelo.put("amigo", amigo);
        modelo.put("usuario", usuario);
        modelo.put("movimientosCompartidos", movimientosCompartidos);
        return new ModelAndView("movimientos-compartidos-amigo", modelo);
    }

}
