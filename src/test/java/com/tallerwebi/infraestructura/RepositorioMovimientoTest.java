package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.movimiento.CategoriaMovimiento;
import com.tallerwebi.dominio.movimiento.Movimiento;
import com.tallerwebi.dominio.movimiento.RepositorioMovimiento;
import com.tallerwebi.dominio.movimiento.TipoDeMovimiento;
import com.tallerwebi.infraestructura.config.HibernateTestInfraestructuraConfig;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateTestInfraestructuraConfig.class})
public class RepositorioMovimientoTest {
    @Autowired
    private SessionFactory sessionFactory;

    private RepositorioMovimiento repositorioMovimiento;

    @BeforeEach
    public void init(){
        repositorioMovimiento = new RepositorioMovimientoImpl(sessionFactory);
    }

    @Test
    @Transactional
    @Rollback
    public void queAlSolicitarAlRepositorioLosMovimientosDeUnUsuarioEspecificoDevuelvaUnaListaDeMovimientos(){
        //preparacion
        Movimiento movimiento1 = new Movimiento("Regalo", TipoDeMovimiento.INGRESO, CategoriaMovimiento.REGALO, 20000.0, LocalDateTime.now());
        Movimiento movimiento2 = new Movimiento("Compra", TipoDeMovimiento.EGRESO, CategoriaMovimiento.INDUMENTARIA, 20000.0, LocalDateTime.now());
        Usuario usuario = new Usuario("clarisa@test", "1234", "USER", true);

        Set<Movimiento> movimientoSet = new HashSet<>();
        movimientoSet.add(movimiento1);
        movimientoSet.add(movimiento2);

        usuario.setMovimientos(movimientoSet);
        this.crearUsuario(usuario);

        //ejecucion
        List<Movimiento> movimientos =  repositorioMovimiento.obtenerMovimientos(1L);

        //validacion
        assertThat(movimientos, notNullValue());
        assertThat(movimientos, not(empty()));
        assertThat(movimientos, containsInAnyOrder(movimiento1, movimiento2));
        assertThat(movimientos, hasSize(2));
    }

    private void crearUsuario(Usuario usuario) {
        Session session = sessionFactory.getCurrentSession();
        session.save(usuario);
    }
}
