package com.tallerwebi.presentacion.notificaciones;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionMetaNoExistente;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;
import com.tallerwebi.dominio.meta.ServicioMeta;
import com.tallerwebi.dominio.movimientoCompartido.ServicioMovimientoCompartido;
import com.tallerwebi.dominio.notificacion.Notificacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;

@ControllerAdvice
public class ControladorNotificacionHeader {
    private ServicioMovimientoCompartido servicioMovimientoCompartido;
    private ServicioMeta servicioMeta;

    @Autowired
    public ControladorNotificacionHeader(ServicioMovimientoCompartido servicioMovimientoCompartido, ServicioMeta servicioMeta) {
        this.servicioMovimientoCompartido = servicioMovimientoCompartido;
        this.servicioMeta = servicioMeta;
    }

    public ControladorNotificacionHeader() {
    }

    @ModelAttribute
    public void eliminarMetasVencidasParaTodosLosUsuarios(HttpServletRequest request) throws ExcepcionMetaNoExistente, ExcepcionBaseDeDatos, UsuarioInexistente {
        HttpSession httpSession = request.getSession(false);
        if (httpSession != null)
            servicioMeta.eliminarMetasVencidasParaTodosLosUsuarios();

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
