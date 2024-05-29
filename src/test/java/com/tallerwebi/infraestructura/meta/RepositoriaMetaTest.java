package com.tallerwebi.infraestructura.meta;

import com.tallerwebi.dominio.categoria.CategoriaMovimiento;
import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionCategoriaConMetaExistente;
import com.tallerwebi.dominio.excepcion.ExcepcionMetaNoExistente;
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


import static org.junit.jupiter.api.Assertions.*;
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
    public void queAlSolicitarAlRepositorioGuardarSeGuardeLaMeta() throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente {
        // preparacion
        Meta meta = new Meta();
        meta.setCategoriaMovimiento(new CategoriaMovimiento());
        meta.setUsuario(new Usuario());

        RepositorioMeta repositorioMetaSpy = spy(repositorioMeta);
        doAnswer(invocation -> {
            Meta argument = (Meta) invocation.getArguments()[0];
            argument.setId(1L);
            return null;
        }).when(repositorioMetaSpy).guardar(any(Meta.class));

        Session sessionMock = mock(Session.class);
        when(sessionFactoryMock.getCurrentSession()).thenReturn(sessionMock);
        when(sessionMock.get(Meta.class, 1L)).thenReturn(meta);

        // ejecucion
        repositorioMetaSpy.guardar(meta);

        // ejecucion
        assertNotNull(meta.getId());
        Meta metaGuardada = repositorioMetaSpy.obtenerMetaPorId(meta.getId());

        assertNotNull(metaGuardada);
        assertEquals(meta.getId(), metaGuardada.getId());
        assertEquals(meta.getUsuario(), metaGuardada.getUsuario());
        assertEquals(meta.getCategoriaMovimiento(), metaGuardada.getCategoriaMovimiento());
        verify(repositorioMetaSpy, times(1)).guardar(meta);
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

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioExisteMetaConUsuarioYCategoriaSeLanceExcepcionCategoriaConMetaExistente() throws ExcepcionBaseDeDatos, ExcepcionCategoriaConMetaExistente {
        // preparacion
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        CategoriaMovimiento categoriaMovimiento = new CategoriaMovimiento();
        categoriaMovimiento.setId(1L);

        RepositorioMeta repositorioMetaMock = mock(RepositorioMeta.class);
        doThrow(new ExcepcionCategoriaConMetaExistente()).when(repositorioMetaMock).existeMetaConUsuarioYCategoria(usuario, categoriaMovimiento);

        // ejecucion y verificacion
        Assertions.assertThrows(ExcepcionCategoriaConMetaExistente.class, () -> repositorioMetaMock.existeMetaConUsuarioYCategoria(usuario, categoriaMovimiento));
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioExisteMetaConUsuarioYCategoriaSeLanceExcepcionBaseDeDatos() throws ExcepcionBaseDeDatos, ExcepcionCategoriaConMetaExistente {
        // preparacion
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        CategoriaMovimiento categoriaMovimiento = new CategoriaMovimiento();
        categoriaMovimiento.setId(1L);

        RepositorioMeta repositorioMetaMock = mock(RepositorioMeta.class);
        doThrow(new ExcepcionBaseDeDatos()).when(repositorioMetaMock).existeMetaConUsuarioYCategoria(usuario, categoriaMovimiento);

        // ejecucion y verificacion
        Assertions.assertThrows(ExcepcionBaseDeDatos.class, () -> repositorioMetaMock.existeMetaConUsuarioYCategoria(usuario, categoriaMovimiento));
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioObtenerMetaPorIdSeObtengaLaMeta() throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente {
        // preparacion
        Meta meta = new Meta();
        meta.setId(1L);
        meta.setCategoriaMovimiento(new CategoriaMovimiento());
        meta.setUsuario(new Usuario());
        when(sessionFactoryMock.getCurrentSession().get(Meta.class, 1L)).thenReturn(meta);

        // ejecucion
        Meta metaObtenida = repositorioMeta.obtenerMetaPorId(1L);

        // verificacion
        assertNotNull(metaObtenida);
        assertEquals(meta.getId(), metaObtenida.getId());
        assertEquals(meta.getUsuario(), metaObtenida.getUsuario());
        assertEquals(meta.getCategoriaMovimiento(), metaObtenida.getCategoriaMovimiento());
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioObtenerMetaPorIdSeLanceExcepcionMetaNoExistente() throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente {
        // preparacion
        RepositorioMeta repositorioMetaMock = mock(RepositorioMeta.class);
        doThrow(new ExcepcionMetaNoExistente()).when(repositorioMetaMock).obtenerMetaPorId(1L);

        // ejecucion y verificacion
        Assertions.assertThrows(ExcepcionMetaNoExistente.class, () -> repositorioMetaMock.obtenerMetaPorId(1L));
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioObtenerMetaPorIdSeLanceExcepcionBaseDeDatos() throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente {
        // preparacion
        Meta meta = new Meta();
        meta.setId(1L);
        meta.setCategoriaMovimiento(new CategoriaMovimiento());
        meta.setUsuario(new Usuario());

        RepositorioMeta repositorioMetaMock = mock(RepositorioMeta.class);
        doThrow(new ExcepcionBaseDeDatos()).when(repositorioMetaMock).obtenerMetaPorId(1L);

        // ejecucion y verificacion
        Assertions.assertThrows(ExcepcionBaseDeDatos.class, () -> repositorioMetaMock.obtenerMetaPorId(1L));
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioEliminarMetaSeElimineLaMeta() throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente {
        // preparacion
        Meta meta = new Meta();
        meta.setId(1L);
        meta.setCategoriaMovimiento(new CategoriaMovimiento());
        meta.setUsuario(new Usuario());
        when(sessionFactoryMock.getCurrentSession().get(Meta.class, 1L)).thenReturn(meta);

        // ejecucion
        repositorioMeta.eliminarMeta(meta);

        // verificacion
        verify(sessionFactoryMock.getCurrentSession(), times(1)).delete(meta);
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioEliminarMetaSeLanceExcepcionMetaNoExistente() throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente {
        // preparacion
        Meta meta = new Meta();
        meta.setId(1L);
        meta.setCategoriaMovimiento(new CategoriaMovimiento());
        meta.setUsuario(new Usuario());

        RepositorioMeta repositorioMetaMock = mock(RepositorioMeta.class);
        doThrow(new ExcepcionMetaNoExistente()).when(repositorioMetaMock).eliminarMeta(meta);

        // ejecucion y verificacion
        Assertions.assertThrows(ExcepcionMetaNoExistente.class, () -> repositorioMetaMock.eliminarMeta(meta));
        verify(repositorioMetaMock, times(1)).eliminarMeta(meta);
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioEliminarMetaSeLanceExcepcionBaseDeDatos() throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente {
        // preparacion
        Meta meta = new Meta();
        meta.setId(1L);
        meta.setCategoriaMovimiento(new CategoriaMovimiento());
        meta.setUsuario(new Usuario());

        RepositorioMeta repositorioMetaMock = mock(RepositorioMeta.class);
        doThrow(new ExcepcionBaseDeDatos()).when(repositorioMetaMock).eliminarMeta(meta);

        // ejecucion y verificacion
        Assertions.assertThrows(ExcepcionBaseDeDatos.class, () -> repositorioMetaMock.eliminarMeta(meta));
        verify(repositorioMetaMock, times(1)).eliminarMeta(meta);
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioActualizarMetaSeActualiceLaMeta() throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente {
        // preparacion
        Meta meta = new Meta();
        meta.setId(1L);
        meta.setCategoriaMovimiento(new CategoriaMovimiento());
        meta.setUsuario(new Usuario());
        when(sessionFactoryMock.getCurrentSession().get(Meta.class, 1L)).thenReturn(meta);

        // ejecucion
        repositorioMeta.actualizarMeta(meta);

        // verificacion
        verify(sessionFactoryMock.getCurrentSession(), times(1)).update(meta);
        Meta metaActualizada = repositorioMeta.obtenerMetaPorId(1L);
        assertNotNull(metaActualizada);
        assertEquals(meta.getId(), metaActualizada.getId());
        assertEquals(meta.getUsuario(), metaActualizada.getUsuario());
        assertEquals(meta.getCategoriaMovimiento(), metaActualizada.getCategoriaMovimiento());
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioActualizarMetaSeLanceExcepcionMetaNoExistente() throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente {
        // preparacion
        Meta meta = new Meta();
        meta.setId(1L);
        meta.setCategoriaMovimiento(new CategoriaMovimiento());
        meta.setUsuario(new Usuario());

        RepositorioMeta repositorioMetaMock = mock(RepositorioMeta.class);
        doThrow(new ExcepcionMetaNoExistente()).when(repositorioMetaMock).actualizarMeta(meta);

        // ejecucion y verificacion
        Assertions.assertThrows(ExcepcionMetaNoExistente.class, () -> repositorioMetaMock.actualizarMeta(meta));
        verify(repositorioMetaMock, times(1)).actualizarMeta(meta);
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioActualizarMetaSeLanceExcepcionBaseDeDatos() throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente {
        // preparacion
        Meta meta = new Meta();
        meta.setId(1L);
        meta.setCategoriaMovimiento(new CategoriaMovimiento());
        meta.setUsuario(new Usuario());

        RepositorioMeta repositorioMetaMock = mock(RepositorioMeta.class);
        doThrow(new ExcepcionBaseDeDatos()).when(repositorioMetaMock).actualizarMeta(meta);

        // ejecucion y verificacion
        Assertions.assertThrows(ExcepcionBaseDeDatos.class, () -> repositorioMetaMock.actualizarMeta(meta));
        verify(repositorioMetaMock, times(1)).actualizarMeta(meta);
    }

}
