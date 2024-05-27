package com.tallerwebi.dominio.movimiento;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionMovimientoNoEncontrado;


import java.time.LocalDate;
import java.util.List;

public interface RepositorioMovimiento {
    List<Movimiento> obtenerMovimientos(Long idUsuario) throws ExcepcionBaseDeDatos;

    Movimiento obtenerMovimientoPorId(Long id) throws ExcepcionMovimientoNoEncontrado, ExcepcionBaseDeDatos;

    void actualizarMovimiento(Movimiento movimiento) throws ExcepcionBaseDeDatos, ExcepcionMovimientoNoEncontrado;

    void guardarMovimiento(Movimiento movimiento) throws ExcepcionBaseDeDatos;

    List<Movimiento> obtenerMovimientosPorFecha(Long idUsuario, LocalDate fecha) throws ExcepcionBaseDeDatos;

    void eliminarMovimiento(Movimiento movimiento) throws ExcepcionBaseDeDatos, ExcepcionMovimientoNoEncontrado;
    Long obtenerCantidadDeMovimientosPorId(Long idUsuario) throws ExcepcionBaseDeDatos;
}
