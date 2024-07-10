package com.tallerwebi.presentacion.notificaciones;

public class DatosNotificacion {

    private Long id;
    private String json;
    private Long idUsuario;
    private Long idUsuarioSolicitante;

    public DatosNotificacion() {
    }

    public DatosNotificacion(Long id, String json, Long idUsuario, Long idUsuarioSolicitante) {
        this.id = id;
        this.json = json;
        this.idUsuario = idUsuario;
        this.idUsuarioSolicitante = idUsuarioSolicitante;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdUsuarioSolicitante() {
        return idUsuarioSolicitante;
    }

    public void setIdUsuarioSolicitante(Long idUsuarioSolicitante) {
        this.idUsuarioSolicitante = idUsuarioSolicitante;
    }
}
