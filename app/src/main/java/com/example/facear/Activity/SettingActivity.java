package com.example.facear.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.facear.R;
import com.example.facear.Utils.Preferences;

public class SettingActivity extends AppCompatActivity {
    private EditText txtBaseURL,txtAPIKey,txtSecretKey,txtUserName;
    private Button btSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        configureView();
        setEditView();
        onSave();
    }
    private void configureView(){
        txtBaseURL = findViewById(R.id.txtBaseURL);
        txtAPIKey = findViewById(R.id.txtApiKey);
        txtSecretKey = findViewById(R.id.txtSecretKey);
        txtUserName = findViewById(R.id.txtUserName);
        btSave = findViewById(R.id.btSave);
    }
    private void setEditView(){
        txtBaseURL.setText(Preferences.getValue_String(SettingActivity.this, Preferences.BASE_URL));
//        txtVideoDuration.setText(Preferences.getValue_String(SettingActivity.this, Preferences.Video_duration));
        txtAPIKey.setText(Preferences.getValue_String(SettingActivity.this, Preferences.API_Key));
        txtSecretKey.setText(Preferences.getValue_String(SettingActivity.this, Preferences.API_SecretKey));
        txtUserName.setText(Preferences.getValue_String(SettingActivity.this, Preferences.UserName));
    }
    private void onSave(){
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Preferences.setValue(SettingActivity.this, Preferences.isSaved, false);
                Preferences.setValue(SettingActivity.this, Preferences.BASE_URL, txtBaseURL.getText().toString());
                Preferences.setValue(SettingActivity.this, Preferences.API_Key, txtAPIKey.getText().toString());
                Preferences.setValue(SettingActivity.this, Preferences.API_SecretKey, txtSecretKey.getText().toString());
                Preferences.setValue(SettingActivity.this, Preferences.UserName, txtUserName.getText().toString());
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}