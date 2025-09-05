package com.propertyapp.android;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.propertyapp.android.api.ApiClient;
import com.propertyapp.android.api.ApiResponse;
import com.propertyapp.android.api.ApiService;
import com.propertyapp.android.api.InquiryRequest;
import com.propertyapp.android.model.Property;
import com.propertyapp.android.utils.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InquiryActivity extends AppCompatActivity {

    private TextView tvPropertyTitle, tvPropertyLocation;
    private TextInputEditText etInquiryDescription;
    private CheckBox cbAcceptTerms;
    private MaterialButton btnSubmitInquiry;
    private ProgressBar progressBar;
    private ImageView ivBack;

    private Property property;
    private ApiService apiService;
    private SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry);

        initViews();
        initData();
        setupClickListeners();
        populatePropertyInfo();
    }

    private void initViews() {
        tvPropertyTitle = findViewById(R.id.tvPropertyTitle);
        tvPropertyLocation = findViewById(R.id.tvPropertyLocation);
        etInquiryDescription = findViewById(R.id.etInquiryDescription);
        cbAcceptTerms = findViewById(R.id.cbAcceptTerms);
        btnSubmitInquiry = findViewById(R.id.btnSubmitInquiry);
        progressBar = findViewById(R.id.progressBar);
        ivBack = findViewById(R.id.ivBack);
    }

    private void initData() {
        apiService = ApiClient.getClient().create(ApiService.class);
        sharedPrefManager = new SharedPrefManager(this);

        // Get property from intent
        if (getIntent().hasExtra("property")) {
            property = (Property) getIntent().getSerializableExtra("property");
        }

        if (property == null) {
            Toast.makeText(this, "Property information not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupClickListeners() {
        ivBack.setOnClickListener(v -> finish());

        btnSubmitInquiry.setOnClickListener(v -> {
            if (validateInput()) {
                submitInquiry();
            }
        });

        cbAcceptTerms.setOnCheckedChangeListener((buttonView, isChecked) -> {
            btnSubmitInquiry.setEnabled(isChecked);
        });
    }

    private void populatePropertyInfo() {
        if (property != null) {
            tvPropertyTitle.setText(property.getTitle());
            tvPropertyLocation.setText(property.getCity() + ", " + property.getState());
        }
    }

    private boolean validateInput() {
        if (!cbAcceptTerms.isChecked()) {
            Toast.makeText(this, "Please accept terms and conditions", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void submitInquiry() {
        String description = etInquiryDescription.getText().toString().trim();
        if (description.isEmpty()) {
            description = "I am interested in this property. Please contact me.";
        }

        InquiryRequest request = new InquiryRequest(
            property.getId(),
            description,
            true
        );

        showLoading(true);

        Call<ApiResponse<Object>> call = apiService.createInquiry(
            "Bearer " + sharedPrefManager.getUser().getMobileNumber(),
            request
        );

        call.enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Object> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        Toast.makeText(InquiryActivity.this, 
                            "Inquiry submitted successfully!", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(InquiryActivity.this, 
                            apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(InquiryActivity.this, 
                        "Failed to submit inquiry", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                showLoading(false);
                Toast.makeText(InquiryActivity.this, 
                    "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnSubmitInquiry.setEnabled(!show && cbAcceptTerms.isChecked());
    }
}
