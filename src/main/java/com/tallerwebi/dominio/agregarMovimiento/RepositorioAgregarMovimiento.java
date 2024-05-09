package com.tallerwebi.dominio.agregarMovimiento;

import com.tallerwebi.dominio.movimiento.Movimiento;

public interface RepositorioAgregarMovimiento {
    void guardarMovimiento(Long idUsuario, Movimiento movimiento);
}
