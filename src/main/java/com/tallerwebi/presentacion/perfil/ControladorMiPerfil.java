package com.tallerwebi.presentacion.perfil;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;
import com.tallerwebi.dominio.usuario.ServicioUsuario;
import com.tallerwebi.dominio.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class ControladorMiPerfil {

    private ServicioUsuario servicioUsuario;
    
    @Autowired
    public ControladorMiPerfil(ServicioUsuario servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
    }

    @GetMapping("/mi-perfil")
    public ModelAndView irAMiPerfil(HttpServletRequest request) throws ExcepcionBaseDeDatos, UsuarioInexistente {
        HttpSession session = request.getSession(false);
        if (session == null)
            return new ModelAndView("redirect:/login");

        Long id = (Long) session.getAttribute("idUsuario");
        if (id == null)
            return new ModelAndView("redirect:/login");

        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(id);
        if (usuario == null)
            return new ModelAndView("redirect:/login");

        ModelAndView modelAndView = new ModelAndView("mi-perfil");
        modelAndView.addObject("usuario", usuario);
        return modelAndView;
    }

    @GetMapping("/perfil/panel")
    public ModelAndView volverAPanel(HttpServletRequest request){
        if (request.getSession(false) == null)
            return new ModelAndView("redirect:/login");

        return new ModelAndView("redirect:/panel");
    }

}
