package com.tallerwebi.infraestructura.movimientoCompartido;

import com.tallerwebi.dominio.movimientoCompartido.RepositorioMovimientoCompartido;
import com.tallerwebi.infraestructura.config.HibernateTestInfraestructuraConfig;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateTestInfraestructuraConfig.class})
public class RepositorioMovimientoCompartidoTest {
    @Autowired
    private SessionFactory sessionFactory;

    private RepositorioMovimientoCompartido repositorioMovimientoCompartido;



}
