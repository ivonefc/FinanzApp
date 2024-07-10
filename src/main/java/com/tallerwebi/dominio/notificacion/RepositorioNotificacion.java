package com.tallerwebi.dominio.notificacion;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;

import java.util.Date;
import java.util.List;

public interface RepositorioNotificacion {

    public void guardar(Notificacion notificacion) throws ExcepcionBaseDeDatos;

    public void actualizar(Long idNotificacion, String estado) throws ExcepcionBaseDeDatos;

    List<Notificacion> obtenerNotificacionesMetaFiltradaPorFecha(Long idUsuario, Date fechaInicio, Date fechaFin, String estado,String nombre);

    List<Notificacion> obtenerNotificacionMetasConcretadas(Long idUsuario);
}
