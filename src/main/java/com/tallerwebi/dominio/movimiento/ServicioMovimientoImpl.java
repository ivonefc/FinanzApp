package com.tallerwebi.dominio.movimiento;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ServicioMovimientoImpl implements ServicioMovimiento {

    private RepositorioMovimiento repositorioMovimiento;

    public ServicioMovimientoImpl(RepositorioMovimiento repositorioMovimiento) {
        this.repositorioMovimiento = repositorioMovimiento;
    }

    @Transactional
    @Override
    public List<Movimiento> obtenerMovimientos(Long idUsuario) {
        List<Movimiento> movimientos = repositorioMovimiento.obtenerMovimientos(idUsuario);
        return movimientos;
    }

    @Transactional
    @Override
    public Optional<Movimiento> obtenerMovimientoPorId(Long idUsuario, Long id) {
        return repositorioMovimiento.obtenerMovimientoPorId(idUsuario, id);

    }

    @Transactional
    @Override
    public void editarMovimiento(Movimiento movimiento) {
        repositorioMovimiento.editarMovimiento(movimiento);
    }

}
