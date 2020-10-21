package com.example.facear.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.facear.BackgroundService.BackgroundService;
import com.example.facear.FaceActivity;
import com.example.facear.Models.AuthenticationResponse;
import com.example.facear.Models.UserEnrolledCheckResponse;
import com.example.facear.R;
import com.example.facear.Services.RetrofitClient;
import com.example.facear.Utils.AppConstants;
import com.example.facear.Utils.CoreApp;
import com.example.facear.Utils.LoadingDialog;
import com.example.facear.Utils.Preferences;
import com.jdev.countryutil.Constants;
import com.jdev.countryutil.CountryUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView btnSetting;
    Button btnLiveness,btnEnroll,btnAuth,btnFaceMatch,btnKeepID,btnSanction,btnIDDocVeri;
    String faceTarget;
    String isEnrollCheck = "";
    String countryName;
    int countryFlag;
    LoadingDialog loadingDialog;
    private static final int PERMISSONS_REQUEST_CODE= 1240;
    String isSelectCountry = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, BackgroundService.class));
        BackgroundService.mContext = MainActivity.this;
        configureView();
        isPermissionGranted();
        btnSetting.setOnClickListener(this);
        btnLiveness.setOnClickListener(this);
        btnEnroll.setOnClickListener(this);
        btnAuth.setOnClickListener(this);
        btnFaceMatch.setOnClickListener(this);
