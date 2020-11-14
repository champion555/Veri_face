package com.example.facear.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.example.facear.BackgroundService.BackgroundService;
import com.example.facear.FaceActivity;
import com.example.facear.R;

public class IntentActivity extends AppCompatActivity {
    private Bundle bundle;
    String intentTarget;
    TextView txtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent);
        BackgroundService.mContext = IntentActivity.this;
        txtTitle = findViewById(R.id.txtTitle);
        bundle = getIntent().getExtras();
        intentTarget = bundle.getString("intentTarget");
        if (intentTarget.equals("faceMatchID")){
            txtTitle.setText("Face Match ID Card");
        }else if (intentTarget.equals("POA")){
            txtTitle.setText("Video Liveness");
        }
        new CountDownTimer(1500, 1000)
        {
            public void onTick(long millisUntilFinished)
            {}
            public void onFinish()
            {
                if (intentTarget.equals("faceMatchID")){
                    Intent intent_liveness = new Intent(IntentActivity.this, FaceActivity.class);
                    intent_liveness.putExtra("faceTarget", "faceMatchToIDCard");
                    startActivity(intent_liveness);
                    finish();
                }else if (intentTarget.equals("POA")){
                    Intent intent = new Intent(IntentActivity.this, FaceActivity.class);
                    intent.putExtra("faceTarget", "SanctionPEP");
                    startActivity(intent);
                }


            }
        }.start();
    }
}