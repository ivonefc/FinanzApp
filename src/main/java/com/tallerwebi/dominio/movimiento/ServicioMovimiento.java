package com.tallerwebi.dominio.movimiento;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ServicioMovimiento {
    List<Movimiento> obtenerMovimientos(Long idUsuario);

}
