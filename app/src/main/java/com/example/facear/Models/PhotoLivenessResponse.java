package com.example.facear.Models;

public class PhotoLivenessResponse {
    private String job_id;
    private String result;
    private String score;
    private String duration;

    public PhotoLivenessResponse(String job_id, String score,String duration, String result) {
        this.job_id = job_id;
        this.result = result;
        this.score = score;
        this.duration = duration;
    }
    public String getJob_id() {
        return job_id;
    }
    public String getScore() {
        return score;
    }
    public String getDuration() {return duration;}
    public String getResult() {return result;}
}