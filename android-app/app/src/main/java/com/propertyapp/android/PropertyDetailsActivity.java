package com.propertyapp.android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.propertyapp.android.model.Property;
import com.propertyapp.android.model.UserType;
import com.propertyapp.android.utils.SharedPrefManager;

import java.text.NumberFormat;
import java.util.Locale;

public class PropertyDetailsActivity extends AppCompatActivity {

    private ImageView ivBack, ivPropertyImage;
    private TextView tvPropertyTitle, tvPropertyPrice, tvPropertyType, tvPropertySize;
    private TextView tvPropertyLocation, tvPropertyDescription;
    private TextView tvSellerName, tvSellerContact;
    private MaterialButton btnInquire;

    private Property property;
    private SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_details);

        initViews();
        initData();
        setupClickListeners();
        populatePropertyDetails();
    }

    private void initViews() {
        ivBack = findViewById(R.id.ivBack);
        ivPropertyImage = findViewById(R.id.ivPropertyImage);
        tvPropertyTitle = findViewById(R.id.tvPropertyTitle);
        tvPropertyPrice = findViewById(R.id.tvPropertyPrice);
        tvPropertyType = findViewById(R.id.tvPropertyType);
        tvPropertySize = findViewById(R.id.tvPropertySize);
        tvPropertyLocation = findViewById(R.id.tvPropertyLocation);
        tvPropertyDescription = findViewById(R.id.tvPropertyDescription);
        tvSellerName = findViewById(R.id.tvSellerName);
        tvSellerContact = findViewById(R.id.tvSellerContact);
        btnInquire = findViewById(R.id.btnInquire);
    }

    private void initData() {
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

        btnInquire.setOnClickListener(v -> {
            // Check if user is logged in
            if (!sharedPrefManager.isLoggedIn()) {
                Toast.makeText(this, "Please login to inquire", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if user is a customer
            if (sharedPrefManager.getUser().getUserType() != UserType.CUSTOMER) {
                Toast.makeText(this, "Only customers can make inquiries", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if user is trying to inquire about their own property
            if (property.getSeller() != null && 
                property.getSeller().getMobileNumber().equals(sharedPrefManager.getUser().getMobileNumber())) {
                Toast.makeText(this, "You cannot inquire about your own property", Toast.LENGTH_SHORT).show();
                return;
            }

            // Navigate to inquiry screen
            Intent intent = new Intent(PropertyDetailsActivity.this, InquiryActivity.class);
            intent.putExtra("property", property);
            startActivity(intent);
        });
    }

    private void populatePropertyDetails() {
        if (property == null) return;

        // Property basic info
        tvPropertyTitle.setText(property.getTitle());
        
        // Format price in Indian currency
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        tvPropertyPrice.setText(formatter.format(property.getPrice()));
        
        tvPropertyType.setText(property.getPropertyType().toString().replace("_", " "));
        tvPropertySize.setText(property.getPropertySize() + " sq ft");
        
        // Location
        String location = property.getCity() + ", " + property.getState();
        if (property.getPincode() != null && !property.getPincode().isEmpty()) {
            location += " - " + property.getPincode();
        }
        tvPropertyLocation.setText(location);
        
        // Description
        if (property.getDescription() != null && !property.getDescription().isEmpty()) {
            tvPropertyDescription.setText(property.getDescription());
        } else {
            tvPropertyDescription.setText("No description available");
        }

        // Seller info
        if (property.getSeller() != null) {
            tvSellerName.setText(property.getSeller().getFullName());
            tvSellerContact.setText(property.getSeller().getMobileNumber());
        } else {
            tvSellerName.setText("Information not available");
            tvSellerContact.setText("Contact via inquiry");
        }

        // Load property image
        if (property.getPropertyImages() != null && !property.getPropertyImages().isEmpty()) {
            Glide.with(this)
                .load(property.getPropertyImages())
                .placeholder(R.drawable.ic_home)
                .error(R.drawable.ic_home)
                .into(ivPropertyImage);
        }

        // Hide inquire button if user is seller or not logged in
        if (!sharedPrefManager.isLoggedIn() || 
            sharedPrefManager.getUser().getUserType() == UserType.SELLER) {
            btnInquire.setText("Login as Customer to Inquire");
            btnInquire.setEnabled(false);
        }
    }
}
