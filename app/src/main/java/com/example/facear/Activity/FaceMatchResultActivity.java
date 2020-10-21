package com.example.facear.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.facear.FaceActivity;
import com.example.facear.Models.FaceMatchIDResponse;
import com.example.facear.R;
import com.example.facear.Utils.AppConstants;

public class FaceMatchResultActivity extends AppCompatActivity {
    private ImageView resultMark,btnBack;
    private TextView txtScore;
    private Button btnReturn;
    FaceMatchIDResponse faceMatchIDResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_match_result);
        init();
        onReturn();
        onBack();
        faceMatchIDResponse = AppConstants.faceMatchIDResponse;
        float matched_score = Float.parseFloat(faceMatchIDResponse.getMatch_score());
        float score = (float) (matched_score/100.0);
        String score_value = String.valueOf(score);
        if (score > 0.8){
            resultMark.setImageResource(R.drawable.ic_success);
            txtScore.setText(score_value);
            btnReturn.setVisibility(View.GONE);
        }else {
            resultMark.setImageResource(R.drawable.ic_failed);
            txtScore.setText(score_value);
            btnReturn.setVisibility(View.VISIBLE);
        }
    }
    private void init(){
        resultMark = findViewById(R.id.resultMark);
        txtScore = findViewById(R.id.txtScore);
        btnReturn = findViewById(R.id.btnReturn);
        btnBack = findViewById(R.id.btnBack);
    }
    private void onReturn(){
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FaceMatchResultActivity.this, FaceActivity.class);
                intent.putExtra("faceTarget", "faceMatchToIDCard");
                startActivity(intent);
            }
        });
    }
    private void onBack(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FaceMatchResultActivity.this, IDDocMainActivity.class);
                intent.putExtra("Target","FaceMatchToIDDoc");
                startActivity(intent);
            }
        });
    }

}