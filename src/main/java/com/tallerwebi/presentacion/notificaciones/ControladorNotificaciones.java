package com.tallerwebi.presentacion.notificaciones;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;
import com.tallerwebi.dominio.movimientoCompartido.ServicioMovimientoCompartido;
import com.tallerwebi.dominio.notificacion.Notificacion;
import com.tallerwebi.dominio.usuario.ServicioUsuario;
import com.tallerwebi.dominio.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ControladorNotificaciones {

    private ServicioMovimientoCompartido servicioMovimientoCompartido;
    private ServicioUsuario servicioUsuario;

    @Autowired
    public ControladorNotificaciones(ServicioMovimientoCompartido servicioMovimientoCompartido, ServicioUsuario servicioUsuario) {
        this.servicioMovimientoCompartido = servicioMovimientoCompartido;
        this.servicioUsuario = servicioUsuario;
    }

    @GetMapping("/notificaciones")
    public ModelAndView irANotificaciones(HttpServletRequest request) throws ExcepcionBaseDeDatos, UsuarioInexistente {
        ModelMap modelo = new ModelMap();
        HttpSession httpSession = request.getSession(false);
        if (request.getSession(false) == null)
            return new ModelAndView("redirect:/login");

        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario);
        modelo.put("usuario", usuario);
        List<Notificacion> solicitudesRecibidas = servicioMovimientoCompartido.obtenerSolicitudesRecibidas(idUsuario);
        List<Notificacion> solicitudesAceptadas = servicioMovimientoCompartido.obtenerSolicitudesAceptadas(idUsuario);
        modelo.put("solicitudesRecibidas", solicitudesRecibidas);
        modelo.put("solicitudesAceptadas", solicitudesAceptadas);
        return new ModelAndView("notificaciones", modelo);
    }

}

