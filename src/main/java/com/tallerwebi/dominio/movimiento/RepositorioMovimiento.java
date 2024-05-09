package com.tallerwebi.dominio.movimiento;

import java.util.List;
import java.util.Optional;

public interface RepositorioMovimiento {
    List<Movimiento> obtenerMovimientos(Long idUsuario);

    Optional<Movimiento> obtenerMovimientoPorId(Long idUsuario, Long id);

    void editarMovimiento(Movimiento movimiento);
}
