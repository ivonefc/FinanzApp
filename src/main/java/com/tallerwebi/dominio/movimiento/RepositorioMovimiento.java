package com.tallerwebi.dominio.movimiento;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;


import java.time.LocalDate;
import java.util.List;

public interface RepositorioMovimiento {
    List<Movimiento> obtenerMovimientos(Long idUsuario) throws ExcepcionBaseDeDatos;

    Movimiento obtenerMovimientoPorId(Long id);

    void actualizarMovimiento(Movimiento movimiento);

    void guardarMovimiento(Movimiento movimiento) throws ExcepcionBaseDeDatos;

    List<Movimiento> obtenerMovimientosPorFecha(Long idUsuario, LocalDate fecha);
}
