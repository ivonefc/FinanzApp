package com.tallerwebi.dominio.meta;

import com.tallerwebi.dominio.categoria.CategoriaMovimiento;
import com.tallerwebi.dominio.categoria.RepositorioCategoria;
import com.tallerwebi.dominio.excepcion.*;
import com.tallerwebi.dominio.movimiento.ServicioMovimiento;
import com.tallerwebi.dominio.notificacion.Notificacion;
import com.tallerwebi.dominio.notificacion.RepositorioNotificacion;
import com.tallerwebi.dominio.usuario.RepositorioUsuario;
import com.tallerwebi.dominio.usuario.Usuario;
import com.tallerwebi.presentacion.meta.DatosEditarMeta;
import com.tallerwebi.presentacion.meta.DatosMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class ServicioMetaImpl implements ServicioMeta{
    private RepositorioNotificacion repositorioNotificacion;
    private RepositorioMeta repositorioMeta;
    private RepositorioCategoria repositorioCategoria;
    private RepositorioUsuario repositorioUsuario;
    private RepositorioMetaVencida repositorioMetaVencida;
    private ServicioMovimiento servicioMovimiento;

    @Autowired
    public ServicioMetaImpl(RepositorioMeta repositorioMeta, RepositorioCategoria repositorioCategoria, RepositorioUsuario repositorioUsuario, RepositorioMetaVencida repositorioMetaVencida, ServicioMovimiento servicioMovimiento, RepositorioNotificacion repositorioNotificacion) {
        this.repositorioMeta = repositorioMeta;
        this.repositorioCategoria = repositorioCategoria;
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioMetaVencida = repositorioMetaVencida;
        this.servicioMovimiento = servicioMovimiento;
        this.repositorioNotificacion = repositorioNotificacion;
    }

    @Override
    public void eliminarMetasVencidasParaTodosLosUsuarios() throws ExcepcionBaseDeDatos, UsuarioInexistente, ExcepcionMetaNoExistente {
        List<Usuario> usuarios = repositorioUsuario.obtenerTodosLosUsuarios();
        if(usuarios.isEmpty())
            throw new UsuarioInexistente();
        for (Usuario usuario : usuarios) {
            eliminarMetasVencidas(usuario.getId());
        }
    }

    @Override
    public List<Notificacion> obtenerNotificacionMetasVencidas(Long idUsuario) throws ExcepcionBaseDeDatos {
        return repositorioMetaVencida.obtenerNotificacionMetasVencidas(idUsuario);
    }

    public void eliminarMetasVencidas(Long idUsuario) throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente {
        List<Meta> metas = repositorioMeta.obtenerMetas(idUsuario);
        LocalDate hoy = LocalDate.now();
        for (Meta meta : metas) {
            Date fechaFin = meta.getFechaFin();
            LocalDate fechaFinParse = fechaFin.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (fechaFinParse.isBefore(hoy)) {
                Double totalGastado = servicioMovimiento.obtenerTotalGastado(idUsuario, meta.getCategoriaMovimiento().getId(), meta.getFechaInicio(), meta.getFechaFin());
                repositorioMetaVencida.guardarMetaVencida(meta, totalGastado);
                eliminarMeta(meta.getId());

                MetaVencida metaVencida = this.obtenerMetaVencida();

                this.crearNotificacion(metaVencida);

            }
        }
    }

    private MetaVencida obtenerMetaVencida() {
        return repositorioMetaVencida.obtenerMetaVencida();
    }

    private void crearNotificacion(MetaVencida metaVencida) throws ExcepcionBaseDeDatos {
        Notificacion notificacion = new Notificacion();
        notificacion.setDescripcion("La meta de la categoría " + metaVencida.getCategoriaMovimiento().getNombre() + " ha sido vencida" + " con un total gastado de " + metaVencida.getTotalGastado() + " y un monto de " + metaVencida.getMontoMeta() + " en el periodo de " + metaVencida.getFechaInicio() + " a " + metaVencida.getFechaFin());
        notificacion.setFecha(new Date());
        notificacion.setUsuario(metaVencida.getUsuario());
        repositorioNotificacion.guardar(notificacion);
    }

    @Transactional
    @Override
    public void guardarMeta(Long idUsuario, DatosMeta datosMeta) throws ExcepcionCamposInvalidos, ExcepcionBaseDeDatos, ExcepcionCategoriaConMetaExistente, UsuarioInexistente {
        datosMeta.validarCampos();
        Usuario usuario = repositorioUsuario.obtenerUsuarioPorId(idUsuario);
        CategoriaMovimiento categoriaMovimiento = repositorioCategoria.obtenerCategoriaPorNombre(datosMeta.getCategoria());
        repositorioMeta.existeMetaConUsuarioYCategoria(usuario, categoriaMovimiento);
        Meta meta = new Meta(usuario, categoriaMovimiento, datosMeta.getMonto(), datosMeta.getFechaInicio(), datosMeta.getFechaFin());
        repositorioMeta.guardar(meta);
    }

    @Transactional
    @Override
    public Meta obtenerMetaPorId(Long idMeta) throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente {
        return repositorioMeta.obtenerMetaPorId(idMeta);
    }

    @Transactional
    @Override
    public void actualizarMeta(DatosEditarMeta datosEditarMeta) throws ExcepcionCamposInvalidos, ExcepcionBaseDeDatos, ExcepcionMetaNoExistente {

        Double monto = datosEditarMeta.getMontoMeta();

        CategoriaMovimiento categoria = repositorioCategoria.obtenerCategoriaPorNombre(datosEditarMeta.getCategoria());

        datosEditarMeta.validarCampos();

        if (categoria == null) {
            Map<String, String> errores = new HashMap<>();
            errores.put("categoria", "La categoría no existe");
            throw new ExcepcionCamposInvalidos(errores);
        }

        Meta meta = repositorioMeta.obtenerMetaPorId(datosEditarMeta.getId());
        if (meta == null)
            throw new ExcepcionMetaNoExistente();

        meta.setCategoriaMovimiento(categoria);
        meta.setMontoMeta(monto);
        repositorioMeta.actualizarMeta(meta);
    }

    @Transactional
    @Override
    public List<Meta> obtenerMetas(Long idUsuario) throws ExcepcionBaseDeDatos {
        return repositorioMeta.obtenerMetas(idUsuario);
    }

    @Override
    public List<Notificacion> obtenerNotificacionMetasConcretadas(Long idUsuario) {
        return repositorioNotificacion.obtenerNotificacionMetasConcretadas(idUsuario);
    }

    @Transactional
    @Override
    public void eliminarMeta(Long idMeta) throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente {
        Meta meta = repositorioMeta.obtenerMetaPorId(idMeta);
        if (meta == null)
            throw new ExcepcionMetaNoExistente();
        repositorioMeta.eliminarMeta(meta);
    }


}
