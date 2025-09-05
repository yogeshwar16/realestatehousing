package com.propertyapp.service;

import com.propertyapp.dto.InquiryRequest;
import com.propertyapp.model.Inquiry;
import com.propertyapp.model.InquiryStatus;
import com.propertyapp.model.Property;
import com.propertyapp.model.User;
import com.propertyapp.repository.InquiryRepository;
import com.propertyapp.repository.PropertyRepository;
import com.propertyapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InquiryService {
    
    @Autowired
    private InquiryRepository inquiryRepository;
    
    @Autowired
    private PropertyRepository propertyRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private SMSService smsService;
    
    public Inquiry createInquiry(Long customerId, InquiryRequest inquiryRequest) {
        Optional<User> customer = userRepository.findById(customerId);
        if (!customer.isPresent()) {
            throw new RuntimeException("Customer not found with ID: " + customerId);
        }
        
        if (!customer.get().getUserType().equals(com.propertyapp.model.UserType.CUSTOMER)) {
            throw new RuntimeException("Only customers can create inquiries");
        }
        
        Optional<Property> property = propertyRepository.findById(inquiryRequest.getPropertyId());
        if (!property.isPresent()) {
            throw new RuntimeException("Property not found with ID: " + inquiryRequest.getPropertyId());
        }
        
        if (!inquiryRequest.getTermsAccepted()) {
            throw new RuntimeException("Terms and conditions must be accepted to create an inquiry");
        }
        
        Inquiry inquiry = new Inquiry();
        inquiry.setProperty(property.get());
        inquiry.setCustomer(customer.get());
        inquiry.setSeller(property.get().getSeller());
        inquiry.setInquiryDescription(inquiryRequest.getInquiryDescription());
        inquiry.setTermsAccepted(inquiryRequest.getTermsAccepted());
        inquiry.setInquiryDate(LocalDateTime.now());
        inquiry.setExpiryDate(LocalDateTime.now().plusMonths(3)); // 3 months expiry
        inquiry.setInquiryStatus(InquiryStatus.OPEN);
        
        Inquiry savedInquiry = inquiryRepository.save(inquiry);
        
        // Send notification to seller
        try {
            String message = "New inquiry received for your property: " + property.get().getTitle() + 
                           ". Customer: " + customer.get().getFullName() + 
                           " (" + customer.get().getMobileNumber() + ")";
            smsService.sendNotification(property.get().getSeller().getMobileNumber(), message);
        } catch (Exception e) {
            System.err.println("Failed to send notification to seller: " + e.getMessage());
        }
        
        return savedInquiry;
    }
    
    public List<Inquiry> getInquiriesByCustomer(Long customerId) {
        Optional<User> customer = userRepository.findById(customerId);
        if (!customer.isPresent()) {
            throw new RuntimeException("Customer not found with ID: " + customerId);
        }
        return inquiryRepository.findByCustomerOrderByCreatedAtDesc(customer.get());
    }
    
    public List<Inquiry> getInquiriesBySeller(Long sellerId) {
        Optional<User> seller = userRepository.findById(sellerId);
        if (!seller.isPresent()) {
            throw new RuntimeException("Seller not found with ID: " + sellerId);
        }
        return inquiryRepository.findBySellerOrderByCreatedAtDesc(seller.get());
    }
    
    public List<Inquiry> getInquiriesByProperty(Long propertyId) {
        Optional<Property> property = propertyRepository.findById(propertyId);
        if (!property.isPresent()) {
            throw new RuntimeException("Property not found with ID: " + propertyId);
        }
        return inquiryRepository.findByPropertyOrderByCreatedAtDesc(property.get());
    }
    
    public Inquiry updateInquiryStatus(Long inquiryId, Long sellerId, InquiryStatus status, String closingReason) {
        Optional<Inquiry> inquiry = inquiryRepository.findById(inquiryId);
        if (!inquiry.isPresent()) {
            throw new RuntimeException("Inquiry not found with ID: " + inquiryId);
        }
        
        if (!inquiry.get().getSeller().getUserId().equals(sellerId)) {
            throw new RuntimeException("Only the seller can update inquiry status");
        }
        
        inquiry.get().setInquiryStatus(status);
        if (status == InquiryStatus.CLOSED) {
            inquiry.get().setClosedDate(LocalDateTime.now());
            inquiry.get().setClosingReason(closingReason);
        }
        
        Inquiry updatedInquiry = inquiryRepository.save(inquiry.get());
        
        // Send notification to customer
        try {
            String message = "Your inquiry for property '" + inquiry.get().getProperty().getTitle() + 
                           "' has been " + status.toString().toLowerCase() + ".";
            if (closingReason != null && !closingReason.isEmpty()) {
                message += " Reason: " + closingReason;
            }
            smsService.sendNotification(inquiry.get().getCustomer().getMobileNumber(), message);
        } catch (Exception e) {
            System.err.println("Failed to send notification to customer: " + e.getMessage());
        }
        
        return updatedInquiry;
    }
    
    public void expireOldInquiries() {
        List<Inquiry> expiredInquiries = inquiryRepository.findExpiredInquiries(LocalDateTime.now());
        for (Inquiry inquiry : expiredInquiries) {
            inquiry.setInquiryStatus(InquiryStatus.EXPIRED);
            inquiryRepository.save(inquiry);
        }
    }
    
    public Long getOpenInquiriesCountBySeller(Long sellerId) {
        Optional<User> seller = userRepository.findById(sellerId);
        if (!seller.isPresent()) {
            throw new RuntimeException("Seller not found with ID: " + sellerId);
        }
        return inquiryRepository.countOpenInquiriesBySeller(seller.get());
    }
    
    public Long getInquiriesCountByCustomer(Long customerId) {
        Optional<User> customer = userRepository.findById(customerId);
        if (!customer.isPresent()) {
            throw new RuntimeException("Customer not found with ID: " + customerId);
        }
        return inquiryRepository.countInquiriesByCustomer(customer.get());
    }
}
