package com.tallerwebi.presentacion.ajustes;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;
import com.tallerwebi.dominio.usuario.ServicioUsuario;
import com.tallerwebi.dominio.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class ControladorAjustes {

    private ServicioUsuario servicioUsuario;

    @Autowired
    public ControladorAjustes(ServicioUsuario servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
    }

    @GetMapping("/ajustes")
    public ModelAndView irAAjustes(HttpServletRequest request) throws ExcepcionBaseDeDatos, UsuarioInexistente {
        HttpSession httpSession = request.getSession(false);
        if (request.getSession(false) == null)
            return new ModelAndView("redirect:/login");
        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario);
        ModelMap modelo = new ModelMap();
        modelo.put("usuario", usuario);

        return new ModelAndView("ajustes", modelo);
    }

    @GetMapping("/ajustes/mi-perfil")
    public ModelAndView irAMiPerfil(HttpServletRequest request) throws ExcepcionBaseDeDatos, UsuarioInexistente {
        HttpSession httpSession = request.getSession(false);
        if (request.getSession(false) == null)
            return new ModelAndView("redirect:/login");
        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario);
        ModelMap modelo = new ModelMap();
        modelo.put("usuario", usuario);

        return new ModelAndView("redirect:/mi-perfil", modelo);
    }

    @GetMapping("/ajustes/categorias/editar-colores")
    public ModelAndView irAEditarColores(HttpServletRequest request) throws ExcepcionBaseDeDatos, UsuarioInexistente {
        HttpSession httpSession = request.getSession(false);
        if (request.getSession(false) == null)
            return new ModelAndView("redirect:/login");
        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario);
        ModelMap modelo = new ModelMap();
        modelo.put("usuario", usuario);

        return new ModelAndView("redirect:/categorias/editar-colores", modelo);
    }

    @GetMapping("/ajustes/premium")
    public ModelAndView irAPremium(HttpServletRequest request) throws ExcepcionBaseDeDatos, UsuarioInexistente {
        HttpSession httpSession = request.getSession(false);
        if (request.getSession(false) == null)
            return new ModelAndView("redirect:/login");
        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario);
        ModelMap modelo = new ModelMap();
        modelo.put("usuario", usuario);

        return new ModelAndView("redirect:/premium", modelo);
    }

    @GetMapping("/ajustes/movimientos/agregar-movimiento")
    public ModelAndView irAAgregarMovimiento(HttpServletRequest request) throws ExcepcionBaseDeDatos, UsuarioInexistente {
        HttpSession httpSession = request.getSession(false);
        if (request.getSession(false) == null)
            return new ModelAndView("redirect:/login");
        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario);
        ModelMap modelo = new ModelMap();
        modelo.put("usuario", usuario);

        return new ModelAndView("redirect:/agregar-movimiento", modelo);
    }

}
