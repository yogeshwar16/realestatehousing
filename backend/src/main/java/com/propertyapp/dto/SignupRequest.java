package com.propertyapp.dto;

import com.propertyapp.model.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class SignupRequest {
    
    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name must not exceed 100 characters")
    private String fullName;
    
    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid mobile number format")
    private String mobileNumber;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;
    
    @NotBlank(message = "Aadhaar number is required")
    @Pattern(regexp = "^\\d{12}$", message = "Aadhaar number must be 12 digits")
    private String aadhaarNumber;
    
    @NotBlank(message = "PAN card is required")
    @Pattern(regexp = "^[A-Z]{5}[0-9]{4}[A-Z]{1}$", message = "Invalid PAN card format")
    private String panCard;
    
    @NotNull(message = "User type is required")
    private UserType userType;
    
    @Size(max = 500, message = "Address must not exceed 500 characters")
    private String address;
    
    // Constructors
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
