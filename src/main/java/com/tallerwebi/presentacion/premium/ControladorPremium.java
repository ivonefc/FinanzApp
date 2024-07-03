package com.tallerwebi.presentacion.premium;

import com.tallerwebi.dominio.email.Response;
import com.tallerwebi.dominio.email.ServicioEmail;
import com.tallerwebi.dominio.excepcion.ErrorEmail;
import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
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

    private ServicioUsuario servicioUsuario;
    private ServicioEmail servicioEmail;

    public ControladorPremium(ServicioUsuario servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
    }

    @Autowired
    public ControladorPremium(ServicioUsuario servicioUsuario, ServicioEmail servicioEmail) {
        this.servicioUsuario = servicioUsuario;
        this.servicioEmail = servicioEmail;
    }

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
    public ModelAndView irAMetodoPagoPremium(HttpServletRequest request) throws ExcepcionBaseDeDatos, UsuarioInexistente {
        HttpSession session = request.getSession(false);
        if (session == null)
            return new ModelAndView("redirect:/login");
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario);
        ModelMap modelo = new ModelMap();
        modelo.put("usuario", usuario);

        return new ModelAndView("metodo-pago", modelo);
    }

    @PostMapping("/pagar")
    public ModelAndView pagar(HttpServletRequest request) throws ExcepcionBaseDeDatos, UsuarioInexistente, ErrorEmail {
        HttpSession session = request.getSession(false);
        ModelMap model = new ModelMap();
        if (session == null)
            return new ModelAndView("redirect:/login");

        Long idUsuario = (Long) session.getAttribute("idUsuario");
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario);
        usuario.setRol("PREMIUM");
        servicioUsuario.actualizarPlan(usuario);

        //envio de mail
        String emailUsuario = usuario.getEmail();
        String mensaje = "¡Hola " + usuario.getNombre() + "!\n" +
                "\n" +
                "¡Gracias por unirte a la familia Premium de FinanzApp!\n" +
                "\n" +
                "Beneficios Exclusivos de Premium:\n" +
                "\n" +
                "Exportar Movimientos en PDF o XLSX: Descarga tus movimientos financieros en formato PDF o XLSX para un mejor seguimiento y análisis.\n" +
                "Generar Metas de Gastos: Establece y gestiona metas de gastos semanales, quincenales o mensuales, y realiza un seguimiento detallado de las mismas.\n" +
                "Compartir Movimientos con Amigos: Comparte tus movimientos financieros con amigos para gestionar gastos compartidos de manera eficiente.\n" +
                "\n" +
                "Estamos aquí para ayudarte a aprovechar al máximo tu suscripción Premium. Si tenes alguna pregunta o necesitas asistencia, no dudes en contactarnos.\n" +
                "\n" +
                "Gracias nuevamente por confiar en nosotros para gestionar tus finanzas.\n" +
                "\n" +
                "Saludos cordiales,\n" +
                "\n" +
                "El equipo de FinanzApp";

        String responseBody ="En breve recibirás un email con todos los detalles de tu suscripción.\n" +
                "\n" +
                "¡Gracias por elegirnos para gestionar tus finanzas!";

        Response response = servicioEmail.enviarEmail(
                "finanzapp29@gmail.com",
                "Bienvenido a Premium: ¡Disfrutá de todas las ventajas exclusivas!",
                emailUsuario,
                "text/plain",
                mensaje,
                responseBody);
        //fin envio de mail

        model.put("mensajePremium", response.getResponseBody());
        model.put("usuario", usuario);

        return new ModelAndView("email-premium-enviado", model);
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