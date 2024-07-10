package com.tallerwebi.presentacion.meta;

import com.tallerwebi.dominio.excepcion.*;
import com.tallerwebi.dominio.meta.Meta;
import com.tallerwebi.dominio.meta.MetaVencida;
import com.tallerwebi.dominio.meta.RepositorioMetaVencida;
import com.tallerwebi.dominio.meta.ServicioMeta;
import com.tallerwebi.dominio.movimiento.ServicioMovimiento;
import com.tallerwebi.dominio.usuario.ServicioUsuario;
import com.tallerwebi.dominio.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
public class ControladorMeta {

    private ServicioMovimiento servicioMovimiento;
    private ServicioMeta servicioMeta;
    private ServicioUsuario servicioUsuario;
    private RepositorioMetaVencida repositorioMetaVencida;

    @Autowired
    public ControladorMeta(ServicioMeta servicioMeta, ServicioMovimiento servicioMovimiento, ServicioUsuario servicioUsuario, RepositorioMetaVencida repositorioMetaVencida) {
        this.servicioMeta = servicioMeta;
        this.servicioMovimiento = servicioMovimiento;
        this.servicioUsuario = servicioUsuario;
        this.repositorioMetaVencida = repositorioMetaVencida;
    }

    @GetMapping("/metas")
    public ModelAndView irAMetas(HttpServletRequest request) throws ExcepcionBaseDeDatos, UsuarioInexistente {
        ModelMap modelo = new ModelMap();
        HttpSession httpSession = request.getSession(false);
        if (httpSession == null)
            return new ModelAndView("redirect:/login");

        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario);
        if(usuario.getRol().equals("FREE"))
            return new ModelAndView("redirect:/panel");

        List<Meta> metas = servicioMeta.obtenerMetas(idUsuario);
        modelo.put("metas", metas);
        modelo.put("usuario", usuario);
        Map<String, Double> totalGastadoPorCategoria = servicioMovimiento.obtenerMetasConFecha(idUsuario);
        modelo.put("totales", totalGastadoPorCategoria);
        return new ModelAndView("metas", modelo);
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

    @GetMapping("/metas/panel")
    public ModelAndView volverAPanel(HttpServletRequest request){
        if (request.getSession(false) == null)
            return new ModelAndView("redirect:/login");

        return new ModelAndView("redirect:/panel");
    }

    @GetMapping("/metas/agregar")
    public ModelAndView irAAgregarMetas(HttpServletRequest request) throws ExcepcionBaseDeDatos, UsuarioInexistente {
        HttpSession httpSession = request.getSession(false);
        ModelMap modelo = new ModelMap();
        modelo.put("meta", new DatosMeta());
        if (request.getSession(false) == null)
            return new ModelAndView("redirect:/login");

        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario);
        if(usuario.getRol().equals("FREE"))
            return new ModelAndView("redirect:/panel");

        modelo.put("usuario", usuario);
        return new ModelAndView("agregar-meta", modelo);
    }

    @GetMapping("/metas/historial")
    public ModelAndView irAHistorialDeMetas(HttpServletRequest request) throws ExcepcionBaseDeDatos, UsuarioInexistente {
        HttpSession httpSession = request.getSession(false);
        ModelMap modelo = new ModelMap();
        if (request.getSession(false) == null) {
            return new ModelAndView("redirect:/login");
        }
        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario);
        if(usuario.getRol().equals("FREE")){
            return new ModelAndView("redirect:/panel");
        }
        modelo.put("usuario", usuario);
        List <MetaVencida> metasVencidas = repositorioMetaVencida.obtenerMetasVencidas(idUsuario);
        modelo.put("metasVencidas", metasVencidas);
        return new ModelAndView("historial-metas", modelo);
    }

    @PostMapping("/metas/guardar")
    public ModelAndView crearMeta(@ModelAttribute("meta") DatosMeta datosMeta, HttpServletRequest request) throws ExcepcionBaseDeDatos, UsuarioInexistente {
        HttpSession httpSession = request.getSession(false);
        if (httpSession == null)
            return new ModelAndView("redirect:/login");

        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario);
        if(usuario.getRol().equals("FREE"))
            return new ModelAndView("redirect:/panel");

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
        }
    }

    @GetMapping("/metas/editar/{id}")
    public ModelAndView irAFormularioEditarMetas(HttpServletRequest request, @PathVariable Long id) throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente, UsuarioInexistente {
        //obtenerModelo y Sesión iniciada
        ModelMap modelo = new ModelMap();
        HttpSession httpSession = request.getSession(false);
        if (httpSession == null)
            return new ModelAndView("redirect:/login");

        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario);
        if(usuario.getRol().equals("FREE"))
            return new ModelAndView("redirect:/panel");

        modelo.put("usuario", usuario);
        Meta meta = servicioMeta.obtenerMetaPorId(id);
        DatosEditarMeta datosEditarMeta = DatosEditarMeta.construirDesdeMeta(meta);
        modelo.put("meta",datosEditarMeta);
        return new ModelAndView("editar-meta",modelo);
    }

    @PostMapping("/metas/editar")
    public ModelAndView editarMeta(@ModelAttribute("meta") DatosEditarMeta datosEditarMeta, HttpServletRequest httpServletRequest) throws ExcepcionMetaNoExistente, ExcepcionBaseDeDatos, UsuarioInexistente {
        HttpSession httpSession = httpServletRequest.getSession(false);
        ModelMap modelo = new ModelMap();
        if (httpSession == null)
            return new ModelAndView("redirect:/login");

        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario);
        if(usuario.getRol().equals("FREE"))
            return new ModelAndView("redirect:/panel");

        modelo.put("usuario", usuario);

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
