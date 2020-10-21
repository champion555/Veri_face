package com.example.facear.Models;

public class FaceMatchIDResponse {
    private String job_id;
    private String status;
    private String statusCode;
    private String duration;
    private String match_score;

    public FaceMatchIDResponse(String job_id, String status, String statusCode,String duration, String match_score) {
        this.job_id = job_id;
        this.status = status;
        this.statusCode = statusCode;
        this.duration = duration;
        this.match_score = match_score;
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
    public String getDuration() {return duration;}
    public String getMatch_score() {return match_score;}
}
