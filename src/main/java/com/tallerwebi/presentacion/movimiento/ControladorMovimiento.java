package com.tallerwebi.presentacion.movimiento;

import com.itextpdf.text.DocumentException;
import com.tallerwebi.dominio.excepcion.*;
import com.tallerwebi.dominio.exportar.ServicioDeExportacion;
import com.tallerwebi.dominio.exportar.TipoDeArchivo;
import com.tallerwebi.dominio.movimiento.*;
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


    public ControladorMovimiento(ServicioMovimiento servicioMovimiento) {
        this.servicioMovimiento = servicioMovimiento;
    }

    @Autowired
    public ControladorMovimiento(ServicioMovimiento servicioMovimiento, ServicioDeExportacion servicioDeExportacion) {
        this.servicioMovimiento = servicioMovimiento;
        this.servicioDeExportacion = servicioDeExportacion;
    }

    @GetMapping("/movimientos")
    public ModelAndView obtenerMovimientosPorPagina(HttpServletRequest httpServletRequest, @RequestParam(defaultValue = "1") int pagina) throws ExcepcionBaseDeDatos, PaginaInexistente {
        ModelMap model = new ModelMap();
        HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpSession == null)
            return new ModelAndView("redirect:/login");

        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        int tamanioDePagina = 10;
        List<Movimiento> movimientos = servicioMovimiento.obtenerMovimientosPorPagina(idUsuario, pagina, tamanioDePagina);
        model.put("movimientos", movimientos);

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
    public ModelAndView irAFormularioEditarMovimiento(HttpServletRequest httpServletRequest, @PathVariable Long id) throws ExcepcionMovimientoNoEncontrado, ExcepcionBaseDeDatos {
        ModelMap modelo = new ModelMap();
        HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpSession == null)
            return new ModelAndView("redirect:/login");

        Movimiento movimiento = servicioMovimiento.obtenerMovimientoPorId(id);
        DatosEditarMovimiento datosEditarMovimiento = DatosEditarMovimiento.contruirDesdeMovimiento(movimiento);
        modelo.put("movimiento", datosEditarMovimiento);
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
    public ModelAndView irAAgregarMovimiento(HttpServletRequest httpServletRequest) {
        ModelMap modelo = new ModelMap();
        HttpSession httpSession = httpServletRequest.getSession(false);

        if (httpSession == null)
            return new ModelAndView("redirect:/login");

        modelo.put("agregarMovimiento", new DatosAgregarMovimiento());
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
        }
        return new ModelAndView("redirect:/movimientos");
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
}


