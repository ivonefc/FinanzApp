package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioLogin;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionCamposInvalidos;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ControladorLogin {

    private ServicioLogin servicioLogin;

    @Autowired
    public ControladorLogin(ServicioLogin servicioLogin){
        this.servicioLogin = servicioLogin;
    }

    @RequestMapping("/login")
    public ModelAndView irALogin() {

        ModelMap modelo = new ModelMap();
        modelo.put("datosLogin", new DatosLogin());
        return new ModelAndView("login", modelo);
    }

    @RequestMapping(path = "/validar-login", method = RequestMethod.POST)
    public ModelAndView validarLogin(@ModelAttribute("datosLogin") DatosLogin datosLogin, HttpServletRequest request) throws ExcepcionBaseDeDatos{
        ModelMap model = new ModelMap();

        Usuario usuarioBuscado = null;
        try {
            usuarioBuscado = servicioLogin.consultarUsuario(datosLogin.getEmail(), datosLogin.getPassword());
        } catch (UsuarioInexistente e) {
            model.put("error", e.getMessage());
            return new ModelAndView("login", model);
        }
        request.getSession().setAttribute("idUsuario", usuarioBuscado.getId());
        return new ModelAndView("redirect:/panel");
    }

    @RequestMapping(path = "/registrarme", method = RequestMethod.POST)
    public ModelAndView registrarme(@ModelAttribute("usuario") DatosRegistroUsuario datosRegistroUsuario) throws ExcepcionBaseDeDatos{
        ModelMap model = new ModelMap();

        try {
            servicioLogin.registrar(datosRegistroUsuario);
            return new ModelAndView("redirect:/login");
        } catch (UsuarioExistente e) {
            model.put("error", e.getMessage());
            return new ModelAndView("nuevo-usuario", model);
        } catch (ExcepcionCamposInvalidos e) {
            model.put("errores", e.getErrores());
            return new ModelAndView("nuevo-usuario", model);
        }
    }

    @RequestMapping(path = "/nuevo-usuario", method = RequestMethod.GET)
    public ModelAndView nuevoUsuario() {
        ModelMap model = new ModelMap();
        model.put("usuario", new DatosRegistroUsuario());
        return new ModelAndView("nuevo-usuario", model);
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ModelAndView inicio() {
        return new ModelAndView("redirect:/login");
    }
}

