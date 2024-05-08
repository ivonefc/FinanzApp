package com.tallerwebi.dominio.movimiento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service("servicioMovimiento")
@Transactional
public class ServicioMovimientoImpl implements ServicioMovimiento {

    private RepositorioMovimiento repositorioMovimiento;

    @Autowired
    public ServicioMovimientoImpl(RepositorioMovimiento repositorioMovimiento) {
        this.repositorioMovimiento = repositorioMovimiento;
    }


    @Override
    public List<Movimiento> obtenerMovimientos(Long idUsuario) {
        List<Movimiento> movimientos = repositorioMovimiento.obtenerMovimientos(idUsuario);
        return movimientos;
    }


}
