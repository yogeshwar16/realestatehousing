package com.propertyapp.service;

import com.propertyapp.model.OTPVerification;
import com.propertyapp.repository.OTPVerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class OTPService {
    
    @Autowired
    private OTPVerificationRepository otpRepository;
    
    @Autowired
    private SMSService smsService;
    
    @Value("${otp.expiry.minutes:10}")
    private int otpExpiryMinutes;
    
    public String generateAndSendOTP(String mobileNumber) {
        // Generate 6-digit OTP
        String otp = generateOTP();
        
        // Save OTP to database
        OTPVerification otpVerification = new OTPVerification(mobileNumber, otp);
        otpRepository.save(otpVerification);
        
        // Send OTP via SMS
        try {
            smsService.sendOTP(mobileNumber, otp);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send OTP: " + e.getMessage());
        }
        
        return "OTP sent successfully to " + mobileNumber;
    }
    
    public boolean verifyOTP(String mobileNumber, String otp) {
        Optional<OTPVerification> otpVerification = otpRepository
                .findByMobileNumberAndOtpCodeAndIsVerifiedFalse(mobileNumber, otp);
        
        if (otpVerification.isPresent()) {
            OTPVerification verification = otpVerification.get();
            
            if (!verification.isExpired()) {
                verification.setIsVerified(true);
                otpRepository.save(verification);
                return true;
            }
        }
        return false;
    }
    
    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Generate 6-digit number
        return String.valueOf(otp);
    }
    
    public void cleanupExpiredOTPs() {
        var expiredOTPs = otpRepository.findExpiredOTPs(LocalDateTime.now());
        otpRepository.deleteAll(expiredOTPs);
    }
}
