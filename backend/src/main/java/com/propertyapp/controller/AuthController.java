package com.propertyapp.controller;

import com.propertyapp.dto.ApiResponse;
import com.propertyapp.dto.LoginRequest;
import com.propertyapp.dto.OTPRequest;
import com.propertyapp.dto.SignupRequest;
import com.propertyapp.dto.UserUpdateRequest;
import com.propertyapp.model.User;
import com.propertyapp.service.OTPService;
import com.propertyapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private OTPService otpService;
    
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<User>> signup(@Valid @RequestBody SignupRequest signupRequest) {
        try {
            User user = userService.signup(signupRequest);
            return ResponseEntity.ok(ApiResponse.success("User registered successfully", user));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Signup failed: " + e.getMessage()));
        }
    }
    
    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse<String>> sendOTP(@Valid @RequestBody OTPRequest otpRequest) {
        try {
            // Check if user exists
            if (!userService.isUserExists(otpRequest.getMobileNumber())) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("User not found with this mobile number"));
            }
            
            String message = otpService.generateAndSendOTP(otpRequest.getMobileNumber());
            return ResponseEntity.ok(ApiResponse.success(message));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to send OTP: " + e.getMessage()));
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<User>> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Verify OTP
            boolean isOTPValid = otpService.verifyOTP(loginRequest.getMobileNumber(), loginRequest.getOtp());
            if (!isOTPValid) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Invalid or expired OTP"));
            }
            
            // Get user details
            Optional<User> user = userService.findByMobileNumber(loginRequest.getMobileNumber());
            if (user.isPresent()) {
                if (!user.get().getIsActive()) {
                    return ResponseEntity.badRequest()
                            .body(ApiResponse.error("User account is deactivated"));
                }
                return ResponseEntity.ok(ApiResponse.success("Login successful", user.get()));
            } else {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("User not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Login failed: " + e.getMessage()));
        }
    }
    
    @GetMapping("/user/{mobileNumber}")
    public ResponseEntity<ApiResponse<User>> getUserByMobileNumber(@PathVariable String mobileNumber) {
        try {
            User user = userService.findByMobileNumber(mobileNumber);
            if (user != null) {
                return ResponseEntity.ok(new ApiResponse<>(true, "User found", user, null));
            } else {
                return ResponseEntity.ok(new ApiResponse<>(false, "User not found", null, null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "Error retrieving user", null, e.getMessage()));
        }
    }

    @PutMapping("/user/{mobileNumber}")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable String mobileNumber, 
                                                       @Valid @RequestBody UserUpdateRequest request) {
        try {
            User existingUser = userService.findByMobileNumber(mobileNumber);
            if (existingUser == null) {
                return ResponseEntity.ok(new ApiResponse<>(false, "User not found", null, null));
            }

            // Update user fields
            existingUser.setFullName(request.getFullName());
            existingUser.setEmail(request.getEmail());
            existingUser.setAadhaarNumber(request.getAadhaarNumber());
            existingUser.setPanCard(request.getPanCard());
            existingUser.setAddress(request.getAddress());

            User updatedUser = userService.updateUser(existingUser);
            return ResponseEntity.ok(new ApiResponse<>(true, "User updated successfully", updatedUser, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "Error updating user", null, e.getMessage()));
        }
    }
}
