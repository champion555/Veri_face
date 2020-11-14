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
import com.example.facear.Utils.AppConstants;

public class ResultActivity extends AppCompatActivity {
    private ImageView resultMark,btBack,confirmCheck;
    private TextView txtResult,txtLiveness;
    private LinearLayout linResult;
    private Button btRetry;
    String isEnrollCheck;
    String faceTarget,isEnrollment,isAuthentication;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        configureView();
        onRetry();
        onBack();
        bundle = getIntent().getExtras();
        faceTarget = bundle.getString("faceTarget");
        isEnrollCheck = bundle.getString("isEnrollCheck");
        isEnrollment = bundle.getString("isEnrollment");
        isAuthentication = bundle.getString("isAuthentication");
        if (faceTarget.equals("faceLiveness")){
            String result = AppConstants.photoLivenessResponse.getResult();
            if (result.equals("LIVENESS")){
                resultMark.setImageResource(R.drawable.ic_success);
                txtResult.setText("Liveness Confirmed");
                btRetry.setVisibility(View.GONE);
                linResult.setVisibility(View.GONE);
            }else if (result.equals("SPOOF")){
                resultMark.setImageResource(R.drawable.ic_failed);
                txtResult.setText("Spoof detected");
                btRetry.setVisibility(View.VISIBLE);
                linResult.setVisibility(View.GONE);
            }else{
                resultMark.setImageResource(R.drawable.ic_unable);
                txtResult.setText("Unable to confirm liveness");
                btRetry.setVisibility(View.VISIBLE);
                confirmCheck.setVisibility(View.GONE);
                txtLiveness.setText("Image is too blurry or contain glares!");
            }
        }else if (faceTarget.equals("userEnrollment")){
            linResult.setVisibility(View.VISIBLE);
            if (isEnrollment.equals("success")){
                resultMark.setImageResource(R.drawable.ic_success);
                txtResult.setText("Enrollment Successfully");
                txtLiveness.setText("Liveness Confirmed:");
                confirmCheck.setImageResource(R.drawable.ic_confirmcheck);
                btRetry.setVisibility(View.GONE);
            }else if (isEnrollment.equals("failed")){
                resultMark.setImageResource(R.drawable.ic_failed);
                txtResult.setText("Enrollment Failed");
                txtLiveness.setText("Liveness Confirmed:");
                confirmCheck.setImageResource(R.drawable.ic_failedconfirm);
                btRetry.setVisibility(View.VISIBLE);
            }else if (isEnrollment.equals("unable")){
                resultMark.setImageResource(R.drawable.ic_failed);
                txtResult.setText("Enrollment Failed");
                txtLiveness.setText("Liveness Confirmed:");
                confirmCheck.setImageResource(R.drawable.ic_unable);
                btRetry.setVisibility(View.VISIBLE);
            }
        }else  if (faceTarget.equals("userAuthentication")){

            linResult.setVisibility(View.VISIBLE);
            if (isAuthentication.equals("success")){
                resultMark.setImageResource(R.drawable.ic_success);
                txtResult.setText("Authentication Successfully");
                txtLiveness.setText("Liveness Confirmed:");
                confirmCheck.setImageResource(R.drawable.ic_confirmcheck);
                btRetry.setVisibility(View.GONE);
            }else if (isAuthentication.equals("no_success")){
                resultMark.setImageResource(R.drawable.ic_failed);
                txtResult.setText("Authentication Failed");
                txtLiveness.setText("Liveness Confirmed:");
                confirmCheck.setImageResource(R.drawable.ic_confirmcheck);
                btRetry.setVisibility(View.VISIBLE);
            }else if (isAuthentication.equals("failed")){
                resultMark.setImageResource(R.drawable.ic_failed);
                txtResult.setText("Authentication Failed");
                txtLiveness.setText("Liveness Confirmed:");
                confirmCheck.setImageResource(R.drawable.ic_failedconfirm);
                btRetry.setVisibility(View.VISIBLE);
            }else if (isAuthentication.equals("unable")){
                resultMark.setImageResource(R.drawable.ic_failed);
                txtResult.setText("Authentication Failed");
                txtLiveness.setText("Liveness Confirmed:");
                confirmCheck.setImageResource(R.drawable.ic_unable);
                btRetry.setVisibility(View.VISIBLE);
            }
        }
    }
    private void configureView(){
        resultMark = findViewById(R.id.resultMark);
        txtResult = findViewById(R.id.txtResult);
        btRetry = findViewById(R.id.btRetry);
        btBack = findViewById(R.id.btBack);
        linResult = findViewById(R.id.linResult);
        confirmCheck = findViewById(R.id.confirmCheck);
        txtLiveness = findViewById(R.id.txtLiveness);
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