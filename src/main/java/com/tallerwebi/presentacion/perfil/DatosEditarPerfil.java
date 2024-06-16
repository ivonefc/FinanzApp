package com.tallerwebi.presentacion.perfil;

import com.tallerwebi.dominio.excepcion.ExcepcionCamposInvalidos;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class DatosEditarPerfil {
    private Long id;
    private String nombre;
    private String apellido;
    private String nombreUsuario;
    private String email;
    private LocalDate fechaNacimiento;
    private String pais;
    private Long telefono;

    public DatosEditarPerfil() {
    }

    public DatosEditarPerfil(Long id, String nombre, String apellido, String nombreUsuario, String email, LocalDate fechaNacimiento, String pais, Long telefono) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.fechaNacimiento = fechaNacimiento;
        this.pais = pais;
        this.telefono = telefono;
    }

    public void validarCampos() throws ExcepcionCamposInvalidos {
        Map<String, String> errores = new HashMap<>();

        if(this.nombre == null || this.nombre.isEmpty())
            errores.put("nombre", "El campo es requerido");

        if(this.apellido == null || this.apellido.isEmpty())
            errores.put("apellido", "El campo es requerido");

        if(this.nombreUsuario == null || this.nombreUsuario.isEmpty())
            errores.put("nombreUsuario", "El campo es requerido");

        if(this.email == null || this.email.isEmpty())
            errores.put("email", "El campo es requerido");

        if(this.fechaNacimiento == null)
            errores.put("fechaNacimiento", "El campo es requerido");

        if(this.pais == null || this.pais.isEmpty())
            errores.put("pais", "El campo es requerido");

        if(this.telefono == null)
            errores.put("telefono", "El campo es requerido");

        if(!errores.isEmpty())
            throw new ExcepcionCamposInvalidos(errores);
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
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

    @Override
    public String toString() {
        return "DatosEditarPerfil{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", email='" + email + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                ", pais='" + pais + '\'' +
                ", telefono=" + telefono +
                '}';
    }

}
