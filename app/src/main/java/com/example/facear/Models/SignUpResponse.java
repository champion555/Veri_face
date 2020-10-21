package com.example.facear.Models;

public class SignUpResponse {
    private String status;
    private String statusCode;
    private String message;

    public SignUpResponse(String status, String statusCode, String message) {
        this.status = status;
        this.statusCode = statusCode;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }
    public String getStatusCode() {
        return statusCode;
    }
    public String getMessage() {
        return message;
    }
}
