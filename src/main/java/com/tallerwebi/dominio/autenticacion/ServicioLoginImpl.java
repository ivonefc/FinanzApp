package com.tallerwebi.dominio.autenticacion;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionCamposInvalidos;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;
import com.tallerwebi.dominio.usuario.RepositorioUsuario;
import com.tallerwebi.dominio.usuario.Usuario;
import com.tallerwebi.presentacion.autenticacion.DatosRegistroUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service("servicioLogin")
@Transactional
public class ServicioLoginImpl implements ServicioLogin {

    private RepositorioUsuario repositorioUsuario;

    @Autowired
    public ServicioLoginImpl(RepositorioUsuario repositorioUsuario){
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public Usuario consultarUsuario (String email, String password) throws UsuarioInexistente, ExcepcionBaseDeDatos {
        return repositorioUsuario.buscarUsuarioPorEmailYPassword(email, password);
    }

    @Override
    public void registrar(DatosRegistroUsuario datosRegistroUsuario) throws UsuarioExistente, ExcepcionBaseDeDatos, ExcepcionCamposInvalidos {
        datosRegistroUsuario.validarCampos();
        Usuario usuario = new Usuario(
                datosRegistroUsuario.getNombre(),
                datosRegistroUsuario.getEmail(),
                datosRegistroUsuario.getPassword(),
                "USER",
                true
        );
        try {
            repositorioUsuario.buscarUsuarioPorEmail(datosRegistroUsuario.getEmail());
            throw new UsuarioExistente();
        } catch (UsuarioInexistente e) {
            repositorioUsuario.guardar(usuario);
        }
    }

}

