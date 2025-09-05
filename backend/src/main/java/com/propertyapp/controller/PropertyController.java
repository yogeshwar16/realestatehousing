package com.propertyapp.controller;

import com.propertyapp.dto.ApiResponse;
import com.propertyapp.dto.PropertyRequest;
import com.propertyapp.model.Property;
import com.propertyapp.model.PropertyType;
import com.propertyapp.service.PropertyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/properties")
@CrossOrigin(origins = "*")
public class PropertyController {
    
    @Autowired
    private PropertyService propertyService;
    
    @PostMapping("/create/{sellerId}")
    public ResponseEntity<ApiResponse<Property>> createProperty(
            @PathVariable Long sellerId,
            @Valid @RequestBody PropertyRequest propertyRequest) {
        try {
            Property property = propertyService.createProperty(sellerId, propertyRequest);
            return ResponseEntity.ok(ApiResponse.success("Property created successfully", property));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to create property: " + e.getMessage()));
        }
    }
    
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Property>>> getAllProperties() {
        try {
            List<Property> properties = propertyService.getAllActiveProperties();
            return ResponseEntity.ok(ApiResponse.success("Properties retrieved successfully", properties));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve properties: " + e.getMessage()));
        }
    }
    
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<ApiResponse<List<Property>>> getPropertiesBySeller(@PathVariable Long sellerId) {
        try {
            List<Property> properties = propertyService.getPropertiesBySeller(sellerId);
            return ResponseEntity.ok(ApiResponse.success("Seller properties retrieved successfully", properties));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve seller properties: " + e.getMessage()));
        }
    }
    
    @GetMapping("/type/{propertyType}")
    public ResponseEntity<ApiResponse<List<Property>>> getPropertiesByType(@PathVariable PropertyType propertyType) {
        try {
            List<Property> properties = propertyService.getPropertiesByType(propertyType);
            return ResponseEntity.ok(ApiResponse.success("Properties by type retrieved successfully", properties));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve properties by type: " + e.getMessage()));
        }
    }
    
    @GetMapping("/city/{city}")
    public ResponseEntity<ApiResponse<List<Property>>> getPropertiesByCity(@PathVariable String city) {
        try {
            List<Property> properties = propertyService.getPropertiesByCity(city);
            return ResponseEntity.ok(ApiResponse.success("Properties by city retrieved successfully", properties));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve properties by city: " + e.getMessage()));
        }
    }
    
    @GetMapping("/price-range")
    public ResponseEntity<ApiResponse<List<Property>>> getPropertiesByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        try {
            List<Property> properties = propertyService.getPropertiesByPriceRange(minPrice, maxPrice);
            return ResponseEntity.ok(ApiResponse.success("Properties by price range retrieved successfully", properties));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve properties by price range: " + e.getMessage()));
        }
    }
    
    @GetMapping("/nearby")
    public ResponseEntity<ApiResponse<List<Object[]>>> getNearbyProperties(
            @RequestParam BigDecimal latitude,
            @RequestParam BigDecimal longitude,
            @RequestParam(defaultValue = "10") BigDecimal radiusKm) {
        try {
            List<Object[]> properties = propertyService.getNearbyProperties(latitude, longitude, radiusKm);
            return ResponseEntity.ok(ApiResponse.success("Nearby properties retrieved successfully", properties));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve nearby properties: " + e.getMessage()));
        }
    }
    
    @GetMapping("/{propertyId}")
    public ResponseEntity<ApiResponse<Property>> getPropertyById(@PathVariable Long propertyId) {
        try {
            Optional<Property> property = propertyService.getPropertyById(propertyId);
            if (property.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success("Property retrieved successfully", property.get()));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve property: " + e.getMessage()));
        }
    }
    
    @PutMapping("/update/{propertyId}/{sellerId}")
    public ResponseEntity<ApiResponse<Property>> updateProperty(
            @PathVariable Long propertyId,
            @PathVariable Long sellerId,
            @Valid @RequestBody PropertyRequest propertyRequest) {
        try {
            Property property = propertyService.updateProperty(propertyId, sellerId, propertyRequest);
            return ResponseEntity.ok(ApiResponse.success("Property updated successfully", property));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to update property: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/delete/{propertyId}/{sellerId}")
    public ResponseEntity<ApiResponse<String>> deleteProperty(
            @PathVariable Long propertyId,
            @PathVariable Long sellerId) {
        try {
            propertyService.deleteProperty(propertyId, sellerId);
            return ResponseEntity.ok(ApiResponse.success("Property deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to delete property: " + e.getMessage()));
        }
    }
}
