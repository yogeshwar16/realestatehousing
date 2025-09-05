package com.propertyapp.controller;

import com.propertyapp.dto.ApiResponse;
import com.propertyapp.model.Inquiry;
import com.propertyapp.model.InquiryStatus;
import com.propertyapp.model.User;
import com.propertyapp.model.UserType;
import com.propertyapp.service.InquiryService;
import com.propertyapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private InquiryService inquiryService;
    
    @GetMapping("/users/login-report")
    public ResponseEntity<ApiResponse<List<User>>> getLoginReport() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(ApiResponse.success("Login report retrieved successfully", users));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve login report: " + e.getMessage()));
        }
    }
    
    @GetMapping("/users/customers")
    public ResponseEntity<ApiResponse<List<User>>> getAllCustomers() {
        try {
            List<User> customers = userService.getUsersByType(UserType.CUSTOMER);
            return ResponseEntity.ok(ApiResponse.success("Customers retrieved successfully", customers));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve customers: " + e.getMessage()));
        }
    }
    
    @GetMapping("/users/sellers")
    public ResponseEntity<ApiResponse<List<User>>> getAllSellers() {
        try {
            List<User> sellers = userService.getUsersByType(UserType.SELLER);
            return ResponseEntity.ok(ApiResponse.success("Sellers retrieved successfully", sellers));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve sellers: " + e.getMessage()));
        }
    }
    
    @GetMapping("/inquiries/closed-report")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getClosedInquiriesReport() {
        try {
            List<User> allUsers = userService.getAllUsers();
            
            Map<String, Object> report = allUsers.stream()
                    .filter(user -> user.getUserType() == UserType.CUSTOMER)
                    .collect(Collectors.toMap(
                            user -> user.getFullName(),
                            user -> {
                                List<Inquiry> customerInquiries = inquiryService.getInquiriesByCustomer(user.getUserId());
                                List<Inquiry> closedInquiries = customerInquiries.stream()
                                        .filter(inquiry -> inquiry.getInquiryStatus() == InquiryStatus.CLOSED)
                                        .collect(Collectors.toList());
                                
                                return Map.of(
                                        "contactNo", user.getMobileNumber(),
                                        "address", user.getAddress() != null ? user.getAddress() : "N/A",
                                        "closedInquiries", closedInquiries.stream()
                                                .map(inquiry -> Map.of(
                                                        "propertyTitle", inquiry.getProperty().getTitle(),
                                                        "closingReason", inquiry.getClosingReason() != null ? inquiry.getClosingReason() : "N/A",
                                                        "closedDate", inquiry.getClosedDate()
                                                ))
                                                .collect(Collectors.toList())
                                );
                            }
                    ));
            
            return ResponseEntity.ok(ApiResponse.success("Closed inquiries report retrieved successfully", report));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve closed inquiries report: " + e.getMessage()));
        }
    }
    
    @GetMapping("/inquiries/all-report")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAllInquiriesReport() {
        try {
            List<User> allUsers = userService.getAllUsers();
            
            Map<String, Object> report = allUsers.stream()
                    .filter(user -> user.getUserType() == UserType.CUSTOMER)
                    .collect(Collectors.toMap(
                            user -> user.getFullName(),
                            user -> {
                                List<Inquiry> customerInquiries = inquiryService.getInquiriesByCustomer(user.getUserId());
                                
                                return Map.of(
                                        "contactNo", user.getMobileNumber(),
                                        "address", user.getAddress() != null ? user.getAddress() : "N/A",
                                        "totalInquiries", customerInquiries.size(),
                                        "openInquiries", customerInquiries.stream()
                                                .filter(inquiry -> inquiry.getInquiryStatus() == InquiryStatus.OPEN)
                                                .count(),
                                        "closedInquiries", customerInquiries.stream()
                                                .filter(inquiry -> inquiry.getInquiryStatus() == InquiryStatus.CLOSED)
                                                .count(),
                                        "expiredInquiries", customerInquiries.stream()
                                                .filter(inquiry -> inquiry.getInquiryStatus() == InquiryStatus.EXPIRED)
                                                .count()
                                );
                            }
                    ));
            
            return ResponseEntity.ok(ApiResponse.success("All inquiries report retrieved successfully", report));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve all inquiries report: " + e.getMessage()));
        }
    }
    
    @GetMapping("/stats/dashboard")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStats() {
        try {
            List<User> allUsers = userService.getAllUsers();
            long totalCustomers = allUsers.stream().filter(user -> user.getUserType() == UserType.CUSTOMER).count();
            long totalSellers = allUsers.stream().filter(user -> user.getUserType() == UserType.SELLER).count();
            long activeUsers = allUsers.stream().filter(User::getIsActive).count();
            
            Map<String, Object> stats = Map.of(
                    "totalUsers", allUsers.size(),
                    "totalCustomers", totalCustomers,
                    "totalSellers", totalSellers,
                    "activeUsers", activeUsers,
                    "inactiveUsers", allUsers.size() - activeUsers
            );
            
            return ResponseEntity.ok(ApiResponse.success("Dashboard stats retrieved successfully", stats));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve dashboard stats: " + e.getMessage()));
        }
    }
}
