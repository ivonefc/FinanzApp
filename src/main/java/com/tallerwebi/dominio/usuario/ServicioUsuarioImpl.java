package com.tallerwebi.dominio.usuario;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Usuario buscarUsuarioPorEmailYPassword(String email, String password) throws UsuarioInexistente, ExcepcionBaseDeDatos {
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
    public void modificar(Usuario usuario) {
        repositorioUsuario.modificar(usuario);
    } //modificar test

    @Transactional
    @Override
    public Usuario obtenerUsuarioPorId(Long id) throws ExcepcionBaseDeDatos, UsuarioInexistente {
        return repositorioUsuario.obtenerUsuarioPorId(id);
    }
}
