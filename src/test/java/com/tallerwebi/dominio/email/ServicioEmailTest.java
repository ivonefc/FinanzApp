package com.tallerwebi.dominio.email;

import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.tallerwebi.dominio.excepcion.ErrorEmail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServicioEmailTest {

    SendGrid sendGrid;
    Request request;
    ServicioEmail servicioEmail;

    @BeforeEach
    public void init() {
        sendGrid = mock(SendGrid.class);
        request = mock(Request.class);
        servicioEmail = new ServicioEmailSendGrid();
        ReflectionTestUtils.setField(servicioEmail, "APIKey", "falso_api_key");
    }
    @Test
    public void queAlSolicitarEnviarUnMailAlServicioDevuelvaUnaResponse() throws IOException, ErrorEmail {
        //preparacion
        String from = "from@ejemplo.com";
        String subject = "Test Subject";
        String to = "to@ejemplo.com";
        String contentType = "text/plain";
        String message = "Mensaje";
        String responseMessage = "Email enviado";

        when(sendGrid.api(request)).thenReturn(new com.sendgrid.Response(202, "Accepted", null));

        //ejecucion
        Response response = servicioEmail.enviarEmail(from, subject, to, contentType, message,responseMessage);

        //
        assertThat(response.getResponseBody(), equalTo(responseMessage));
    }

    /*
    @Test
    void queAlSolicitarEnviarUnMailLanceUnErrorEmail() throws IOException {
        // Preparación
        String from = "from@ejemplo.com";
        String subject = "Asunto";
        String to = "to@ejemplo.com";
        String contentType = "text/plain";
        String message = "Mensaje";
        String responseMessage = "Email enviado";

        doThrow(IOException.class).when(sendGrid).api(request);

        // Validación y ejecución
        assertThrows(ErrorEmail.class, () -> {
            servicioEmail.enviarEmail(from, subject, to, contentType, message, responseMessage);
        });
    }

     */

}
