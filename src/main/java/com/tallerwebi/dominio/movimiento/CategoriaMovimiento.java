package com.tallerwebi.dominio.movimiento;

import jdk.jfr.Name;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "categorias_movimiento")
public class CategoriaMovimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @ManyToOne
    @JoinColumn(name = "id_tipo")
    private TipoMovimiento tipo;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "categoria")
    private Set<Movimiento> movimientos;

    public CategoriaMovimiento(Long id, String nombre, TipoMovimiento tipo) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public CategoriaMovimiento() {
    }

    public TipoMovimiento getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimiento tipo) {
        this.tipo = tipo;
    }

    public Set<Movimiento> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(Set<Movimiento> movimientos) {
        this.movimientos = movimientos;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
