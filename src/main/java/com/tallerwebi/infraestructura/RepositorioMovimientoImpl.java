package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.movimiento.CategoriaMovimiento;
import com.tallerwebi.dominio.movimiento.Movimiento;
import com.tallerwebi.dominio.movimiento.RepositorioMovimiento;
import com.tallerwebi.presentacion.DatosAgregarMovimiento;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository("repositorioMovimiento")
public class RepositorioMovimientoImpl implements RepositorioMovimiento {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioMovimientoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Movimiento> obtenerMovimientos(Long idUsuario) throws ExcepcionBaseDeDatos{
        try{
            Session session = sessionFactory.getCurrentSession();
            Usuario usuario =  session.get(Usuario.class, idUsuario);
            return new ArrayList<>(usuario.getMovimientos());
        }catch (Exception he){
            throw new ExcepcionBaseDeDatos("Base de datos no disponible");
        }
    }

    @Override
    public Optional<Movimiento> obtenerMovimientoPorId(Long idUsuario, Long id) {
        Session session = sessionFactory.getCurrentSession();

        Movimiento movimiento = session.createQuery("FROM Movimiento M WHERE  M.usuario.id = :idUsuario AND M.id = :idMovimiento", Movimiento.class)
                .setParameter("idUsuario", idUsuario)
                .setParameter("idMovimiento", id)
                .uniqueResult();
        return Optional.ofNullable(movimiento);
    }

    @Override
    public void editarMovimiento(Long idUsuario, Movimiento movimiento) {
        Session session = sessionFactory.getCurrentSession();
        Usuario usuario = session.get(Usuario.class, idUsuario);
        movimiento.setUsuario(usuario);
        session.update(movimiento);
    }

    @Override
    public void guardarMovimiento(Long idUsuario, Movimiento movimiento, CategoriaMovimiento categoria) {
        Session session = sessionFactory.getCurrentSession();
        Usuario usuario = session.get(Usuario.class, idUsuario);
        CategoriaMovimiento categoriaMovimiento = session.get(CategoriaMovimiento.class, categoria.getId());
        usuario.getMovimientos().add(movimiento);
        categoriaMovimiento.getMovimientos().add(movimiento);
        movimiento.setUsuario(usuario);
        movimiento.setCategoria(categoriaMovimiento);
        session.update(usuario);
    }

    @Override
    public void eliminarMovimiento(Long idUsuario, Movimiento movimiento) {
        Session session = sessionFactory.getCurrentSession();
        Usuario usuario = session.get(Usuario.class, idUsuario);
        CategoriaMovimiento categoriaMovimiento = movimiento.getCategoria();
        usuario.getMovimientos().remove(movimiento);
        categoriaMovimiento.getMovimientos().remove(movimiento);
        session.delete(movimiento);
    }

    @Override
    public List<Movimiento> obtenerMovimientosPorFecha(Long idUsuario, LocalDate fecha) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM Movimiento m WHERE m.usuario.id = :idUsuario AND m.fechayHora = :fecha", Movimiento.class)
                .setParameter("idUsuario", idUsuario)
                .setParameter("fecha", fecha)
                .getResultList();


    }
}
