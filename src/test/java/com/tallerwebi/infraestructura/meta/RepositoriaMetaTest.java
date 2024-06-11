package com.tallerwebi.infraestructura.meta;

import com.tallerwebi.dominio.categoria.CategoriaMovimiento;
import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionCategoriaConMetaExistente;
import com.tallerwebi.dominio.excepcion.ExcepcionMetaNoExistente;
import com.tallerwebi.dominio.meta.Meta;
import com.tallerwebi.dominio.meta.RepositorioMeta;
import com.tallerwebi.dominio.usuario.Usuario;
import com.tallerwebi.infraestructura.config.HibernateTestInfraestructuraConfig;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
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
        Session sessionMock = mock(Session.class);
        when(sessionFactoryMock.getCurrentSession()).thenReturn(sessionMock);
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

        // Usamos la implementación real del repositorio
        RepositorioMeta repositorioMetaReal = new RepositorioMetaImpl(sessionFactory);

        // ejecucion
        repositorioMetaReal.guardar(meta);

        // verificacion
        assertNotNull(meta.getId());
        Meta metaGuardada = repositorioMetaReal.obtenerMetaPorId(meta.getId());

        assertNotNull(metaGuardada);
        assertEquals(meta.getId(), metaGuardada.getId());
        assertEquals(meta.getUsuario(), metaGuardada.getUsuario());
        assertEquals(meta.getCategoriaMovimiento(), metaGuardada.getCategoriaMovimiento());
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioGuardarSeLanceExcepcionBaseDeDatos() {
        // preparacion
        Meta meta = new Meta();
        meta.setId(1L);
        meta.setCategoriaMovimiento(new CategoriaMovimiento());
        meta.setUsuario(new Usuario());

        // mockeando la session factory para que lance una excepcion
        SessionFactory sessionFactoryMock = mock(SessionFactory.class);
        when(sessionFactoryMock.getCurrentSession()).thenThrow(new HibernateException("Base de datos no disponible"));

        RepositorioMeta repositorioMeta = new RepositorioMetaImpl(sessionFactoryMock);

        // ejecucion y verificacion
        assertThrows(ExcepcionBaseDeDatos.class, () -> repositorioMeta.guardar(meta));
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
    public void queAlSolicitarAlRepositorioExisteMetaConUsuarioYCategoriaSeLanceExcepcionCategoriaConMetaExistente() {
        // preparacion
        // genera usuario y categoria
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        CategoriaMovimiento categoriaMovimiento = new CategoriaMovimiento();
        categoriaMovimiento.setId(1L);

        // genera meta
        Meta meta = new Meta();
        meta.setUsuario(usuario);
        meta.setCategoriaMovimiento(categoriaMovimiento);

        // guarda usuario, categoria y meta
        Session session = sessionFactory.getCurrentSession();
        session.save(usuario);
        session.save(categoriaMovimiento);
        session.save(meta);

        RepositorioMeta repositorioMeta = new RepositorioMetaImpl(sessionFactory); // use real implementation

        // ejecucion y verificacion
        assertThrows(ExcepcionCategoriaConMetaExistente.class, () -> repositorioMeta.existeMetaConUsuarioYCategoria(usuario, categoriaMovimiento));
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioExisteMetaConUsuarioYCategoriaSeLanceExcepcionBaseDeDatos() {
        // preparacion
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        CategoriaMovimiento categoriaMovimiento = new CategoriaMovimiento();
        categoriaMovimiento.setId(1L);

        // mockeando la session factory para que lance una excepcion
        SessionFactory sessionFactoryMock = mock(SessionFactory.class);
        when(sessionFactoryMock.getCurrentSession()).thenThrow(new HibernateException("Base de datos no disponible"));

        RepositorioMeta repositorioMeta = new RepositorioMetaImpl(sessionFactoryMock);

        // ejecucion y verificacion
        assertThrows(ExcepcionBaseDeDatos.class, () -> repositorioMeta.existeMetaConUsuarioYCategoria(usuario, categoriaMovimiento));
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
    public void queAlSolicitarAlRepositorioObtenerMetaPorIdSeLanceExcepcionMetaNoExistente() {
        // preparacion
        Long idMetaNoExistente = 1L;
        RepositorioMeta repositorioMeta = new RepositorioMetaImpl(sessionFactory);

        // ejecucion y verificacion
        assertThrows(ExcepcionMetaNoExistente.class, () -> repositorioMeta.obtenerMetaPorId(idMetaNoExistente));
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioObtenerMetaPorIdSeLanceExcepcionBaseDeDatos()  {
        // preparacion
        Long idMetaNoExistente = 1L;

        // mockeando la session factory para que lance una excepcion
        SessionFactory sessionFactoryMock = mock(SessionFactory.class);
        when(sessionFactoryMock.getCurrentSession()).thenThrow(new HibernateException("Base de datos no disponible"));

        RepositorioMeta repositorioMeta = new RepositorioMetaImpl(sessionFactoryMock);

        // ejecucion y verificacion
        assertThrows(ExcepcionBaseDeDatos.class, () -> repositorioMeta.obtenerMetaPorId(idMetaNoExistente));
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
    public void queAlSolicitarAlRepositorioEliminarMetaSeLanceExcepcionMetaNoExistente() {
        // preparacion
        Long idMetaNoExistente = 1L;
        Meta meta = new Meta();
        meta.setId(idMetaNoExistente);
        meta.setCategoriaMovimiento(new CategoriaMovimiento());
        meta.setUsuario(new Usuario());

        // ejecucion y verificacion
        assertThrows(ExcepcionMetaNoExistente.class, () -> repositorioMeta.eliminarMeta(meta));
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioEliminarMetaSeLanceExcepcionBaseDeDatos()  {
        // preparacion
        Meta meta = new Meta();
        meta.setId(1L);
        meta.setCategoriaMovimiento(new CategoriaMovimiento());
        meta.setUsuario(new Usuario());

        // mockeando la session factory para que lance una excepcion
        SessionFactory sessionFactoryMock = mock(SessionFactory.class);
        when(sessionFactoryMock.getCurrentSession()).thenThrow(new HibernateException("Base de datos no disponible"));

        RepositorioMeta repositorioMeta = new RepositorioMetaImpl(sessionFactoryMock); // use real implementation

        // ejecucion y verificacion
        assertThrows(ExcepcionBaseDeDatos.class, () -> repositorioMeta.eliminarMeta(meta));
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
    public void queAlSolicitarAlRepositorioActualizarMetaSeLanceExcepcionMetaNoExistente() {
        // preparacion
        Long idMetaNoExistente = 1L;

        Meta meta = new Meta();
        meta.setId(idMetaNoExistente);
        meta.setCategoriaMovimiento(new CategoriaMovimiento());
        meta.setUsuario(new Usuario());

        RepositorioMeta repositorioMeta = new RepositorioMetaImpl(sessionFactory); // use real implementation

        // ejecucion y verificacion
        assertThrows(ExcepcionMetaNoExistente.class, () -> repositorioMeta.actualizarMeta(meta));
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioActualizarMetaSeLanceExcepcionBaseDeDatos() {
        // preparacion
        Meta meta = new Meta();
        meta.setId(1L);
        meta.setCategoriaMovimiento(new CategoriaMovimiento());
        meta.setUsuario(new Usuario());

        // mockeando la session factory para que lance una excepcion
        SessionFactory sessionFactoryMock = mock(SessionFactory.class);
        when(sessionFactoryMock.getCurrentSession()).thenThrow(new HibernateException("Base de datos no disponible"));

        RepositorioMeta repositorioMeta = new RepositorioMetaImpl(sessionFactoryMock);

        // ejecucion y verificacion
        assertThrows(ExcepcionBaseDeDatos.class, () -> repositorioMeta.actualizarMeta(meta));
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioObtenerMetasSeObtenganLasMetas() throws ExcepcionBaseDeDatos {
        //preparacion
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Meta meta1 = new Meta();
        Meta meta2 = new Meta();
        meta1.setUsuario(usuario);
        meta2.setUsuario(usuario);

        Session sessionMock = mock(Session.class);
        Query queryMock = mock(Query.class);
        when(sessionFactoryMock.getCurrentSession()).thenReturn(sessionMock);
        when(sessionMock.createQuery(anyString(), eq(Meta.class))).thenReturn(queryMock);
        when(queryMock.setParameter(anyString(), any())).thenReturn(queryMock);
        when(queryMock.getResultList()).thenReturn(Arrays.asList(meta1, meta2));

        RepositorioMeta repositorioMeta = new RepositorioMetaImpl(sessionFactoryMock); // use real implementation

        //ejecucion
        List<Meta> metas = repositorioMeta.obtenerMetas(usuario.getId());

        //validacion
        verify(sessionMock, times(1)).createQuery("FROM Meta m WHERE m.usuario.id = :idUsuario", Meta.class);
        verify(queryMock, times(1)).setParameter("idUsuario", usuario.getId());
        verify(queryMock, times(1)).getResultList();

        assertThat(metas, notNullValue());
        assertThat(metas, not(empty()));
        assertThat(metas, containsInAnyOrder(meta1, meta2));
        assertThat(metas, hasSize(2));
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioObtenerMetasSeLanceExcepcionBaseDeDatos(){
        // preparacion
        // mockeando la session factory para que lance una excepcion
        SessionFactory sessionFactoryMock = mock(SessionFactory.class);
        when(sessionFactoryMock.getCurrentSession()).thenThrow(new HibernateException("Base de datos no disponible"));

        // inicializando repositorioMeta con sessionFactoryMock
        repositorioMeta = new RepositorioMetaImpl(sessionFactoryMock);

        // ejecucion y verificacion
        assertThrows(ExcepcionBaseDeDatos.class, () -> repositorioMeta.obtenerMetas(1L));
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void queAlSolicitarAlRepositorioObtenerMetasMeDevuelvaUnaListaVaciaYaQueNoTieneMetas() throws ExcepcionBaseDeDatos {
        // preparacion
        Session sessionMock = mock(Session.class);
        Query queryMock = mock(Query.class);

        when(sessionFactoryMock.getCurrentSession()).thenReturn(sessionMock);
        when(sessionMock.createQuery(anyString(), eq(Meta.class))).thenReturn(queryMock);
        when(queryMock.setParameter(anyString(), any())).thenReturn(queryMock);
        when(queryMock.getResultList()).thenReturn(Collections.emptyList());

        repositorioMeta = new RepositorioMetaImpl(sessionFactoryMock);

        // ejecucion
        List<Meta> metas = repositorioMeta.obtenerMetas(1L);

        // validacion
        assertThat(metas, notNullValue());
        assertThat(metas, empty());
    }



}
