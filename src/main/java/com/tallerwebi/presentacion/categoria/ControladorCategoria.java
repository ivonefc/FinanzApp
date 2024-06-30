package com.tallerwebi.presentacion.categoria;
import com.tallerwebi.dominio.categoria.CategoriaMovimiento;
import com.tallerwebi.dominio.categoria.ServicioCategoria;
import com.tallerwebi.dominio.categoria.ServicioCategoriaImpl;
import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionCamposInvalidos;
import com.tallerwebi.dominio.excepcion.ExcepcionMovimientoNoEncontrado;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;
import com.tallerwebi.dominio.usuario.ServicioUsuario;
import com.tallerwebi.dominio.usuario.Usuario;
import com.tallerwebi.presentacion.movimiento.DatosEditarMovimiento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class ControladorCategoria {

    private ServicioCategoria servicioCategoria;
    private ServicioUsuario servicioUsuario;

    @Autowired
    public ControladorCategoria(ServicioCategoria servicioCategoria, ServicioUsuario servicioUsuario) {
        this.servicioCategoria = servicioCategoria;
        this.servicioUsuario = servicioUsuario;
    }

    public ControladorCategoria(ServicioCategoria servicioCategoria){
        this.servicioCategoria = servicioCategoria;
    }

    @GetMapping("/categorias/editar-colores")
    public ModelAndView irAVistaEditarColores(HttpServletRequest httpServletRequest) throws ExcepcionBaseDeDatos, UsuarioInexistente {
        ModelMap modelo = new ModelMap();
        HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpSession == null)
            return new ModelAndView("redirect:/login");
        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario);
        modelo.put("usuario", usuario);

        List<CategoriaMovimiento> categorias = servicioCategoria.obtenerCategorias();
        modelo.put("categorias", categorias);
        return new ModelAndView("editar-colores", modelo);
    }

    @PostMapping("/categorias/actualizar-color")
    public String actualizarColor(@RequestParam String nombre, @RequestParam String color, Model model) throws ExcepcionBaseDeDatos {
        try {
            servicioCategoria.actualizarColor(nombre, color);
        } catch (Exception e) {
            model.addAttribute("error", "Error al actualizar el color");
            return "editar-colores";
        }
        return "redirect:/categorias/editar-colores";
    }


}
