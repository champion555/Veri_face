package com.example.facear.Models;

public class ImageGlareArray {
    private String errorType;
    private String details;

    public ImageGlareArray(String errorType, String details) {
        this.errorType = errorType;
        this.details = details;
    }

    public String getErrorType() {
        return errorType;
    }
    public String getDetails() {
        return details;
    }
}
