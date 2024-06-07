package com.tallerwebi.dominio.tipo;

import com.itextpdf.text.BaseColor;
import javax.persistence.*;

@Entity
@Table(name = "tipos_movimiento")
public class TipoMovimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;

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

    public BaseColor obtenerBaseColor(){
        if(this.nombre.equals("INGRESO")){
            return BaseColor.GREEN;
        }
        return BaseColor.RED;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "TipoMovimiento{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
