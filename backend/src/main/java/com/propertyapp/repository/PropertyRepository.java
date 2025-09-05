package com.propertyapp.repository;

import com.propertyapp.model.Property;
import com.propertyapp.model.PropertyType;
import com.propertyapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    
    List<Property> findBySeller(User seller);
    
    List<Property> findByPropertyType(PropertyType propertyType);
    
    List<Property> findByCity(String city);
    
    List<Property> findByState(String state);
    
    List<Property> findByIsActiveTrue();
    
    @Query("SELECT p FROM Property p WHERE p.seller = :seller AND p.isActive = true")
    List<Property> findActivePropertiesBySeller(@Param("seller") User seller);
    
    @Query("SELECT p FROM Property p WHERE p.propertyType = :propertyType AND p.isActive = true")
    List<Property> findActivePropertiesByType(@Param("propertyType") PropertyType propertyType);
    
    @Query("SELECT p FROM Property p WHERE p.city = :city AND p.isActive = true")
    List<Property> findActivePropertiesByCity(@Param("city") String city);
    
    @Query("SELECT p FROM Property p WHERE p.price BETWEEN :minPrice AND :maxPrice AND p.isActive = true")
    List<Property> findActivePropertiesByPriceRange(@Param("minPrice") BigDecimal minPrice, 
                                                   @Param("maxPrice") BigDecimal maxPrice);
    
    @Query("SELECT p FROM Property p WHERE p.propertyType = :propertyType AND p.city = :city AND p.isActive = true")
    List<Property> findActivePropertiesByTypeAndCity(@Param("propertyType") PropertyType propertyType, 
                                                    @Param("city") String city);
    
    @Query(value = "SELECT p.*, u.full_name as seller_name, u.mobile_number as seller_contact " +
           "FROM Properties p " +
           "INNER JOIN Users u ON p.seller_id = u.user_id " +
           "WHERE p.is_active = 1 AND u.is_active = 1 " +
           "AND (6371 * ACOS(COS(RADIANS(:latitude)) * COS(RADIANS(p.latitude)) * " +
           "COS(RADIANS(p.longitude) - RADIANS(:longitude)) + " +
           "SIN(RADIANS(:latitude)) * SIN(RADIANS(p.latitude)))) <= :radiusKm " +
           "ORDER BY (6371 * ACOS(COS(RADIANS(:latitude)) * COS(RADIANS(p.latitude)) * " +
           "COS(RADIANS(p.longitude) - RADIANS(:longitude)) + " +
           "SIN(RADIANS(:latitude)) * SIN(RADIANS(p.latitude))))", 
           nativeQuery = true)
    List<Object[]> findNearbyProperties(@Param("latitude") BigDecimal latitude, 
                                       @Param("longitude") BigDecimal longitude, 
                                       @Param("radiusKm") BigDecimal radiusKm);
}
