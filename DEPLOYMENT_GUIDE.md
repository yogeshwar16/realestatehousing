# Property Listing App - Deployment Guide

## Overview
Complete deployment guide for the Property Listing Android application with Java Spring Boot backend.

## Prerequisites
- Java 17 or higher
- Android Studio Arctic Fox or later
- SQL Server 2019 or later
- Maven 3.6+
- Twilio Account (for SMS OTP)
- Google Maps API Key

## Backend Deployment

### 1. Environment Setup
Create environment variables or update `application.yml`:

```yaml
# Database Configuration
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password
DB_URL=jdbc:sqlserver://localhost:1433;databaseName=PropertyListingDB

# JWT Configuration
JWT_SECRET=your-256-bit-secret-key-here

# Twilio Configuration
TWILIO_ACCOUNT_SID=your_twilio_account_sid
TWILIO_AUTH_TOKEN=your_twilio_auth_token
TWILIO_PHONE_NUMBER=your_twilio_phone_number
```

### 2. Database Setup
1. Create SQL Server database named `PropertyListingDB`
2. Run the schema script: `database/schema.sql`
3. Verify all tables are created successfully

### 3. Build and Run Backend
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

Backend will start on `http://localhost:8080`

## Android App Deployment

### 1. Configuration
Update `ApiClient.java` with your backend URL:
```java
private static final String BASE_URL = "http://YOUR_SERVER_IP:8080/";
```

### 2. Google Maps Setup
1. Get Google Maps API key from Google Cloud Console
2. Update `AndroidManifest.xml`:
```xml
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="YOUR_GOOGLE_MAPS_API_KEY" />
```

### 3. Build APK
1. Open project in Android Studio
2. Build → Generate Signed Bundle/APK
3. Choose APK and follow signing process
4. Install APK on target devices

## Production Deployment

### Backend (Cloud Deployment)
1. **AWS/Azure/GCP**: Deploy Spring Boot as containerized application
2. **Database**: Use managed SQL Server instance
3. **Environment Variables**: Configure via cloud provider's secret management
4. **Load Balancer**: Setup for high availability
5. **SSL Certificate**: Enable HTTPS

### Android App
1. **Google Play Store**: Upload signed APK/AAB
2. **Firebase**: Setup for push notifications (optional)
3. **Analytics**: Integrate Google Analytics or Firebase Analytics

## Testing Checklist

### Backend API Testing
- [ ] User signup with validation
- [ ] OTP generation and verification
- [ ] User login with JWT token
- [ ] Property CRUD operations
- [ ] Inquiry creation and management
- [ ] Admin reporting endpoints

### Android App Testing
- [ ] Signup flow with validation
- [ ] Login with OTP verification
- [ ] Dashboard property listing
- [ ] Property filtering and search
- [ ] Property details view
- [ ] Inquiry submission
- [ ] Terms & conditions acceptance

## Monitoring and Maintenance

### Backend Monitoring
- Application logs via Logback
- Database connection monitoring
- API response time tracking
- Error rate monitoring

### Android App Monitoring
- Crash reporting via Firebase Crashlytics
- User analytics and behavior tracking
- Performance monitoring

## Security Considerations

### Backend Security
- JWT token expiration (currently 24 hours)
- Input validation and sanitization
- SQL injection prevention via JPA
- Rate limiting for OTP requests

### Android Security
- API key protection
- Secure storage of user data
- Network security with HTTPS only
- Input validation on client side

## Troubleshooting

### Common Backend Issues
1. **Database Connection**: Check connection string and credentials
2. **Twilio SMS**: Verify account SID, auth token, and phone number
3. **CORS Issues**: Update CORS configuration for frontend domains

### Common Android Issues
1. **Network Errors**: Check backend URL and network connectivity
2. **Maps Not Loading**: Verify Google Maps API key and permissions
3. **OTP Not Received**: Check Twilio configuration and phone number format

## Performance Optimization

### Backend
- Database indexing on frequently queried columns
- Connection pooling configuration
- Caching for property listings
- Pagination for large datasets

### Android
- Image loading optimization with Glide
- RecyclerView optimization for large lists
- Network request caching
- Background task optimization

## Backup and Recovery

### Database Backup
- Automated daily backups
- Point-in-time recovery setup
- Backup verification procedures

### Application Backup
- Source code version control
- Configuration backup
- Documentation maintenance

## Support and Maintenance

### Regular Maintenance Tasks
- Security updates for dependencies
- Database maintenance and optimization
- Log file cleanup
- Performance monitoring review

### User Support
- Error logging and tracking
- User feedback collection
- Bug report management
- Feature request tracking

---

**Contact Information:**
- Development Team: [Your Contact]
- Support Email: [Support Email]
- Documentation: [Documentation URL]
