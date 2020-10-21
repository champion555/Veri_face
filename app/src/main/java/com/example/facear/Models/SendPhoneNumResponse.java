package com.example.facear.Models;

public class SendPhoneNumResponse {
    private String job_id;
    private String statusCode;
    private String message;
    private String phone;
    private String send_time;
    private String status;

    public SendPhoneNumResponse(String job_id, String statusCode, String message, String phone, String send_time, String status) {
        this.job_id = job_id;
        this.statusCode = statusCode;
        this.message = message;
        this.phone = phone;
        this.send_time = send_time;
        this.status = status;
    }

    public String getJob_id() {
        return job_id;
    }
    public String getStatusCode() {
        return statusCode;
    }
    public String getMessage() {return message;}
    public String getPhone() {
        return phone;
    }
    public String getSend_time() {
        return send_time;
    }
    public String getStatus() {
        return status;
    }
}
