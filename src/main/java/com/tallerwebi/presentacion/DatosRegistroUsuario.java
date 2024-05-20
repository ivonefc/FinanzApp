package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.excepcion.ExcepcionCamposInvalidos;

import java.util.HashMap;
import java.util.Map;

public class DatosRegistroUsuario {
    private String nombre;
    private String email;
    private String password;

    public DatosRegistroUsuario() {}

    public DatosRegistroUsuario(String nombre, String email, String password) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
    }

    public boolean tieneEmail(){
        return this.email != null && !this.email.isEmpty();
    }

    public boolean tieneEmailValido(){
        if(!this.email.contains("@")){
            return false;
        }
        return true;
    }

    public boolean tieneNombreValido(){
        if(this.nombre == null || this.nombre.isEmpty()){
            return false;
        }
        return true;
    }

    public boolean tienePasswordValido(){
        if(this.password == null || this.password.isEmpty()){
            return false;
        }
        return true;
    }

    public void validarCampos() throws ExcepcionCamposInvalidos {
        Map<String, String> errores = new HashMap<>();
        if(!this.tieneEmail()){
            errores.put("email", "El campo es requerido");
        }
        if(this.tieneEmail() && !this.tieneEmailValido()){
            errores.put("email", "El email es invalido");
        }
        if(!this.tieneNombreValido()){
            errores.put("nombre", "El campo es requerido");
        }
        if(!this.tienePasswordValido()){
            errores.put("password", "El campo es requerido");
        }
        if(!errores.isEmpty()){
            throw new ExcepcionCamposInvalidos(errores);
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
