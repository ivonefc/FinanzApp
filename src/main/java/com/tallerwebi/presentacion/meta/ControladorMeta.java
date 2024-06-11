package com.tallerwebi.presentacion.meta;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionCamposInvalidos;
import com.tallerwebi.dominio.excepcion.ExcepcionCategoriaConMetaExistente;
import com.tallerwebi.dominio.excepcion.ExcepcionMetaNoExistente;
import com.tallerwebi.dominio.meta.Meta;
import com.tallerwebi.dominio.meta.ServicioMeta;
import com.tallerwebi.dominio.movimiento.ServicioMovimiento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class ControladorMeta {

    private ServicioMovimiento servicioMovimiento;
    private ServicioMeta servicioMeta;

    @Autowired
    public ControladorMeta(ServicioMeta servicioMeta, ServicioMovimiento servicioMovimiento) {
        this.servicioMeta = servicioMeta;
        this.servicioMovimiento = servicioMovimiento;
    }

    @GetMapping("/metas")
    public ModelAndView irAMetas(HttpServletRequest request) throws ExcepcionBaseDeDatos {
        ModelMap modelo = new ModelMap();
        HttpSession httpSession = request.getSession(false);
        if (httpSession == null) {
            return new ModelAndView("redirect:/login");
        }
        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        List<Meta> metas = servicioMeta.obtenerMetas(idUsuario);
        modelo.put("metas", metas);
        return new ModelAndView("metas", modelo);
    }

    @GetMapping("/metas/definidas")
    @ResponseBody
    public List<Meta> obtenerMetasDefinidas(HttpServletRequest request) throws ExcepcionBaseDeDatos {
        HttpSession httpSession = request.getSession(false);
        if (httpSession == null)
            return null;
        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        return servicioMeta.obtenerMetas(idUsuario);
    }

    @GetMapping("/metas/seguimiento")
    @ResponseBody
    public Map<String, Double> obtenerTotalGastadoPorCategoriasConMetas(HttpServletRequest request) throws ExcepcionBaseDeDatos {
        HttpSession httpSession = request.getSession(false);
        if (httpSession == null)
            return null;
        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        return servicioMovimiento.obtenerTotalGastadoEnCategoriasConMetas(idUsuario);
    }

    @GetMapping("/metas/{id}")
    public ModelAndView obtenerMeta(@PathVariable Long id, HttpServletRequest httpServletRequest) throws ExcepcionMetaNoExistente, ExcepcionBaseDeDatos {
        HttpSession httpSession = httpServletRequest.getSession(false);
        if(httpSession == null)
            return new ModelAndView("redirect:/login");

        ModelMap modelo = new ModelMap();
        Meta meta = servicioMeta.obtenerMetaPorId(id);
        modelo.put("meta", meta);
        return new ModelAndView("metas", modelo);
    }

    @GetMapping("/meta/panel")
    public ModelAndView volverAPanel(HttpServletRequest request){
        if (request.getSession(false) == null) {
            return new ModelAndView("redirect:/login");
        }
        return new ModelAndView("redirect:/panel");
    }

    @GetMapping("/metas/agregar")
    public ModelAndView irAAgregarMetas(HttpServletRequest request) {
        ModelMap modelo = new ModelMap();
        modelo.put("meta", new DatosMeta());
        if (request.getSession(false) == null) {
            return new ModelAndView("redirect:/login");
        }
        return new ModelAndView("agregar-meta", modelo);
    }

    @PostMapping("/metas/guardar")
    public ModelAndView crearMeta(@ModelAttribute("meta") DatosMeta datosMeta, HttpServletRequest request) throws ExcepcionBaseDeDatos {
        HttpSession httpSession = request.getSession(false);
        if (httpSession == null) {
            return new ModelAndView("redirect:/login");
        }
        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        ModelMap modelo = new ModelMap();
        try {
            servicioMeta.guardarMeta(idUsuario, datosMeta);
            return new ModelAndView("redirect:/metas");
        } catch (ExcepcionCamposInvalidos e) {
            modelo.put("errores", e.getErrores());
            modelo.put("meta", new DatosMeta());
            return new ModelAndView("agregar-meta", modelo);
        } catch (ExcepcionCategoriaConMetaExistente e) {
            modelo.put("error", e.getMessage());
            modelo.put("meta", new DatosMeta());
            return new ModelAndView("agregar-meta", modelo);
        } catch (UsuarioInexistente e) {
            modelo.put("error", "El usuario no existe");
            modelo.put("meta", new DatosMeta());
            return new ModelAndView("agregar-meta", modelo);
        }
    }

    @GetMapping("/metas/editar/{id}")
    public ModelAndView irAFormularioEditarMetas(HttpServletRequest request, @PathVariable Long id) throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente {
        //obtenerModelo y Sesión iniciada
        ModelMap modelo = new ModelMap();
        HttpSession httpSession = request.getSession(false);

        //Verificacion de sesion
        if (httpSession == null) {
            return new ModelAndView("redirect:/login");
        }

        Meta meta = servicioMeta.obtenerMetaPorId(id);

        DatosEditarMeta datosEditarMeta = DatosEditarMeta.construirDesdeMeta(meta);

        modelo.put("meta",datosEditarMeta);
        return new ModelAndView("editar-meta",modelo);
    }

    @PostMapping("/metas/editar")
    public ModelAndView editarMeta(@ModelAttribute("meta") DatosEditarMeta datosEditarMeta, HttpServletRequest httpServletRequest) throws ExcepcionMetaNoExistente, ExcepcionBaseDeDatos {
        HttpSession httpSession = httpServletRequest.getSession(false);
        ModelMap modelo = new ModelMap();

        if (httpSession == null)
            return new ModelAndView("redirect:/login");

        try {
            servicioMeta.actualizarMeta(datosEditarMeta);
        } catch (ExcepcionCamposInvalidos e) {
            modelo.put("errores", e.getErrores());
            return new ModelAndView("editar-meta", modelo);
        }
        return new ModelAndView("redirect:/metas");
    }

    @PostMapping("/metas/eliminar/{id}")
    public ModelAndView eliminarMeta(@PathVariable Long id, HttpServletRequest httpServletRequest) throws ExcepcionMetaNoExistente, ExcepcionBaseDeDatos {
        HttpSession httpSession = httpServletRequest.getSession(false);
        if(httpSession == null)
            return new ModelAndView("redirect:/login");

        servicioMeta.eliminarMeta(id);
        return new ModelAndView("redirect:/metas");
    }

}
