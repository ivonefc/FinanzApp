package com.tallerwebi.dominio.email;

import com.tallerwebi.dominio.excepcion.ErrorEmail;

public interface ServicioEmail {
     Response enviarEmailPremium(String from, String subject, String to, String contentType, String message) throws ErrorEmail;
}
