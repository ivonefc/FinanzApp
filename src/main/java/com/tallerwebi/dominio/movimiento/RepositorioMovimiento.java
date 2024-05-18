package com.tallerwebi.dominio.movimiento;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RepositorioMovimiento {
    List<Movimiento> obtenerMovimientos(Long idUsuario) throws ExcepcionBaseDeDatos;

    Optional<Movimiento> obtenerMovimientoPorId(Long idUsuario, Long id);

    void editarMovimiento(Long idUsuario, Movimiento movimiento);

    void guardarMovimiento(Long idUsuario, Movimiento movimiento, CategoriaMovimiento categoriaMovimiento);

    List<Movimiento> obtenerMovimientosPorFecha(Long idUsuario, LocalDate fecha);

    void eliminarMovimiento(Long idUsuario, Movimiento movimiento);
}
