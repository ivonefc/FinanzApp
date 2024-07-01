package com.tallerwebi.presentacion.notificaciones;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;
import com.tallerwebi.dominio.movimientoCompartido.ServicioMovimientoCompartido;
import com.tallerwebi.dominio.notificacion.Notificacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;

@ControllerAdvice
public class ControladorNotificacionHeader {
    private ServicioMovimientoCompartido servicioMovimientoCompartido;

    @Autowired
    public ControladorNotificacionHeader(ServicioMovimientoCompartido servicioMovimientoCompartido) {

    this.servicioMovimientoCompartido = servicioMovimientoCompartido;

    }
    public ControladorNotificacionHeader() {
    }

    @ModelAttribute("notificacionesRecibidas")
    public List<Notificacion> obtenerNotificaciones(HttpServletRequest httpServletRequest) throws ExcepcionBaseDeDatos {
        HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpSession == null)
            return Collections.emptyList();

        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        if(idUsuario == null)
            return Collections.emptyList();

        return servicioMovimientoCompartido.obtenerSolicitudesRecibidas(idUsuario);
    }

    @ModelAttribute("notificacionesAceptadas")
    public List<Notificacion> obtenerNotificacionesAceptadas(HttpServletRequest httpServletRequest) throws ExcepcionBaseDeDatos, UsuarioInexistente {
        HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpSession == null)
            return Collections.emptyList();

        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        if(idUsuario == null)
            return Collections.emptyList();

        return servicioMovimientoCompartido.obtenerSolicitudesAceptadas(idUsuario);
    }
}
