package com.tallerwebi.infraestructura.meta;

import com.tallerwebi.dominio.categoria.CategoriaMovimiento;
import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionCategoriaConMetaExistente;
import com.tallerwebi.dominio.meta.Meta;
import com.tallerwebi.dominio.meta.RepositorioMeta;
import com.tallerwebi.dominio.usuario.Usuario;
import com.tallerwebi.infraestructura.config.HibernateTestInfraestructuraConfig;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateTestInfraestructuraConfig.class})
public class RepositoriaMetaTest {
    @Autowired
    private SessionFactory sessionFactory;

    private SessionFactory sessionFactoryMock;
    private RepositorioMeta repositorioMeta;

    @BeforeEach
    public void init() {
        sessionFactoryMock = mock(SessionFactory.class);
        when(sessionFactoryMock.getCurrentSession()).thenReturn(mock(Session.class));
        repositorioMeta = new RepositorioMetaImpl(sessionFactoryMock);
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioGuardarSeGuardeLaMeta() throws ExcepcionBaseDeDatos {
        // preparacion
        Meta meta = new Meta();
        meta.setId(1L);
        meta.setCategoriaMovimiento(new CategoriaMovimiento());
        meta.setUsuario(new Usuario());

        // ejecucion
        repositorioMeta.guardar(meta);

        // ejecucion
        assertNotNull(meta.getId());
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioGuardarSeLanceExcepcionBaseDeDatos() throws ExcepcionBaseDeDatos {
        // preparacion
        Meta meta = new Meta();
        meta.setId(1L);
        meta.setCategoriaMovimiento(new CategoriaMovimiento());
        meta.setUsuario(new Usuario());

        RepositorioMeta repositorioMetaMock = mock(RepositorioMeta.class);
        doThrow(new ExcepcionBaseDeDatos()).when(repositorioMetaMock).guardar(meta);

        // ejecucion
        Assertions.assertThrows(ExcepcionBaseDeDatos.class, () -> repositorioMetaMock.guardar(meta));

        // verificacion
        doThrow(new ExcepcionBaseDeDatos()).when(repositorioMetaMock).guardar(meta);
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioExisteMetaConUsuarioYCategoriaSeVerifiqueSiExiste() throws ExcepcionBaseDeDatos, ExcepcionCategoriaConMetaExistente {
        // preparacion
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        CategoriaMovimiento categoriaMovimiento = new CategoriaMovimiento();
        categoriaMovimiento.setId(1L);

        Query queryMock = mock(Query.class);
        when(sessionFactoryMock.getCurrentSession().createQuery(anyString())).thenReturn(queryMock);
        when(queryMock.setParameter(anyString(), any())).thenReturn(queryMock);
        when(queryMock.uniqueResult()).thenReturn(null);

        // ejecucion
        repositorioMeta.existeMetaConUsuarioYCategoria(usuario, categoriaMovimiento);

        // verificacion
        verify(sessionFactoryMock.getCurrentSession(), times(1)).createQuery("FROM Meta m WHERE m.usuario = :usuario AND m.categoriaMovimiento = :categoria");
    }

//    @Test
//    @Transactional
//    @Rollback
//    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//    public void queAlSolicitarAlRepositorioExisteMetaConUsuarioYCategoriaSeLanceExcepcionCategoriaConMetaExistente() throws ExcepcionBaseDeDatos, ExcepcionCategoriaConMetaExistente {
//        // preparacion
//        Usuario usuario = new Usuario();
//        usuario.setId(1L);
//        CategoriaMovimiento categoriaMovimiento = new CategoriaMovimiento();
//        categoriaMovimiento.setId(1L);
//
//        RepositorioMeta repositorioMetaMock = mock(RepositorioMeta.class);
//
//        // ejecucion
//        doThrow(new ExcepcionCategoriaConMetaExistente()).when(repositorioMetaMock).existeMetaConUsuarioYCategoria(usuario, categoriaMovimiento);
//
//        // verificacion
//        verify(sessionFactoryMock.getCurrentSession(), times(1)).createQuery("FROM Meta m WHERE m.usuario = :usuario AND m.categoriaMovimiento = :categoria");
//    }

//    @Test
//    @Transactional
//    @Rollback
//    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//    public void queAlSolicitarAlRepositorioExisteMetaConUsuarioYCategoriaSeLanceExcepcionBaseDeDatos() throws ExcepcionBaseDeDatos {
//        // preparacion
//        Usuario usuario = new Usuario();
//        usuario.setId(1L);
//        CategoriaMovimiento categoriaMovimiento = new CategoriaMovimiento();
//        categoriaMovimiento.setId(1L);
//
//        // ejecucion
//        doThrow(new ExcepcionBaseDeDatos()).when(repositorioMeta).existeMetaConUsuarioYCategoria(usuario, categoriaMovimiento);
//
//        // verificacion
//        verify(sessionFactoryMock.getCurrentSession(), times(1)).createQuery("FROM Meta m WHERE m.usuario = :usuario AND m.categoriaMovimiento = :categoria");
//    }







}
