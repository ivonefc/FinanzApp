package com.tallerwebi.presentacion.notificaciones;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionMetaNoExistente;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;
import com.tallerwebi.dominio.meta.ServicioMeta;
import com.tallerwebi.dominio.movimiento.ServicioMovimiento;
import com.tallerwebi.dominio.movimientoCompartido.ServicioMovimientoCompartido;
import com.tallerwebi.dominio.notificacion.Notificacion;
import com.tallerwebi.presentacion.movimiento.DatosAgregarMovimiento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@ControllerAdvice
public class ControladorNotificacionHeader {

    private ServicioMovimiento servicioMovimiento;
    private ServicioMovimientoCompartido servicioMovimientoCompartido;
    private ServicioMeta servicioMeta;

    @Autowired
    public ControladorNotificacionHeader(ServicioMovimientoCompartido servicioMovimientoCompartido, ServicioMeta servicioMeta, ServicioMovimiento servicioMovimiento) {
        this.servicioMovimientoCompartido = servicioMovimientoCompartido;
        this.servicioMeta = servicioMeta;
        this.servicioMovimiento = servicioMovimiento;
    }

    public ControladorNotificacionHeader() {
    }

    @ModelAttribute
    public void eliminarMetasVencidasParaTodosLosUsuarios(HttpServletRequest request) throws ExcepcionMetaNoExistente, ExcepcionBaseDeDatos, UsuarioInexistente {
        HttpSession httpSession = request.getSession(false);
        if (httpSession != null)
            servicioMeta.eliminarMetasVencidasParaTodosLosUsuarios();
    }

    @ModelAttribute
    public void generarNotificacionSiSeSuperoUnaMeta(HttpServletRequest request) throws ExcepcionMetaNoExistente, ExcepcionBaseDeDatos, UsuarioInexistente {
        HttpSession httpSession = request.getSession(false);
        if(httpSession != null){
            Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
            servicioMovimiento.calcularTodasLasMetas(idUsuario);
        }
    }

    @ModelAttribute("notificacionesRecibidas")
    public List<Notificacion> obtenerNotificaciones(HttpServletRequest httpServletRequest) throws ExcepcionBaseDeDatos, UsuarioInexistente {
        HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpSession == null) {
            return Collections.emptyList();
        }

        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");

        if(idUsuario == null) {
            return Collections.emptyList();
        }

        List<Notificacion> solicitudes_Recibidas = servicioMovimientoCompartido.obtenerSolicitudesRecibidas(idUsuario);
        List<Notificacion> solicitudes_Aceptadas = servicioMovimientoCompartido.obtenerSolicitudesAceptadas(idUsuario);
        List<Notificacion> movimientos_Compartidos = servicioMovimiento.obtenerMovimientosCompartidos(idUsuario);
        List<Notificacion> metas_concretadas = servicioMeta.obtenerNotificacionMetasConcretadas(idUsuario);
        List<Notificacion> metas_Vencidas = servicioMeta.obtenerNotificacionMetasVencidas(idUsuario);

        ObjectMapper objectMapper = new ObjectMapper();
        for (Notificacion notificacion : movimientos_Compartidos) {
            try {
                DatosAgregarMovimiento datosAM = objectMapper.readValue(notificacion.getDatosAgregarMovimiento(), DatosAgregarMovimiento.class);
                notificacion.setDatosAgregarMovimientoObject(datosAM);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<Notificacion> notificaciones = new ArrayList<>();

        notificaciones.addAll(solicitudes_Recibidas);
        notificaciones.addAll(solicitudes_Aceptadas);
        notificaciones.addAll(movimientos_Compartidos);
        notificaciones.addAll(metas_Vencidas);
        notificaciones.addAll(metas_concretadas);

        notificaciones.sort(Comparator.comparing(Notificacion::getFecha).reversed());

        if(notificaciones.size() > 10){
            notificaciones = notificaciones.subList(0, 10);
        }

        return notificaciones;
    }
}
