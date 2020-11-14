package com.example.facear.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.facear.FaceActivity;
import com.example.facear.Models.AuthenticationResponse;
import com.example.facear.Models.CheckMailResponse;
import com.example.facear.Models.SendMailResponse;
import com.example.facear.Models.SignUpResponse;
import com.example.facear.R;
import com.example.facear.Services.RetrofitClient;
import com.example.facear.Utils.AppConstants;
import com.example.facear.Utils.Common;
import com.example.facear.Utils.CoreApp;
import com.example.facear.Utils.LoadingDialog;
import com.example.facear.Utils.Preferences;
import com.jdev.countryutil.Constants;
import com.jdev.countryutil.CountryUtil;
import com.rilixtech.widget.countrycodepicker.Country;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAccountActivity extends AppCompatActivity {
    LoadingDialog loadingDialog;
    CountryCodePicker ccp;
    EditText txtFistName,txtLastName,txtEmail,txtPhoneNumber,txtJobTitle,txtPassword,txtConfirmPassword,txtCompanyName;
    RelativeLayout countryRel;
    ImageView imgFlag,checkBox;
    TextView txtCountryName;
    Spinner spinner;
    String names[] = {"C","C++","Java","Php","javascript","c#"};
    ArrayAdapter<String> arrayAdapter;
    Button btnContinue;
    String countrycode;
    String jobId;
//    int unitedFlag = 2131165792;
    String firstName, lastName,email,phoneNumber,companyName,industryType,countryName,jobTitle,confirmedpassword;
    boolean isChecked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        init();
        onContinue();
        onSelectCountry();
        onCountryPickerClick();
        btnContinue.setBackgroundResource(R.drawable.disable_round_button);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isChecked == false){
                    checkBox.setImageResource(R.drawable.ic_checked);
                    btnContinue.setBackgroundResource(R.drawable.round_button);
                    isChecked = true;
                }else {
                    checkBox.setImageResource(R.drawable.ic_uncheck);
                    btnContinue.setBackgroundResource(R.drawable.disable_round_button);
                    isChecked = false;
                }
            }
        });
    }
    private void init(){
        txtFistName = findViewById(R.id.txtFirstName);
        txtLastName = findViewById(R.id.txtLastName);
        txtEmail = findViewById(R.id.txtMail);
        txtPhoneNumber = findViewById(R.id.txtPhoneNum);
        txtJobTitle = findViewById(R.id.txtJobTitle);
        txtCompanyName = findViewById(R.id.txtCompanyName);
        countryRel = findViewById(R.id.countryView);
        imgFlag = findViewById(R.id.imgFlag);
        txtCountryName = findViewById(R.id.txtCountryName);
        txtPassword = findViewById(R.id.txtPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
        btnContinue = findViewById(R.id.btnNext);
        checkBox = findViewById(R.id.checkbox);
        ccp = findViewById(R.id.ccp);
        ccp.registerPhoneNumberTextView(txtPhoneNumber);
        countrycode = ccp.getSelectedCountryCodeWithPlus();
        spinner = findViewById(R.id.txtIndustryType);
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,names);
        spinner.setAdapter(arrayAdapter);
//        imgFlag.setImageResource(unitedFlag);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),"selection is "+ names[position],Toast.LENGTH_SHORT);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void onSelectCountry(){
        countryRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CountryUtil(CreateAccountActivity.this).setTitle("Select the country").build();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.KEY_RESULT_CODE) {
            try {
                String countryName = data.getStringExtra(Constants.KEY_COUNTRY_NAME);
                int countryFlag = data.getIntExtra(Constants.KEY_COUNTRY_FLAG, 0);
                imgFlag.setImageResource(countryFlag);
                txtCountryName.setText(countryName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void onCountryPickerClick(){
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                countrycode = ccp.getSelectedCountryCodeWithPlus();
            }
        });
    }
    private void onContinue(){
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View var1) {
                firstName = txtFistName.getText().toString();
                lastName = txtLastName.getText().toString();
                email = txtEmail.getText().toString();
                phoneNumber = countrycode+txtPhoneNumber.getText().toString();
                companyName = txtCompanyName.getText().toString();
                industryType = spinner.getSelectedItem().toString();
                countryName = txtCountryName.getText().toString();
                jobTitle = txtJobTitle.getText().toString();
                String password = txtPassword.getText().toString();
                String confirmPassword = txtConfirmPassword.getText().toString();

                if (firstName.isEmpty()) {
                    Common.showAlert(CreateAccountActivity.this, "Warning", "Please enter First Name.");
                    return;
                }
                if (lastName.isEmpty()) {
                    Common.showAlert(CreateAccountActivity.this, "Warning", "Please enter Last Name.");
                    return;
                }
                if (email.isEmpty()) {
                    Common.showAlert(CreateAccountActivity.this, "Warning", "Please enter Email Address.");
                    return;
                }
                if(!Common.isValidEmail(email)) {
                    Common.showAlert(CreateAccountActivity.this, "Warning", "Invalid Email");
                    return;
                }
                if (phoneNumber.isEmpty()) {
                    Common.showAlert(CreateAccountActivity.this, "Warning", "Please enter Phone Number.");
                    return;
                }
                if (companyName.isEmpty()) {
                    Common.showAlert(CreateAccountActivity.this, "Warning", "Please enter Company Name.");
                    return;
                }
                if (industryType.isEmpty()) {
                    Common.showAlert(CreateAccountActivity.this, "Warning", "Please Select Industy Type");
                    return;
                }
                if (countryName.isEmpty()) {
                    Common.showAlert(CreateAccountActivity.this, "Warning", "Please Select Country.");
                    return;
                }
                if (jobTitle.isEmpty()) {
                    Common.showAlert(CreateAccountActivity.this, "Warning", "Please Enter Job Title.");
                    return;
                }
                if (password.isEmpty()) {
                    Common.showAlert(CreateAccountActivity.this, "Warning", "Please Enter Password.");
                    return;
                }
                if (password.length() < 6){
                    Common.showAlert(CreateAccountActivity.this, "Warning", "Password must more than 6 letters");
                    return;
                }
                if (confirmPassword.isEmpty()) {
                    Common.showAlert(CreateAccountActivity.this, "Warning", "Please Confirm the Password");
                    return;
                }
                String[] parts = email.split("\\@"); // escape .
                String part1 = parts[1];
                String[] otherparts = part1.split("\\."); // escape .
                String otherpart2 = otherparts[0];
                if (!companyName.equals(otherpart2)){
                    Common.showAlert(CreateAccountActivity.this, "Warning", "Please enter Work Business Email, ex: username@companyname.com ");
                    return;
                }
                if (!isChecked) {
                    Common.showAlert(CreateAccountActivity.this, "Warning", "Please Accept General Condition and Policy");
                    return;
                }
                if (password.equals(confirmPassword)){
                    confirmedpassword = confirmPassword;
                    AuthenticationToServer();
                }

            }
        });
    }
    private void AuthenticationToServer() {
        String api_key = Preferences.getValue_String(CreateAccountActivity.this, Preferences.API_Key);
        String secret_key = Preferences.getValue_String(CreateAccountActivity.this, Preferences.API_SecretKey);
        loadingDialog = new LoadingDialog(CreateAccountActivity.this, false);
        if (CoreApp.isNetworkConnection(CreateAccountActivity.this)) {
            Call<AuthenticationResponse> call = RetrofitClient
                    .getInstance().getApi().authenticateCheck(api_key, secret_key);
            call.enqueue(new Callback<AuthenticationResponse>() {
                @Override
                public void onResponse(Call<AuthenticationResponse> call, Response<AuthenticationResponse> response) {
                    AuthenticationResponse authenticationResponse = response.body();
                    int statuscode = Integer.parseInt(authenticationResponse.getStatusCode());
                    if (statuscode == 200) {
                        AppConstants.api_token = authenticationResponse.getApi_access_token();
                        RegistrationToServer();
                    } else {
                        Toast.makeText(CreateAccountActivity.this, response.message(), Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Call<AuthenticationResponse> call, Throwable t) {
                    Toast.makeText(CreateAccountActivity.this, "Server is not working now", Toast.LENGTH_LONG).show();
                    loadingDialog.hide();
                }
            });
        } else {
            loadingDialog.hide();
            Toast.makeText(CreateAccountActivity.this, "No internet connection available", Toast.LENGTH_LONG).show();
        }
    }
    private void RegistrationToServer() {
        Call<SignUpResponse> call = RetrofitClient
                .getInstance().getApi().signUp(firstName, lastName,email,phoneNumber,companyName,industryType,countryName,jobTitle,confirmedpassword);
        call.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                SignUpResponse signUpResponse = response.body();
                int statusCode = Integer.parseInt(signUpResponse.getStatusCode());
                if (statusCode == 200) {
                    sendMailToServer();
                } else {
                    Toast.makeText(CreateAccountActivity.this, signUpResponse.getMessage(), Toast.LENGTH_LONG).show();
                    loadingDialog.hide();
                }
            }
            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                Toast.makeText(CreateAccountActivity.this, "Server is not working now", Toast.LENGTH_LONG).show();
                loadingDialog.hide();
            }
        });
    }
    private void sendMailToServer() {
        String language = "en";
        Call<SendMailResponse> call = RetrofitClient
                .getInstance().getApi().sendDemoEmail(email, language);
        call.enqueue(new Callback<SendMailResponse>() {
            @Override
            public void onResponse(Call<SendMailResponse> call, Response<SendMailResponse> response) {
                loadingDialog.hide();
                SendMailResponse sendMailResponse = response.body();
                jobId = sendMailResponse.getJob_id();
                int statuscode = Integer.parseInt(sendMailResponse.getStatusCode());
                if (statuscode == 200) {
                    Intent intent = new Intent(CreateAccountActivity.this,EmailVerificationActivity.class);
                    intent.putExtra("txtEmail", email);
                    intent.putExtra("jobId",jobId);
                    startActivity(intent);
                } else {
                    Toast.makeText(CreateAccountActivity.this, response.message(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<SendMailResponse> call, Throwable t) {
                Toast.makeText(CreateAccountActivity.this, "email or password is wrong, please try again.", Toast.LENGTH_LONG).show();
                loadingDialog.hide();
            }
        });
    }

}