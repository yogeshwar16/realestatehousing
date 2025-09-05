package com.propertyapp.android.api;

public class OTPRequest {
    private String mobileNumber;

    public OTPRequest() {}

    public OTPRequest(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    // Getters and Setters
    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }
}
