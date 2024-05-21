package com.tallerwebi.dominio.meta;

import com.tallerwebi.dominio.categoria.CategoriaMovimiento;
import com.tallerwebi.dominio.usuario.Usuario;

import javax.persistence.*;

@Entity
@Table(name = "metas")
public class Meta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private CategoriaMovimiento categoriaMovimiento;
    private Double montoMeta;
    public void setId(Long id) {
        this.id = id;
    }

    public Meta(Long id, Usuario usuario, CategoriaMovimiento categoriaMovimiento, Double montoMeta) {
        this.id = id;
        this.usuario = usuario;
        this.categoriaMovimiento = categoriaMovimiento;
        this.montoMeta = montoMeta;
    }

    public Meta() {
    }

    public Meta(Usuario usuario, CategoriaMovimiento categoriaMovimiento, Double montoMeta) {
        this.usuario = usuario;
        this.categoriaMovimiento = categoriaMovimiento;
        this.montoMeta = montoMeta;
    }

    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public CategoriaMovimiento getCategoriaMovimiento() {
        return categoriaMovimiento;
    }

    public void setCategoriaMovimiento(CategoriaMovimiento categoriaMovimiento) {
        this.categoriaMovimiento = categoriaMovimiento;
    }

    public Double getMontoMeta() {
        return montoMeta;
    }

    public void setMontoMeta(Double montoMeta) {
        this.montoMeta = montoMeta;
    }
}
