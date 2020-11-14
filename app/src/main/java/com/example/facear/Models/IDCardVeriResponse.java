package com.example.facear.Models;

public class IDCardVeriResponse {
    private String job_id;
    private String status;
    private String statusCode;
    private String duration;
    private String mrz_type;
    private String valid_score;
    private String type;
    private String country;
    private String number;
    private String date_of_birth;
    private String expiration_date;
    private String nationality;
    private String gender;
    private String first_name;
    private String last_name;
    private String valid_number;
    private String valid_date_of_birth;
    private String valid_expiration_date;

    public IDCardVeriResponse(String job_id, String status, String statusCode, String duration, String mrz_type, String valid_score, String type,
                              String country, String number, String date_of_birth, String expiration_date, String nationality,
                              String gender, String first_name, String last_name , String valid_number, String valid_date_of_birth, String valid_expiration_date) {
        this.job_id = job_id;
        this.status = status;
        this.statusCode = statusCode;
        this.duration = duration;
        this.mrz_type = mrz_type;
        this.valid_score = valid_score;
        this.type = type;
        this.country = country;
        this.number = number;
        this.date_of_birth = date_of_birth;
        this.expiration_date = expiration_date;
        this.nationality = nationality;
        this.gender = gender;
        this.first_name = first_name;
        this.last_name = last_name;
        this.valid_number = valid_number;
        this.valid_date_of_birth = valid_date_of_birth;
        this.valid_expiration_date = valid_expiration_date;
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
    public String getMrz_type() { return mrz_type; }
    public String getValid_score() {
        return valid_score;
    }
    public String getType() {
        return type;
    }
    public String getCountry() {return country;}
    public String getNumber() { return number; }
    public String getDate_of_birth() {
        return date_of_birth;
    }
    public String getExpiration_date() {
        return expiration_date;
    }
    public String getNationality() {return nationality;}
    public String getGender() { return gender; }
    public String getFirst_name() {
        return first_name;
    }
    public String getLast_name() {
        return last_name;
    }
    public String getValid_number() {return valid_number;}
    public String getValid_date_of_birth() { return valid_date_of_birth; }
    public String getValid_expiration_date() {
        return valid_expiration_date;
    }

}
