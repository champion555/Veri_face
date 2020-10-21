package com.example.facear.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.facear.Models.AuthenticationResponse;
import com.example.facear.Models.CheckMailResponse;
import com.example.facear.Models.SendMailResponse;
import com.example.facear.R;
import com.example.facear.Services.RetrofitClient;
import com.example.facear.Utils.AppConstants;
import com.example.facear.Utils.CoreApp;
import com.example.facear.Utils.LoadingDialog;
import com.example.facear.Utils.Preferences;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailVerificationActivity extends AppCompatActivity implements View.OnClickListener, OnOtpCompletionListener {

    private Button btnValidation;
    private TextView btnResend;
    private OtpView otpView;
    private TextView txtMail;
    private ImageView btnshow;
    Bundle bundle;
    String txtEmail,jobId;
    boolean isshow = false;
    boolean isFullCode = false;
    boolean isValidate = false;
    boolean isSendMail = false;
    LoadingDialog  loadingDialog;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);
        initializeUi();
        setListener();

        bundle = getIntent().getExtras();
        txtEmail = bundle.getString("txtEmail");
        jobId = bundle.getString("jobId");
        txtMail.setText(txtEmail);

    }
    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnValidation:
                isValidate = true;
                AuthenticationToServer();
                break;
            case R.id.btnReSend:
                isSendMail = true;
                AuthenticationToServer();
                break;
            case R.id.btnShow:
                if (isshow == false) {
                    btnshow.setImageResource(R.drawable.ic_show);
                    otpView.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                    isshow = true;
                } else {
                    btnshow.setImageResource(R.drawable.ic_hide);
                    otpView.setInputType(InputType.TYPE_CLASS_NUMBER);
                    isshow = false;
                }
                break;
        }
    }
    private void initializeUi() {
        otpView = findViewById(R.id.otp_view);
        btnValidation = findViewById(R.id.btnValidation);
        btnResend = findViewById(R.id.btnReSend);
        btnshow = findViewById(R.id.btnShow);
        txtMail = findViewById(R.id.txtEmail);
    }
    private void setListener(){
        btnValidation.setOnClickListener(this);
        btnResend.setOnClickListener(this);
        otpView.setOtpCompletionListener(this);
        btnshow.setOnClickListener(this);
    }
    @Override public void onOtpCompleted(String otp) {
        isFullCode = true;
    }
    private void AuthenticationToServer() {
        String api_key = Preferences.getValue_String(EmailVerificationActivity.this, Preferences.API_Key);
        String secret_key = Preferences.getValue_String(EmailVerificationActivity.this, Preferences.API_SecretKey);
        loadingDialog = new LoadingDialog(EmailVerificationActivity.this, false);
        if (CoreApp.isNetworkConnection(EmailVerificationActivity.this)) {
            Call<AuthenticationResponse> call = RetrofitClient
                    .getInstance().getApi().authenticateCheck(api_key, secret_key);
            call.enqueue(new Callback<AuthenticationResponse>() {
                @Override
                public void onResponse(Call<AuthenticationResponse> call, Response<AuthenticationResponse> response) {
                    AuthenticationResponse authenticationResponse = response.body();
                    int statuscode = Integer.parseInt(authenticationResponse.getStatusCode());
                    if (statuscode == 200) {
                        AppConstants.api_token = authenticationResponse.getApi_access_token();
                        if (isSendMail){
                            isSendMail = false;
                            sendMailToServer();
                        }
                        if (isValidate){
                            isValidate = false;
                            if (isFullCode){
                                validateMailToServer();
                            }else {
                                Toast.makeText(EmailVerificationActivity.this, "you must enter the code fully", Toast.LENGTH_LONG).show();
                            }
                        }

                    } else {
                        Toast.makeText(EmailVerificationActivity.this, response.message(), Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Call<AuthenticationResponse> call, Throwable t) {
                    Toast.makeText(EmailVerificationActivity.this, "Server is not working now", Toast.LENGTH_LONG).show();
                    loadingDialog.hide();
                }
            });
        } else {
            loadingDialog.hide();
            Toast.makeText(EmailVerificationActivity.this, "No internet connection available", Toast.LENGTH_LONG).show();
        }
    }

    private void validateMailToServer() {
        final String otpCode = otpView.getText().toString();
        Call<CheckMailResponse> call = RetrofitClient
                .getInstance().getApi().checkDemoEmail(jobId, otpCode);
        call.enqueue(new Callback<CheckMailResponse>() {
            @Override
            public void onResponse(Call<CheckMailResponse> call, Response<CheckMailResponse> response) {
                loadingDialog.hide();
                CheckMailResponse checkMailResponse = response.body();
                String status = checkMailResponse.getStatus();
                int statuscode = Integer.parseInt(checkMailResponse.getStatusCode());
                int otp = Integer.parseInt(checkMailResponse.getOtp());
                if (otp == Integer.parseInt(otpCode)) {
//                    Preferences.setValue(EmailVerificationActivity.this, Preferences.isSignup, false);
                    Intent intent = new Intent(EmailVerificationActivity.this,MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(EmailVerificationActivity.this, checkMailResponse.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<CheckMailResponse> call, Throwable t) {
                Toast.makeText(EmailVerificationActivity.this, "Server is not working now", Toast.LENGTH_LONG).show();
                loadingDialog.hide();
            }
        });
    }
    private void sendMailToServer() {
        String language = "en";
        Call<SendMailResponse> call = RetrofitClient
                .getInstance().getApi().sendDemoEmail(txtEmail, language);
        call.enqueue(new Callback<SendMailResponse>() {
            @Override
            public void onResponse(Call<SendMailResponse> call, Response<SendMailResponse> response) {
                loadingDialog.hide();
                SendMailResponse sendMailResponse = response.body();
                jobId = sendMailResponse.getJob_id();
                int statuscode = Integer.parseInt(sendMailResponse.getStatusCode());
                if (statuscode == 200) {
                    Toast.makeText(EmailVerificationActivity.this, "Please Check your email, We have already sent the code to your email", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(EmailVerificationActivity.this, response.message(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<SendMailResponse> call, Throwable t) {
                Toast.makeText(EmailVerificationActivity.this, "Server is not working now", Toast.LENGTH_LONG).show();
                loadingDialog.hide();
            }
        });
    }

}