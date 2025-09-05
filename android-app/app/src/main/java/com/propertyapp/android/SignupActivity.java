package com.propertyapp.android;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.propertyapp.android.api.ApiClient;
import com.propertyapp.android.api.ApiResponse;
import com.propertyapp.android.api.ApiService;
import com.propertyapp.android.api.SignupRequest;
import com.propertyapp.android.model.User;
import com.propertyapp.android.model.UserType;
import com.propertyapp.android.utils.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    private TextInputEditText etFullName, etMobileNumber, etEmail, etAadhaarNumber, etPanCard, etAddress;
    private RadioGroup rgUserType;
    private RadioButton rbSeller, rbCustomer;
    private CheckBox cbTerms;
    private MaterialButton btnSignup;
    private ProgressBar progressBar;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initViews();
        setupClickListeners();
        apiService = ApiClient.getApiService();
    }

    private void initViews() {
        etFullName = findViewById(R.id.etFullName);
        etMobileNumber = findViewById(R.id.etMobileNumber);
        etEmail = findViewById(R.id.etEmail);
        etAadhaarNumber = findViewById(R.id.etAadhaarNumber);
        etPanCard = findViewById(R.id.etPanCard);
        etAddress = findViewById(R.id.etAddress);
        rgUserType = findViewById(R.id.rgUserType);
        rbSeller = findViewById(R.id.rbSeller);
        rbCustomer = findViewById(R.id.rbCustomer);
        cbTerms = findViewById(R.id.cbTerms);
        btnSignup = findViewById(R.id.btnSignup);
        progressBar = findViewById(R.id.progressBar);

        // Set default selection to Customer
        rbCustomer.setChecked(true);
    }

    private void setupClickListeners() {
        btnSignup.setOnClickListener(v -> {
            if (validateInputs()) {
                performSignup();
            }
        });

        findViewById(R.id.tvLogin).setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
        });
    }

    private boolean validateInputs() {
        String fullName = etFullName.getText().toString().trim();
        String mobileNumber = etMobileNumber.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String aadhaarNumber = etAadhaarNumber.getText().toString().trim();
        String panCard = etPanCard.getText().toString().trim();

        if (TextUtils.isEmpty(fullName)) {
            etFullName.setError("Full name is required");
            etFullName.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(mobileNumber) || mobileNumber.length() != 10) {
            etMobileNumber.setError("Valid 10-digit mobile number is required");
            etMobileNumber.requestFocus();
            return false;
        }

        if (!mobileNumber.matches("^[6-9]\\d{9}$")) {
            etMobileNumber.setError("Invalid mobile number format");
            etMobileNumber.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Valid email address is required");
            etEmail.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(aadhaarNumber) || aadhaarNumber.length() != 12) {
            etAadhaarNumber.setError("Valid 12-digit Aadhaar number is required");
            etAadhaarNumber.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(panCard) || !panCard.matches("^[A-Z]{5}[0-9]{4}[A-Z]{1}$")) {
            etPanCard.setError("Valid PAN card number is required (e.g., ABCDE1234F)");
            etPanCard.requestFocus();
            return false;
        }

        if (rgUserType.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select user type", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!cbTerms.isChecked()) {
            Toast.makeText(this, "Please accept terms and conditions", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void performSignup() {
        showLoading(true);

        String fullName = etFullName.getText().toString().trim();
        String mobileNumber = etMobileNumber.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String aadhaarNumber = etAadhaarNumber.getText().toString().trim();
        String panCard = etPanCard.getText().toString().trim().toUpperCase();
        String address = etAddress.getText().toString().trim();

        UserType userType = rbSeller.isChecked() ? UserType.SELLER : UserType.CUSTOMER;

        SignupRequest request = new SignupRequest(fullName, mobileNumber, email, aadhaarNumber, panCard, userType);
        if (!TextUtils.isEmpty(address)) {
            request.setAddress(address);
        }

        Call<ApiResponse<User>> call = apiService.signup(request);
        call.enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                showLoading(false);
                
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<User> apiResponse = response.body();
                    
                    if (apiResponse.isSuccess()) {
                        User user = apiResponse.getData();
                        
                        // Save user data to SharedPreferences
                        SharedPrefManager.getInstance(SignupActivity.this).saveUser(user);
                        
                        Toast.makeText(SignupActivity.this, "Signup successful!", Toast.LENGTH_SHORT).show();
                        
                        // Navigate to Login screen
                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                        intent.putExtra("mobile_number", mobileNumber);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SignupActivity.this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(SignupActivity.this, "Signup failed. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                showLoading(false);
                Toast.makeText(SignupActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showLoading(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            btnSignup.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            btnSignup.setEnabled(true);
        }
    }
}
