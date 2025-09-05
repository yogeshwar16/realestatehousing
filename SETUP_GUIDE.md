# Property Listing App - Quick Setup Guide

## Prerequisites Checklist
- [ ] Java 17 installed
- [ ] SQL Server installed and running
- [ ] Android Studio installed
- [ ] Twilio account created
- [ ] Google Maps API key obtained

## Step-by-Step Setup

### 1. Database Setup (5 minutes)

1. Open SQL Server Management Studio
2. Create new database named `PropertyListingDB`
3. Execute the schema file:
   ```sql
   -- Navigate to database/ folder and run schema.sql
   ```

### 2. Backend Configuration (10 minutes)

1. Open `backend/src/main/resources/application.yml`
2. Update database connection:
   ```yaml
   spring:
     datasource:
       url: jdbc:sqlserver://localhost:1433;databaseName=PropertyListingDB;encrypt=true;trustServerCertificate=true
       username: sa  # Your SQL Server username
       password: your_password  # Your SQL Server password
   ```

3. Configure Twilio SMS service:
   ```yaml
   twilio:
     account-sid: ACxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
     auth-token: your_auth_token
     phone-number: +1234567890
   ```

4. Start the backend:
   ```bash
   cd backend
   mvn spring-boot:run
   ```
   Backend will be available at: http://localhost:8080

### 3. Android App Configuration (5 minutes)

1. Open Android Studio and import the `android-app` project
2. Update API base URL in `ApiClient.java`:
   ```java
   // For emulator use: http://10.0.2.2:8080/api/
   // For real device use your computer's IP: http://192.168.1.100:8080/api/
   private static final String BASE_URL = "http://10.0.2.2:8080/api/";
   ```

3. Add Google Maps API key in `AndroidManifest.xml`:
   ```xml
   <meta-data
       android:name="com.google.android.geo.API_KEY"
       android:value="AIzaSyBxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" />
   ```

4. Build and run the app on emulator or device

## Testing the Application

### 1. Test Backend APIs
Use Postman or curl to test:

```bash
# Test signup
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "John Doe",
    "mobileNumber": "9876543210",
    "email": "john@example.com",
    "aadhaarNumber": "123456789012",
    "panCard": "ABCDE1234F",
    "userType": "CUSTOMER"
  }'

# Test send OTP
curl -X POST http://localhost:8080/api/auth/send-otp \
  -H "Content-Type: application/json" \
  -d '{"mobileNumber": "9876543210"}'
```

### 2. Test Android App
1. Launch app on emulator/device
2. Sign up as a new user
3. Login with OTP
4. Browse properties on dashboard
5. Create inquiry (for customers)
6. Add property (for sellers)

## Common Issues & Solutions

### Backend Issues

**Issue**: Database connection failed
**Solution**: 
- Verify SQL Server is running
- Check connection string in application.yml
- Ensure database `PropertyListingDB` exists

**Issue**: SMS not sending
**Solution**:
- Verify Twilio credentials
- Check Twilio account balance
- Ensure phone number is verified in Twilio

### Android Issues

**Issue**: Network error when calling APIs
**Solution**:
- Check if backend is running on port 8080
- Update base URL with correct IP address
- Add network security config for HTTP (if needed)

**Issue**: Google Maps not loading
**Solution**:
- Verify Google Maps API key is correct
- Enable Maps SDK for Android in Google Cloud Console
- Check API key restrictions

## Production Deployment

### Backend Deployment
1. Build production JAR:
   ```bash
   mvn clean package -Pprod
   ```

2. Deploy to server:
   ```bash
   java -jar target/property-listing-backend-1.0.0.jar
   ```

### Android App Deployment
1. Generate signed APK in Android Studio
2. Upload to Google Play Console
3. Follow Google Play publishing guidelines

## Environment Variables

For production, use environment variables instead of hardcoded values:

```bash
# Backend environment variables
export DB_USERNAME=your_db_username
export DB_PASSWORD=your_db_password
export JWT_SECRET=your_jwt_secret_key
export TWILIO_ACCOUNT_SID=your_twilio_sid
export TWILIO_AUTH_TOKEN=your_twilio_token
export TWILIO_PHONE_NUMBER=your_twilio_phone
```

## Support

If you encounter issues:
1. Check the logs in `logs/property-app.log`
2. Verify all prerequisites are installed
3. Ensure all configuration values are correct
4. Test API endpoints individually
5. Check network connectivity between Android app and backend

## Next Steps

After successful setup:
1. Customize the UI/UX as needed
2. Add additional property types
3. Implement push notifications
4. Add payment gateway integration
5. Enhance search and filtering capabilities
6. Add property image upload functionality
7. Implement real-time chat between buyers and sellers
