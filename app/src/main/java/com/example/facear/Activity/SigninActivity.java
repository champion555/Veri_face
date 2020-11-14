package com.example.facear.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.facear.BackgroundService.BackgroundService;
import com.example.facear.Models.AuthenticationResponse;
import com.example.facear.Models.LoginResponse;
import com.example.facear.Models.SendMailResponse;
import com.example.facear.R;
import com.example.facear.Services.RetrofitClient;
import com.example.facear.Utils.AppConstants;
import com.example.facear.Utils.Common;
import com.example.facear.Utils.CoreApp;
import com.example.facear.Utils.LoadingDialog;
import com.example.facear.Utils.Preferences;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SigninActivity extends AppCompatActivity {
    private EditText txtEmail,txtPassword;
    private Button btnSignin,btnCreate;
    LoadingDialog loadingDialog;
    String email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sigin);
//        Intent intent = new Intent(SigninActivity.this,MainActivity.class);
//        startActivity(intent);
        init();
        onSignin();
        onCreateAccount();
    }
    private void init(){
        txtEmail = findViewById(R.id.txtMail);
        txtPassword = findViewById(R.id.txtPassword);
        btnSignin  = findViewById(R.id.btnSignin);
        btnCreate = findViewById(R.id.btnCreate);
    }
    private void onSignin(){
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = txtEmail.getText().toString();
                password = txtPassword.getText().toString();
                if (email.isEmpty()) {
                    Common.showAlert(SigninActivity.this, "Warning", "Please enter Email.");
                    return;
                }
                if(!Common.isValidEmail(email)) {
                    Common.showAlert(SigninActivity.this, "Warning", "Invalid Email");
                    return;
                }
                if (password.isEmpty()) {
                    Common.showAlert(SigninActivity.this, "Warning", "Please enter Password.");
                    return;
                }
                if (password.length() < 6){
                    Common.showAlert(SigninActivity.this, "Warning", "Password must more than 6 letters");
                    return;
                }
                AuthenticationToServer();
            }
        });
    }
    private void onCreateAccount(){
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SigninActivity.this,CreateAccountActivity.class);
                startActivity(intent);
            }
        });
    }
    private void AuthenticationToServer() {
        String api_key = Preferences.getValue_String(SigninActivity.this, Preferences.API_Key);
        String secret_key = Preferences.getValue_String(SigninActivity.this, Preferences.API_SecretKey);
        loadingDialog = new LoadingDialog(SigninActivity.this, false);
        if (CoreApp.isNetworkConnection(SigninActivity.this)) {
            Call<AuthenticationResponse> call = RetrofitClient
                    .getInstance().getApi().authenticateCheck(api_key, secret_key);
            call.enqueue(new Callback<AuthenticationResponse>() {
                @Override
                public void onResponse(Call<AuthenticationResponse> call, Response<AuthenticationResponse> response) {
                    AuthenticationResponse authenticationResponse = response.body();
                    int statuscode = Integer.parseInt(authenticationResponse.getStatusCode());
                    if (statuscode == 200) {
                        AppConstants.api_token = authenticationResponse.getApi_access_token();
                        LoginToServer();
                    } else {
                        Toast.makeText(SigninActivity.this, response.message(), Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Call<AuthenticationResponse> call, Throwable t) {
                    Toast.makeText(SigninActivity.this, "Server is not working now", Toast.LENGTH_LONG).show();
                    loadingDialog.hide();
                }
            });
        } else {
            loadingDialog.hide();
            Toast.makeText(SigninActivity.this, "No internet connection available", Toast.LENGTH_LONG).show();
        }
    }
    private void LoginToServer() {
        Call<LoginResponse> call = RetrofitClient
                .getInstance().getApi().login(email, password);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                loadingDialog.hide();
                LoginResponse loginResponse = response.body();
                int statuscode = Integer.parseInt(loginResponse.getStatusCode());
                String status = loginResponse.getStatus();
                if (status.equals("SUCCESS")) {
                    Preferences.setValue(SigninActivity.this, Preferences.isLogin, false);
                    Intent intent = new Intent(SigninActivity.this,MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(SigninActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(SigninActivity.this, "Server is not working now", Toast.LENGTH_LONG).show();
                loadingDialog.hide();
            }
        });
    }
}