package com.propertyapp.android.api;

import com.propertyapp.android.model.UserType;

public class SignupRequest {
    private String fullName;
    private String mobileNumber;
    private String email;
    private String aadhaarNumber;
    private String panCard;
    private UserType userType;
    private String address;

    public SignupRequest() {}

    public SignupRequest(String fullName, String mobileNumber, String email, 
                        String aadhaarNumber, String panCard, UserType userType) {
        this.fullName = fullName;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.aadhaarNumber = aadhaarNumber;
        this.panCard = panCard;
        this.userType = userType;
    }

    // Getters and Setters
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAadhaarNumber() { return aadhaarNumber; }
    public void setAadhaarNumber(String aadhaarNumber) { this.aadhaarNumber = aadhaarNumber; }

    public String getPanCard() { return panCard; }
    public void setPanCard(String panCard) { this.panCard = panCard; }

    public UserType getUserType() { return userType; }
    public void setUserType(UserType userType) { this.userType = userType; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
