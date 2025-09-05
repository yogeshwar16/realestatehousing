package com.propertyapp.repository;

import com.propertyapp.model.User;
import com.propertyapp.model.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByMobileNumber(String mobileNumber);
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByAadhaarNumber(String aadhaarNumber);
    
    Optional<User> findByPanCard(String panCard);
    
    List<User> findByUserType(UserType userType);
    
    List<User> findByIsActiveTrue();
    
    @Query("SELECT u FROM User u WHERE u.userType = :userType AND u.isActive = true")
    List<User> findActiveUsersByType(@Param("userType") UserType userType);
    
    boolean existsByMobileNumber(String mobileNumber);
    
    boolean existsByEmail(String email);
    
    boolean existsByAadhaarNumber(String aadhaarNumber);
    
    boolean existsByPanCard(String panCard);
}
