package com.example.facear.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.facear.R;
import com.example.facear.Utils.AppConstants;
import com.example.facear.Utils.Preferences;


public class SplashActivity extends AppCompatActivity {
    private final  int splashTimer = 3000;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (Preferences.getValue_Boolean(SplashActivity.this, Preferences.isLogin, true)){
                if (Preferences.getValue_Boolean(SplashActivity.this, Preferences.isSaved, true)){
                    Preferences.setValue(SplashActivity.this, Preferences.isSaved, false);
                    Preferences.setValue(SplashActivity.this, Preferences.BASE_URL, AppConstants.BaseURL);
                    Preferences.setValue(SplashActivity.this, Preferences.API_Key, AppConstants.apiKey);
                    Preferences.setValue(SplashActivity.this, Preferences.API_SecretKey, AppConstants.secretKey);
                    Preferences.setValue(SplashActivity.this, Preferences.UserName, AppConstants.userName);
                    Preferences.setValue(SplashActivity.this, Preferences.user_session, AppConstants.userSession);
                }
                Intent intent = new Intent(SplashActivity.this, SigninActivity.class);
//                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
            }else{
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//                Intent intent = new Intent(SplashActivity.this, KYC_LoginActivity.class);
                startActivity(intent);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        handler.postAtTime(runnable,System.currentTimeMillis()+splashTimer);
        handler.postDelayed(runnable,splashTimer);
    }
}