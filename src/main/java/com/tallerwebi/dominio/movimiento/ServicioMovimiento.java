package com.tallerwebi.dominio.movimiento;


import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExceptionSinDatos;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ServicioMovimiento {
    List<Movimiento> obtenerMovimientos(Long idUsuario) throws ExcepcionBaseDeDatos;

    Optional<Movimiento> obtenerMovimientoPorId(Long idUsuario, Long id);

    void editarMovimiento(Long idUsuario, Movimiento movimiento) throws ExcepcionBaseDeDatos, ExceptionSinDatos;

    void nuevoMovimiento(Long idUsuario, Movimiento movimiento, CategoriaMovimiento categoriaMovimiento) throws ExcepcionBaseDeDatos, ExceptionSinDatos;

    void eliminarMovimiento(Long idUsuario, Movimiento movimiento) throws ExcepcionBaseDeDatos;

    List<Movimiento> obtenerMovimientosPorFecha(Long idUsuario, LocalDate fecha);
}
