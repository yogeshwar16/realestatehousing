package com.propertyapp.repository;

import com.propertyapp.model.Inquiry;
import com.propertyapp.model.InquiryStatus;
import com.propertyapp.model.Property;
import com.propertyapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    
    List<Inquiry> findByCustomer(User customer);
    
    List<Inquiry> findBySeller(User seller);
    
    List<Inquiry> findByProperty(Property property);
    
    List<Inquiry> findByInquiryStatus(InquiryStatus status);
    
    @Query("SELECT i FROM Inquiry i WHERE i.customer = :customer ORDER BY i.createdAt DESC")
    List<Inquiry> findByCustomerOrderByCreatedAtDesc(@Param("customer") User customer);
    
    @Query("SELECT i FROM Inquiry i WHERE i.seller = :seller ORDER BY i.createdAt DESC")
    List<Inquiry> findBySellerOrderByCreatedAtDesc(@Param("seller") User seller);
    
    @Query("SELECT i FROM Inquiry i WHERE i.property = :property ORDER BY i.createdAt DESC")
    List<Inquiry> findByPropertyOrderByCreatedAtDesc(@Param("property") Property property);
    
    @Query("SELECT i FROM Inquiry i WHERE i.inquiryStatus = 'OPEN' AND i.expiryDate < :currentDate")
    List<Inquiry> findExpiredInquiries(@Param("currentDate") LocalDateTime currentDate);
    
    @Query("SELECT i FROM Inquiry i WHERE i.seller = :seller AND i.inquiryStatus = :status")
    List<Inquiry> findBySellerAndStatus(@Param("seller") User seller, @Param("status") InquiryStatus status);
    
    @Query("SELECT i FROM Inquiry i WHERE i.customer = :customer AND i.inquiryStatus = :status")
    List<Inquiry> findByCustomerAndStatus(@Param("customer") User customer, @Param("status") InquiryStatus status);
    
    @Query("SELECT COUNT(i) FROM Inquiry i WHERE i.seller = :seller AND i.inquiryStatus = 'OPEN'")
    Long countOpenInquiriesBySeller(@Param("seller") User seller);
    
    @Query("SELECT COUNT(i) FROM Inquiry i WHERE i.customer = :customer")
    Long countInquiriesByCustomer(@Param("customer") User customer);
}
