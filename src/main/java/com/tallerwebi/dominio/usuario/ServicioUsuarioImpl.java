package com.tallerwebi.dominio.usuario;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionCamposInvalidos;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;
import com.tallerwebi.presentacion.perfil.DatosEditarPerfil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service("servicioUsuario")
@Transactional
public class ServicioUsuarioImpl implements ServicioUsuario{

    private RepositorioUsuario repositorioUsuario;

    @Autowired
    public ServicioUsuarioImpl(RepositorioUsuario repositorioUsuario){
        this.repositorioUsuario = repositorioUsuario;
    }

    @Transactional
    @Override
    public Usuario buscarUsuarioPorEmailYPassword(String email, String password) throws UsuarioInexistente, ExcepcionBaseDeDatos, ExcepcionCamposInvalidos {
        return repositorioUsuario.buscarUsuarioPorEmailYPassword(email, password);
    }

    @Transactional
    @Override
    public void guardar(Usuario usuario) throws ExcepcionBaseDeDatos, UsuarioInexistente, UsuarioExistente {
        repositorioUsuario.guardar(usuario);
    }

    @Transactional
    @Override
    public Usuario buscarUsuarioPorEmail(String email) throws UsuarioInexistente, ExcepcionBaseDeDatos {
        return repositorioUsuario.buscarUsuarioPorEmail(email);
    }

    @Transactional
    @Override
    public void modificar(DatosEditarPerfil datosEditarPerfil) throws ExcepcionBaseDeDatos, UsuarioInexistente, ExcepcionCamposInvalidos {
        datosEditarPerfil.validarCampos();
        String nombre = datosEditarPerfil.getNombre();
        String apellido = datosEditarPerfil.getApellido();
        String nombreUsuario = datosEditarPerfil.getNombreUsuario();
        String email = datosEditarPerfil.getEmail();
        String pais = datosEditarPerfil.getPais();
        Long telefono = datosEditarPerfil.getTelefono();
        LocalDate fechaNacimiento = datosEditarPerfil.getFechaNacimiento();
        Long id = datosEditarPerfil.getId();

        Usuario usuario = repositorioUsuario.obtenerUsuarioPorId(id);
        if (usuario == null)
            throw new UsuarioInexistente();

        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setNombreUsuario(nombreUsuario);
        usuario.setEmail(email);
        usuario.setPais(pais);
        usuario.setTelefono(telefono);
        usuario.setFechaNacimiento(fechaNacimiento);
        repositorioUsuario.modificar(usuario);
    }

    @Transactional
    @Override
    public Usuario obtenerUsuarioPorId(Long id) throws ExcepcionBaseDeDatos, UsuarioInexistente {
        return repositorioUsuario.obtenerUsuarioPorId(id);
    }

    @Override
    public List<Usuario> obtenerAmigosDeUnUsuario(Long idUsuario) throws ExcepcionBaseDeDatos, UsuarioInexistente {
        if (idUsuario == null || idUsuario == 0)
            throw new UsuarioInexistente();

        return repositorioUsuario.obtenerAmigosDeUnUsuario(idUsuario);
    }
}
