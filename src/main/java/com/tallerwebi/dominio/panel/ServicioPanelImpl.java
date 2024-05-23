package com.tallerwebi.dominio.panel;

import com.tallerwebi.dominio.categoria.CategoriaMovimiento;
import com.tallerwebi.dominio.categoria.RepositorioCategoria;
import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionCamposInvalidos;
import com.tallerwebi.dominio.excepcion.ExcepcionCategoriaConMetaExistente;
import com.tallerwebi.dominio.movimiento.Movimiento;
import com.tallerwebi.dominio.movimiento.ServicioMovimiento;
import com.tallerwebi.dominio.usuario.RepositorioUsuario;
import com.tallerwebi.dominio.usuario.Usuario;
import com.tallerwebi.presentacion.meta.DatosMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service("servicioPanel")
public class ServicioPanelImpl implements ServicioPanel{

    @Autowired
    private ServicioMovimiento servicioMovimiento;


    /*public Map<String, Double> obtenerMontosPorCategoria(Long idUsuario) throws ExcepcionBaseDeDatos {
        List<Movimiento> movimientos = servicioMovimiento.obtenerMovimientos(idUsuario);

        Map<String, Double> montosPorCategoria = movimientos.stream()
                .filter(movimiento -> "EGRESO".equals(movimiento.getCategoria().getTipo().getNombre()))
                .collect(Collectors.groupingBy(
                        movimiento -> movimiento.getCategoria().getNombre(),
                        Collectors.summingDouble(Movimiento::getMonto)
                ));

        return montosPorCategoria;
    }*/

    public List<Movimiento> obtenerMovimientosEgresosPorUsuario(Long idUsuario) throws ExcepcionBaseDeDatos {
        List<Movimiento> movimientos = servicioMovimiento.obtenerMovimientos(idUsuario);

        List<Movimiento> movimientosEgresos = movimientos.stream()
                .filter(movimiento -> "EGRESO".equals(movimiento.getCategoria().getTipo().getNombre()))
                .collect(Collectors.toList());

        return movimientosEgresos;
    }

    public List<Movimiento> obtenerMovimientosIngresosPorUsuario(Long idUsuario) throws ExcepcionBaseDeDatos {
        List<Movimiento> movimientos = servicioMovimiento.obtenerMovimientos(idUsuario);

        List<Movimiento> movimientosIngresos = movimientos.stream()
                .filter(movimiento -> "INGRESO".equals(movimiento.getCategoria().getTipo().getNombre()))
                .collect(Collectors.toList());

        return movimientosIngresos;
    }
}
