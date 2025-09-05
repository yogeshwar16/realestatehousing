package com.propertyapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Inquiries")
public class Inquiry {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inquiry_id")
    private Long inquiryId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;
    
    @Column(name = "inquiry_description", columnDefinition = "NVARCHAR(MAX)")
    private String inquiryDescription;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "inquiry_status")
    private InquiryStatus inquiryStatus = InquiryStatus.OPEN;
    
    @Size(max = 500, message = "Closing reason must not exceed 500 characters")
    @Column(name = "closing_reason")
    private String closingReason;
    
    @Column(name = "terms_accepted")
    private Boolean termsAccepted = false;
    
    @Column(name = "inquiry_date")
    private LocalDateTime inquiryDate;
    
    @NotNull
    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;
    
    @Column(name = "closed_date")
    private LocalDateTime closedDate;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public Inquiry() {}
    
    public Inquiry(Property property, User customer, User seller, String inquiryDescription) {
        this.property = property;
        this.customer = customer;
        this.seller = seller;
        this.inquiryDescription = inquiryDescription;
        this.inquiryDate = LocalDateTime.now();
        this.expiryDate = LocalDateTime.now().plusMonths(3); // 3 months expiry
    }
    
    // Getters and Setters
    public Long getInquiryId() { return inquiryId; }
    public void setInquiryId(Long inquiryId) { this.inquiryId = inquiryId; }
    
    public Property getProperty() { return property; }
    public void setProperty(Property property) { this.property = property; }
    
    public User getCustomer() { return customer; }
    public void setCustomer(User customer) { this.customer = customer; }
    
    public User getSeller() { return seller; }
    public void setSeller(User seller) { this.seller = seller; }
    
    public String getInquiryDescription() { return inquiryDescription; }
    public void setInquiryDescription(String inquiryDescription) { this.inquiryDescription = inquiryDescription; }
    
    public InquiryStatus getInquiryStatus() { return inquiryStatus; }
    public void setInquiryStatus(InquiryStatus inquiryStatus) { this.inquiryStatus = inquiryStatus; }
    
    public String getClosingReason() { return closingReason; }
    public void setClosingReason(String closingReason) { this.closingReason = closingReason; }
    
    public Boolean getTermsAccepted() { return termsAccepted; }
    public void setTermsAccepted(Boolean termsAccepted) { this.termsAccepted = termsAccepted; }
    
    public LocalDateTime getInquiryDate() { return inquiryDate; }
    public void setInquiryDate(LocalDateTime inquiryDate) { this.inquiryDate = inquiryDate; }
    
    public LocalDateTime getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDateTime expiryDate) { this.expiryDate = expiryDate; }
    
    public LocalDateTime getClosedDate() { return closedDate; }
    public void setClosedDate(LocalDateTime closedDate) { this.closedDate = closedDate; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
