package com.tallerwebi.infraestructura.movimientoCompartido;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.movimiento.Movimiento;
import com.tallerwebi.dominio.movimientoCompartido.RepositorioMovimientoCompartido;
import com.tallerwebi.dominio.usuario.Usuario;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("repositorioMovimientoCompartido")
public class RepositorioMovimientoCompartidoImpl implements RepositorioMovimientoCompartido {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioMovimientoCompartidoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    @Override
    public List<Usuario> obtenerAmigos(Long idUsuario) throws ExcepcionBaseDeDatos { //BUSCA POR ID DE USUARIO
        try{
            Session session = sessionFactory.getCurrentSession();
            return session.createQuery("SELECT a FROM Usuario u JOIN u.amigos a WHERE u.id = :idUsuario", Usuario.class)
                    .setParameter("idUsuario", idUsuario)
                    .getResultList();
        }catch (Exception he){
            throw new ExcepcionBaseDeDatos("Base de datos no disponible");
        }
    }

    @Override
    public void agregarNuevoAmigo(Long idUsuario, String email) throws ExcepcionBaseDeDatos {
        try {
            Session session = sessionFactory.getCurrentSession();

            // Buscar el usuario por email
            Usuario amigo = (Usuario) session.createQuery("FROM Usuario u WHERE u.email = :email")
                    .setParameter("email", email)
                    .uniqueResult();

            // Si el usuario con el email proporcionado no existe, lanzar una excepción
            if (amigo == null) {
                throw new ExcepcionBaseDeDatos("No se encontró un usuario con el email proporcionado");
            }

            // Buscar el usuario por id
            Usuario usuario = session.get(Usuario.class, idUsuario);

            // Si el usuario con el id proporcionado no existe, lanzar una excepción
            if (usuario == null) {
                throw new ExcepcionBaseDeDatos("No se encontró un usuario con el id proporcionado");
            }

            if(email.equals(usuario.getEmail())){
                throw new ExcepcionBaseDeDatos("No se puede agregar a si mismo");
            }
            // Agregar el amigo a la lista de amigos del usuario
            usuario.agregarAmigo(amigo);

        } catch (Exception e) {
            throw new ExcepcionBaseDeDatos("Base de datos no disponible", e);
        }
    }
}
