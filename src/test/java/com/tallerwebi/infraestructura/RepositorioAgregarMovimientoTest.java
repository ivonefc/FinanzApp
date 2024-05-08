package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.agregarMovimiento.RepositorioAgregarMovimiento;
import com.tallerwebi.dominio.movimiento.CategoriaMovimiento;
import com.tallerwebi.dominio.movimiento.Movimiento;
import com.tallerwebi.dominio.movimiento.RepositorioMovimiento;
import com.tallerwebi.dominio.movimiento.TipoDeMovimiento;
import com.tallerwebi.infraestructura.config.HibernateTestInfraestructuraConfig;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateTestInfraestructuraConfig.class})


public class RepositorioAgregarMovimientoTest {

    @Autowired
    private SessionFactory sessionFactory;

    private RepositorioAgregarMovimiento repositorioAgregarMovimiento;

    @BeforeEach
    public void init(){
        repositorioAgregarMovimiento = new RepositorioAgregarMovimientoImpl(sessionFactory);
    }

    @Test
    @Transactional
    @Rollback
    public void queSePuedaGuardarUnMovimiento(){
        //preparacion
        Movimiento movimiento = new Movimiento("Regalo", TipoDeMovimiento.INGRESO, CategoriaMovimiento.REGALO, 20000.0, LocalDateTime.now());

        //ejecucion
        this.repositorioAgregarMovimiento.guardarMovimiento(movimiento);

        //validacion
        Movimiento movimientoObtenido = (Movimiento) this.sessionFactory.getCurrentSession()
                .createQuery("FROM Movimiento where id_movimiento = " + movimiento.getId())
                .getSingleResult();
        assertThat(movimientoObtenido, equalTo(movimiento));
    }
}
