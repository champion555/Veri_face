package com.example.facear.Utils;

import com.example.facear.Models.FaceMatchIDResponse;
import com.example.facear.Models.IDCardVeriResponse;
import com.example.facear.Models.PhotoLivenessResponse;
import com.example.facear.Models.UserAuthenticationResponse;
import com.example.facear.Models.UserEnrolledCheckResponse;
import com.example.facear.Models.UserEnrollmentResponse;

public class AppConstants {

//    public static String BaseURL = "https://109.238.12.179:5000/v1/api/";
    public static String BaseURL = "https://api.ikyx.net:5000/v1/api/";
    public static String apiKey = "Mzc0MTExMjUtNTBmMS00ZTA3LWEwNjktZjQxM2UwNjA3ZGEw";
    public static String secretKey = "YTE4YmM5YmYtZjZhYS00MTU5LWI4Y2EtYjQyYTRkNzAxOWZj";
    public static String videoDuration = "6000";
    public static String userName = "demo_app";
    public static String userSession = "25";
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

    public static String IDCardSanction_frontPath = "";
    public static String IDCardSanction_backPath = "";
    public static String PassportSanctionPath = "";
    public static String DrivingSanction_frontPath = "";
    public static String DrivingSanction_backPath = "";
    public static String ResidentSanction_frontPath = "";
    public static String ResidentSanction_backPath = "";

    public static String POADoc_path = "";

    public static String countryName = "";

    public static IDCardVeriResponse  idCardVeriResponse = null;
    public static FaceMatchIDResponse faceMatchIDResponse = null;

    public static PhotoLivenessResponse photoLivenessResponse = null;
    public static UserEnrollmentResponse userEnrollmentResponse = null;
    public static UserAuthenticationResponse userAuthenticationResponse = null;

}
