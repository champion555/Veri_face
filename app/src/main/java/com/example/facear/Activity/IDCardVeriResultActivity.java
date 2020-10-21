package com.example.facear.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.facear.BackgroundService.BackgroundService;
import com.example.facear.Models.IDCardVeriResponse;
import com.example.facear.R;
import com.example.facear.Utils.AppConstants;

import org.json.JSONException;
import org.json.JSONObject;

public class IDCardVeriResultActivity extends AppCompatActivity {
    private LinearLayout linFirstName,linLastName,linBirth,linGender,linIDDocNum,linIDDocType,linExpiration,linNationality,linVailedScore;
    private ImageView firstNameMark,lastNameMark,birthMark,genderMark,IDDocNumMark,IDDocTypeMark,IDExpirationMark,nationalMark,vailedScoreMark,btnBack;
    private TextView txtFirstName,txtLastName,txtBirth,txtGender,txtIDDocNum,txtIDDocType,txtExpiration,txtNationality,txtVailedScore;
    IDCardVeriResponse idCardVeriResponse;
    Bundle bundle;
    String IDCardType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idcard_veri_result);
        BackgroundService.mContext = IDCardVeriResultActivity.this;
        init();
        onBack();
        bundle = getIntent().getExtras();
        IDCardType = bundle.getString("IDCardType");
        idCardVeriResponse = AppConstants.idCardVeriResponse;
        String firstName = idCardVeriResponse.getFirst_name();
        String lastName = idCardVeriResponse.getLast_name();
        String birth = idCardVeriResponse.getDate_of_birth();
        String gender = idCardVeriResponse.getSex();
        String IDDocNum = idCardVeriResponse.getNumber();
        String IDDocType = IDCardType;
        String expiration = idCardVeriResponse.getExpiration_date();
        String nationality = idCardVeriResponse.getNationality();
        String vailedScore = idCardVeriResponse.getValid_score();
        if (firstName.isEmpty()){
            firstNameMark.setImageResource(R.drawable.ic_failed);
        }else if (firstName != null){
            txtFirstName.setText(firstName);
            firstNameMark.setImageResource(R.drawable.ic_success);
        }
        if (lastName.isEmpty()){
            lastNameMark.setImageResource(R.drawable.ic_failed);
        }else if (lastName != null){
            txtLastName.setText(lastName);
            lastNameMark.setImageResource(R.drawable.ic_success);
        }
        if (birth.isEmpty()){
            birthMark.setImageResource(R.drawable.ic_failed);
        }else if (birth != null){
            txtBirth.setText(birth);
            birthMark.setImageResource(R.drawable.ic_success);
        }
        if (gender.isEmpty()){
            genderMark.setImageResource(R.drawable.ic_failed);
        }else if (gender.equals("M")){
            txtGender.setText("Male");
            genderMark.setImageResource(R.drawable.ic_success);
        }else if (gender.equals("F")){
            txtGender.setText("Female");
            genderMark.setImageResource(R.drawable.ic_success);
        }
        if (IDDocNum.isEmpty()){
            IDDocNumMark.setImageResource(R.drawable.ic_failed);
        }else if (IDDocNum != null){
            txtIDDocNum.setText(IDDocNum);
            IDDocNumMark.setImageResource(R.drawable.ic_success);
        }
        if (IDDocType.isEmpty()){
            IDDocTypeMark.setImageResource(R.drawable.ic_failed);
        }else if (IDDocType != null){
            txtIDDocType.setText(IDDocType);
            IDDocTypeMark.setImageResource(R.drawable.ic_success);
        }
        if (expiration.isEmpty()){
            IDExpirationMark.setImageResource(R.drawable.ic_failed);
        }else if (expiration != null){
            txtExpiration.setText(expiration);
            IDExpirationMark.setImageResource(R.drawable.ic_success);
        }
        if (nationality.isEmpty()){
            nationalMark.setImageResource(R.drawable.ic_failed);
        }else if (nationality != null){
            txtNationality.setText(nationality);
            nationalMark.setImageResource(R.drawable.ic_success);
        }
        if (vailedScore.isEmpty()){
            vailedScoreMark.setImageResource(R.drawable.ic_failed);
        }else if (vailedScore != null){
            txtVailedScore.setText(vailedScore);
            vailedScoreMark.setImageResource(R.drawable.ic_success);
        }
    }
    private void init(){
        firstNameMark = findViewById(R.id.firstNameMark);
        lastNameMark = findViewById(R.id.lastNameMark);
        birthMark = findViewById(R.id.birthMark);
        genderMark = findViewById(R.id.genderMark);
        IDDocNumMark = findViewById(R.id.IDDocNumMark);
        IDDocTypeMark = findViewById(R.id.IDDocTypeMark);
        IDExpirationMark = findViewById(R.id.expirationMark);
        nationalMark = findViewById(R.id.nationalityMark);
        vailedScoreMark = findViewById(R.id.vaildScoreMark);

        txtFirstName = findViewById(R.id.txtFirstName);
        txtLastName = findViewById(R.id.txtLastName);
        txtBirth = findViewById(R.id.txtBirth);
        txtGender = findViewById(R.id.txtGender);
        txtIDDocNum = findViewById(R.id.txtIDDocNum);
        txtIDDocType = findViewById(R.id.txtIDDocType);
        txtExpiration = findViewById(R.id.txtExpirationDate);
        txtNationality = findViewById(R.id.txtNationality);
        txtVailedScore = findViewById(R.id.txtVailedScore);
        btnBack = findViewById(R.id.btnBack);
    }
    private void onBack(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IDCardVeriResultActivity.this,IDDocMainActivity.class);
                intent.putExtra("Target","IDDocVerification");
                startActivity(intent);
            }
        });
    }
}