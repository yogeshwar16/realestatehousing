-- Property Listing App Database Schema
-- SQL Server Database

-- Create Database
CREATE DATABASE PropertyListingDB;
GO

USE PropertyListingDB;
GO

-- Users table (for both sellers and customers)
CREATE TABLE Users (
    user_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    full_name NVARCHAR(100) NOT NULL,
    mobile_number NVARCHAR(15) NOT NULL UNIQUE,
    email NVARCHAR(100) NOT NULL UNIQUE,
    aadhaar_number NVARCHAR(12) NOT NULL UNIQUE,
    pan_card NVARCHAR(10) NOT NULL UNIQUE,
    user_type NVARCHAR(10) NOT NULL CHECK (user_type IN ('SELLER', 'CUSTOMER')),
    address NVARCHAR(500),
    is_active BIT DEFAULT 1,
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE()
);

-- OTP verification table
CREATE TABLE OTPVerification (
    otp_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    mobile_number NVARCHAR(15) NOT NULL,
    otp_code NVARCHAR(6) NOT NULL,
    is_verified BIT DEFAULT 0,
    expires_at DATETIME2 NOT NULL,
    created_at DATETIME2 DEFAULT GETDATE()
);

-- Property listings table
CREATE TABLE Properties (
    property_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    seller_id BIGINT NOT NULL,
    property_type NVARCHAR(20) NOT NULL CHECK (property_type IN ('LAND', 'FLAT', 'ROW_HOUSE', 'BUNGALOW')),
    title NVARCHAR(200) NOT NULL,
    description NVARCHAR(MAX),
    property_size DECIMAL(10,2), -- in sq ft or acres
    price DECIMAL(15,2) NOT NULL,
    address NVARCHAR(500) NOT NULL,
    city NVARCHAR(100) NOT NULL,
    state NVARCHAR(100) NOT NULL,
    pincode NVARCHAR(10) NOT NULL,
    latitude DECIMAL(10,8),
    longitude DECIMAL(11,8),
    property_images NVARCHAR(MAX), -- JSON array of image URLs
    ptr_document NVARCHAR(500), -- PTR or 7/12 document path
    is_active BIT DEFAULT 1,
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (seller_id) REFERENCES Users(user_id)
);

-- Customer inquiries table
CREATE TABLE Inquiries (
    inquiry_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    property_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    seller_id BIGINT NOT NULL,
    inquiry_description NVARCHAR(MAX),
    inquiry_status NVARCHAR(20) DEFAULT 'OPEN' CHECK (inquiry_status IN ('OPEN', 'IN_PROGRESS', 'CLOSED', 'EXPIRED')),
    closing_reason NVARCHAR(500),
    terms_accepted BIT DEFAULT 0,
    inquiry_date DATETIME2 DEFAULT GETDATE(),
    expiry_date DATETIME2 NOT NULL, -- 3 months from inquiry_date
    closed_date DATETIME2,
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (property_id) REFERENCES Properties(property_id),
    FOREIGN KEY (customer_id) REFERENCES Users(user_id),
    FOREIGN KEY (seller_id) REFERENCES Users(user_id)
);

-- Admin login logs table
CREATE TABLE LoginLogs (
    log_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_id BIGINT NOT NULL,
    mobile_number NVARCHAR(15) NOT NULL,
    login_time DATETIME2 DEFAULT GETDATE(),
    ip_address NVARCHAR(45),
    device_info NVARCHAR(500),
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

-- Terms and conditions table
CREATE TABLE TermsConditions (
    terms_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    version NVARCHAR(10) NOT NULL,
    content NVARCHAR(MAX) NOT NULL,
    is_active BIT DEFAULT 1,
    created_at DATETIME2 DEFAULT GETDATE()
);

-- Create indexes for better performance
CREATE INDEX IX_Users_MobileNumber ON Users(mobile_number);
CREATE INDEX IX_Users_UserType ON Users(user_type);
CREATE INDEX IX_Properties_SellerId ON Properties(seller_id);
CREATE INDEX IX_Properties_PropertyType ON Properties(property_type);
CREATE INDEX IX_Properties_City ON Properties(city);
CREATE INDEX IX_Properties_IsActive ON Properties(is_active);
CREATE INDEX IX_Inquiries_PropertyId ON Inquiries(property_id);
CREATE INDEX IX_Inquiries_CustomerId ON Inquiries(customer_id);
CREATE INDEX IX_Inquiries_SellerId ON Inquiries(seller_id);
CREATE INDEX IX_Inquiries_Status ON Inquiries(inquiry_status);
CREATE INDEX IX_LoginLogs_UserId ON LoginLogs(user_id);
CREATE INDEX IX_LoginLogs_LoginTime ON LoginLogs(login_time);

-- Insert default terms and conditions
INSERT INTO TermsConditions (version, content, is_active) VALUES 
('1.0', 
'Terms and Conditions for Property Listing App:

1. Seller Responsibilities:
   - Seller must self-update property details for sale
   - Seller must verify that inquiries are not fraudulent
   - Seller is responsible for any fraudulent activities as we only provide inquiries
   - All property information must be accurate and up-to-date

2. Customer Responsibilities:
   - Customers must provide genuine inquiry details
   - Inquiry response period is 3 months from the date of inquiry
   - Customers must verify property details independently

3. Platform Responsibilities:
   - We provide a platform to connect buyers and sellers
   - We are not responsible for any fraudulent activities
   - We suggest nearby location-wise inquiries for better matches

4. General Terms:
   - All users must provide valid Aadhaar and PAN card details
   - Mobile number verification is mandatory
   - Users must accept these terms to use the platform

By using this app, you agree to these terms and conditions.',
1);

-- Create stored procedures for common operations

-- Procedure to get nearby properties
CREATE PROCEDURE GetNearbyProperties
    @Latitude DECIMAL(10,8),
    @Longitude DECIMAL(11,8),
    @RadiusKm DECIMAL(5,2) = 10
AS
BEGIN
    SELECT p.*, u.full_name as seller_name, u.mobile_number as seller_contact
    FROM Properties p
    INNER JOIN Users u ON p.seller_id = u.user_id
    WHERE p.is_active = 1
    AND u.is_active = 1
    AND (
        6371 * ACOS(
            COS(RADIANS(@Latitude)) * COS(RADIANS(p.latitude)) * 
            COS(RADIANS(p.longitude) - RADIANS(@Longitude)) + 
            SIN(RADIANS(@Latitude)) * SIN(RADIANS(p.latitude))
        )
    ) <= @RadiusKm
    ORDER BY (
        6371 * ACOS(
            COS(RADIANS(@Latitude)) * COS(RADIANS(p.latitude)) * 
            COS(RADIANS(p.longitude) - RADIANS(@Longitude)) + 
            SIN(RADIANS(@Latitude)) * SIN(RADIANS(p.latitude))
        )
    );
END;
GO

-- Procedure to expire old inquiries
CREATE PROCEDURE ExpireOldInquiries
AS
BEGIN
    UPDATE Inquiries 
    SET inquiry_status = 'EXPIRED', 
        updated_at = GETDATE()
    WHERE inquiry_status = 'OPEN' 
    AND expiry_date < GETDATE();
END;
GO

-- Create a job to run inquiry expiration daily (optional - requires SQL Server Agent)
-- EXEC msdb.dbo.sp_add_job @job_name = 'ExpireOldInquiries';
