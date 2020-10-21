package com.example.facear.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.facear.BackgroundService.BackgroundService;
import com.example.facear.FaceActivity;
import com.example.facear.Models.AuthenticationResponse;
import com.example.facear.Models.IDCardVeriResponse;
import com.example.facear.Models.PhotoLivenessResponse;
import com.example.facear.Models.SignUpResponse;
import com.example.facear.R;
import com.example.facear.Services.RetrofitClient;
import com.example.facear.Utils.AppConstants;
import com.example.facear.Utils.CoreApp;
import com.example.facear.Utils.LoadingDialog;
import com.example.facear.Utils.Preferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IDCardVerificationActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView imgFrontIDCard,imgBackIdCard,btnBack;
    private ScrollView captureRel;
    private Button btnVerification,btnEdit;
    private TextView txtFrontIDCard,txtBackIDCard;
    Bitmap frontbitmap,backbitmap;
    int width;
    String mergedPhotoFileName,mergedPhotoFilePath;
    File mergedPhotoFile;
    LoadingDialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idcard_verification);
        BackgroundService.mContext = IDCardVerificationActivity.this;
        init();
        btnVerification.setVisibility(View.GONE);
        String frontPath = AppConstants.IDCardVeri_frontCardPath;
        String backPath = AppConstants.IDCardVeri_backCardPath;
        if (frontPath.isEmpty()){
            imgFrontIDCard.setImageResource(R.drawable.ic_idcard_front);
            txtFrontIDCard.setText("Capture Front ID Card");
        }else if (frontPath != null){
            frontbitmap = BitmapFactory.decodeFile(frontPath);
            imgFrontIDCard.setImageBitmap(frontbitmap);
            txtFrontIDCard.setText("Edit Front ID Card");
        }
        if (backPath.isEmpty()){
            imgBackIdCard.setImageResource(R.drawable.ic_idcard_back);
            txtBackIDCard.setText("Capture Back ID Card");
        }else if (backPath != null){
            backbitmap = BitmapFactory.decodeFile(backPath);
            imgBackIdCard.setImageBitmap(backbitmap);
            txtBackIDCard.setText("Edit Back ID Card");
            btnVerification.setVisibility(View.VISIBLE);
        }
        imgFrontIDCard.setOnClickListener(this);
        imgBackIdCard.setOnClickListener(this);
        btnVerification.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }
    private void init (){
        imgFrontIDCard = findViewById(R.id.imgFrontCard);
        imgBackIdCard = findViewById(R.id.imgBackCard);
//        frontIDCameraIcon = findViewById(R.id.frontIDCameraIcon);
//        backIDCameraIcon = findViewById(R.id.backIDCameraIcon);
        captureRel = findViewById(R.id.captureRel);
        txtFrontIDCard = findViewById(R.id.txtFrontIDCard);
        txtBackIDCard = findViewById(R.id.txtBackIdCard);
        btnVerification = findViewById(R.id.btnVerify);
        btnBack = findViewById(R.id.btnBack);
//        btnEdit = findViewById(R.id.btnEdit);

    }
    private Bitmap createSingleImageFromMultipleImages(Bitmap firstImage, Bitmap secondImage) {
        if(firstImage.getWidth() < secondImage.getWidth()){
            width = firstImage.getWidth();
        }else {
            width = secondImage.getWidth();
        }
        Bitmap resultPhoto = Bitmap.createBitmap(width, firstImage.getHeight()+secondImage.getHeight(), firstImage.getConfig());
        Canvas canvas = new Canvas(resultPhoto);
        canvas.drawBitmap(firstImage, 0f, 0f, null);
        canvas.drawBitmap(secondImage, 0f, firstImage.getHeight(), null);
        mergedPhotoFileName = "Veri_mergeIDCard.jpg";
        mergedPhotoFilePath = IDCardVerificationActivity.this.getFilesDir().getPath().toString() + "/" + mergedPhotoFileName;
        mergedPhotoFile = new File(mergedPhotoFilePath);
        if (mergedPhotoFile.exists())
            mergedPhotoFile.delete();
        try {
            FileOutputStream out = new FileOutputStream(mergedPhotoFile);
            Log.e("out", String.valueOf(out));
            resultPhoto.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            Preferences.setValue(IDCardVerificationActivity.this, Preferences.IDCARD_VERIFY_PATH, mergedPhotoFilePath);
            AuthenticationToServer();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultPhoto;
    }
    private void AuthenticationToServer() {
        String api_key = Preferences.getValue_String(IDCardVerificationActivity.this, Preferences.API_Key);
        String secret_key = Preferences.getValue_String(IDCardVerificationActivity.this, Preferences.API_SecretKey);
        loadingDialog = new LoadingDialog(IDCardVerificationActivity.this, false);
        if (CoreApp.isNetworkConnection(IDCardVerificationActivity.this)) {
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
                        Toast.makeText(IDCardVerificationActivity.this, response.message(), Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Call<AuthenticationResponse> call, Throwable t) {
                    Toast.makeText(IDCardVerificationActivity.this, "Server is not working now", Toast.LENGTH_LONG).show();
                    loadingDialog.hide();
                }
            });
        } else {
            loadingDialog.hide();
            Toast.makeText(IDCardVerificationActivity.this, "No internet connection available", Toast.LENGTH_LONG).show();
        }
    }
    private void VerificationIDCardToServer() {
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/jpeg"),
                mergedPhotoFile);
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
                    Intent intent = new Intent(IDCardVerificationActivity.this,IDCardVeriResultActivity.class);
                    intent.putExtra("IDCardType","ID Card");
                    startActivity(intent);
                } else {
                    loadingDialog.hide();
                    Toast.makeText(IDCardVerificationActivity.this, "Bad input MRZ image!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<IDCardVeriResponse> call, Throwable t) {
                Toast.makeText(IDCardVerificationActivity.this, "Server is not working now", Toast.LENGTH_LONG).show();
                loadingDialog.hide();
            }
        });

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgFrontCard:
                Intent intent_front = new Intent(IDCardVerificationActivity.this, IDDocCameraActivity.class);
                intent_front.putExtra("CardType","IDcard_FrontCard");
                startActivity(intent_front);
                break;
            case R.id.imgBackCard:
                Intent intent_back = new Intent(IDCardVerificationActivity.this, IDDocCameraActivity.class);
                intent_back.putExtra("CardType","IDcard_BackCard");
                startActivity(intent_back);
                break;
            case  R.id.btnVerify:
                createSingleImageFromMultipleImages(frontbitmap,backbitmap);
                break;
            case R.id.btnBack:
                Intent intent = new Intent(IDCardVerificationActivity.this, IDDocMainActivity.class);
                intent.putExtra("Target","IDDocVerification");
                startActivity(intent);
                break;

        }
    }
}