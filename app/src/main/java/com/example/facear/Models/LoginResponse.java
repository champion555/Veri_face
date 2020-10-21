package com.example.facear.Models;

public class LoginResponse {
    private String status;
    private String statusCode;
    private String message;
    private String login_token;
    private String user_demo_id;

    public LoginResponse(String status, String statusCode, String message,String login_token,String user_demo_id) {
        this.status = status;
        this.statusCode = statusCode;
        this.message = message;
        this.login_token = login_token;
        this.user_demo_id = user_demo_id;
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
    public String getLogin_token() {
        return login_token;
    }
    public String getUser_demo_id() {
        return user_demo_id;
    }
}
