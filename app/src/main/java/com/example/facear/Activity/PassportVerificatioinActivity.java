package com.example.facear.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.facear.BackgroundService.BackgroundService;
import com.example.facear.Models.AuthenticationResponse;
import com.example.facear.Models.IDCardVeriResponse;
import com.example.facear.R;
import com.example.facear.Services.RetrofitClient;
import com.example.facear.Utils.AppConstants;
import com.example.facear.Utils.CoreApp;
import com.example.facear.Utils.LoadingDialog;
import com.example.facear.Utils.Preferences;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassportVerificatioinActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView imgPassport,btnBack;
    private Button btnVerification;
    private TextView txtPassport;
    String currentPhotoPath,passportPath;
    Uri photoURI,demoPassportUri;
    static final int REQUEST_TAKE_PHOTO = 1;
    Bitmap passportBitmap;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passport_verificatioin);
        BackgroundService.mContext = PassportVerificatioinActivity.this;
        init();
        String passportPath = AppConstants.PassportVeriPath;
        if (passportPath.isEmpty()){
            imgPassport.setImageResource(R.drawable.ic_passport);
            txtPassport.setText("Capture Passport");
        }else if (passportPath != null){
            Uri passportUri = Uri.parse(passportPath);
            imgPassport.setImageURI(passportUri);
//            passportBitmap = BitmapFactory.decodeFile(passportPath);
//            imgPassport.setImageBitmap(passportBitmap);
            txtPassport.setText("Edit Passport");
        }
        imgPassport.setOnClickListener(this);
        btnVerification.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }
    private void init(){
        imgPassport = findViewById(R.id.passportImg);
        btnVerification = findViewById(R.id.btnVerification);
        txtPassport = findViewById(R.id.txtPassport);
        btnBack = findViewById(R.id.btnBack);
    }
    private void AuthenticationToServer() {
        String api_key = Preferences.getValue_String(PassportVerificatioinActivity.this, Preferences.API_Key);
        String secret_key = Preferences.getValue_String(PassportVerificatioinActivity.this, Preferences.API_SecretKey);
        loadingDialog = new LoadingDialog(PassportVerificatioinActivity.this, false);
        if (CoreApp.isNetworkConnection(PassportVerificatioinActivity.this)) {
            Call<AuthenticationResponse> call = RetrofitClient
                    .getInstance().getApi().authenticateCheck(api_key, secret_key);
            call.enqueue(new Callback<AuthenticationResponse>() {
                @Override
                public void onResponse(Call<AuthenticationResponse> call, Response<AuthenticationResponse> response) {
                    AuthenticationResponse authenticationResponse = response.body();
                    int statuscode = Integer.parseInt(authenticationResponse.getStatusCode());
                    if (statuscode == 200) {
                        AppConstants.api_token = authenticationResponse.getApi_access_token();
                        VerificationIDCardToServer();
                    } else {
                        Toast.makeText(PassportVerificatioinActivity.this, response.message(), Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Call<AuthenticationResponse> call, Throwable t) {
                    Toast.makeText(PassportVerificatioinActivity.this, "Server is not working now", Toast.LENGTH_LONG).show();
                    loadingDialog.hide();
                }
            });
        } else {
            loadingDialog.hide();
            Toast.makeText(PassportVerificatioinActivity.this, "No internet connection available", Toast.LENGTH_LONG).show();
        }
    }
    private void VerificationIDCardToServer() {
        File file = new File(AppConstants.PassportVeriPath);
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/jpeg"),
                file);
        Call<IDCardVeriResponse> call = RetrofitClient
                .getInstance().getApi().IDCardVerification(reqFile);
        call.enqueue(new Callback<IDCardVeriResponse>() {
            @Override
            public void onResponse(Call<IDCardVeriResponse> call, Response<IDCardVeriResponse> response) {
                IDCardVeriResponse idCardVeriResponse = response.body();
                String status = idCardVeriResponse.getStatus();
                if (status.equals("SUCCESS")) {
                    loadingDialog.hide();
                    AppConstants.idCardVeriResponse = idCardVeriResponse;
                    Intent intent = new Intent(PassportVerificatioinActivity.this,IDCardVeriResultActivity.class);
                    intent.putExtra("IDCardType","Passport");
                    startActivity(intent);
                } else {
                    loadingDialog.hide();
                    Toast.makeText(PassportVerificatioinActivity.this, "Bad input MRZ image!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<IDCardVeriResponse> call, Throwable t) {
                Toast.makeText(PassportVerificatioinActivity.this, "Server is not working now", Toast.LENGTH_LONG).show();
                loadingDialog.hide();
            }
        });

    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.passportImg:
               Intent intent_passport = new Intent(PassportVerificatioinActivity.this, IDDocCameraActivity.class);
               intent_passport.putExtra("CardType","Passport");
               startActivity(intent_passport);
               break;
           case R.id.btnVerification:
               AuthenticationToServer();
               break;
           case R.id.btnBack:
               Intent intent_back = new Intent(PassportVerificatioinActivity.this, IDDocMainActivity.class);
               intent_back.putExtra("Target","IDDocVerification");
               startActivity(intent_back);
               break;
       }
    }
}