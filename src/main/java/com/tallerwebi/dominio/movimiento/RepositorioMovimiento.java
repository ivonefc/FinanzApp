package com.tallerwebi.dominio.movimiento;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionMovimientoNoEncontrado;


import java.time.LocalDate;
import java.util.List;

public interface RepositorioMovimiento {
    List<Movimiento> obtenerMovimientos(Long idUsuario) throws ExcepcionBaseDeDatos;

    Movimiento obtenerMovimientoPorId(Long id) throws ExcepcionMovimientoNoEncontrado;

    void actualizarMovimiento(Movimiento movimiento) throws ExcepcionBaseDeDatos;

    void guardarMovimiento(Movimiento movimiento) throws ExcepcionBaseDeDatos;

    List<Movimiento> obtenerMovimientosPorFecha(Long idUsuario, LocalDate fecha);

    void eliminarMovimiento(Movimiento movimiento);
}
