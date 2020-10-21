package com.example.facear.Models;

public class UserEnrollmentResponse {
    private String job_id;
    private String status;
    private String statusCode;
    private String liveness_status;
    private String liveness_score;
    private String liveness_threshold;
    private String duration;
    private String enrollment_id;

    public UserEnrollmentResponse(String job_id, String status, String statusCode, String liveness_status, String liveness_score, String liveness_threshold, String duration, String enrollment_id) {
        this.job_id = job_id;
        this.status = status;
        this.statusCode = statusCode;
        this.liveness_status = liveness_status;
        this.liveness_score = liveness_score;
        this.liveness_threshold = liveness_threshold;
        this.duration = duration;
        this.enrollment_id = enrollment_id;
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
    public String getLiveness_status() {return liveness_status;}
    public String getLiveness_score() {
        return liveness_score;
    }
    public String getLiveness_threshold() {return liveness_threshold;}
    public String getDuration() {return duration;}
    public String getEnrollment_id() {return enrollment_id;}



}
