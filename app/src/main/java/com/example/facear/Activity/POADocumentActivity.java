package com.example.facear.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.facear.BackgroundService.BackgroundService;
import com.example.facear.R;
import com.example.facear.Utils.AppConstants;
import com.jdev.countryutil.Constants;
import com.jdev.countryutil.CountryUtil;

public class POADocumentActivity extends AppCompatActivity {
    private  LinearLayout btnPOADoc;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poa_document);
        BackgroundService.mContext = POADocumentActivity.this;
        btnPOADoc = findViewById(R.id.btnPOADoc);
        btnBack = findViewById(R.id.btnBack);
        btnPOADoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CountryUtil(POADocumentActivity.this).setTitle("Select issuing Country").build();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(POADocumentActivity.this, IDDocMainActivity.class);
                intent.putExtra("Target","SanctionsPEP");
                startActivity(intent);
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
                AppConstants.countryName = countryName;
                Intent intent = new Intent(POADocumentActivity.this, POACameraActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}