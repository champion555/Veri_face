package com.example.facear.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.facear.BackgroundService.BackgroundService;
import com.example.facear.R;
import com.example.facear.Utils.AppConstants;
import com.jdev.countryutil.Constants;
import com.jdev.countryutil.CountryUtil;

public class IDDocMainActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout btnPassport,btnNationalID,btnDrivingLicense,btnResidentialPermit;
    private ImageView btnBack;
    Bundle bundle;
    String IDDocType = "";
    String Target = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_doc_main);
        BackgroundService.mContext = IDDocMainActivity.this;
        init();
        listener();
        bundle = getIntent().getExtras();
        Target = bundle.getString("Target");
    }
    private void init(){
        btnPassport = findViewById(R.id.btnPassport);
        btnNationalID = findViewById(R.id.btnIdcard);
        btnDrivingLicense = findViewById(R.id.btnDrivingLicence);
        btnResidentialPermit = findViewById(R.id.btnResidentialPermit);
        btnBack = findViewById(R.id.btnBack);
    }
    private void listener(){
        btnPassport.setOnClickListener(this);
        btnNationalID.setOnClickListener(this);
        btnDrivingLicense.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnResidentialPermit.setOnClickListener(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.KEY_RESULT_CODE) {
            try {
                String countryName = data.getStringExtra(Constants.KEY_COUNTRY_NAME);
                int countryFlag = data.getIntExtra(Constants.KEY_COUNTRY_FLAG, 0);
                AppConstants.countryName = countryName;
                if (IDDocType.equals("Passport")){
                    if (Target.equals("FaceMatchToIDDoc")){
                        Intent intent_passport = new Intent(IDDocMainActivity.this, IDDocCameraActivity.class);
                        intent_passport.putExtra("CardType","faceMatch_Passport");
                        startActivity(intent_passport);
                    }else if (Target.equals("IDDocVerification")){
                        Intent intent_passport = new Intent(IDDocMainActivity.this, IDDocCameraActivity.class);
                        intent_passport.putExtra("CardType","Passport");
                        startActivity(intent_passport);
                    }else if (Target.equals("SanctionsPEP")){
                        Intent intent_passport = new Intent(IDDocMainActivity.this, SanctionCameraActivity.class);
                        intent_passport.putExtra("CardType","Passport");
                        startActivity(intent_passport);
                    }
                }else if (IDDocType.equals("IDCard")){
                    if (Target.equals("FaceMatchToIDDoc")){
                        Intent intent_id = new Intent(IDDocMainActivity.this, IDDocCameraActivity.class);
                        intent_id.putExtra("CardType","FaceMatch_FrontCard");
                        startActivity(intent_id);
                    }else if (Target.equals("IDDocVerification")){
                        Intent intent_id = new Intent(IDDocMainActivity.this, IDDocCameraActivity.class);
                        intent_id.putExtra("CardType","IDcard_FrontCard");
                        startActivity(intent_id);
                    }else if (Target.equals("SanctionsPEP")){
                        Intent intent_id = new Intent(IDDocMainActivity.this, SanctionCameraActivity.class);
                        intent_id.putExtra("CardType","IDCard");
                        startActivity(intent_id);
                    }

                }else if (IDDocType.equals("DrivingLicense")){
                    if (Target.equals("FaceMatchToIDDoc")){
                        Intent intent_driving = new Intent(IDDocMainActivity.this, IDDocCameraActivity.class);
                        intent_driving.putExtra("CardType","faceMatch_FrontDriving");
                        startActivity(intent_driving);
                    }else if (Target.equals("IDDocVerification")){
                        Intent intent_driving = new Intent(IDDocMainActivity.this, IDDocCameraActivity.class);
                        intent_driving.putExtra("CardType","Driving_FrontCard");
                        startActivity(intent_driving);
                    }else if (Target.equals("SanctionsPEP")){
                        Intent intent_driving = new Intent(IDDocMainActivity.this, SanctionCameraActivity.class);
                        intent_driving.putExtra("CardType","DrivingLicense");
                        startActivity(intent_driving);
                    }

                }else if (IDDocType.equals("ResidentPermit")){
                    if (Target.equals("FaceMatchToIDDoc")){
                        Intent intent_resident = new Intent(IDDocMainActivity.this, IDDocCameraActivity.class);
                        intent_resident.putExtra("CardType","faceMatch_frontResident");
                        startActivity(intent_resident);
                    }else if (Target.equals("IDDocVerification")){
                        Intent intent_resident = new Intent(IDDocMainActivity.this, IDDocCameraActivity.class);
                        intent_resident.putExtra("CardType","Resident_Front");
                        startActivity(intent_resident);
                    }else if (Target.equals("SanctionsPEP")){
                        Intent intent_resident = new Intent(IDDocMainActivity.this, SanctionCameraActivity.class);
                        intent_resident.putExtra("CardType","ResidentPermit");
                        startActivity(intent_resident);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnPassport:
                IDDocType  = "Passport";
                new CountryUtil(IDDocMainActivity.this).setTitle("Select issuing Country").build();
                break;
            case R.id.btnIdcard:
                IDDocType  = "IDCard";
                new CountryUtil(IDDocMainActivity.this).setTitle("Select issuing Country").build();
                break;
            case R.id.btnDrivingLicence:
                IDDocType  = "DrivingLicense";
                new CountryUtil(IDDocMainActivity.this).setTitle("Select issuing Country").build();
                break;
            case R.id.btnResidentialPermit:
                IDDocType  = "ResidentPermit";
                new CountryUtil(IDDocMainActivity.this).setTitle("Select issuing Country").build();
                break;
            case R.id.btnBack:
                Intent intent_back = new Intent(IDDocMainActivity.this,MainActivity.class);
                startActivity(intent_back);
                break;

        }

    }
}