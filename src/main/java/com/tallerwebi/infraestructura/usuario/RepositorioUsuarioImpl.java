package com.tallerwebi.infraestructura.usuario;

import com.tallerwebi.dominio.categoria.CategoriaMovimiento;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.usuario.Usuario;
import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;
import com.tallerwebi.dominio.usuario.RepositorioUsuario;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository("repositorioUsuario")
public class RepositorioUsuarioImpl implements RepositorioUsuario {

    @PersistenceContext
    private EntityManager entityManager;

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioUsuarioImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Usuario buscarUsuarioPorEmailYPassword(String email, String password) throws UsuarioInexistente, ExcepcionBaseDeDatos {
        Usuario usuario;
        try {
            Session session = sessionFactory.getCurrentSession();
            usuario = (Usuario) session.createQuery("FROM Usuario u WHERE u.email = :email and u.password = :password")
                    .setParameter("email", email)
                    .setParameter("password", password)
                    .uniqueResult();

            if (usuario == null)
                throw new UsuarioInexistente();

            return usuario;
        }catch (HibernateException e){
            throw new ExcepcionBaseDeDatos(e);
        }
    }

    @Override
    public void guardar(Usuario usuario) throws ExcepcionBaseDeDatos, UsuarioExistente { // CREAR USUARIO Y GUARDARLO EN LA BDD
        try {
            Session session = sessionFactory.getCurrentSession();
            Usuario usuarioExistente = (Usuario) session.createQuery("FROM Usuario u WHERE u.email = :email")
                    .setParameter("email", usuario.getEmail())
                    .uniqueResult();

            if (usuarioExistente != null)
                throw new UsuarioExistente();

            session.save(usuario);
        } catch (HibernateException e) {
            throw new ExcepcionBaseDeDatos(e);
        }
    }

    @Override
    public Usuario buscarUsuarioPorEmail(String email) throws UsuarioInexistente, ExcepcionBaseDeDatos {
        try{
            Session session = sessionFactory.getCurrentSession();
            Usuario usuario = (Usuario) session.createQuery("FROM Usuario u WHERE u.email = :email")
                    .setParameter("email", email)
                    .uniqueResult();

            if(usuario == null)
                throw new UsuarioInexistente();

            return usuario;
        } catch (HibernateException e) {
            throw new ExcepcionBaseDeDatos(e);
        }
    }

    @Override
    public boolean validarQueUsuarioNoExista(String email) {
            Session session = sessionFactory.getCurrentSession();
            Usuario usuario = (Usuario) session.createQuery("FROM Usuario u WHERE u.email = :email")
                    .setParameter("email", email)
                    .uniqueResult();

            if(usuario != null) {
                return true;
            } else {
                return false;
            }

    }

    @Override
    public List<Usuario> obtenerAmigosDeUnUsuario(Long idUsuario) throws ExcepcionBaseDeDatos {
        try {
            Session session = sessionFactory.getCurrentSession();

            NativeQuery query = session.createNativeQuery("SELECT * FROM usuarios u WHERE u.id IN (SELECT a.amigo_id FROM amigos a WHERE a.usuario_id = '1')", Usuario.class);
            query.setParameter("idUsuario", idUsuario);
            List<Usuario> amigos = query.getResultList();

            return amigos;
        } catch (HibernateException e) {
            throw new ExcepcionBaseDeDatos(e);
        }
    }

    @Override
    public void modificar(Usuario usuario) throws ExcepcionBaseDeDatos, UsuarioInexistente {
        if (usuario == null)
            throw new UsuarioInexistente();

        try {
            Session session = sessionFactory.getCurrentSession();
            Usuario usuarioExistente = session.get(Usuario.class, usuario.getId());
            if (usuarioExistente == null)
                throw new UsuarioInexistente();
            session.update(usuario);
        } catch (HibernateException e) {
            throw new ExcepcionBaseDeDatos("Base de datos no disponible");
        }
    }

    @Override
    public Usuario obtenerUsuarioPorId(Long id) throws ExcepcionBaseDeDatos, UsuarioInexistente {
        try {
            Session session = sessionFactory.getCurrentSession();
            Usuario usuario = session.get(Usuario.class, id);

            if (usuario == null)
                throw new UsuarioInexistente();

            return usuario;
        } catch (HibernateException e) {
            throw new ExcepcionBaseDeDatos(e);
        }
    }

    public void actualizarPlan(Usuario usuario) throws ExcepcionBaseDeDatos {
        try {
            entityManager.merge(usuario);
        } catch (Exception e) {
            throw new ExcepcionBaseDeDatos("Base de datos no disponible");
        }
    }

}
