package com.tallerwebi.infraestructura.categoria;

import com.tallerwebi.dominio.categoria.CategoriaMovimiento;
import com.tallerwebi.dominio.categoria.RepositorioCategoria;
import com.tallerwebi.dominio.categoria.ServicioCategoria;
import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionMovimientoNoEncontrado;
import com.tallerwebi.dominio.movimiento.Movimiento;
import com.tallerwebi.dominio.movimiento.RepositorioMovimiento;
import com.tallerwebi.dominio.tipo.TipoMovimiento;
import com.tallerwebi.dominio.usuario.Usuario;
import com.tallerwebi.infraestructura.config.HibernateTestInfraestructuraConfig;
import com.tallerwebi.infraestructura.movimiento.RepositorioMovimientoImpl;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateTestInfraestructuraConfig.class})

public class RepositorioCategoriaTest {

    @Autowired
    private SessionFactory sessionFactory;
    private SessionFactory sessionFactoryMock;
    private RepositorioCategoria repositorioCategoria;
    private ServicioCategoria servicioCategoria;
    @Mock
    private EntityManager entityManagerMock;


    @BeforeEach
    public void init() {

        sessionFactoryMock = mock(SessionFactory.class);
        servicioCategoria = mock(ServicioCategoria.class);
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioObtenerCategoriasDevuelvaLaListaDeCategorias() throws ExcepcionBaseDeDatos {
        //preparacion
        repositorioCategoria = new RepositorioCategoriaImpl(sessionFactory);
        CategoriaMovimiento categoria1 = new CategoriaMovimiento("SUELDO", new TipoMovimiento("INGRESO"));
        CategoriaMovimiento categoria2 = new CategoriaMovimiento("SUPERMERCADO", new TipoMovimiento("EGRESO"));
        guardarCategoria(categoria1);
        guardarCategoria(categoria2);

        //ejecucion
        List<CategoriaMovimiento> categorias = repositorioCategoria.obtenerCategorias();

        //validacion
        assertThat(categorias, notNullValue());
        assertThat(categorias, not(empty()));
        assertThat(categorias, containsInAnyOrder(categoria1, categoria2));
        assertThat(categorias, hasSize(2));
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioObtenerCategoriasLanceUnaExcepcionDeBDD() throws ExcepcionBaseDeDatos {
        //preparacion
        repositorioCategoria = new RepositorioCategoriaImpl(sessionFactoryMock);
        when(sessionFactoryMock.getCurrentSession()).thenThrow(HibernateException.class);

        //ejecucion y validacion
        assertThrows(ExcepcionBaseDeDatos.class,  () -> {
            repositorioCategoria.obtenerCategorias();
        }, "Base de datos no disponible");
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioObtenerCategoriaPorNombreDevuelvaUnaCategoria() throws ExcepcionBaseDeDatos {
        //preparacion
        repositorioCategoria = new RepositorioCategoriaImpl(sessionFactory);
        CategoriaMovimiento categoria1 = new CategoriaMovimiento("SUELDO", new TipoMovimiento("INGRESO"));
        guardarCategoria(categoria1);

        //ejecucion
        CategoriaMovimiento categoriaObtenida = repositorioCategoria.obtenerCategoriaPorNombre("SUELDO");

        //validacion
        assertEquals(categoria1, categoriaObtenida);

    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioObtenerCategoriaPorNombreLanceUnaExcepcionDeBDD() throws ExcepcionBaseDeDatos {
        //preparacion
        repositorioCategoria = new RepositorioCategoriaImpl(sessionFactoryMock);
        when(sessionFactoryMock.getCurrentSession()).thenThrow(HibernateException.class);

        //ejecucion y validacion
        assertThrows(ExcepcionBaseDeDatos.class,  () -> {
            repositorioCategoria.obtenerCategoriaPorNombre("nombreCategoria");
        }, "Base de datos no disponible");
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlActualizarUnColorSeActualiceCorrectamente() throws ExcepcionBaseDeDatos {
        //preparacion
        repositorioCategoria = new RepositorioCategoriaImpl(sessionFactory);
        CategoriaMovimiento categoria = new CategoriaMovimiento("SUPERMERCADO", new TipoMovimiento("INGRESO"));
        guardarCategoria(categoria);
        String colorElegido = "#000000";
        categoria.setColor(colorElegido);
        entityManagerMock = mock(EntityManager.class);
        repositorioCategoria.setEntityManager(entityManagerMock);


        //ejecucion
        repositorioCategoria.actualizarColor(categoria);

        //validacion
        assertEquals(colorElegido, categoria.getColor());
    }


    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlActualizarUnColorLanceUnaExcepcionDeBDD() throws ExcepcionBaseDeDatos {
        //preparacion
        repositorioCategoria = new RepositorioCategoriaImpl(sessionFactoryMock);
        when(sessionFactoryMock.getCurrentSession()).thenThrow(HibernateException.class);
        CategoriaMovimiento categoria = new CategoriaMovimiento("SUPERMERCADO", new TipoMovimiento("INGRESO"));


        //ejecucion y validacion
        assertThrows(ExcepcionBaseDeDatos.class,  () -> {
            repositorioCategoria.actualizarColor(categoria);
        }, "Base de datos no disponible");

    }



    // METODOS PRIVADOS
    private void guardarCategoria(CategoriaMovimiento categoria) {
        sessionFactory.getCurrentSession().save(categoria);
    }


}
