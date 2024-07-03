package com.tallerwebi.dominio.email;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.tallerwebi.dominio.excepcion.ErrorEmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ServicioEmailSendGrid implements ServicioEmail{

    @Value("${sendgrid.apikey}")
    private String APIKey;

    @Override
    public Response enviarEmail(String from, String subject, String to, String contentType, String message, String responseMessage) throws ErrorEmail {
        Email fromEmail = new Email(from);
        Email toEmail = new Email(to);
        Content content = new Content(contentType, message);
        SendGrid sendGrid = new SendGrid(APIKey);
        Request request = new Request();
        Mail mail = new Mail(fromEmail, subject, toEmail, content);
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sendGrid.api(request);
            return new Response(responseMessage);
        } catch (IOException e) {
            throw new ErrorEmail();
        }

    }
}
