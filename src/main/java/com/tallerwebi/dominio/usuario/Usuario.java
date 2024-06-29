package com.tallerwebi.dominio.usuario;

import com.tallerwebi.dominio.movimiento.Movimiento;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;
    private String nombreUsuario;
    private String email;
    private String password;
    private String rol;
    private Boolean activo = false;

    @OneToMany(mappedBy = "amigo")
    private Set<Movimiento> movimientos = new HashSet<>();

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;
    private String pais;
    private Long telefono;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "amigos",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "amigo_id")
    )
    private List<Usuario> amigos;


    public Usuario(String email, String password, String rol, Boolean activo) {
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.activo = activo;
        this.amigos = new ArrayList<>();
    }

    public Usuario(String nombre, String email, String password, String rol, Boolean activo) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.activo = activo;
        this.amigos = new ArrayList<>();
    }

    public Usuario(){
        this.amigos = new ArrayList<>();
    }

    public Usuario(String nombre, String apellido, String nombreUsuario, String email, String password, LocalDate fechaNacimiento, String pais, Long telefono, String rol, boolean activo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.activo = activo;
        this.pais = pais;
        this.telefono = telefono;
        this.fechaNacimiento = fechaNacimiento;
        this.amigos = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}

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

    public String getRol() {
        return rol;
    }
    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getApellido() {return apellido;}
    public void setApellido(String apellido) {this.apellido = apellido;}

    public String getNombreUsuario() {return nombreUsuario;}
    public void setNombreUsuario(String nombreUsuario) {this.nombreUsuario = nombreUsuario;}

    public String getPais() {return pais;}
    public void setPais(String pais) {this.pais = pais;}

    public Long getTelefono() {return telefono;}
    public void setTelefono(Long telefono) {this.telefono = telefono;}

    public LocalDate getFechaNacimiento() {return fechaNacimiento;}
    public void setFechaNacimiento(LocalDate fechaNacimiento) {this.fechaNacimiento = fechaNacimiento;}

    public Boolean getActivo() {
        return activo;
    }
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

/*
    public Set<Movimiento> getMovimientos() {
        return movimientos;
    }
    public void setMovimientos(Set<Movimiento> movimientos) {
        this.movimientos = movimientos;
    }

    public List<Usuario> getAmigos() {
        return amigos;
    }
    public void setAmigos(List<Usuario> amigos) {
        this.amigos = amigos;
    }*/

    public boolean existeAmigo(Usuario amigo) {
        if (amigo == null || amigos == null)
            return false;

        return amigos.contains(amigo);
    }

    public void activar() {
        activo = true;
    }

    public void agregarAmigo(Usuario amigo) {
        this.amigos.add(amigo);
    }

    public void eliminarAmigo(Usuario amigo) {
        amigos.remove(amigo);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", rol='" + rol + '\'' +
                ", activo=" + activo +
                '}';
    }
}
