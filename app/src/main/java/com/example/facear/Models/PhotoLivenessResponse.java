package com.example.facear.Models;

public class PhotoLivenessResponse {
    private String job_id;
    private String status;
    private String statusCode;
    private String score;
    private String duration;
    private String threshold;

    public PhotoLivenessResponse(String job_id, String status, String statusCode, String score,String duration, String threshold) {
        this.job_id = job_id;
        this.status = status;
        this.statusCode = statusCode;
        this.score = score;
        this.duration = duration;
        this.threshold = threshold;
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
    public String getScore() {
        return score;
    }
    public String getDuration() {return duration;}
    public String getThreshold() {return threshold;}
}