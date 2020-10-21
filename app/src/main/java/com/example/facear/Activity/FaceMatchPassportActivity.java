package com.example.facear.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.facear.BackgroundService.BackgroundService;
import com.example.facear.FaceActivity;
import com.example.facear.R;
import com.example.facear.Utils.AppConstants;
import com.example.facear.Utils.Preferences;

import java.io.File;

public class FaceMatchPassportActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView imgPassport,btnBack;
    private Button btnContinue;
    private TextView txtPassport;
    String currentPhotoPath,passportPath;
    Uri photoURI,demoPassportUri;
    static final int REQUEST_TAKE_PHOTO = 1;
    Bitmap passportBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_match_passport);
        BackgroundService.mContext = FaceMatchPassportActivity.this;
        init();
        String passportPath = AppConstants.faceMatch_passportPath;
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
        btnContinue.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }
    private void init(){
        imgPassport = findViewById(R.id.passportImg);
        btnContinue = findViewById(R.id.btnContinue);
        txtPassport = findViewById(R.id.txtPassport);
        btnBack = findViewById(R.id.btnBack);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.passportImg:
                Intent intent_passport = new Intent(FaceMatchPassportActivity.this, IDDocCameraActivity.class);
                intent_passport.putExtra("CardType","faceMatch_Passport");
                startActivity(intent_passport);
                break;
            case R.id.btnContinue:
                Preferences.setValue(FaceMatchPassportActivity.this, Preferences.FACEMATCHIDPATH, AppConstants.faceMatch_passportPath);
                btnContinue.setVisibility(View.GONE);
                Intent intent_liveness = new Intent(FaceMatchPassportActivity.this, FaceActivity.class);
                intent_liveness.putExtra("faceTarget", "faceMatchToIDCard");
                startActivity(intent_liveness);
                finish();
//                Intent intent_continue = new Intent(FaceMatchPassportActivity.this, FaceActivity.class);
//                startActivity(intent_continue);
                break;
            case R.id.btnBack:
                Intent intent_back = new Intent(FaceMatchPassportActivity.this, IDDocMainActivity.class);
                intent_back.putExtra("Target","FaceMatchToIDDoc");
                startActivity(intent_back);
                break;
        }
    }
}