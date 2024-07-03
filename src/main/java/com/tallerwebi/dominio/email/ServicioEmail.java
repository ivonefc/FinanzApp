package com.tallerwebi.dominio.email;

import com.tallerwebi.dominio.excepcion.ErrorEmail;

public interface ServicioEmail {
     Response enviarEmail(String from, String subject, String to, String contentType, String message, String responseMessage) throws ErrorEmail;
}
