package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.movimiento.Movimiento;
import com.tallerwebi.dominio.movimiento.RepositorioMovimiento;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class RepositorioMovimientoImpl implements RepositorioMovimiento {
    private SessionFactory sessionFactory;

    public RepositorioMovimientoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Movimiento> obtenerMovimientos(Long idUsuario) {
        Session session = sessionFactory.getCurrentSession();
        Usuario usuario =  session.get(Usuario.class, idUsuario);

        return new ArrayList<>(usuario.getMovimientos()) ;
    }

    @Override
    public Optional<Movimiento> obtenerMovimientoPorId(Long idUsuario, Long id) {
        Session session = sessionFactory.getCurrentSession();
        Usuario usuario = session.get(Usuario.class, idUsuario);

        Set<Movimiento> movimientos = usuario.getMovimientos();

        return movimientos
                .stream()
                .filter(movimiento -> movimiento.getId().equals(id))
                .findFirst();
    }

    @Override
    public void editarMovimiento(Movimiento movimiento) {
        Session session = sessionFactory.getCurrentSession();

        session.update(movimiento);
    }
}
