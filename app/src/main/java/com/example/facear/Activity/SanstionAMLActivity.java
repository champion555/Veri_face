package com.example.facear.Activity;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.facear.BackgroundService.BackgroundService;
import com.example.facear.R;
import com.example.facear.Utils.Common;

public class SanstionAMLActivity extends AppCompatActivity {
    private EditText txtFirstName,txtLastName;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sanstion_aml);
        BackgroundService.mContext = SanstionAMLActivity.this;
        init();
        onSubmit();
    }
    private void init(){
        txtFirstName = findViewById(R.id.txtFirstName);
        txtLastName = findViewById(R.id.txtLastName);
        btnSubmit = findViewById(R.id.btnSubmit);
    }
    private void onSubmit(){
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = txtFirstName.getText().toString();
                String lastName = txtLastName.getText().toString();
                if (firstName.isEmpty()) {
                    Common.showAlert(SanstionAMLActivity.this, "Warning", "Please enter First Name.");
                    return;
                }
                if (lastName.isEmpty()) {
                    Common.showAlert(SanstionAMLActivity.this, "Warning", "Please enter Last Name.");
                    return;
                }

                // api call

            }
        });
    }
}