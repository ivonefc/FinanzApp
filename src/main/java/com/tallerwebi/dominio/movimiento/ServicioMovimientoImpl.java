package com.tallerwebi.dominio.movimiento;

import com.tallerwebi.dominio.categoria.CategoriaMovimiento;
import com.tallerwebi.dominio.categoria.RepositorioCategoria;
import com.tallerwebi.dominio.usuario.RepositorioUsuario;
import com.tallerwebi.dominio.usuario.Usuario;
import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionCamposInvalidos;
import com.tallerwebi.dominio.excepcion.ExcepcionMovimientoNoEncontrado;
import com.tallerwebi.presentacion.movimiento.DatosAgregarMovimiento;
import com.tallerwebi.presentacion.movimiento.DatosEditarMovimiento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    public List<Movimiento> obtenerMovimientos(Long idUsuario) throws ExcepcionBaseDeDatos{ //ID DE USUARIO
        return repositorioMovimiento.obtenerMovimientos(idUsuario);
    }

    @Transactional
    @Override
    public Movimiento obtenerMovimientoPorId(Long id) throws ExcepcionMovimientoNoEncontrado, ExcepcionBaseDeDatos { //ID DE MOVIMIENTO
        return repositorioMovimiento.obtenerMovimientoPorId(id);
    }

    @Transactional
    @Override
    public void actualizarMovimiento(DatosEditarMovimiento datosEditarMovimiento) throws ExcepcionCamposInvalidos, ExcepcionMovimientoNoEncontrado, ExcepcionBaseDeDatos {
        datosEditarMovimiento.validarCampos();
        String categoria = datosEditarMovimiento.getCategoria();
        String descripcion = datosEditarMovimiento.getDescripcion();
        Double monto = datosEditarMovimiento.getMonto();
        Long id = datosEditarMovimiento.getId();
        CategoriaMovimiento categoriaMovimiento = repositorioCategoria.obtenerCategoriaPorNombre(categoria);
        if (categoriaMovimiento == null) {
            Map<String, String> errores = new HashMap<>();
            errores.put("categoria", "La categoría no existe");
            throw new ExcepcionCamposInvalidos(errores);
        }
        Movimiento movimiento = repositorioMovimiento.obtenerMovimientoPorId(id);
        if (movimiento == null)
            throw new ExcepcionMovimientoNoEncontrado("El movimiento no existe");
        movimiento.setCategoria(categoriaMovimiento);
        movimiento.setDescripcion(descripcion);
        movimiento.setMonto(monto);
        repositorioMovimiento.actualizarMovimiento(movimiento);
    }

    @Transactional
    @Override
    public void eliminarMovimiento(Long id) throws ExcepcionMovimientoNoEncontrado, ExcepcionBaseDeDatos {
        Movimiento movimiento = repositorioMovimiento.obtenerMovimientoPorId(id);
        if (movimiento == null)
            throw new ExcepcionMovimientoNoEncontrado("No se encontró ningún movimiento con el ID proporcionado");
        repositorioMovimiento.eliminarMovimiento(movimiento);
    }

    @Override
    public List<Movimiento> obtenerMovimientosPorFecha(Long idUsuario, LocalDate fecha) throws ExcepcionBaseDeDatos {
        return repositorioMovimiento.obtenerMovimientosPorFecha(idUsuario, fecha);
    }

    @Transactional
    @Override
    public void nuevoMovimiento(Long idUsuario, DatosAgregarMovimiento datosAgregarMovimiento) throws ExcepcionBaseDeDatos, ExcepcionCamposInvalidos {
       datosAgregarMovimiento.validarCampos();

       String descripcion = datosAgregarMovimiento.getDescripcion();
       Double monto = datosAgregarMovimiento.getMonto();
       String categoria = datosAgregarMovimiento.getCategoria();

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
