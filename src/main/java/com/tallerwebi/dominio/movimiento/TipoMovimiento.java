package com.tallerwebi.dominio.movimiento;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "tipos_movimiento")
public class TipoMovimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    @OneToMany(mappedBy = "tipo")
    private Set<CategoriaMovimiento> categorias;

    public TipoMovimiento(String nombre) {
        this.nombre = nombre;
    }

    public TipoMovimiento() {
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

    @Override
    public String toString() {
        return "TipoMovimiento{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", categorias=" + categorias +
                '}';
    }
}
