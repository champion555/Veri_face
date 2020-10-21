package com.example.facear.Utils;

import com.example.facear.Models.FaceMatchIDResponse;
import com.example.facear.Models.IDCardVeriResponse;

public class AppConstants {

    public static String BaseURL = "https://109.238.12.179:5000/v1/api/";
    public static String apiKey = "Mzc0MTExMjUtNTBmMS00ZTA3LWEwNjktZjQxM2UwNjA3ZGEw";
    public static String secretKey = "YTE4YmM5YmYtZjZhYS00MTU5LWI4Y2EtYjQyYTRkNzAxOWZj";
    public static String videoDuration = "6000";
    public static String userName = "demo_app";
    public static String userSession = "30";
    public static String api_token = "";
    public static String userProfilePincode = "";
    public static String faceMatch_passportPath = "";
    public static String faceMatch_frontCardPath = "";
    public static String faceMatch_backCardPath = "";
    public static String faceMatch_frontDriving = "";
    public static String faceMatch_backDriving = "";
    public static String faceMatch_frontResident = "";
    public static String faceMatch_backResident = "";

    public static String IDCardVeri_frontCardPath = "";
    public static String IDCardVeri_backCardPath = "";
    public static String PassportVeriPath = "";
    public static String DrivingVeri_frontCardPath = "";
    public static String DrivingVeri_backCardPath = "";
    public static String ResidentVeri_frontCardPath = "";
    public static String ResidentVeri_backCardPath = "";

    public static String countryName = "";

    public static IDCardVeriResponse  idCardVeriResponse = null;
    public static FaceMatchIDResponse faceMatchIDResponse = null;

}
