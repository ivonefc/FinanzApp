package com.tallerwebi.presentacion.meta;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionCamposInvalidos;
import com.tallerwebi.dominio.excepcion.ExcepcionCategoriaConMetaExistente;
import com.tallerwebi.dominio.meta.ServicioMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class ControladorMeta {
    private ServicioMeta servicioMeta;

    @Autowired
    public ControladorMeta(ServicioMeta servicioMeta) {
        this.servicioMeta = servicioMeta;
    }

    @GetMapping("/metas")
    public ModelAndView irAMetas(HttpServletRequest request){
        if (request.getSession(false) == null) {
            return new ModelAndView("redirect:/login");
        }
        return new ModelAndView("metas");
    }

    @GetMapping("/metas/agregar")
    public ModelAndView irAAgregarMetas(HttpServletRequest request){
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
        if(httpSession == null){
            return new ModelAndView("redirect:/login");
        }
        Long idUsuario = (Long)httpSession.getAttribute("idUsuario");
        ModelMap modelo = new ModelMap();
        try {
            servicioMeta.guardarMeta(idUsuario, datosMeta);
        } catch (ExcepcionCamposInvalidos e) {
            modelo.put("errores", e.getErrores());
            modelo.put("meta", new DatosMeta());
            return new ModelAndView("agregar-meta", modelo);
        } catch (ExcepcionCategoriaConMetaExistente e) {
            modelo.put("error", e.getMessage());
            modelo.put("meta", new DatosMeta());
            return new ModelAndView("agregar-meta", modelo);
        }
        return new ModelAndView("redirect:/metas");
    }

}
