package com.tallerwebi.dominio.movimiento;


import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionCamposInvalidos;
import com.tallerwebi.presentacion.DatosAgregarMovimiento;
import com.tallerwebi.presentacion.DatosEditarMovimiento;

import java.time.LocalDate;
import java.util.List;

public interface ServicioMovimiento {
    List<Movimiento> obtenerMovimientos(Long idUsuario) throws ExcepcionBaseDeDatos;

    Movimiento obtenerMovimientoPorId(Long id);

    void actualizarMovimiento(DatosEditarMovimiento datosEditarMovimiento) throws ExcepcionCamposInvalidos;

    List<Movimiento> obtenerMovimientosPorFecha(Long idUsuario, LocalDate fecha);

    void nuevoMovimiento(Long idUsuario, DatosAgregarMovimiento datosAgregarMovimiento) throws ExcepcionBaseDeDatos, ExcepcionCamposInvalidos;
}
