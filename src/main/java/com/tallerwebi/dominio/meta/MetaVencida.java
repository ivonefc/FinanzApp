package com.tallerwebi.dominio.meta;

import com.tallerwebi.dominio.categoria.CategoriaMovimiento;
import com.tallerwebi.dominio.usuario.Usuario;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "metas_vencidas")
public class MetaVencida {
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
    private Date fechaInicio;
    private Date fechaFin;
    private Double totalGastado;

    public CategoriaMovimiento getCategoriaMovimiento() {
        return categoriaMovimiento;
    }

    public void setCategoriaMovimiento(CategoriaMovimiento categoriaMovimiento) {
        this.categoriaMovimiento = categoriaMovimiento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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

    public Double getTotalGastado() {
        return totalGastado;
    }

    public void setTotalGastado(Double totalGastado) {
        this.totalGastado = totalGastado;
    }

}