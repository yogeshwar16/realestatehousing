package com.propertyapp.android;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class TermsConditionsActivity extends AppCompatActivity {

    private ImageView ivBack;
    private MaterialButton btnAccept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        ivBack = findViewById(R.id.ivBack);
        btnAccept = findViewById(R.id.btnAccept);
    }

    private void setupClickListeners() {
        ivBack.setOnClickListener(v -> finish());

        btnAccept.setOnClickListener(v -> {
            // Set result and finish
            setResult(RESULT_OK);
            finish();
        });
    }
}
