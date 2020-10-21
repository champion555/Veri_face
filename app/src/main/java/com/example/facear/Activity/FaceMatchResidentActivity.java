package com.example.facear.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.facear.BackgroundService.BackgroundService;
import com.example.facear.FaceActivity;
import com.example.facear.R;
import com.example.facear.Utils.AppConstants;
import com.example.facear.Utils.Preferences;

import java.io.File;
import java.io.FileOutputStream;

public class FaceMatchResidentActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView imgFrontResident,imgBackResident,btnBack;
    private ScrollView captureRel;
    private Button btnContinue;
    private TextView txtFrontIDCard,txtBackIDCard;
    Bitmap frontbitmap,backbitmap;
    int width;
    String mergedPhotoFileName,mergedPhotoFilePath;
    File mergedPhotoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_match_resident);
        BackgroundService.mContext = FaceMatchResidentActivity.this;
        init();
        btnContinue.setVisibility(View.GONE);
        String frontPath = AppConstants.faceMatch_frontResident;
        String backPath = AppConstants.faceMatch_backResident;
        if (frontPath.isEmpty()){
            imgFrontResident.setImageResource(R.drawable.ic_idcard_front);
            txtFrontIDCard.setText("Capture Front Residential Permit");
        }else if (frontPath != null){
            frontbitmap = BitmapFactory.decodeFile(frontPath);
            imgFrontResident.setImageBitmap(frontbitmap);
            txtFrontIDCard.setText("Edit Front Residential Permit");
        }
        if (backPath.isEmpty()){
            imgBackResident.setImageResource(R.drawable.ic_license_back);
            txtBackIDCard.setText("Capture Back Residential Permit");
        }else if (backPath != null){
            backbitmap = BitmapFactory.decodeFile(backPath);
            imgBackResident.setImageBitmap(backbitmap);
            txtBackIDCard.setText("Edit Back Residential Permit");
            btnContinue.setVisibility(View.VISIBLE);
        }
        imgFrontResident.setOnClickListener(this);
        imgBackResident.setOnClickListener(this);
        btnContinue.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }
    private void init (){
        imgFrontResident = findViewById(R.id.imgResidentFront);
        imgBackResident = findViewById(R.id.imgResidentBack);
//        frontIDCameraIcon = findViewById(R.id.frontIDCameraIcon);
//        backIDCameraIcon = findViewById(R.id.backIDCameraIcon);
        captureRel = findViewById(R.id.captureRel);
        txtFrontIDCard = findViewById(R.id.txtFrontIDCard);
        txtBackIDCard = findViewById(R.id.txtBackIdCard);
        btnContinue = findViewById(R.id.btnContinue);
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
        mergedPhotoFileName = "faceMatch_mergedResident.jpg";
        mergedPhotoFilePath = FaceMatchResidentActivity.this.getFilesDir().getPath().toString() + "/" + mergedPhotoFileName;
        mergedPhotoFile = new File(mergedPhotoFilePath);
        if (mergedPhotoFile.exists())
            mergedPhotoFile.delete();
        try {
            FileOutputStream out = new FileOutputStream(mergedPhotoFile);
            Log.e("out", String.valueOf(out));
            resultPhoto.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            Preferences.setValue(FaceMatchResidentActivity.this, Preferences.RESIDENT_VERIFY_PATH, mergedPhotoFilePath);
            Preferences.setValue(FaceMatchResidentActivity.this, Preferences.FACEMATCHIDPATH, mergedPhotoFilePath);
            btnContinue.setVisibility(View.GONE);
            Intent intent_liveness = new Intent(FaceMatchResidentActivity.this, FaceActivity.class);
            intent_liveness.putExtra("faceTarget", "faceMatchToIDCard");
            startActivity(intent_liveness);
            finish();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultPhoto;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgResidentFront:
                Intent intent_front = new Intent(FaceMatchResidentActivity.this, IDDocCameraActivity.class);
                intent_front.putExtra("CardType","faceMatch_frontResident");
                startActivity(intent_front);
                break;
            case R.id.imgResidentBack:
                Intent intent_back = new Intent(FaceMatchResidentActivity.this, IDDocCameraActivity.class);
                intent_back.putExtra("CardType","faceMatch_backResident");
                startActivity(intent_back);
                break;
            case  R.id.btnContinue:
                createSingleImageFromMultipleImages(frontbitmap,backbitmap);
                // goto faceActivity for liveness cheking
                break;
            case R.id.btnBack:
                Intent intent = new Intent(FaceMatchResidentActivity.this, IDDocMainActivity.class);
                intent.putExtra("Target","FaceMatchToIDDoc");
                startActivity(intent);
                break;

        }
    }
}