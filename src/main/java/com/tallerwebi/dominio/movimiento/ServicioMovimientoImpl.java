package com.tallerwebi.dominio.movimiento;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service("servicioMovimiento")
@Transactional
public class ServicioMovimientoImpl implements ServicioMovimiento {

    private RepositorioMovimiento repositorioMovimiento;

    @Autowired
    public ServicioMovimientoImpl(RepositorioMovimiento repositorioMovimiento) {
        this.repositorioMovimiento = repositorioMovimiento;
    }


    @Override
    public List<Movimiento> obtenerMovimientos(Long idUsuario) throws ExcepcionBaseDeDatos{
        return repositorioMovimiento.obtenerMovimientos(idUsuario);
    }


    @Transactional
    @Override
    public Optional<Movimiento> obtenerMovimientoPorId(Long idUsuario, Long id) {
        return repositorioMovimiento.obtenerMovimientoPorId(idUsuario, id);

    }

    @Transactional
    @Override
    public void editarMovimiento(Long idUsuario, Movimiento movimiento) {
        repositorioMovimiento.editarMovimiento(idUsuario, movimiento);
    }

    @Transactional
    @Override
    public void nuevoMovimiento(Long idUsuario, Movimiento movimiento, CategoriaMovimiento categoriaMovimiento) {
        repositorioMovimiento.guardarMovimiento(idUsuario, movimiento, categoriaMovimiento);
    }

    @Override
    public List<Movimiento> obtenerMovimientosPorFecha(Long idUsuario, LocalDate fecha) {
        return repositorioMovimiento.obtenerMovimientosPorFecha(idUsuario, fecha);
    }
}
