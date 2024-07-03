package com.tallerwebi.dominio.email;

public class Response {
    String responseBody;
    public Response(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getResponseBody() {
        return responseBody;
    }
}
