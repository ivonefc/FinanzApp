package com.tallerwebi.dominio.movimiento;


import com.tallerwebi.dominio.Usuario;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Movimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimiento")
    private Long id;

    private String descripcion;
    @Enumerated(value = EnumType.STRING)
    private TipoDeMovimiento tipo;
    @Enumerated(value = EnumType.STRING)
    private CategoriaMovimiento categoria;
    private Double monto;
    private LocalDateTime fechayHora;

    @ManyToMany(mappedBy = "movimientos")
    private Set<Usuario> usuarios;

    public Movimiento(String descripcion, TipoDeMovimiento tipo, CategoriaMovimiento categoria, Double monto, LocalDateTime fechayHora) {
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.categoria = categoria;
        this.monto = monto;
        this.fechayHora = fechayHora;
        this.usuarios = new HashSet<>();
    }

    public Movimiento(){}

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoDeMovimiento getTipo() {
        return tipo;
    }

    public void setTipo(TipoDeMovimiento tipo) {
        this.tipo = tipo;
    }

    public CategoriaMovimiento getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaMovimiento categoria) {
        this.categoria = categoria;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public LocalDateTime getFechayHora() {
        return fechayHora;
    }

    public void setFechayHora(LocalDateTime fechayHora) {
        this.fechayHora = fechayHora;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
