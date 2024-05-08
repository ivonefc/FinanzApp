package com.tallerwebi.dominio.agregarMovimiento;

import com.tallerwebi.dominio.movimiento.Movimiento;
import org.springframework.stereotype.Service;

@Service
public interface ServicioAgregarMovimiento {
    void nuevoMovimiento(Movimiento movimiento);
}
