package com.tallerwebi.presentacion.premium;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;
import com.tallerwebi.dominio.usuario.ServicioUsuario;
import com.tallerwebi.dominio.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class ControladorPremium {

    @Autowired
    private ServicioUsuario servicioUsuario;

    @GetMapping("/premium")
    public ModelAndView irAPremium(HttpServletRequest request) throws ExcepcionBaseDeDatos, UsuarioInexistente {
        HttpSession session = request.getSession(false);
        if (session == null)
            return new ModelAndView("redirect:/login");

        Long idUsuario = (Long) session.getAttribute("idUsuario");
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario);
        ModelMap modelo = new ModelMap();
        modelo.put("usuario", usuario);

        return new ModelAndView("premium", modelo);
    }

    @GetMapping("/metodo-pago")
    public ModelAndView irAMetodoPagoPremium(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null)
            return new ModelAndView("redirect:/login");

        return new ModelAndView("metodo-pago");
    }

    @PostMapping("/pagar")
    public ModelAndView pagar(HttpServletRequest request) throws ExcepcionBaseDeDatos, UsuarioInexistente {
        HttpSession session = request.getSession(false);

        if (session == null)
            return new ModelAndView("redirect:/login");

        Long idUsuario = (Long) session.getAttribute("idUsuario");
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario);
        usuario.setRol("PREMIUM");
        servicioUsuario.actualizarPlan(usuario);

        return new ModelAndView("redirect:/premium");
    }

    @GetMapping("/cancelar-plan")
    public ModelAndView cencelarPlan(HttpServletRequest request) throws ExcepcionBaseDeDatos, UsuarioInexistente {
        HttpSession session = request.getSession(false);

        if (session == null)
            return new ModelAndView("redirect:/login");

        Long idUsuario = (Long) session.getAttribute("idUsuario");
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario);
        usuario.setRol("FREE");
        servicioUsuario.actualizarPlan(usuario);

        return new ModelAndView("redirect:/premium");
    }

}