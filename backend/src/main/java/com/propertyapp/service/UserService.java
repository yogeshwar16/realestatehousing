package com.propertyapp.service;

import com.propertyapp.dto.SignupRequest;
import com.propertyapp.model.User;
import com.propertyapp.model.UserType;
import com.propertyapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public User signup(SignupRequest signupRequest) {
        // Check if user already exists
        if (userRepository.existsByMobileNumber(signupRequest.getMobileNumber())) {
            throw new RuntimeException("User with this mobile number already exists");
        }
        
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new RuntimeException("User with this email already exists");
        }
        
        if (userRepository.existsByAadhaarNumber(signupRequest.getAadhaarNumber())) {
            throw new RuntimeException("User with this Aadhaar number already exists");
        }
        
        if (userRepository.existsByPanCard(signupRequest.getPanCard())) {
            throw new RuntimeException("User with this PAN card already exists");
        }
        
        // Create new user
        User user = new User();
        user.setFullName(signupRequest.getFullName());
        user.setMobileNumber(signupRequest.getMobileNumber());
        user.setEmail(signupRequest.getEmail());
        user.setAadhaarNumber(signupRequest.getAadhaarNumber());
        user.setPanCard(signupRequest.getPanCard());
        user.setUserType(signupRequest.getUserType());
        user.setAddress(signupRequest.getAddress());
        user.setIsActive(true);
        
        return userRepository.save(user);
    }
    
    public Optional<User> findByMobileNumber(String mobileNumber) {
        return userRepository.findByMobileNumber(mobileNumber);
    }
    
    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public List<User> getUsersByType(UserType userType) {
        return userRepository.findActiveUsersByType(userType);
    }
    
    public List<User> getActiveUsers() {
        return userRepository.findByIsActiveTrue();
    }
    
    public User updateUser(Long userId, User updatedUser) {
        Optional<User> existingUser = userRepository.findById(userId);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setFullName(updatedUser.getFullName());
            user.setEmail(updatedUser.getEmail());
            user.setAddress(updatedUser.getAddress());
            return userRepository.save(user);
        }
        throw new RuntimeException("User not found with ID: " + userId);
    }
    
    public void deactivateUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            user.get().setIsActive(false);
            userRepository.save(user.get());
        } else {
            throw new RuntimeException("User not found with ID: " + userId);
        }
    }
    
    public boolean isUserExists(String mobileNumber) {
        return userRepository.existsByMobileNumber(mobileNumber);
    }
}
