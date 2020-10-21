package com.example.facear.Models;

public class CheckPhoneNumResponse {
    private String job_id;
    private String statusCode;
    private String message;
    private String otp;
    private String verified_time;
    private String status;

    public CheckPhoneNumResponse(String job_id, String statusCode, String message, String otp, String verified_time, String status) {
        this.job_id = job_id;
        this.statusCode = statusCode;
        this.message = message;
        this.otp = otp;
        this.verified_time = verified_time;
        this.status = status;
    }

    public String getJob_id() {
        return job_id;
    }
    public String getStatusCode() {
        return statusCode;
    }
    public String getMessage() {return message;}
    public String getOtp() { return otp; }
    public String getSend_time() {
        return verified_time;
    }
    public String getStatus() {
        return status;
    }
}
