package com.example.facear.Models;

import java.lang.reflect.Array;
import java.util.List;

public class ImageCheckResponse {
    private String status;
    private String statusCode;
    private List<ImageBlurryArray> errorList;
    private String message;

    public ImageCheckResponse(String status, String statusCode, List<ImageBlurryArray> errorList,String message) {
        this.status = status;
        this.statusCode = statusCode;
        this.errorList = errorList;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }
    public String getStatusCode() {
        return statusCode;
    }
    public List<ImageBlurryArray> getErrorList () {return errorList;}
    public String getMessage() {return message;}
}
