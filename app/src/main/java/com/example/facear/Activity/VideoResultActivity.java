package com.example.facear.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.facear.BackgroundService.BackgroundService;
import com.example.facear.FaceActivity;
import com.example.facear.R;
import com.example.facear.Utils.AppConstants;

public class VideoResultActivity extends AppCompatActivity {
    private ImageView resultMark,btBack;
    private TextView txtResult,lblScore,txtScore,txtMessage;
    private Button btRetry;
    private LinearLayout scoreLin;
    Bundle bundle;
    String scoreResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_result);
        BackgroundService.mContext = VideoResultActivity.this;
        init();
        onRetry();
        onBack();
        bundle = getIntent().getExtras();
        scoreResult = bundle.getString("scoreResult");

        if (scoreResult.equals("ok")){
            resultMark.setImageResource(R.drawable.ic_success);
            txtResult.setText("Liveness Confirmed");
            txtScore.setText(AppConstants.photoLivenessResponse.getScore());
            txtMessage.setVisibility(View.GONE);
            btRetry.setVisibility(View.GONE);
        }else if (scoreResult.equals("fail")){
            resultMark.setImageResource(R.drawable.ic_failed);
            txtResult.setText("Spoof Detection");
            txtScore.setText(AppConstants.photoLivenessResponse.getScore());
            txtMessage.setVisibility(View.GONE);
            btRetry.setVisibility(View.VISIBLE);
        }else if (scoreResult.equals("no")){
            resultMark.setImageResource(R.drawable.ic_unable);
            txtResult.setText("Unable to confirm liveness");
            scoreLin.setVisibility(View.GONE);
            txtMessage.setVisibility(View.VISIBLE);
            txtMessage.setText("Make sure the video quality is better, please try again");
            btRetry.setVisibility(View.VISIBLE);
        }
    }
    private void init(){
        resultMark = findViewById(R.id.resultMark);
        txtResult = findViewById(R.id.txtResult);
        txtScore = findViewById(R.id.txtScore);
        lblScore = findViewById(R.id.lblScore);
        btRetry = findViewById(R.id.btRetry);
        btBack = findViewById(R.id.btBack);
        scoreLin = findViewById(R.id.scoreLin);
        txtMessage = findViewById(R.id.txtMes);
    }
    private void onRetry(){
        btRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View var1) {
                Intent intent = new Intent(VideoResultActivity.this, VideoLivenessActivity.class);
                intent.putExtra("Target", "videoLiveness");
                startActivity(intent);
            }
        });
    }
    private void onBack(){
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View var1) {
                Intent intent = new Intent(VideoResultActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}