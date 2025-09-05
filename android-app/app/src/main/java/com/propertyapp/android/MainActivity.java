package com.propertyapp.android;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.propertyapp.android.utils.SharedPrefManager;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Show splash screen and then navigate
        new Handler().postDelayed(() -> {
            if (SharedPrefManager.getInstance(this).isLoggedIn()) {
                // User is logged in, go to dashboard
                startActivity(new Intent(MainActivity.this, DashboardActivity.class));
            } else {
                // User not logged in, go to signup
                startActivity(new Intent(MainActivity.this, SignupActivity.class));
            }
            finish();
        }, SPLASH_DELAY);
    }
}
