package com.propertyapp.dto;

import jakarta.validation.constraints.NotNull;

public class InquiryRequest {
    
    @NotNull(message = "Property ID is required")
    private Long propertyId;
    
    private String inquiryDescription;
    
    @NotNull(message = "Terms acceptance is required")
    private Boolean termsAccepted;
    
    // Constructors
    public InquiryRequest() {}
    
    public InquiryRequest(Long propertyId, String inquiryDescription, Boolean termsAccepted) {
        this.propertyId = propertyId;
        this.inquiryDescription = inquiryDescription;
        this.termsAccepted = termsAccepted;
    }
    
    // Getters and Setters
    public Long getPropertyId() { return propertyId; }
    public void setPropertyId(Long propertyId) { this.propertyId = propertyId; }
    
    public String getInquiryDescription() { return inquiryDescription; }
    public void setInquiryDescription(String inquiryDescription) { this.inquiryDescription = inquiryDescription; }
    
    public Boolean getTermsAccepted() { return termsAccepted; }
    public void setTermsAccepted(Boolean termsAccepted) { this.termsAccepted = termsAccepted; }
}
