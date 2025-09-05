package com.propertyapp.repository;

import com.propertyapp.model.OTPVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OTPVerificationRepository extends JpaRepository<OTPVerification, Long> {
    
    Optional<OTPVerification> findByMobileNumberAndOtpCodeAndIsVerifiedFalse(String mobileNumber, String otpCode);
    
    List<OTPVerification> findByMobileNumber(String mobileNumber);
    
    @Query("SELECT o FROM OTPVerification o WHERE o.mobileNumber = :mobileNumber AND o.isVerified = false AND o.expiresAt > :currentTime ORDER BY o.createdAt DESC")
    List<OTPVerification> findValidOTPsByMobileNumber(@Param("mobileNumber") String mobileNumber, 
                                                     @Param("currentTime") LocalDateTime currentTime);
    
    @Query("SELECT o FROM OTPVerification o WHERE o.expiresAt < :currentTime AND o.isVerified = false")
    List<OTPVerification> findExpiredOTPs(@Param("currentTime") LocalDateTime currentTime);
    
    void deleteByMobileNumberAndIsVerifiedTrue(String mobileNumber);
}
