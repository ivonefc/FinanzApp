package com.tallerwebi.dominio.movimiento;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionMovimientoNoEncontrado;
import com.tallerwebi.dominio.notificacion.Notificacion;
import com.tallerwebi.dominio.meta.Meta;


import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface RepositorioMovimiento {
    List<Movimiento> obtenerMovimientos(Long idUsuario) throws ExcepcionBaseDeDatos;

    Movimiento obtenerMovimientoPorId(Long id) throws ExcepcionMovimientoNoEncontrado, ExcepcionBaseDeDatos;

    void actualizarMovimiento(Movimiento movimiento) throws ExcepcionBaseDeDatos, ExcepcionMovimientoNoEncontrado;

    void guardarMovimiento(Movimiento movimiento) throws ExcepcionBaseDeDatos;

    List<Movimiento> obtenerMovimientosPorFecha(Long idUsuario, LocalDate fecha) throws ExcepcionBaseDeDatos;

    void eliminarMovimiento(Movimiento movimiento) throws ExcepcionBaseDeDatos, ExcepcionMovimientoNoEncontrado;

    Long obtenerCantidadDeMovimientosPorId(Long idUsuario) throws ExcepcionBaseDeDatos;

    List<Movimiento> obtenerMovimientosPorPagina(Long idUsuario, int pagina, int tamanioDePagina) throws ExcepcionBaseDeDatos;

    Double obtenerTotalPorCategoriaEnMesYAnioActual(Long id, int mes, int anio) throws ExcepcionBaseDeDatos;

    List<Notificacion> obtenerMovimientosCompartidos(Long idUsuario) throws ExcepcionBaseDeDatos;

    Double obtenerTotalPorCategoriaPorFecha(Long idUsuario, Long idCategoria, Date fechaInicio, Date fechaFin) throws ExcepcionBaseDeDatos;

    List<Movimiento> obtenerMovimientosFiltradosCategoriaFecha(String categoria, Date fechaInicio, Date fechaFin, Long idUsuario) throws ExcepcionBaseDeDatos;


}
