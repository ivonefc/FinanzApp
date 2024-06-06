package com.tallerwebi.infraestructura.usuario;

import com.tallerwebi.dominio.RepositorioUsuario;
import com.tallerwebi.infraestructura.config.HibernateTestInfraestructuraConfig;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.mock;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateTestInfraestructuraConfig.class})
public class RepositorioUsuarioTest {

    private SessionFactory sessionFactoryMock;
    private RepositorioUsuario repositorioUsuario;

    @BeforeEach
    public void init() {
        sessionFactoryMock = mock(SessionFactory.class);
    }

}
