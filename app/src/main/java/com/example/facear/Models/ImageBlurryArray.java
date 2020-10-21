package com.example.facear.Models;

public class ImageBlurryArray {
    private String errorType;
    private String details;

    public ImageBlurryArray(String errorType, String details) {
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