//        btnKeepID.setOnClickListener(this);
        btnIDDocVeri.setOnClickListener(this);
        btnSanction.setOnClickListener(this);
    }
    private void configureView(){
        btnSetting = findViewById(R.id.btnSetting);
        btnLiveness = findViewById(R.id.btnLiveness);
        btnEnroll = findViewById(R.id.btnEnroll);
        btnAuth = findViewById(R.id.btnAuthe);
        btnFaceMatch = findViewById(R.id.btnFaceMatch);
//        btnKeepID = findViewById(R.id.btnKeepID);
        btnSanction = findViewById(R.id.btnSanction);
        btnIDDocVeri = findViewById(R.id.btnIDDocVeri);
    }
    private void isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkPermission()){
                initData();
            } else {
                requestPermission(MainActivity.this);
            }
        } else {
            initData();
        }
    }
    public boolean checkPermission() {
        int cameraResult = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CAMERA);
        int recordAudioResult = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.RECORD_AUDIO);
        int StorageResult = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int storageReadResult = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE);
        return cameraResult == PackageManager.PERMISSION_GRANTED && recordAudioResult ==
                PackageManager.PERMISSION_GRANTED && StorageResult == PackageManager.PERMISSION_GRANTED && storageReadResult == PackageManager.PERMISSION_GRANTED;
    }
    public void requestPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSONS_REQUEST_CODE);
    }
    private void initData(){
//        setContentView(R.layout.activity_kyc_main);
    }

    private void AuthenticationToServer() {
        String api_key = Preferences.getValue_String(MainActivity.this, Preferences.API_Key);
        String secret_key = Preferences.getValue_String(MainActivity.this, Preferences.API_SecretKey);
        loadingDialog = new LoadingDialog(MainActivity.this, false);
        if (CoreApp.isNetworkConnection(MainActivity.this)) {
            Call<AuthenticationResponse> call = RetrofitClient
                    .getInstance().getApi().authenticateCheck(api_key, secret_key);
            call.enqueue(new Callback<AuthenticationResponse>() {
                @Override
                public void onResponse(Call<AuthenticationResponse> call, Response<AuthenticationResponse> response) {
                    AuthenticationResponse authenticationResponse = response.body();
                    int statuscode = Integer.parseInt(authenticationResponse.getStatusCode());
                    if (statuscode == 200) {
                        AppConstants.api_token = authenticationResponse.getApi_access_token();
                        userEnrolledCheck();
                    } else {
                        Toast.makeText(MainActivity.this, "Authentication Error, Please try again.", Toast.LENGTH_LONG).show();
                        loadingDialog.hide();
                    }
                }

                @Override
                public void onFailure(Call<AuthenticationResponse> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Server is not working now", Toast.LENGTH_LONG).show();
                    loadingDialog.hide();
                }
            });
        } else {
            loadingDialog.hide();
            Toast.makeText(MainActivity.this, "No internet connection available", Toast.LENGTH_LONG).show();
        }
    }
    private void userEnrolledCheck(){
        String username = Preferences.getValue_String(MainActivity.this, Preferences.UserName);
        Call<UserEnrolledCheckResponse> call = RetrofitClient
                .getInstance().getApi().userEnrolledCheck(username);
        call.enqueue(new Callback<UserEnrolledCheckResponse>() {
            @Override
            public void onResponse(Call<UserEnrolledCheckResponse> call, Response<UserEnrolledCheckResponse> response) {
                loadingDialog.hide();
                UserEnrolledCheckResponse userEnrolledCheckResponse = response.body();
                Boolean isEnroll = userEnrolledCheckResponse.getIsEnrolled();
                if (isEnroll) {
                    showAlert();
                } else {
                    faceTarget = "userEnrollment";
                    isEnrollCheck = "false";
                    Intent enroll_intent = new Intent(MainActivity.this, FaceActivity.class);
                    enroll_intent.putExtra("faceTarget", faceTarget);
                    enroll_intent.putExtra("isEnrollCheck", isEnrollCheck);
                    startActivity(enroll_intent);
                }
            }
            @Override
            public void onFailure(Call<UserEnrolledCheckResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Server is not working now", Toast.LENGTH_LONG).show();
                loadingDialog.hide();
            }
        });
    }
    public void showAlert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle(MainActivity.this.getResources().getString(R.string.app_name));
        dialog.setTitle("User Enrollment Checked");
        dialog.setMessage("Enrolled user is already exist. if you want to reenroll, please click OK else NO");
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                faceTarget = "userEnrollment";
                isEnrollCheck = "true";
                Intent enroll_intent = new Intent(MainActivity.this,FaceActivity.class);
                enroll_intent.putExtra("faceTarget", faceTarget);
                enroll_intent.putExtra("isEnrollCheck", isEnrollCheck);
                startActivity(enroll_intent);
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    @Override
    public void onClick(View var1) {
        switch (var1.getId()) {
            case R.id.btnSetting:
                Intent intent_setting = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent_setting);
                break;
            case R.id.btnLiveness:
                faceTarget = "faceLiveness";
                Intent intent_liveness = new Intent(MainActivity.this, FaceActivity.class);
                intent_liveness.putExtra("faceTarget", faceTarget);
                startActivity(intent_liveness);
                break;
            case R.id.btnEnroll:
                AuthenticationToServer();
                break;
            case R.id.btnAuthe:
                faceTarget = "userAuthentication";
                Intent intent_authentication = new Intent(MainActivity.this, FaceActivity.class);
                intent_authentication.putExtra("faceTarget", faceTarget);
                startActivity(intent_authentication);
                break;
//            case R.id.btnKeepID:
//                if (Preferences.getValue_Boolean(MainActivity.this, Preferences.isUserProfile, true)){
//                    Intent intent = new Intent(MainActivity.this, KYC_MainActivity.class);
//                    startActivity(intent);
//                }else{
//                    Intent intent = new Intent(MainActivity.this, KYC_LoginActivity.class);
//                    startActivity(intent);
//                }
//                break;
            case R.id.btnFaceMatch:
                Intent intent_FaceMatch = new Intent(MainActivity.this, IDDocMainActivity.class);
                intent_FaceMatch.putExtra("Target","FaceMatchToIDDoc");
                startActivity(intent_FaceMatch);
                break;
            case R.id.btnSanction:
                Intent intent_sanction = new Intent(MainActivity.this, SanstionAMLActivity.class);
                startActivity(intent_sanction);
                break;
            case R.id.btnIDDocVeri:
                Intent intent_IDVeri = new Intent(MainActivity.this, IDDocMainActivity.class);
                intent_IDVeri.putExtra("Target","IDDocVerification");
                startActivity(intent_IDVeri);
                break;
        }

    }
}