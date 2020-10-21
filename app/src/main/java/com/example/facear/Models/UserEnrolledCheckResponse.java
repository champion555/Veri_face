package com.example.facear.Models;

public class UserEnrolledCheckResponse {
    private String job_id;
    private String status;
    private String statusCode;
    private Boolean isEnrolled;
    private String duration;

    public UserEnrolledCheckResponse(String job_id, String status, String statusCode, Boolean isEnrolled, String duration) {
        this.job_id = job_id;
        this.status = status;
        this.statusCode = statusCode;
        this.isEnrolled = isEnrolled;
        this.duration = duration;
    }
    public String getJob_id() {
        return job_id;
    }
    public String getStatus() {
        return status;
    }
    public String getStatusCode() {
        return statusCode;
    }
    public Boolean getIsEnrolled() {
        return isEnrolled;
    }
    public String getDuration() {return duration;}
}
