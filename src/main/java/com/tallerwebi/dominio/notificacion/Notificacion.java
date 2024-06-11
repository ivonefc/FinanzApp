package com.tallerwebi.dominio.notificacion;

import com.tallerwebi.dominio.usuario.Usuario;

import javax.persistence.*;

@Entity
@Table(name = "notificaciones")
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;

    @ManyToOne(cascade = CascadeType.ALL )
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    public Notificacion(Long id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public Notificacion() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
