package com.propertyapp.android.api;

import com.propertyapp.android.model.PropertyType;

import java.math.BigDecimal;

public class PropertyRequest {
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

    public PropertyRequest() {}

    // Getters and Setters
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
}
