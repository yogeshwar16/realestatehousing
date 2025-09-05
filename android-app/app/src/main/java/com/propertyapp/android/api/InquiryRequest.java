package com.propertyapp.android.api;

public class InquiryRequest {
    private Long propertyId;
    private String inquiryDescription;
    private Boolean termsAccepted;

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
