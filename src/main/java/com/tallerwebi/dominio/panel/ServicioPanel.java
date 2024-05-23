package com.tallerwebi.dominio.panel;

import com.tallerwebi.dominio.categoria.CategoriaMovimiento;
import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.movimiento.Movimiento;

import java.util.List;
import java.util.Map;

public interface ServicioPanel {

    //Map<String, Double> obtenerMontosPorCategoria(Long idUsuario)throws ExcepcionBaseDeDatos;

    List<Movimiento> obtenerMovimientosEgresosPorUsuario(Long idUsuario)throws ExcepcionBaseDeDatos;
    List<Movimiento> obtenerMovimientosIngresosPorUsuario(Long idUsuario)throws ExcepcionBaseDeDatos;
}
