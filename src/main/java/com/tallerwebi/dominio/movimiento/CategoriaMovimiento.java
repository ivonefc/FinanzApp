package com.tallerwebi.dominio.movimiento;


import javax.persistence.*;


@Entity
@Table(name = "categorias_movimiento")
public class CategoriaMovimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_tipo")
    private TipoMovimiento tipo;


    public CategoriaMovimiento(String nombre) {
        this.nombre = nombre;
    }

    public CategoriaMovimiento() {
    }

    public CategoriaMovimiento(String nombre, TipoMovimiento tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public TipoMovimiento getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimiento tipo) {
        this.tipo = tipo;
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
        return "CategoriaMovimiento{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", tipo=" + tipo +
                '}';
    }
}
