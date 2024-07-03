package com.tallerwebi.infraestructura.movimiento;

import com.tallerwebi.dominio.categoria.CategoriaMovimiento;
import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionMovimientoNoEncontrado;
import com.tallerwebi.dominio.movimiento.Movimiento;
import com.tallerwebi.dominio.movimiento.RepositorioMovimiento;
import com.tallerwebi.dominio.tipo.TipoMovimiento;
import com.tallerwebi.dominio.usuario.Usuario;
import com.tallerwebi.infraestructura.config.HibernateTestInfraestructuraConfig;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateTestInfraestructuraConfig.class})
public class RepositorioMovimientoTest {
    @Autowired
    private SessionFactory sessionFactory;

    private SessionFactory sessionFactoryMock;
    private RepositorioMovimiento repositorioMovimiento;

    @BeforeEach
    public void init() {
        sessionFactoryMock = mock(SessionFactory.class);
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioObtenerMovimientosPorUsurioDevuelvaLaListaDeMovimientos() throws ExcepcionBaseDeDatos {
        //preparacion
        repositorioMovimiento = new RepositorioMovimientoImpl(sessionFactory);
        CategoriaMovimiento categoria = new CategoriaMovimiento("SUELDO", new TipoMovimiento("INGRESO"));
        Movimiento movimiento1 = new Movimiento("Sueldo", 100000.0, LocalDate.now());
        Movimiento movimiento2 = new Movimiento("Compra de ropa", 60000.0, LocalDate.now());
        Usuario usuario = new Usuario("victoria@test", "1234", "USER", true);
        guardarUsuario(usuario);
        guardarCategoria(categoria);
        Usuario usuarioObtenido = obtenerUsuarioPorId(1L);
        CategoriaMovimiento categoriaObtenida = obtenerCategoriaPorId(1L);

        movimiento1.setUsuario(usuarioObtenido);
        movimiento2.setUsuario(usuarioObtenido);
        movimiento1.setCategoria(categoriaObtenida);
        movimiento2.setCategoria(categoriaObtenida);

        guardarMovimiento(movimiento1);
        guardarMovimiento(movimiento2);

        //ejecucion
        List<Movimiento> movimientos = repositorioMovimiento.obtenerMovimientos(1L);

        //validacion
        assertThat(movimientos, notNullValue());
        assertThat(movimientos, not(empty()));
        assertThat(movimientos, containsInAnyOrder(movimiento1, movimiento2));
        assertThat(movimientos, hasSize(2));
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioObtenerMovimientosPorUsurioLanceUnaExcepcionDeBDD() throws ExcepcionBaseDeDatos {
        //preparacion
        //Se genera un sessionFactoryMock que esté configurado para lanzar una HibernateException, sino no lanzaba la excepción.
        SessionFactory sessionFactoryMock = mock(SessionFactory.class);
        when(sessionFactoryMock.getCurrentSession()).thenThrow(HibernateException.class);
        repositorioMovimiento = new RepositorioMovimientoImpl(sessionFactoryMock);

        //ejecucion y validacion
        assertThrows(ExcepcionBaseDeDatos.class, () -> repositorioMovimiento.obtenerMovimientos(1L));
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioObtenerMovimientosPorUsuarioDevuelvaListaVacioYaQueNoTieneMovimientos() throws ExcepcionBaseDeDatos {
        //preparacion
        repositorioMovimiento = new RepositorioMovimientoImpl(sessionFactory);

        //ejecucion
        List<Movimiento> movimientos = repositorioMovimiento.obtenerMovimientos(1L);

        //validacion
        assertThat(movimientos, notNullValue());
        assertThat(movimientos, empty());
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioUnMovimientoPorIdDevuelvaUnMovimiento() throws ExcepcionBaseDeDatos, ExcepcionMovimientoNoEncontrado { //BUSCA POR ID DE MOVIMIENTO
        //preparacion
        repositorioMovimiento = new RepositorioMovimientoImpl(sessionFactory);
        CategoriaMovimiento categoria = new CategoriaMovimiento("SUPERMERCADO", new TipoMovimiento("INGRESO"));
        Movimiento movimiento = new Movimiento("Compras", 20000.0, LocalDate.now());
        Usuario usuario = new Usuario("victoria@test", "1234", "USER", true);
        guardarUsuario(usuario);
        guardarCategoria(categoria);
        Usuario usuarioObtenido = obtenerUsuarioPorId(1L);

        movimiento.setUsuario(usuarioObtenido);
        movimiento.setCategoria(categoria);

        guardarMovimiento(movimiento);

        //ejecucion
        Movimiento movimientoObtenido = repositorioMovimiento.obtenerMovimientoPorId(1L); //ID DEL MOVIMIENTO

        //validacion
        assertThat(movimientoObtenido, notNullValue());
        assertThat(movimientoObtenido, is(movimiento));
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioUnMovimientoPorIdLanceUnaExcepcionDeMovimientoNoEncontrado(){
        //preparacion
        repositorioMovimiento = new RepositorioMovimientoImpl(sessionFactory);

        //ejecucion y validacion
        assertThrows(ExcepcionMovimientoNoEncontrado.class,  () -> {
            repositorioMovimiento.obtenerMovimientoPorId(1L);
        }, "No se encontro el movimiento");
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioUnMovimientoPorIdLanceUnaExcepcionDeBDD(){
        //preparacion
        repositorioMovimiento = new RepositorioMovimientoImpl(sessionFactoryMock);
        when(sessionFactoryMock.getCurrentSession()).thenThrow(HibernateException.class);

        //ejecucion y validacion
        assertThrows(ExcepcionBaseDeDatos.class,  () -> {
            repositorioMovimiento.obtenerMovimientoPorId(1L);
        }, "Base de datos no disponible");
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlActualizarUnMovimientoSeActualiceCorrectamente() throws ExcepcionBaseDeDatos, ExcepcionMovimientoNoEncontrado {
        //preparacion
        repositorioMovimiento = new RepositorioMovimientoImpl(sessionFactory);
        CategoriaMovimiento categoria = new CategoriaMovimiento("SUPERMERCADO", new TipoMovimiento("INGRESO"));
        CategoriaMovimiento categoria2 = new CategoriaMovimiento("SUELDO", new TipoMovimiento("INGRESO"));
        Movimiento movimiento = new Movimiento("Compras", 20000.0, LocalDate.now());
        Usuario usuario = new Usuario("victoria@test", "1234", "USER", true);
        guardarUsuario(usuario);
        guardarCategoria(categoria);
        guardarCategoria(categoria2);
        Usuario usuarioObtenido = obtenerUsuarioPorId(1L);

        // ACTUALIZA CATEGORIA EN EL MOVIMIENTO
        movimiento.setUsuario(usuarioObtenido);
        movimiento.setCategoria(categoria);

        guardarMovimiento(movimiento);

        //ejecucion
        Movimiento movimientoObtenido = repositorioMovimiento.obtenerMovimientoPorId(1L); //ID DEL MOVIMIENTO
        movimientoObtenido.setCategoria(categoria2);
        repositorioMovimiento.actualizarMovimiento(movimientoObtenido);

        //validacion
        Movimiento movimientoActualizado = repositorioMovimiento.obtenerMovimientoPorId(1L);
        assertThat(movimientoActualizado, notNullValue());
        assertThat(movimientoActualizado, is(movimientoObtenido));
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlActualizarUnMovimientoLanceUnaExcepcionDeBDD(){
        //preparacion
        repositorioMovimiento = new RepositorioMovimientoImpl(sessionFactoryMock);
        when(sessionFactoryMock.getCurrentSession()).thenThrow(HibernateException.class);

        //ejecucion y validacion
        assertThrows(ExcepcionBaseDeDatos.class,  () -> {
            repositorioMovimiento.actualizarMovimiento(new Movimiento());
        }, "Base de datos no disponible");
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlActualizarUnMovimientoLanceUnaExcepcionDeMovimientoNoEncontrado(){
        //preparacion
        repositorioMovimiento = new RepositorioMovimientoImpl(sessionFactory);

        //ejecucion y validacion
        assertThrows(ExcepcionMovimientoNoEncontrado.class,  () -> {
            repositorioMovimiento.actualizarMovimiento(null);
        }, "No se encontro el movimiento");
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlGuardarUnMovimientoSeGuardeCorrectamente() throws ExcepcionBaseDeDatos, ExcepcionMovimientoNoEncontrado {
        //preparacion
        repositorioMovimiento = new RepositorioMovimientoImpl(sessionFactory);
        CategoriaMovimiento categoria = new CategoriaMovimiento("SUPERMERCADO", new TipoMovimiento("INGRESO"));
        Movimiento movimiento = new Movimiento("Compras", 20000.0, LocalDate.now());
        Usuario usuario = new Usuario("victoria@test", "1234", "USER", true);
        guardarUsuario(usuario);
        guardarCategoria(categoria);
        Usuario usuarioObtenido = obtenerUsuarioPorId(1L);

        movimiento.setUsuario(usuarioObtenido);
        movimiento.setCategoria(categoria);

        //ejecucion
        repositorioMovimiento.guardarMovimiento(movimiento);

        //validacion
        Movimiento movimientoObtenido = repositorioMovimiento.obtenerMovimientoPorId(1L);
        assertThat(movimientoObtenido, notNullValue());
        assertThat(movimientoObtenido, is(movimiento));
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlGuardarUnMovimientoLanceUnaExcepcionDeBDD(){
        //preparacion
        repositorioMovimiento = new RepositorioMovimientoImpl(sessionFactoryMock);
        when(sessionFactoryMock.getCurrentSession()).thenThrow(HibernateException.class);

        //ejecucion y validacion
        assertThrows(ExcepcionBaseDeDatos.class,  () -> {
            repositorioMovimiento.guardarMovimiento(new Movimiento());
        }, "Base de datos no disponible");
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlEliminarUnMovimientoSeElimineCorrectamente() throws ExcepcionBaseDeDatos, ExcepcionMovimientoNoEncontrado{
        //preparacion
        repositorioMovimiento = new RepositorioMovimientoImpl(sessionFactory);
        CategoriaMovimiento categoria = new CategoriaMovimiento("SUPERMERCADO", new TipoMovimiento("INGRESO"));
        Movimiento movimiento = new Movimiento("Compras", 20000.0, LocalDate.now());
        Usuario usuario = new Usuario("victoria@test", "1234", "USER", true);
        guardarUsuario(usuario);
        guardarCategoria(categoria);
        Usuario usuarioObtenido = obtenerUsuarioPorId(1L);

        movimiento.setUsuario(usuarioObtenido);
        movimiento.setCategoria(categoria);

        guardarMovimiento(movimiento);

        // VERIFICACION DE EXISTENCIA DEL MOVIMIENTO
        Movimiento movimientoExistente = repositorioMovimiento.obtenerMovimientoPorId(1L);
        assertThat(movimientoExistente, notNullValue());

        // OBTENCION DEL TOTAL DE MOVIMIENTOS ANTES DE LA ELIMINACIO
        int totalMovimientosAntes = repositorioMovimiento.obtenerMovimientos(1L).size();

        //ejecucion
        repositorioMovimiento.eliminarMovimiento(movimiento);

        //validacion
        assertThat(repositorioMovimiento.obtenerMovimientos(1L), empty());
        assertThat(repositorioMovimiento.obtenerMovimientosPorFecha(1L, LocalDate.now()), empty());

        // VERIFICACION DE DISMINUCION
        int totalMovimientosDespues = repositorioMovimiento.obtenerMovimientos(1L).size();
        assertThat(totalMovimientosDespues, is(totalMovimientosAntes - 1));
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlEliminarUnMovimientoLanceUnaExcepcionDeBDD(){
        //preparacion
        repositorioMovimiento = new RepositorioMovimientoImpl(sessionFactoryMock);
        when(sessionFactoryMock.getCurrentSession()).thenThrow(HibernateException.class);

        Movimiento movimiento = new Movimiento();
        movimiento.setId(1L);

        //ejecucion y validacion
        assertThrows(ExcepcionBaseDeDatos.class,  () -> {
            repositorioMovimiento.eliminarMovimiento(movimiento);
        }, "Base de datos no disponible");
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlQuererEliminarUnMovimientoQueNoExisteNoSePuedaEliminarYLanceUnaExcepcionDeMovimientoNoEncontrado(){
        //preparacion
        repositorioMovimiento = new RepositorioMovimientoImpl(sessionFactory);

        //ejecucion y validacion
        assertThrows(ExcepcionMovimientoNoEncontrado.class,  () -> {
            repositorioMovimiento.eliminarMovimiento(new Movimiento());
        }, "No se encontro el movimiento");
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioLosMovimientosDeUnUsuarioEspecificoPorFechaDevuelvaUnaListaDeMovimientos() throws ExcepcionBaseDeDatos {
        //preparacion
        repositorioMovimiento = new RepositorioMovimientoImpl(sessionFactory);
        CategoriaMovimiento categoria1 = new CategoriaMovimiento("SUELDO", new TipoMovimiento("INGRESO"));
        CategoriaMovimiento categoria2 = new CategoriaMovimiento("INDUMENTARIA", new TipoMovimiento("EGRESO"));
        Movimiento movimiento1 = new Movimiento("Sueldo", 100000.0, LocalDate.now());
        Movimiento movimiento2 = new Movimiento("Compra de ropa", 60000.0, LocalDate.now());
        Usuario usuario = new Usuario("victoria@test", "1234", "USER", true);
        guardarUsuario(usuario);
        guardarCategoria(categoria1);
        guardarCategoria(categoria2);
        Usuario usuarioObtenido = obtenerUsuarioPorId(1L);

        movimiento1.setUsuario(usuarioObtenido);
        movimiento2.setUsuario(usuarioObtenido);
        movimiento1.setCategoria(categoria1);
        movimiento2.setCategoria(categoria2);

        guardarMovimiento(movimiento1);
        guardarMovimiento(movimiento2);

        //ejecucion
        List<Movimiento> movimientos =  repositorioMovimiento.obtenerMovimientosPorFecha(1L, LocalDate.now());

        //validacion
        assertThat(movimientos, notNullValue());
        assertThat(movimientos, not(empty()));
        assertThat(movimientos, containsInAnyOrder(movimiento1, movimiento2));
        assertThat(movimientos, hasSize(2));
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioLosMovimientosDeUnUsuarioEspecificoPorFechaLanceUnaExcepcionDeBDD(){
        //preparacion
        repositorioMovimiento = new RepositorioMovimientoImpl(sessionFactoryMock);
        when(sessionFactoryMock.getCurrentSession()).thenThrow(HibernateException.class);

        //ejecucion y validacion
        assertThrows(ExcepcionBaseDeDatos.class,  () -> {
            repositorioMovimiento.obtenerMovimientosPorFecha(1L, LocalDate.now());
        }, "Base de datos no disponible");
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioLosMovimientosDeUnUsuarioPorFechaDevuelvaListaVaciaAlNoTenerMovimientos() throws ExcepcionBaseDeDatos {
        //preparacion
        repositorioMovimiento = new RepositorioMovimientoImpl(sessionFactory);

        //ejecucion
        List<Movimiento> movimientos =  repositorioMovimiento.obtenerMovimientosPorFecha(1L, LocalDate.now());

        //validacion
        assertThat(movimientos, notNullValue());
        assertThat(movimientos, empty());
    }

    //TESTEANDO EL METODO obtenerCantidadDeMovimientosPorId() que se utilizará para la paginación.
    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarCantidadDeMovimientosDeUnUsuarioDevuelvaLaCantidadDeMovimientos() throws ExcepcionBaseDeDatos {
        //preparacion
        repositorioMovimiento = new RepositorioMovimientoImpl(sessionFactory);
        CategoriaMovimiento categoria = new CategoriaMovimiento("SUELDO", new TipoMovimiento("INGRESO"));
        Usuario usuario = new Usuario("clarisa@test", "1234", "USER", true);
        guardarUsuario(usuario);
        Usuario usuarioObtenido = obtenerUsuarioPorId(1L);
        guardarCategoria(categoria);
        CategoriaMovimiento categoriaObtenida = obtenerCategoriaPorId(1L);

        generarMovimientos(20, usuarioObtenido, categoriaObtenida);

        //ejecucion
        Long cantidadDePaginas = repositorioMovimiento.obtenerCantidadDeMovimientosPorId(usuarioObtenido.getId());

        //validacion
        assertThat(cantidadDePaginas, equalTo(20L));
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarCantidadDeMovimientosDeUnUsuarioSinMovimientosDevuelvaCero() throws ExcepcionBaseDeDatos {
        //preparacion
        repositorioMovimiento = new RepositorioMovimientoImpl(sessionFactory);
        Usuario usuario = new Usuario("clarisa@test", "1234", "USER", true);
        guardarUsuario(usuario);
        Usuario usuarioObtenido = obtenerUsuarioPorId(1L);

        //ejecucion
        Long cantidadDeMovimientos = repositorioMovimiento.obtenerCantidadDeMovimientosPorId(usuarioObtenido.getId());

        //validacion
        assertThat(cantidadDeMovimientos, equalTo(0L));
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarLaCantidadDeMovimientosDeUnUsuarioLanceUnaExcepcionDeBDD() throws ExcepcionBaseDeDatos {
        //preparacion
        repositorioMovimiento = new RepositorioMovimientoImpl(sessionFactoryMock);
        when(sessionFactoryMock.getCurrentSession()).thenThrow(HibernateException.class);
        Usuario usuario = new Usuario("clarisa@test", "1234", "USER", true);
        guardarUsuario(usuario);
        //ejecucion y validacion
        assertThrows(ExcepcionBaseDeDatos.class, ()->{
            repositorioMovimiento.obtenerCantidadDeMovimientosPorId(usuario.getId());
        }, "Base de datos no disponible" );
    }

    //Testeando método para obtener movimientos por página (paginación).
    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarUnaListaDeMovimientosDeUnaPaginaDevuelvaUnaListaDeMovimientos() throws ExcepcionBaseDeDatos {
        //preparación
        repositorioMovimiento = new RepositorioMovimientoImpl(sessionFactory);
        CategoriaMovimiento categoria = new CategoriaMovimiento("SUELDO", new TipoMovimiento("INGRESO"));
        Usuario usuario = new Usuario("clarisa@test", "1234", "USER", true);
        guardarUsuario(usuario);
        Usuario usuarioObtenido = obtenerUsuarioPorId(1L);
        guardarCategoria(categoria);
        CategoriaMovimiento categoriaObtenida = obtenerCategoriaPorId(1L);

        generarMovimientos(20, usuarioObtenido, categoriaObtenida);
        //ejecución
        List<Movimiento> movimientos = repositorioMovimiento.obtenerMovimientosPorPagina(usuarioObtenido.getId(), 1, 10);

        //validación
        assertThat(movimientos, notNullValue());
        assertThat(movimientos, not(empty()));
        assertThat(movimientos, hasSize(10));
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioLosMovimientosDeUnUsuarioEspecificoLanceUnaExcepcionDeBDD(){
        //preparacion
        repositorioMovimiento = new RepositorioMovimientoImpl(sessionFactoryMock);
        when(sessionFactoryMock.getCurrentSession()).thenThrow(HibernateException.class);

        //ejecucion y validacion
        assertThrows(ExcepcionBaseDeDatos.class,  () -> {
            repositorioMovimiento.obtenerMovimientosPorPagina(1L, 1, 5);
        }, "Base de datos no disponible");
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarElMontoTotalGastadoEnUnaCategoriaEnElMesYAnioActualDevuelvaLaSumaDeSusMontos() throws ExcepcionBaseDeDatos {
        //preparacion
        repositorioMovimiento = new RepositorioMovimientoImpl(sessionFactory);
        CategoriaMovimiento categoria = new CategoriaMovimiento("RESTAURANTE", new TipoMovimiento("EGRESO"));
        Usuario usuario = new Usuario("clarisa@test", "1234", "USER", true);
        guardarUsuario(usuario);
        Usuario usuarioObtenido = obtenerUsuarioPorId(1L);
        guardarCategoria(categoria);
        CategoriaMovimiento categoriaObtenida = obtenerCategoriaPorId(1L);

        generarMovimientos(20, usuarioObtenido, categoriaObtenida);

        LocalDate fechaActual = LocalDate.now();
        int mes = fechaActual.getMonthValue();
        int anio = fechaActual.getYear();

        //ejecucion
        Double totalPorCategoria = repositorioMovimiento.obtenerTotalPorCategoriaEnMesYAnioActual(usuarioObtenido.getId(), mes, anio);

        //validacion
        assertThat(totalPorCategoria, closeTo(190.0, 0.0));
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarElMontoTotalGastadoEnUnaCategoriaEnElMesYAnioActualLantaUnaExcepcionDeBDD(){
        //preparacion
        repositorioMovimiento = new RepositorioMovimientoImpl(sessionFactoryMock);
        when(sessionFactoryMock.getCurrentSession()).thenThrow(HibernateException.class);

        //ejecucion y validacion
        assertThrows(ExcepcionBaseDeDatos.class,  () -> {
            repositorioMovimiento.obtenerTotalPorCategoriaEnMesYAnioActual(1L, 1, 2021);
        }, "Base de datos no disponible");
    }


    // METODOS PRIVADOS
    private void guardarCategoria(CategoriaMovimiento categoria) {
        sessionFactory.getCurrentSession().save(categoria);
    }

    private CategoriaMovimiento obtenerCategoriaPorId(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(CategoriaMovimiento.class, id);
    }

    private void generarMovimientos(int cantidadDeMovimientos, Usuario usuario, CategoriaMovimiento categoria) {
        for (int i = 0; i < cantidadDeMovimientos; i++) {
            Movimiento movimiento = new Movimiento("Descripcion: "+ i, i + 0.0, LocalDate.now(), categoria, usuario);
            sessionFactory.getCurrentSession().save(movimiento);
        }
    }

    private Usuario obtenerUsuarioPorId(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Usuario.class, id);
    }

    private void guardarUsuario(Usuario usuario) {
        sessionFactory.getCurrentSession().save(usuario);
    }

    private void guardarMovimiento(Movimiento movimiento) {
        Session session = sessionFactory.getCurrentSession();
        session.save(movimiento);
    }
}
