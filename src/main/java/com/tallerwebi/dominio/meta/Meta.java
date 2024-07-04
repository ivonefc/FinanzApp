package com.tallerwebi.dominio.meta;

import com.tallerwebi.dominio.categoria.CategoriaMovimiento;
import com.tallerwebi.dominio.usuario.Usuario;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaInicio;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaFin;
    public void setId(Long id) {
        this.id = id;
    }

    public Meta(Long id, Usuario usuario, CategoriaMovimiento categoriaMovimiento, Double montoMeta) {
        this.id = id;
        this.usuario = usuario;
        this.categoriaMovimiento = categoriaMovimiento;
        this.montoMeta = montoMeta;
    }

    public Meta(Usuario usuario, CategoriaMovimiento categoriaMovimiento, Double montoMeta, Date fechaInicio, Date fechaFin) {
        this.usuario = usuario;
        this.categoriaMovimiento = categoriaMovimiento;
        this.montoMeta = montoMeta;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
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

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }
}
