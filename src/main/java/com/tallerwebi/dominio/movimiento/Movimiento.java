package com.tallerwebi.dominio.movimiento;


import com.tallerwebi.dominio.Usuario;
import javax.persistence.*;
import java.time.LocalDate;


@Entity
@Table(name = "movimientos")
public class Movimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;

    private Double monto;

    private LocalDate fechayHora;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_categoria")
    private CategoriaMovimiento categoria;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    public Movimiento(CategoriaMovimiento categoria, String descripcion, Double monto) {
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.monto = monto;
    }

    public Movimiento(String descripcion, Double monto, LocalDate fechayHora) {
        this.descripcion = descripcion;
        this.monto = monto;
        this.fechayHora = fechayHora;
    }

    public Movimiento(String descripcion, Double monto, LocalDate fechayHora, CategoriaMovimiento categoria, Usuario usuario) {
        this.descripcion = descripcion;
        this.monto = monto;
        this.fechayHora = fechayHora;
        this.categoria = categoria;
        this.usuario = usuario;
    }

    public Movimiento(){}

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public LocalDate getFechayHora() {
        return fechayHora;
    }

    public void setFechayHora(LocalDate fechayHora) {
        this.fechayHora = fechayHora;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public CategoriaMovimiento getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaMovimiento categoria) {
        this.categoria = categoria;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Movimiento{" +
                "id=" + id +
                ", descripcion='" + descripcion + '\'' +
                ", monto=" + monto +
                ", fechayHora=" + fechayHora +
                ", categoria=" + categoria +
                ", usuario=" + usuario +
                '}';
    }
}
