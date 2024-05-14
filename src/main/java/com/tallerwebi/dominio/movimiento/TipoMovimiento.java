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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipo")
    private Set<CategoriaMovimiento> categorias;

    public TipoMovimiento(Long id, String nombre) {
        this.id = id;
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
}
