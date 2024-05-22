package com.tallerwebi.presentacion.autenticacion;

import com.tallerwebi.dominio.autenticacion.ServicioLogin;
import com.tallerwebi.dominio.usuario.Usuario;
import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionCamposInvalidos;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;
import org.hamcrest.collection.IsMapWithSize;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.*;

public class ControladorLoginTest {

	private ControladorLogin controladorLogin;
	private DatosRegistroUsuario datosRegistroUsuarioMock;
	private DatosLogin datosLoginMock;
	private HttpServletRequest requestMock;
	private HttpSession sessionMock;
	private ServicioLogin servicioLoginMock;


	@BeforeEach
	public void init(){
		datosLoginMock = new DatosLogin("dami@unlam.com", "123");
		datosRegistroUsuarioMock = mock(DatosRegistroUsuario.class);
		when(datosRegistroUsuarioMock.getEmail()).thenReturn("dami@unlam.com");
		requestMock = mock(HttpServletRequest.class);
		sessionMock = mock(HttpSession.class);
		servicioLoginMock = mock(ServicioLogin.class);
		controladorLogin = new ControladorLogin(servicioLoginMock);
	}

	@Test
	public void loginConUsuarioYPasswordInorrectosDeberiaLlevarALoginNuevamenteYMostrarError() throws ExcepcionBaseDeDatos, UsuarioInexistente {
		// preparacion
		UsuarioInexistente usuarioInexistente = new UsuarioInexistente();
		when(servicioLoginMock.consultarUsuario(anyString(), anyString())).thenThrow(usuarioInexistente);

		// ejecucion
		ModelAndView modelAndView = controladorLogin.validarLogin(datosLoginMock, requestMock);

		// validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("login"));
		assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("No se encontro usuario"));
	}
	
	@Test
	public void loginConUsuarioYPasswordCorrectosDeberiaLLevarAPanel() throws ExcepcionBaseDeDatos, UsuarioInexistente {
		// preparacion
		Usuario usuarioEncontradoMock = mock(Usuario.class);
		when(requestMock.getSession()).thenReturn(sessionMock);
		when(servicioLoginMock.consultarUsuario(anyString(), anyString())).thenReturn(usuarioEncontradoMock);
		
		// ejecucion
		ModelAndView modelAndView = controladorLogin.validarLogin(datosLoginMock, requestMock);
		
		// validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/panel"));
	}

	@Test
	public void registrarmeQueDesdeLaVistaLoginPermitaIrAlFormularioRegistarUsuario(){
		//ejecucion
		ModelAndView modelAndView = controladorLogin.nuevoUsuario();

		//validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("nuevo-usuario"));
		assertThat(modelAndView.getModel().get("usuario"), instanceOf(DatosRegistroUsuario.class));
	}

	@Test
	public void registrameSiUsuarioNoExisteDeberiaCrearUsuarioYVolverAlLogin() throws UsuarioExistente, ExcepcionBaseDeDatos, ExcepcionCamposInvalidos {
		//preparacion
		doNothing().when(servicioLoginMock).registrar(datosRegistroUsuarioMock);
		//ejecucion
		ModelAndView modelAndView =  controladorLogin.registrarme(datosRegistroUsuarioMock);
		//validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
		verify(servicioLoginMock, times(1)).registrar(datosRegistroUsuarioMock);
	}

	@Test
	public void registrarmeSiUsuarioExisteDeberiaVolverAFormularioYMostrarError() throws UsuarioExistente, ExcepcionCamposInvalidos, ExcepcionBaseDeDatos {
		// preparacion
		UsuarioExistente usuarioExistente = new UsuarioExistente();
		doThrow(usuarioExistente).when(servicioLoginMock).registrar(datosRegistroUsuarioMock);

		// ejecucion
		ModelAndView modelAndView = controladorLogin.registrarme(datosRegistroUsuarioMock);

		// validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("nuevo-usuario"));
		assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("El usuario ya existe"));
	}

	@Test
	public void registrarmeSiAlEnviarElFormularioConTodosLosCamposVaciosDeberiaDevolverAFormularioYMostrarErrores() throws UsuarioExistente, ExcepcionCamposInvalidos, ExcepcionBaseDeDatos {
		// preparacion
		Map<String, String> errores = Map.of(
			"email", "El campo es requerido",
			"nombre", "El campo es requerido",
				"password", "El campo es requerido"
		);
		ExcepcionCamposInvalidos excepcionCamposInvalidos = new ExcepcionCamposInvalidos(errores);
		doThrow(excepcionCamposInvalidos).when(servicioLoginMock).registrar(datosRegistroUsuarioMock);

		// ejecucion
		ModelAndView modelAndView = controladorLogin.registrarme(datosRegistroUsuarioMock);

		// validacion
		Map<String, String> erroresObtenidos = (Map<String, String>) modelAndView.getModel().get("errores");
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("nuevo-usuario"));
		assertThat(erroresObtenidos, IsMapWithSize.aMapWithSize(3));
		assertThat(erroresObtenidos, hasEntry("email", "El campo es requerido"));
		assertThat(erroresObtenidos, hasEntry("nombre", "El campo es requerido"));
		assertThat(erroresObtenidos, hasEntry("password", "El campo es requerido"));
	}

	@Test
	public void registrarmeQueAlIntentarRegistrarUnUsuarioConCampoPasswordVacioRedirijaAlFormularioYMuestreUnErrorEnDichoCampo() throws ExcepcionCamposInvalidos, ExcepcionBaseDeDatos, UsuarioExistente {
		// preparacion
		Map<String, String> errores = Map.of(
				"password", "El campo es requerido"
		);
		ExcepcionCamposInvalidos excepcionCamposInvalidos = new ExcepcionCamposInvalidos(errores);
		doThrow(excepcionCamposInvalidos).when(servicioLoginMock).registrar(datosRegistroUsuarioMock);

		// ejecucion
		ModelAndView modelAndView = controladorLogin.registrarme(datosRegistroUsuarioMock);

		// validacion
		Map<String, String> erroresObtenidos = (Map<String, String>) modelAndView.getModel().get("errores");
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("nuevo-usuario"));
		assertThat(erroresObtenidos, IsMapWithSize.aMapWithSize(1));
		assertThat(erroresObtenidos, hasEntry("password", "El campo es requerido"));
	}

	@Test
	public void registrarmeQueAlIntentarRegistrarUnUsuarioConCampoNombreVacioRedirijaAlFormularioYMuestreUnErrorEnDichoCampo() throws ExcepcionCamposInvalidos, ExcepcionBaseDeDatos, UsuarioExistente {
		// preparacion
		Map<String, String> errores = Map.of(
				"nombre", "El campo es requerido"
		);
		ExcepcionCamposInvalidos excepcionCamposInvalidos = new ExcepcionCamposInvalidos(errores);
		doThrow(excepcionCamposInvalidos).when(servicioLoginMock).registrar(datosRegistroUsuarioMock);

		// ejecucion
		ModelAndView modelAndView = controladorLogin.registrarme(datosRegistroUsuarioMock);

		// validacion
		Map<String, String> erroresObtenidos = (Map<String, String>) modelAndView.getModel().get("errores");
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("nuevo-usuario"));
		assertThat(erroresObtenidos, IsMapWithSize.aMapWithSize(1));
		assertThat(erroresObtenidos, hasEntry("nombre", "El campo es requerido"));
	}

	@Test
	public void registrarmeQueAlIntentarRegistrarUnUsuarioConCampoEmailVacioRedirijaAlFormularioYMuestreUnErrorEnDichoCampo() throws ExcepcionCamposInvalidos, ExcepcionBaseDeDatos, UsuarioExistente {
		// preparacion
		Map<String, String> errores = Map.of(
				"email", "El campo es requerido"
		);
		ExcepcionCamposInvalidos excepcionCamposInvalidos = new ExcepcionCamposInvalidos(errores);
		doThrow(excepcionCamposInvalidos).when(servicioLoginMock).registrar(datosRegistroUsuarioMock);

		// ejecucion
		ModelAndView modelAndView = controladorLogin.registrarme(datosRegistroUsuarioMock);

		// validacion
		Map<String, String> erroresObtenidos = (Map<String, String>) modelAndView.getModel().get("errores");
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("nuevo-usuario"));
		assertThat(erroresObtenidos, IsMapWithSize.aMapWithSize(1));
		assertThat(erroresObtenidos, hasEntry("email", "El campo es requerido"));
	}

	@Test
	public void registrarmeSiAlRegistrarUnUsuarioIngresaUnMailSinArrobaDeberiaVolverAFormularioYMostrarError () throws UsuarioExistente, ExcepcionBaseDeDatos, ExcepcionCamposInvalidos {
		// preparacion
		Map<String, String> errores = Map.of(
				"email", "El email es invalido"
		);
		ExcepcionCamposInvalidos excepcionCamposInvalidos = new ExcepcionCamposInvalidos(errores);
		doThrow(excepcionCamposInvalidos).when(servicioLoginMock).registrar(datosRegistroUsuarioMock);// Correo electrónico sin "@"

		// ejecucion
		ModelAndView modelAndView = controladorLogin.registrarme(datosRegistroUsuarioMock);

		// validacion
		Map<String, String> erroresObtenidos = (Map<String, String>) modelAndView.getModel().get("errores");
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("nuevo-usuario"));
		assertThat(erroresObtenidos, IsMapWithSize.aMapWithSize(1));
		assertThat(erroresObtenidos, hasEntry("email", "El email es invalido"));
	}
}
