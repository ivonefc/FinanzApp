package com.tallerwebi.presentacion.movimiento;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.itextpdf.text.DocumentException;
import com.tallerwebi.dominio.excepcion.*;
import com.tallerwebi.dominio.exportar.ServicioDeExportacion;
import com.tallerwebi.dominio.exportar.TipoDeArchivo;
import com.tallerwebi.dominio.movimiento.*;
import com.tallerwebi.dominio.movimientoCompartido.ServicioMovimientoCompartido;
import com.tallerwebi.dominio.notificacion.Notificacion;
import com.tallerwebi.dominio.usuario.ServicioUsuario;
import com.tallerwebi.dominio.usuario.Usuario;
import com.tallerwebi.presentacion.notificaciones.DatosNotificacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    private ServicioDeExportacion servicioDeExportacion;
    private ServicioUsuario servicioUsuario;
    private ServicioMovimientoCompartido servicioMovimientoCompartido;

    public ControladorMovimiento(ServicioMovimiento servicioMovimiento) {
        this.servicioMovimiento = servicioMovimiento;
    }

    public ControladorMovimiento(ServicioMovimiento servicioMovimiento, ServicioDeExportacion servicioDeExportacion) {
        this.servicioMovimiento = servicioMovimiento;
        this.servicioDeExportacion = servicioDeExportacion;
    }

    @Autowired
    public ControladorMovimiento(ServicioMovimiento servicioMovimiento, ServicioDeExportacion servicioDeExportacion, ServicioUsuario servicioUsuario, ServicioMovimientoCompartido servicioMovimientoCompartido) {
        this.servicioMovimiento = servicioMovimiento;
        this.servicioDeExportacion = servicioDeExportacion;
        this.servicioUsuario = servicioUsuario;
        this.servicioMovimientoCompartido = servicioMovimientoCompartido;
    }

    public ControladorMovimiento(ServicioMovimiento servicioMovimientoMock, ServicioDeExportacion servicioDeExportacionMock, ServicioMovimientoCompartido servicioMovimientoCompartidoMock) {
        this.servicioMovimiento = servicioMovimientoMock;
        this.servicioDeExportacion = servicioDeExportacionMock;
        this.servicioMovimientoCompartido = servicioMovimientoCompartidoMock;
    }

    @GetMapping("/movimientos")
    public ModelAndView obtenerMovimientosPorPagina(HttpServletRequest httpServletRequest, @RequestParam(defaultValue = "1") int pagina) throws ExcepcionBaseDeDatos, PaginaInexistente, UsuarioInexistente {
        ModelMap model = new ModelMap();
        HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpSession == null)
            return new ModelAndView("redirect:/login");

        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario);
        int tamanioDePagina = 10;
        List<Movimiento> movimientos = servicioMovimiento.obtenerMovimientosPorPagina(idUsuario, pagina, tamanioDePagina);
        model.put("movimientos", movimientos);
        model.put("usuario", usuario);

        //Verifico que movimientos no sea vacio, para obtener cantidad de paginas
        if(!movimientos.isEmpty()){
            int cantidadDePaginas = servicioMovimiento.calcularCantidadDePaginas(idUsuario, tamanioDePagina);
            if (pagina > cantidadDePaginas) {
                throw new PaginaInexistente();
            }
            model.put("cantidadDePaginas", cantidadDePaginas);
            model.put("paginaActual", pagina);
        }
        return new ModelAndView("movimientos", model);
    }

    @GetMapping("/movimientos/{fecha}")
    @ResponseBody
    public List<Movimiento> obtenerMovimientosPorFecha(@PathVariable String fecha, HttpServletRequest httpServletRequest) throws ExcepcionBaseDeDatos {
        HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpSession == null)
            return null;

        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        LocalDate fechaLD = LocalDate.parse(fecha);
        List<Movimiento> movimientos = servicioMovimiento.obtenerMovimientosPorFecha(idUsuario, fechaLD);
        return movimientos;
    }

    @GetMapping("/movimientos/editar/{id}")
    public ModelAndView irAFormularioEditarMovimiento(HttpServletRequest httpServletRequest, @PathVariable Long id) throws ExcepcionMovimientoNoEncontrado, ExcepcionBaseDeDatos, UsuarioInexistente {
        ModelMap modelo = new ModelMap();
        HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpSession == null)
            return new ModelAndView("redirect:/login");

        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario);
        Movimiento movimiento = servicioMovimiento.obtenerMovimientoPorId(id);
        DatosEditarMovimiento datosEditarMovimiento = DatosEditarMovimiento.contruirDesdeMovimiento(movimiento);
        modelo.put("movimiento", datosEditarMovimiento);
        modelo.put("usuario", usuario);
        return new ModelAndView("editar-movimiento", modelo);
    }

    @PostMapping("/movimientos/editar")
    public ModelAndView editarMovimiento(@ModelAttribute("movimiento") DatosEditarMovimiento datosEditarMovimiento, HttpServletRequest httpServletRequest) throws ExcepcionMovimientoNoEncontrado, ExcepcionBaseDeDatos {
        HttpSession httpSession = httpServletRequest.getSession(false);
        ModelMap modelo = new ModelMap();

        if (httpSession == null)
            return new ModelAndView("redirect:/login");

        try {
            servicioMovimiento.actualizarMovimiento(datosEditarMovimiento);
        } catch (ExcepcionCamposInvalidos e) {
            modelo.put("errores", e.getErrores());
            return new ModelAndView("editar-movimiento", modelo);
        }
        return new ModelAndView("redirect:/movimientos");
    }

    @PostMapping("/movimientos/eliminar/{id}")
    public ModelAndView eliminarMovimiento(@PathVariable Long id, HttpServletRequest httpServletRequest) throws ExcepcionBaseDeDatos, ExcepcionMovimientoNoEncontrado {
        HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpSession == null)
            return new ModelAndView("redirect:/login");

        servicioMovimiento.eliminarMovimiento(id);
        return new ModelAndView("redirect:/movimientos");
    }

    @RequestMapping("/agregar-movimiento")
    public ModelAndView irAAgregarMovimiento(HttpServletRequest httpServletRequest) throws ExcepcionBaseDeDatos, UsuarioInexistente {
        ModelMap modelo = new ModelMap();
        HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpServletRequest.getSession(false) == null) {
            return new ModelAndView("redirect:/login");
        }
        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario);
        modelo.put("agregarMovimiento", new DatosAgregarMovimiento());
        List<Usuario> amigos = servicioMovimientoCompartido.obtenerAmigos(idUsuario);
        if (amigos != null) {
            modelo.put("amigos", amigos);
        }
        modelo.put("usuario", usuario);
        return new ModelAndView("agregar-movimiento", modelo);
    }

    @PostMapping("/movimientos/nuevo-movimiento")
    // La idea es que este método se utilice cuando se manda el form de nuevo movimiento, es decir que nuevo-movimiento sea el action del form
    public ModelAndView ingresarNuevoMovimiento(@ModelAttribute("movimiento") DatosAgregarMovimiento datosAgregarMovimiento, HttpServletRequest httpServletRequest) throws ExcepcionBaseDeDatos {
        HttpSession httpSession = httpServletRequest.getSession(false);
        ModelMap modelo = new ModelMap();
        if (httpSession == null)
            return new ModelAndView("redirect:/login");

        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        try {
            servicioMovimiento.nuevoMovimiento(idUsuario, datosAgregarMovimiento);
        } catch (ExcepcionCamposInvalidos e) {
            modelo.put("errores", e.getErrores());
            modelo.put("agregarMovimiento", new DatosAgregarMovimiento());
            return new ModelAndView("agregar-movimiento", modelo);
        } catch (UsuarioInexistente e) {
            modelo.put("error", e.getMessage());
            modelo.put("agregarMovimiento", new DatosAgregarMovimiento());
            return new ModelAndView("agregar-movimiento", modelo);
        }
        return new ModelAndView("redirect:/movimientos");
    }

    @GetMapping("/usuarios/amigos")
    @ResponseBody
    public List<Usuario> obtenerAmigos(HttpServletRequest httpServletRequest) throws ExcepcionBaseDeDatos, UsuarioInexistente {
        HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpSession == null)
            throw new UsuarioInexistente();

        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        if(idUsuario == null)
            throw new UsuarioInexistente();

        return servicioUsuario.obtenerAmigosDeUnUsuario(idUsuario);
    }

    @GetMapping("/movimientos/exportar/{tipoDeDoc}")
    @ResponseBody
    public ResponseEntity<byte[]> descargarDocumentoDeMovimentos(@PathVariable TipoDeArchivo tipoDeDoc, HttpServletRequest request) throws DocumentException, ExcepcionBaseDeDatos, ExcepcionExportacionDeArchivo {
        Long idUsuario = (Long) request.getSession(false).getAttribute("idUsuario");

        byte[] bytesDelArchivo = servicioDeExportacion.generarArchivo(idUsuario, tipoDeDoc);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok()
                .headers(headers)
                .body(bytesDelArchivo);
    }

    @PostMapping("/movimientos/aceptarSolicitud/{id}")
    public ModelAndView aceptarSolicitud(@PathVariable Long id, @ModelAttribute DatosNotificacion notificacion) {

        ObjectMapper objectMapper = new ObjectMapper();
        DatosAgregarMovimiento datosAgregarMovimiento = null;
        try {
            datosAgregarMovimiento = objectMapper.readValue(notificacion.getJson(), DatosAgregarMovimiento.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Double montoAmigo = datosAgregarMovimiento.getMonto();
        datosAgregarMovimiento.setIdAmigo(notificacion.getIdUsuarioSolicitante());
        datosAgregarMovimiento.setMonto(datosAgregarMovimiento.getMontoAmigo());
        datosAgregarMovimiento.setMontoAmigo(montoAmigo);

        try {
            servicioMovimiento.guardarMovimientoDesdeNotificacion(notificacion.getIdUsuario(), datosAgregarMovimiento);
            servicioMovimiento.aceptarMovimiento(notificacion.getId(), "Aceptada");
        } catch (ExcepcionBaseDeDatos | ExcepcionCamposInvalidos | UsuarioInexistente e) {
            e.printStackTrace();
        }

        return new ModelAndView("redirect:/movimientos");
    }
}


