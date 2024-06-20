package com.tallerwebi.presentacion.autenticacion;

import com.tallerwebi.dominio.excepcion.ExcepcionCamposInvalidos;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class DatosRegistroUsuario {
    private String nombre;
    private String email;
    private String password;
    private String apellido;
    private String nombreUsuario;
    private LocalDate fechaNacimiento;
    private String pais;
    private Long telefono;

    public DatosRegistroUsuario() {}

    public DatosRegistroUsuario(String nombre, String email, String password) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
    }

    public DatosRegistroUsuario(String nombre, String email, String password, String apellido, String nombreUsuario, LocalDate fechaNacimiento, String pais, Long telefono) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.apellido = apellido;
        this.nombreUsuario = nombreUsuario;
        this.fechaNacimiento = fechaNacimiento;
        this.pais = pais;
        this.telefono = telefono;
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

    public boolean tieneApellidoValido(){
        if(this.apellido == null || this.apellido.isEmpty()){
            return false;
        }
        return true;
    }

    public boolean tieneNombreUsuarioValido(){
        if(this.nombreUsuario == null || this.nombreUsuario.isEmpty()){
            return false;
        }
        return true;
    }

    public boolean tieneFechaNacimientoValida(){
        if(this.fechaNacimiento == null){
            return false;
        }
        return true;
    }

    public boolean tienePaisValido(){
        if(this.pais == null || this.pais.isEmpty()){
            return false;
        }
        return true;
    }

    public boolean tieneTelefonoValido(){
        if(this.telefono == null){
            return false;
        }
        return true;
    }

    public void validarCampos() throws ExcepcionCamposInvalidos {
        Map<String, String> errores = new HashMap<>();
        if(!this.tieneEmail())
            errores.put("email", "El campo es requerido");

        if(this.tieneEmail() && !this.tieneEmailValido())
            errores.put("email", "El email es invalido");

        if(!this.tieneNombreValido())
            errores.put("nombre", "El campo es requerido");

        if(!this.tienePasswordValido())
            errores.put("password", "El campo es requerido");

        if(!this.tieneApellidoValido())
            errores.put("apellido", "El campo es requerido");

        if(!this.tieneNombreUsuarioValido())
            errores.put("nombreUsuario", "El campo es requerido");

        if(!this.tieneFechaNacimientoValida())
            errores.put("fechaNacimiento", "El campo es requerido");

        if(!this.tienePaisValido())
            errores.put("pais", "El campo es requerido");

        if(!this.tieneTelefonoValido())
            errores.put("telefono", "El campo es requerido");

        if(!errores.isEmpty())
            throw new ExcepcionCamposInvalidos(errores);
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

    public String getApellido() {
        return apellido;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getPais() {
        return pais;
    }
    public void setPais(String pais) {
        this.pais = pais;
    }

    public Long getTelefono() {
        return telefono;
    }
    public void setTelefono(Long telefono) {
        this.telefono = telefono;
    }

}