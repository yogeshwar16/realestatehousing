package com.propertyapp.android;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.propertyapp.android.api.ApiClient;
import com.propertyapp.android.api.ApiResponse;
import com.propertyapp.android.api.ApiService;
import com.propertyapp.android.api.LoginRequest;
import com.propertyapp.android.api.OTPRequest;
import com.propertyapp.android.model.User;
import com.propertyapp.android.utils.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etMobileNumber, etOTP;
    private MaterialButton btnSendOTP, btnLogin;
    private LinearLayout llOTPSection;
    private TextView tvTimer, tvResendOTP, tvSignup;
    private ProgressBar progressBar;
    private ApiService apiService;
    private CountDownTimer countDownTimer;
    private boolean isOTPSent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        setupClickListeners();
        apiService = ApiClient.getApiService();

        // Pre-fill mobile number if coming from signup
        String mobileNumber = getIntent().getStringExtra("mobile_number");
        if (!TextUtils.isEmpty(mobileNumber)) {
            etMobileNumber.setText(mobileNumber);
        }
    }

    private void initViews() {
        etMobileNumber = findViewById(R.id.etMobileNumber);
        etOTP = findViewById(R.id.etOTP);
        btnSendOTP = findViewById(R.id.btnSendOTP);
        btnLogin = findViewById(R.id.btnLogin);
        llOTPSection = findViewById(R.id.llOTPSection);
        tvTimer = findViewById(R.id.tvTimer);
        tvResendOTP = findViewById(R.id.tvResendOTP);
        tvSignup = findViewById(R.id.tvSignup);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupClickListeners() {
        btnSendOTP.setOnClickListener(v -> {
            if (validateMobileNumber()) {
                sendOTP();
            }
        });

        btnLogin.setOnClickListener(v -> {
            if (validateOTP()) {
                performLogin();
            }
        });

        tvResendOTP.setOnClickListener(v -> {
            if (validateMobileNumber()) {
                sendOTP();
            }
        });

        tvSignup.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            finish();
        });
    }

    private boolean validateMobileNumber() {
        String mobileNumber = etMobileNumber.getText().toString().trim();

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

        return true;
    }

    private boolean validateOTP() {
        String otp = etOTP.getText().toString().trim();

        if (TextUtils.isEmpty(otp) || otp.length() != 6) {
            etOTP.setError("Valid 6-digit OTP is required");
            etOTP.requestFocus();
            return false;
        }

        return true;
    }

    private void sendOTP() {
        showLoading(true);
        String mobileNumber = etMobileNumber.getText().toString().trim();

        OTPRequest request = new OTPRequest(mobileNumber);

        Call<ApiResponse<String>> call = apiService.sendOTP(request);
        call.enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<String> apiResponse = response.body();

                    if (apiResponse.isSuccess()) {
                        isOTPSent = true;
                        showOTPSection();
                        startTimer();
                        Toast.makeText(LoginActivity.this, "OTP sent successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Failed to send OTP. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                showLoading(false);
                Toast.makeText(LoginActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void performLogin() {
        showLoading(true);
        String mobileNumber = etMobileNumber.getText().toString().trim();
        String otp = etOTP.getText().toString().trim();

        LoginRequest request = new LoginRequest(mobileNumber, otp);

        Call<ApiResponse<User>> call = apiService.login(request);
        call.enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<User> apiResponse = response.body();

                    if (apiResponse.isSuccess()) {
                        User user = apiResponse.getData();

                        // Save user data to SharedPreferences
                        SharedPrefManager.getInstance(LoginActivity.this).saveUser(user);

                        Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();

                        // Navigate to Dashboard
                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                showLoading(false);
                Toast.makeText(LoginActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showOTPSection() {
        llOTPSection.setVisibility(View.VISIBLE);
        btnSendOTP.setText("OTP Sent");
        btnSendOTP.setEnabled(false);
        etMobileNumber.setEnabled(false);
    }

    private void startTimer() {
        tvResendOTP.setVisibility(View.GONE);
        tvTimer.setVisibility(View.VISIBLE);

        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTimer.setText("Resend OTP in " + millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                tvTimer.setVisibility(View.GONE);
                tvResendOTP.setVisibility(View.VISIBLE);
                btnSendOTP.setText("Send OTP");
                btnSendOTP.setEnabled(true);
                etMobileNumber.setEnabled(true);
            }
        }.start();
    }

    private void showLoading(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            btnSendOTP.setEnabled(false);
            btnLogin.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            if (!isOTPSent) {
                btnSendOTP.setEnabled(true);
            }
            btnLogin.setEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
