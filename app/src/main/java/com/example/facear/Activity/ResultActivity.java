package com.example.facear.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.facear.FaceActivity;
import com.example.facear.R;

public class ResultActivity extends AppCompatActivity {
    private ImageView resultMark,btBack;
    private TextView txtResult,txtScore,txtScoreValue,txtConfirm,txtConfirmValue;
    private Button btRetry;
    private LinearLayout confirmLinear;
    boolean scoreResult;
//    boolean faceLiveness;
//    boolean userEnrollment;
//    boolean userAuthentication;
    boolean isEnrollment;
    boolean isAuthented;
    boolean livenessForAuthentification;
    String isEnrollCheck;
    String faceTarget;
    Bundle bundle;
    String score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        configureView();
        onRetry();
        onBack();
        bundle = getIntent().getExtras();
        faceTarget = bundle.getString("faceTarget");
        scoreResult = bundle.getBoolean("scoreResult");
//        faceLiveness = bundle.getBoolean("faceLiveness");
        isEnrollment = bundle.getBoolean("isEnrollment");
//        userEnrollment = bundle.getBoolean("userEnrollment");
        isEnrollCheck = bundle.getString("isEnrollCheck");
//        userAuthentication = bundle.getBoolean("userAuthentication");
        isAuthented = bundle.getBoolean("isAuthented");
        livenessForAuthentification = bundle.getBoolean("livenessForAuthentification");
        score = bundle.getString("score");
        if (faceTarget.equals("faceLiveness")){
            confirmLinear.setVisibility(View.GONE);
            if (scoreResult){
                resultMark.setImageResource(R.drawable.ic_success);
                txtResult.setText("Liveness Confirmed");
                txtScoreValue.setText(score);
                btRetry.setVisibility(View.GONE);
            }else {
                resultMark.setImageResource(R.drawable.ic_failed);
                txtResult.setText("Spoof Detected");
                txtScoreValue.setText(score);
                btRetry.setVisibility(View.VISIBLE);
            }
        }else if (faceTarget.equals("userEnrollment")){
            confirmLinear.setVisibility(View.VISIBLE);
            if (isEnrollment){
                resultMark.setImageResource(R.drawable.ic_success);
                txtResult.setText("Enrollment Successfully");
                txtConfirmValue.setText("YES");
                txtConfirm.setText("Liveness confirmed:");
                txtScoreValue.setText(score);
                btRetry.setVisibility(View.GONE);
            }else {
                resultMark.setImageResource(R.drawable.ic_failed);
                txtResult.setText("Enrollment Failed");
                txtConfirmValue.setText("NO");
                txtConfirm.setText("Liveness confirmed:");
                txtScoreValue.setText(score);
                btRetry.setVisibility(View.VISIBLE);
            }
        }else  if (faceTarget.equals("userAuthentication")){
            confirmLinear.setVisibility(View.VISIBLE);
            if(isAuthented){
                resultMark.setImageResource(R.drawable.ic_success);
                txtResult.setText("Authentication Successfully");
                if (livenessForAuthentification){
                    txtConfirmValue.setText("YES");
                }else {
                    txtConfirmValue.setText("No");
                }
                txtConfirm.setText("Liveness confirmed:");
                txtScoreValue.setText(score);
                btRetry.setVisibility(View.GONE);
            } else {
                resultMark.setImageResource(R.drawable.ic_failed);
                txtResult.setText("Authentication Failed");
                if (livenessForAuthentification){
                    txtConfirmValue.setText("YES");
                }else {
                    txtConfirmValue.setText("No");
                }
                txtConfirm.setText("Liveness confirmed:");
                txtScoreValue.setText(score);
                btRetry.setVisibility(View.VISIBLE);
            }
        }
    }
    private void configureView(){
        resultMark = findViewById(R.id.resultMark);
        txtResult = findViewById(R.id.txtResult);
        txtScore = findViewById(R.id.txtScore);
        txtScoreValue = findViewById(R.id.txtScoreValue);
        btRetry = findViewById(R.id.btRetry);
        btBack = findViewById(R.id.btBack);
        confirmLinear = findViewById(R.id.confirmLin);
        txtConfirm = findViewById(R.id.txtConfirm);
        txtConfirmValue = findViewById(R.id.txtConfirmVal);
    }
    private void onRetry(){
        btRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View var1) {
                if (faceTarget.equals("faceLiveness")){
                    Intent intent = new Intent(ResultActivity.this, FaceActivity.class);
                    intent.putExtra("faceTarget", faceTarget);
                    startActivity(intent);
                    finish();
                }
                if (faceTarget.equals("userEnrollment")){
                    Intent intent = new Intent(ResultActivity.this, FaceActivity.class);
                    intent.putExtra("faceTarget",faceTarget);
                    intent.putExtra("isEnrollCheck", isEnrollCheck);
                    startActivity(intent);
                    finish();
                }
                if(faceTarget.equals("userAuthentication")){
                    Intent intent = new Intent(ResultActivity.this, FaceActivity.class);
                    intent.putExtra("faceTarget", faceTarget);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    private void onBack(){
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View var1) {
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}