package com.tallerwebi.dominio;

import com.tallerwebi.dominio.movimiento.Movimiento;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;
    private String email;
    private String password;
    private String rol;
    private Boolean activo = false;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "usuarios_movimientos",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_movimiento")
    )
    private Set<Movimiento> movimientos;

    public Usuario(String email, String password, String rol, Boolean activo) {
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.activo = activo;
        this.movimientos = new HashSet<>();
    }

    public Usuario(){}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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

    public String getRol() {
        return rol;
    }
    public void setRol(String rol) {
        this.rol = rol;
    }

    public Boolean getActivo() {
        return activo;
    }
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public boolean activo() {
        return activo;
    }

    public void activar() {
        activo = true;
    }

    public Set<Movimiento> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(Set<Movimiento> movimientos) {
        this.movimientos = movimientos;
    }
}
