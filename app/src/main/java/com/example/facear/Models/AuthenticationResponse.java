package com.example.facear.Models;

public class AuthenticationResponse {

    private String status;
    private String statusCode;
    private String api_access_token;
    private String token_expiration;

    public AuthenticationResponse(String status, String statusCode, String api_access_token, String token_expiration) {
        this.status = status;
        this.statusCode = statusCode;
        this.api_access_token = api_access_token;
        this.token_expiration = token_expiration;
    }

    public String getStatus() {
        return status;
    }
    public String getStatusCode() {
        return statusCode;
    }
    public String getApi_access_token() {
        return api_access_token;
    }
    public String getToken_expiration() {
        return token_expiration;
    }
}
