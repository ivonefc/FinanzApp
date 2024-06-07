package com.tallerwebi.dominio.meta;

import com.tallerwebi.dominio.categoria.CategoriaMovimiento;
import com.tallerwebi.dominio.categoria.RepositorioCategoria;
import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionCamposInvalidos;
import com.tallerwebi.dominio.excepcion.ExcepcionCategoriaConMetaExistente;
import com.tallerwebi.dominio.excepcion.ExcepcionMetaNoExistente;
import com.tallerwebi.dominio.usuario.RepositorioUsuario;
import com.tallerwebi.dominio.usuario.Usuario;
import com.tallerwebi.presentacion.meta.DatosEditarMeta;
import com.tallerwebi.presentacion.meta.DatosMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class ServicioMetaImpl implements ServicioMeta{
    private RepositorioMeta repositorioMeta;
    private RepositorioCategoria repositorioCategoria;
    private RepositorioUsuario repositorioUsuario;

    @Autowired
    public ServicioMetaImpl(RepositorioMeta repositorioMeta, RepositorioCategoria repositorioCategoria, RepositorioUsuario repositorioUsuario) {
        this.repositorioMeta = repositorioMeta;
        this.repositorioCategoria = repositorioCategoria;
        this.repositorioUsuario = repositorioUsuario;
    }

    @Transactional
    @Override
    public void guardarMeta(Long idUsuario, DatosMeta datosMeta) throws ExcepcionCamposInvalidos, ExcepcionBaseDeDatos, ExcepcionCategoriaConMetaExistente {
        datosMeta.validarCampos();
        Usuario usuario = repositorioUsuario.obtenerUsuarioPorId(idUsuario);
        CategoriaMovimiento categoriaMovimiento = repositorioCategoria.obtenerCategoriaPorNombre(datosMeta.getCategoria());
        repositorioMeta.existeMetaConUsuarioYCategoria(usuario, categoriaMovimiento);
        Meta meta = new Meta(usuario, categoriaMovimiento, datosMeta.getMonto());
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

    @Transactional
    @Override
    public void eliminarMeta(Long idMeta) throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente {
        Meta meta = repositorioMeta.obtenerMetaPorId(idMeta);
        if (meta == null)
            throw new ExcepcionMetaNoExistente();
        repositorioMeta.eliminarMeta(meta);
    }


}
