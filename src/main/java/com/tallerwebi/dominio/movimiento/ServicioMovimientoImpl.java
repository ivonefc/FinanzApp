package com.tallerwebi.dominio.movimiento;

import com.tallerwebi.dominio.RepositorioUsuario;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionCamposInvalidos;
import com.tallerwebi.presentacion.DatosAgregarMovimiento;
import com.tallerwebi.presentacion.DatosEditarMovimiento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;



@Service("servicioMovimiento")
@Transactional
public class ServicioMovimientoImpl implements ServicioMovimiento {

    private RepositorioMovimiento repositorioMovimiento;
    private RepositorioCategoria repositorioCategoria;
    private RepositorioUsuario repositorioUsuario;

    @Autowired
    public ServicioMovimientoImpl(RepositorioMovimiento repositorioMovimiento, RepositorioCategoria repositorioCategoria, RepositorioUsuario repositorioUsuario) {
        this.repositorioMovimiento = repositorioMovimiento;
        this.repositorioCategoria = repositorioCategoria;
        this.repositorioUsuario = repositorioUsuario;
    }


    @Override
    public List<Movimiento> obtenerMovimientos(Long idUsuario) throws ExcepcionBaseDeDatos{
        return repositorioMovimiento.obtenerMovimientos(idUsuario);
    }


    @Transactional
    @Override
    public Movimiento obtenerMovimientoPorId(Long id) {
        return repositorioMovimiento.obtenerMovimientoPorId(id);

    }

    @Transactional
    @Override
    public void actualizarMovimiento(DatosEditarMovimiento datosEditarMovimiento) throws ExcepcionCamposInvalidos {
        datosEditarMovimiento.validarCampos();
        String categoria = datosEditarMovimiento.getCategoria();
        String descripcion = datosEditarMovimiento.getDescripcion();
        Double monto = datosEditarMovimiento.getMonto();
        Long id = datosEditarMovimiento.getId();
        CategoriaMovimiento categoriaMovimiento = repositorioCategoria.obtenerCategoriaPorNombre(categoria);
        Movimiento movimiento = repositorioMovimiento.obtenerMovimientoPorId(id);
        movimiento.setCategoria(categoriaMovimiento);
        movimiento.setDescripcion(descripcion);
        movimiento.setMonto(monto);
        repositorioMovimiento.actualizarMovimiento(movimiento);
    }

    @Transactional
    @Override
    public void eliminarMovimiento(Long idUsuario, Movimiento movimiento) {
        repositorioMovimiento.eliminarMovimiento(idUsuario, movimiento);
    }

    @Override
    public List<Movimiento> obtenerMovimientosPorFecha(Long idUsuario, LocalDate fecha) {
        return repositorioMovimiento.obtenerMovimientosPorFecha(idUsuario, fecha);
    }

   @Transactional
    @Override
    public void nuevoMovimiento(Long idUsuario, DatosAgregarMovimiento datosAgregarMovimiento) throws ExcepcionBaseDeDatos, ExcepcionCamposInvalidos {
       datosAgregarMovimiento.validarCampos();
       //
       String descripcion = datosAgregarMovimiento.getDescripcion();
       Double monto = datosAgregarMovimiento.getMonto();
       String categoria = datosAgregarMovimiento.getCategoria();
       //
       Usuario usuario = repositorioUsuario.obtenerUsuarioPorId(idUsuario);
       CategoriaMovimiento categoriaMovimiento = repositorioCategoria.obtenerCategoriaPorNombre(categoria);
       Movimiento movimiento = new Movimiento(
               descripcion,
               monto,
               LocalDate.now(),
               categoriaMovimiento,
               usuario
       );
       repositorioMovimiento.guardarMovimiento(movimiento);
    }
}
