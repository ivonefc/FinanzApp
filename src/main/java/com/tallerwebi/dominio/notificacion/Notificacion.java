package com.tallerwebi.dominio.notificacion;

import com.tallerwebi.dominio.usuario.Usuario;
import com.tallerwebi.presentacion.movimiento.DatosAgregarMovimiento;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Table(name = "notificaciones")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;

    private String estado;

    private String tipo;

    private Date fecha;

    @Column(columnDefinition = "TEXT")
    private String datosAgregarMovimientoJson;

    @Transient
    private DatosAgregarMovimiento datosAgregarMovimiento;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_usuario_solicitante")
    private Usuario usuarioSolicitante;

    public Notificacion(Long id,Date fecha, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    public Notificacion(Long id, String descripcion,Date fecha, String estado) {
        this.id = id;
        this.descripcion = descripcion;
        this.estado = estado;
        this.fecha = fecha;
    }

    public Notificacion(Long id, String descripcion, String estado, String tipo,Date fecha, Usuario usuario, Usuario usuarioSolicitante) {
        this.id = id;
        this.descripcion = descripcion;
        this.estado = estado;
        this.tipo = tipo;
        this.usuario = usuario;
        this.usuarioSolicitante = usuarioSolicitante;
        this.fecha = fecha;
    }

    public Notificacion(Long id, String descripcion, Date fecha, String estado, Usuario usuario, Usuario usuarioSolicitante, String datosAgregarMovimiento) {
        this.id = id;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.estado = estado;
        this.datosAgregarMovimientoJson = datosAgregarMovimiento;
        this.usuario = usuario;
        this.usuarioSolicitante = usuarioSolicitante;
    }

    public Notificacion() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Usuario getUsuarioSolicitante() {
        return usuarioSolicitante;
    }

    public void setUsuarioSolicitante(Usuario usuarioSolicitante) {
        this.usuarioSolicitante = usuarioSolicitante;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getDatosAgregarMovimiento() {
        return datosAgregarMovimientoJson;
    }

    public void setDatosAgregarMovimiento(String datosAgregarMovimiento) {
        this.datosAgregarMovimientoJson = datosAgregarMovimiento;
    }

    public DatosAgregarMovimiento getDatosAgregarMovimientoObject() {
        return datosAgregarMovimiento;
    }

    public void setDatosAgregarMovimientoObject(DatosAgregarMovimiento datosAgregarMovimiento) {
        this.datosAgregarMovimiento = datosAgregarMovimiento;
    }
}
