package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.agregarMovimiento.RepositorioAgregarMovimiento;
import com.tallerwebi.dominio.movimiento.Movimiento;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("repositorioAgregarMovimiento")
public class RepositorioAgregarMovimientoImpl implements RepositorioAgregarMovimiento {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioAgregarMovimientoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public void guardarMovimiento(Movimiento movimiento) {

        sessionFactory.getCurrentSession().save(movimiento);
    }
}
