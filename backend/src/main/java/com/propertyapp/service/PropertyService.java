package com.propertyapp.service;

import com.propertyapp.dto.PropertyRequest;
import com.propertyapp.model.Property;
import com.propertyapp.model.PropertyType;
import com.propertyapp.model.User;
import com.propertyapp.repository.PropertyRepository;
import com.propertyapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PropertyService {
    
    @Autowired
    private PropertyRepository propertyRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public Property createProperty(Long sellerId, PropertyRequest propertyRequest) {
        Optional<User> seller = userRepository.findById(sellerId);
        if (!seller.isPresent()) {
            throw new RuntimeException("Seller not found with ID: " + sellerId);
        }
        
        if (!seller.get().getUserType().equals(com.propertyapp.model.UserType.SELLER)) {
            throw new RuntimeException("Only sellers can create property listings");
        }
        
        Property property = new Property();
        property.setSeller(seller.get());
        property.setPropertyType(propertyRequest.getPropertyType());
        property.setTitle(propertyRequest.getTitle());
        property.setDescription(propertyRequest.getDescription());
        property.setPropertySize(propertyRequest.getPropertySize());
        property.setPrice(propertyRequest.getPrice());
        property.setAddress(propertyRequest.getAddress());
        property.setCity(propertyRequest.getCity());
        property.setState(propertyRequest.getState());
        property.setPincode(propertyRequest.getPincode());
        property.setLatitude(propertyRequest.getLatitude());
        property.setLongitude(propertyRequest.getLongitude());
        property.setPropertyImages(propertyRequest.getPropertyImages());
        property.setPtrDocument(propertyRequest.getPtrDocument());
        property.setIsActive(true);
        
        return propertyRepository.save(property);
    }
    
    public List<Property> getAllActiveProperties() {
        return propertyRepository.findByIsActiveTrue();
    }
    
    public List<Property> getPropertiesBySeller(Long sellerId) {
        Optional<User> seller = userRepository.findById(sellerId);
        if (!seller.isPresent()) {
            throw new RuntimeException("Seller not found with ID: " + sellerId);
        }
        return propertyRepository.findActivePropertiesBySeller(seller.get());
    }
    
    public List<Property> getPropertiesByType(PropertyType propertyType) {
        return propertyRepository.findActivePropertiesByType(propertyType);
    }
    
    public List<Property> getPropertiesByCity(String city) {
        return propertyRepository.findActivePropertiesByCity(city);
    }
    
    public List<Property> getPropertiesByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return propertyRepository.findActivePropertiesByPriceRange(minPrice, maxPrice);
    }
    
    public List<Property> getPropertiesByTypeAndCity(PropertyType propertyType, String city) {
        return propertyRepository.findActivePropertiesByTypeAndCity(propertyType, city);
    }
    
    public List<Object[]> getNearbyProperties(BigDecimal latitude, BigDecimal longitude, BigDecimal radiusKm) {
        return propertyRepository.findNearbyProperties(latitude, longitude, radiusKm);
    }
    
    public Optional<Property> getPropertyById(Long propertyId) {
        return propertyRepository.findById(propertyId);
    }
    
    public Property updateProperty(Long propertyId, Long sellerId, PropertyRequest propertyRequest) {
        Optional<Property> existingProperty = propertyRepository.findById(propertyId);
        if (!existingProperty.isPresent()) {
            throw new RuntimeException("Property not found with ID: " + propertyId);
        }
        
        Property property = existingProperty.get();
        if (!property.getSeller().getUserId().equals(sellerId)) {
            throw new RuntimeException("Only the property owner can update this property");
        }
        
        property.setPropertyType(propertyRequest.getPropertyType());
        property.setTitle(propertyRequest.getTitle());
        property.setDescription(propertyRequest.getDescription());
        property.setPropertySize(propertyRequest.getPropertySize());
        property.setPrice(propertyRequest.getPrice());
        property.setAddress(propertyRequest.getAddress());
        property.setCity(propertyRequest.getCity());
        property.setState(propertyRequest.getState());
        property.setPincode(propertyRequest.getPincode());
        property.setLatitude(propertyRequest.getLatitude());
        property.setLongitude(propertyRequest.getLongitude());
        property.setPropertyImages(propertyRequest.getPropertyImages());
        property.setPtrDocument(propertyRequest.getPtrDocument());
        
        return propertyRepository.save(property);
    }
    
    public void deleteProperty(Long propertyId, Long sellerId) {
        Optional<Property> property = propertyRepository.findById(propertyId);
        if (!property.isPresent()) {
            throw new RuntimeException("Property not found with ID: " + propertyId);
        }
        
        if (!property.get().getSeller().getUserId().equals(sellerId)) {
            throw new RuntimeException("Only the property owner can delete this property");
        }
        
        property.get().setIsActive(false);
        propertyRepository.save(property.get());
    }
}
