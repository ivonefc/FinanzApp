package com.tallerwebi.dominio.agregarMovimiento;

import com.tallerwebi.dominio.movimiento.Movimiento;
import com.tallerwebi.dominio.movimiento.RepositorioMovimiento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("servicioAgregarMovimiento")
@Transactional
public class ServicioAgregarMovimientoImpl implements ServicioAgregarMovimiento {

    private RepositorioAgregarMovimiento repositorioAgregarMovimiento;

    @Autowired
    public ServicioAgregarMovimientoImpl(RepositorioAgregarMovimiento repositorioAgregarMovimiento) {
        this.repositorioAgregarMovimiento = repositorioAgregarMovimiento;
    }

    @Transactional
    @Override
    public void nuevoMovimiento(Long idUsuario, Movimiento movimiento) {
        repositorioAgregarMovimiento.guardarMovimiento(idUsuario, movimiento);
    }
}
