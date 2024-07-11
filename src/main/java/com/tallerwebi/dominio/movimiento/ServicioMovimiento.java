package com.tallerwebi.dominio.movimiento;


import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionCamposInvalidos;
import com.tallerwebi.dominio.excepcion.ExcepcionMovimientoNoEncontrado;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;
import com.tallerwebi.dominio.notificacion.Notificacion;
import com.tallerwebi.presentacion.movimiento.DatosAgregarMovimiento;
import com.tallerwebi.presentacion.movimiento.DatosEditarMovimiento;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ServicioMovimiento {
    List<Movimiento> obtenerMovimientos(Long idUsuario) throws ExcepcionBaseDeDatos;

    Movimiento obtenerMovimientoPorId(Long id) throws ExcepcionMovimientoNoEncontrado, ExcepcionBaseDeDatos;

    void actualizarMovimiento(DatosEditarMovimiento datosEditarMovimiento) throws ExcepcionCamposInvalidos, ExcepcionMovimientoNoEncontrado, ExcepcionBaseDeDatos;

    void eliminarMovimiento(Long id) throws ExcepcionBaseDeDatos, ExcepcionMovimientoNoEncontrado;

    List<Movimiento> obtenerMovimientosPorFecha(Long idUsuario, LocalDate fecha) throws ExcepcionBaseDeDatos;

    void nuevoMovimiento(Long idUsuario, DatosAgregarMovimiento datosAgregarMovimiento) throws ExcepcionBaseDeDatos, ExcepcionCamposInvalidos, UsuarioInexistente;

    int calcularCantidadDePaginas(Long idUsuario, int tamanioDePaginas) throws ExcepcionBaseDeDatos;

    List<Movimiento> obtenerMovimientosPorPagina(Long idUsuario, int pagina, int tamanioDePagina) throws ExcepcionBaseDeDatos;

    Map<String, Double> obtenerTotalGastadoEnCategoriasConMetas(Long idUsuario) throws ExcepcionBaseDeDatos;

    List<Notificacion> obtenerMovimientosCompartidos(Long idUsuario) throws ExcepcionBaseDeDatos;

    void aceptarMovimiento(Long idNotificacion, String estado) throws ExcepcionBaseDeDatos;

    void guardarMovimientoDesdeNotificacion(Long idUsuario, DatosAgregarMovimiento datosAgregarMovimiento) throws ExcepcionBaseDeDatos, ExcepcionCamposInvalidos, UsuarioInexistente;

    Map<String, Double> obtenerMetasConFecha(Long idUsuario) throws ExcepcionBaseDeDatos;

    Double obtenerTotalGastado(Long idUsuario, Long idMeta, Date fechaInicio, Date fechaFin) throws ExcepcionBaseDeDatos;

    void calcularTodasLasMetas(Long idUsuario) throws ExcepcionBaseDeDatos;
}
