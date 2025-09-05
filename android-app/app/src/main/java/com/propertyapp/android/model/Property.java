package com.propertyapp.android.model;

import java.math.BigDecimal;

public class Property {
    private Long propertyId;
    private User seller;
    private PropertyType propertyType;
    private String title;
    private String description;
    private BigDecimal propertySize;
    private BigDecimal price;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String propertyImages;
    private String ptrDocument;
    private Boolean isActive;
    private String createdAt;
    private String updatedAt;

    public Property() {}

    // Getters and Setters
    public Long getPropertyId() { return propertyId; }
    public void setPropertyId(Long propertyId) { this.propertyId = propertyId; }

    public User getSeller() { return seller; }
    public void setSeller(User seller) { this.seller = seller; }

    public PropertyType getPropertyType() { return propertyType; }
    public void setPropertyType(PropertyType propertyType) { this.propertyType = propertyType; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPropertySize() { return propertySize; }
    public void setPropertySize(BigDecimal propertySize) { this.propertySize = propertySize; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }

    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }

    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }

    public String getPropertyImages() { return propertyImages; }
    public void setPropertyImages(String propertyImages) { this.propertyImages = propertyImages; }

    public String getPtrDocument() { return ptrDocument; }
    public void setPtrDocument(String ptrDocument) { this.ptrDocument = ptrDocument; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}
