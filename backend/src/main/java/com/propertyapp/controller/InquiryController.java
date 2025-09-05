package com.propertyapp.controller;

import com.propertyapp.dto.ApiResponse;
import com.propertyapp.dto.InquiryRequest;
import com.propertyapp.model.Inquiry;
import com.propertyapp.model.InquiryStatus;
import com.propertyapp.service.InquiryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inquiries")
@CrossOrigin(origins = "*")
public class InquiryController {
    
    @Autowired
    private InquiryService inquiryService;
    
    @PostMapping("/create/{customerId}")
    public ResponseEntity<ApiResponse<Inquiry>> createInquiry(
            @PathVariable Long customerId,
            @Valid @RequestBody InquiryRequest inquiryRequest) {
        try {
            Inquiry inquiry = inquiryService.createInquiry(customerId, inquiryRequest);
            return ResponseEntity.ok(ApiResponse.success("Inquiry created successfully", inquiry));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to create inquiry: " + e.getMessage()));
        }
    }
    
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<Inquiry>>> getInquiriesByCustomer(@PathVariable Long customerId) {
        try {
            List<Inquiry> inquiries = inquiryService.getInquiriesByCustomer(customerId);
            return ResponseEntity.ok(ApiResponse.success("Customer inquiries retrieved successfully", inquiries));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve customer inquiries: " + e.getMessage()));
        }
    }
    
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<ApiResponse<List<Inquiry>>> getInquiriesBySeller(@PathVariable Long sellerId) {
        try {
            List<Inquiry> inquiries = inquiryService.getInquiriesBySeller(sellerId);
            return ResponseEntity.ok(ApiResponse.success("Seller inquiries retrieved successfully", inquiries));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve seller inquiries: " + e.getMessage()));
        }
    }
    
    @GetMapping("/property/{propertyId}")
    public ResponseEntity<ApiResponse<List<Inquiry>>> getInquiriesByProperty(@PathVariable Long propertyId) {
        try {
            List<Inquiry> inquiries = inquiryService.getInquiriesByProperty(propertyId);
            return ResponseEntity.ok(ApiResponse.success("Property inquiries retrieved successfully", inquiries));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve property inquiries: " + e.getMessage()));
        }
    }
    
    @PutMapping("/update-status/{inquiryId}/{sellerId}")
    public ResponseEntity<ApiResponse<Inquiry>> updateInquiryStatus(
            @PathVariable Long inquiryId,
            @PathVariable Long sellerId,
            @RequestParam InquiryStatus status,
            @RequestParam(required = false) String closingReason) {
        try {
            Inquiry inquiry = inquiryService.updateInquiryStatus(inquiryId, sellerId, status, closingReason);
            return ResponseEntity.ok(ApiResponse.success("Inquiry status updated successfully", inquiry));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to update inquiry status: " + e.getMessage()));
        }
    }
    
    @GetMapping("/count/open/{sellerId}")
    public ResponseEntity<ApiResponse<Long>> getOpenInquiriesCount(@PathVariable Long sellerId) {
        try {
            Long count = inquiryService.getOpenInquiriesCountBySeller(sellerId);
            return ResponseEntity.ok(ApiResponse.success("Open inquiries count retrieved successfully", count));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to get open inquiries count: " + e.getMessage()));
        }
    }
    
    @GetMapping("/count/customer/{customerId}")
    public ResponseEntity<ApiResponse<Long>> getCustomerInquiriesCount(@PathVariable Long customerId) {
        try {
            Long count = inquiryService.getInquiriesCountByCustomer(customerId);
            return ResponseEntity.ok(ApiResponse.success("Customer inquiries count retrieved successfully", count));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to get customer inquiries count: " + e.getMessage()));
        }
    }
}
