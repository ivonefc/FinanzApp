package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.movimiento.CategoriaMovimiento;
import com.tallerwebi.dominio.movimiento.Movimiento;
import com.tallerwebi.dominio.movimiento.RepositorioMovimiento;
import com.tallerwebi.dominio.movimiento.TipoMovimiento;
import com.tallerwebi.infraestructura.config.HibernateTestInfraestructuraConfig;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    public void queAlSolicitarAlRepositorioLosMovimientosDeUnUsuarioEspecificoDevuelvaUnaListaDeMovimientos() throws ExcepcionBaseDeDatos {
        //preparacion
        repositorioMovimiento = new RepositorioMovimientoImpl(sessionFactory);
        CategoriaMovimiento categoria1 = new CategoriaMovimiento("SUELDO", new TipoMovimiento("INGRESO"));
        CategoriaMovimiento categoria2 = new CategoriaMovimiento("INDUMENTARIA", new TipoMovimiento("EGRESO"));
        Movimiento movimiento1 = new Movimiento("Sueldo", 20000.0, LocalDate.now());
        Movimiento movimiento2 = new Movimiento("Compra de ropa", 20000.0, LocalDate.now());
        Usuario usuario = new Usuario("clarisa@test", "1234", "USER", true);
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
        List<Movimiento> movimientos =  repositorioMovimiento.obtenerMovimientos(1L);



        //validacion
        assertThat(movimientos, notNullValue());
        assertThat(movimientos, not(empty()));
        assertThat(movimientos, containsInAnyOrder(movimiento1, movimiento2));
        assertThat(movimientos, hasSize(2));
    }

    private void guardarCategoria(CategoriaMovimiento categoria) {
        sessionFactory.getCurrentSession().save(categoria);
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


    @Test
    @Transactional
    @Rollback
    public void queAlSolicitarAlRepositorioLosMovimientosDeUnUsuarioEspecificoLanceUnaExcepcionDeBDD(){
        //preparacion
        repositorioMovimiento = new RepositorioMovimientoImpl(sessionFactoryMock);
        when(sessionFactoryMock.getCurrentSession()).thenThrow(HibernateException.class);

        //ejecucion y validacion
        Assertions.assertThrows(ExcepcionBaseDeDatos.class,  () -> {
            repositorioMovimiento.obtenerMovimientos(1L);
        }, "Base de datos no disponible");

    }
}
