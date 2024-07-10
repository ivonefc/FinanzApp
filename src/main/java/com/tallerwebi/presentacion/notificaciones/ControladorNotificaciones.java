package com.tallerwebi.presentacion.notificaciones;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;
import com.tallerwebi.dominio.movimiento.ServicioMovimiento;
import com.tallerwebi.dominio.movimientoCompartido.ServicioMovimientoCompartido;
import com.tallerwebi.dominio.notificacion.Notificacion;
import com.tallerwebi.dominio.usuario.ServicioUsuario;
import com.tallerwebi.dominio.usuario.Usuario;
import com.tallerwebi.presentacion.movimiento.DatosAgregarMovimiento;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
public class ControladorNotificaciones {

    private final ServicioMovimiento servicioMovimiento;
    private ServicioMovimientoCompartido servicioMovimientoCompartido;
    private ServicioUsuario servicioUsuario;

    @Autowired
    public ControladorNotificaciones(ServicioMovimientoCompartido servicioMovimientoCompartido, ServicioUsuario servicioUsuario, ServicioMovimiento servicioMovimiento) {
        this.servicioMovimientoCompartido = servicioMovimientoCompartido;
        this.servicioUsuario = servicioUsuario;
        this.servicioMovimiento = servicioMovimiento;
    }

    @GetMapping("/notificaciones")
    public ModelAndView irANotificaciones(HttpServletRequest request) throws ExcepcionBaseDeDatos, UsuarioInexistente, JsonProcessingException {
        ModelMap modelo = new ModelMap();
        HttpSession httpSession = request.getSession(false);
        if (request.getSession(false) == null) {
            return new ModelAndView("redirect:/login");
        }
        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario);
        modelo.put("usuario", usuario);

        List<Notificacion> solicitudesRecibidas = servicioMovimientoCompartido.obtenerSolicitudesRecibidas(idUsuario);
        List<Notificacion> solicitudesAceptadas = servicioMovimientoCompartido.obtenerSolicitudesAceptadas(idUsuario);
        List<Notificacion> movimientosCompartidos = servicioMovimiento.obtenerMovimientosCompartidos(idUsuario);

        ObjectMapper objectMapper = new ObjectMapper();
        for (Notificacion notificacion : movimientosCompartidos) {
            try {
                DatosAgregarMovimiento datosAM = objectMapper.readValue(notificacion.getDatosAgregarMovimiento(), DatosAgregarMovimiento.class);
                notificacion.setDatosAgregarMovimientoObject(datosAM);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<Notificacion> notificaciones = new ArrayList<>();

        notificaciones.addAll(solicitudesRecibidas);
        notificaciones.addAll(solicitudesAceptadas);
        notificaciones.addAll(movimientosCompartidos);

        notificaciones.sort(Comparator.comparing(Notificacion::getFecha).reversed());

        modelo.put("notificaciones", notificaciones);
        modelo.put("DatosNotificacion", new DatosNotificacion());

        return new ModelAndView("notificaciones", modelo);
    }

    @GetMapping("/notificaciones/panel")
    public ModelAndView volverAPanel(HttpServletRequest request) {
        if (request.getSession(false) == null) {
            return new ModelAndView("redirect:/login");
        }
        return new ModelAndView("redirect:/panel");
    }

}

