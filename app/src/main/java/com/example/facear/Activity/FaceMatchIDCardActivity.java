package com.example.facear.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.facear.BackgroundService.BackgroundService;
import com.example.facear.FaceActivity;
import com.example.facear.R;
import com.example.facear.Utils.AppConstants;
import com.example.facear.Utils.Preferences;

import java.io.File;
import java.io.FileOutputStream;

public class FaceMatchIDCardActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imgFrontIDCard,imgBackIdCard,btnBack;
    private RelativeLayout frontIDCameraIcon,backIDCameraIcon;
    private ScrollView captureRel;
    private Button btnContinue,btnEdit;
    private TextView txtFrontIDCard,txtBackIDCard;
    Bitmap frontbitmap,backbitmap;
    int width;
    String mergedPhotoFileName,mergedPhotoFilePath;
    File mergedPhotoFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_match_idcard);
        BackgroundService.mContext = FaceMatchIDCardActivity.this;
        init();
        btnContinue.setVisibility(View.GONE);
        String frontPath = AppConstants.faceMatch_frontCardPath;
        String backPath = AppConstants.faceMatch_backCardPath;
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
            btnContinue.setVisibility(View.VISIBLE);
        }
        imgFrontIDCard.setOnClickListener(this);
        imgBackIdCard.setOnClickListener(this);
        btnContinue.setOnClickListener(this);
        btnBack.setOnClickListener(this);
//        btnEdit.setOnClickListener(this);
    }
    private void init (){
        imgFrontIDCard = findViewById(R.id.imgFrontCard);
        imgBackIdCard = findViewById(R.id.imgBackCard);
        frontIDCameraIcon = findViewById(R.id.frontIDCameraIcon);
        backIDCameraIcon = findViewById(R.id.backIDCameraIcon);
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
        mergedPhotoFileName = "faceMatch_mergedIDCard.jpg";
        mergedPhotoFilePath = FaceMatchIDCardActivity.this.getFilesDir().getPath().toString() + "/" + mergedPhotoFileName;
        mergedPhotoFile = new File(mergedPhotoFilePath);
        if (mergedPhotoFile.exists())
            mergedPhotoFile.delete();
        try {
            FileOutputStream out = new FileOutputStream(mergedPhotoFile);
            Log.e("out", String.valueOf(out));
            resultPhoto.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

            Preferences.setValue(FaceMatchIDCardActivity.this, Preferences.FACEMATCHIDPATH, mergedPhotoFilePath);
            btnContinue.setVisibility(View.GONE);
            Intent intent_liveness = new Intent(FaceMatchIDCardActivity.this, FaceActivity.class);
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
            case R.id.imgFrontCard:
                Intent intent_front = new Intent(FaceMatchIDCardActivity.this, IDDocCameraActivity.class);
                intent_front.putExtra("CardType","FaceMatch_FrontCard");
                startActivity(intent_front);
                break;
            case R.id.imgBackCard:
                Intent intent_back = new Intent(FaceMatchIDCardActivity.this, IDDocCameraActivity.class);
                intent_back.putExtra("CardType","FaceMatch_BackCard");
                startActivity(intent_back);
                break;
            case  R.id.btnContinue:
                createSingleImageFromMultipleImages(frontbitmap,backbitmap);
                break;
            case R.id.btnBack:
                Intent intent = new Intent(FaceMatchIDCardActivity.this, IDDocMainActivity.class);
                intent.putExtra("Target","FaceMatchToIDDoc");
                startActivity(intent);
                break;

        }
    }
}