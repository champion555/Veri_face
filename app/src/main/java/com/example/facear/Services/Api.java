package com.example.facear.Services;

import com.example.facear.Models.AuthenticationResponse;
import com.example.facear.Models.CheckMailResponse;
import com.example.facear.Models.CheckPhoneNumResponse;
import com.example.facear.Models.FaceMatchIDResponse;
import com.example.facear.Models.IDCardVeriResponse;
import com.example.facear.Models.ImageCheckResponse;
import com.example.facear.Models.LoginResponse;
import com.example.facear.Models.PhotoLivenessResponse;
import com.example.facear.Models.SendMailResponse;
import com.example.facear.Models.SendPhoneNumResponse;
import com.example.facear.Models.SignUpResponse;
import com.example.facear.Models.UserAuthenticationResponse;
import com.example.facear.Models.UserEnrolledCheckResponse;
import com.example.facear.Models.UserEnrollmentResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Api {
    @FormUrlEncoded
    @POST("client/authentificate")
    Call<AuthenticationResponse> authenticateCheck(
            @Field("api_key") String api_key,
            @Field("secret_key") String secret_key
    );

    @Multipart
    @POST("faceliveness")
    Call<PhotoLivenessResponse> photoLivenessCheck(
            @Part("live_image_file\"; filename=\"capturedPhoto.jpg")
                    RequestBody file
    );
    @Multipart
    @POST("videoFaceLiveness")
    Call<PhotoLivenessResponse> videoLivenessCheck(
            @Part("live_video_file\"; filename=\"videoLiveness.mp4")
                    RequestBody file
    );
    @FormUrlEncoded
    @POST("userEnrolledCheck")
    Call<UserEnrolledCheckResponse> userEnrolledCheck(
            @Field("username") String username
    );
    @Multipart
    @POST("faceEnroll")
    Call<UserEnrollmentResponse> faceEnroll(
            @Part("image_file\"; filename=\"capturedPhoto.jpg")
                    RequestBody file,
            @Part("username") RequestBody username,
            @Part("force_enrollment") RequestBody force_enrollment
    );
    @Multipart
    @POST("facePhotoVerif")
    Call<UserAuthenticationResponse> userAuthentication(
            @Part("image_file\"; filename=\"capturedPhoto.jpg")
                    RequestBody file,
            @Part("username") RequestBody username
    );
    @FormUrlEncoded
    @POST("sendMailOtp")
    Call<SendMailResponse> sendMail(
            @Field("email") String email,
            @Field("language") String language
    );
    @FormUrlEncoded
    @POST("checkMailOtp")
    Call<CheckMailResponse> checkMail(
            @Field("job_id") String jobId,
            @Field("otp") String otp
    );
    @FormUrlEncoded
    @POST("sendSMSOtp")
    Call<SendPhoneNumResponse> sendPhone(
            @Field("phone") String phone,
            @Field("language") String language
    );
    @FormUrlEncoded
    @POST("checkSMSOtp")
    Call<CheckPhoneNumResponse> checkPhone(
            @Field("job_id") String jobId,
            @Field("otp") String otp
    );
    @FormUrlEncoded
    @POST("demo/register")
    Call<SignUpResponse> signUp(
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("email") String email,
            @Field("phone_number") String phone_number,
            @Field("company_name") String company_name,
            @Field("industry_type") String industry_type,
            @Field("country_code") String country_code,
            @Field("job_title") String job_title,
            @Field("password") String password
    );
    @FormUrlEncoded
    @POST("demo/emailVerification")
    Call<SendMailResponse> sendDemoEmail(
            @Field("email") String email,
            @Field("language") String language
    );
    @FormUrlEncoded
    @POST("demo/emailOtpCheck")
    Call<CheckMailResponse> checkDemoEmail(
            @Field("job_id") String jobId,
            @Field("otp") String otp
    );
    @FormUrlEncoded
    @POST("demo/login")
    Call<LoginResponse> login(
            @Field("email") String email,
            @Field("password") String password
    );
    @Multipart
    @POST("faceMatch")
    Call<FaceMatchIDResponse> faceMatchIDCard(
            @Part("source_image_file\"; filename=\"capturedPhoto.jpg")
                    RequestBody source_image_file,
            @Part("target_image_file\"; filename=\"faceMatch_mergedIDCard.jpg")
                    RequestBody target_image_file
    );
    @Multipart
    @POST("idDocVerif")
    Call<IDCardVeriResponse> IDCardVerification(
            @Part("document_image_file\"; filename=\"Veri_mergeIDCard.jpg")
                    RequestBody file
    );
    @Multipart
    @POST("ImageQualityCheck")
    Call<ImageCheckResponse> imageCheck(
            @Part("image_file\"; filename=\"Veri_mergeIDCard.jpg")
                    RequestBody file
    );
}
