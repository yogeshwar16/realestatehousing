# Property Listing App

A comprehensive property listing platform similar to Housing and MagicBricks, built with Java Spring Boot backend and Android frontend.

## Features

### User Management
- **Signup**: Users can register as Seller or Customer with full name, mobile number, email, Aadhaar, and PAN card
- **Login**: Mobile OTP-based authentication
- **User Types**: Separate functionality for Sellers and Customers

### Property Management
- **Property Types**: Land, Flat, Row House, Bungalow
- **Property Details**: Title, description, size, price, address, location coordinates, images, PTR/7-12 documents
- **Location-based Search**: Find nearby properties using GPS coordinates
- **Filtering**: Filter by property type, city, price range

### Inquiry System
- **Customer Inquiries**: Customers can raise inquiries on properties
- **3-Month Expiry**: Inquiries automatically expire after 3 months
- **Terms & Conditions**: Mandatory acceptance before creating inquiries
- **Status Management**: Open, In Progress, Closed, Expired statuses

### Admin Features
- **User Reports**: Login reports with user details
- **Inquiry Reports**: Closed inquiry reports with reasons
- **Dashboard Statistics**: User counts, inquiry statistics

## Technology Stack

### Backend
- **Java 17**
- **Spring Boot 3.1.5**
- **Spring Data JPA**
- **Spring Security**
- **SQL Server Database**
- **JWT Authentication**
- **Twilio SMS Service**
- **Maven**

### Android App
- **Java**
- **Material Design Components**
- **Retrofit for API calls**
- **Google Maps Integration**
- **Glide for image loading**

## Project Structure

```
PropertyListingApp/
├── backend/                 # Spring Boot backend
│   ├── src/main/java/
│   │   └── com/propertyapp/
│   │       ├── controller/  # REST controllers
│   │       ├── service/     # Business logic
│   │       ├── repository/  # Data access layer
│   │       ├── model/       # Entity models
│   │       └── dto/         # Data transfer objects
│   └── pom.xml
├── android-app/            # Android application
│   └── app/src/main/
│       ├── java/           # Java source code
│       └── res/            # Resources (layouts, drawables)
├── database/               # Database schema
│   └── schema.sql
└── README.md
```

## Database Schema

### Tables
- **Users**: User information with type (Seller/Customer)
- **Properties**: Property listings with seller details
- **Inquiries**: Customer inquiries on properties
- **OTPVerification**: OTP codes for authentication
- **LoginLogs**: User login tracking
- **TermsConditions**: Terms and conditions versions

## Setup Instructions

### Prerequisites
- Java 17
- SQL Server
- Android Studio
- Twilio Account (for SMS)
- Google Maps API Key

### Backend Setup

1. **Database Setup**
   ```sql
   -- Run the schema.sql file in SQL Server
   sqlcmd -S localhost -d PropertyListingDB -i database/schema.sql
   ```

2. **Configure Application Properties**
   ```yaml
   # Update backend/src/main/resources/application.yml
   spring:
     datasource:
       url: jdbc:sqlserver://localhost:1433;databaseName=PropertyListingDB
       username: your_username
       password: your_password
   
   twilio:
     account-sid: your_twilio_account_sid
     auth-token: your_twilio_auth_token
     phone-number: your_twilio_phone_number
   ```

3. **Run Backend**
   ```bash
   cd backend
   mvn spring-boot:run
   ```
   Backend will start on `http://localhost:8080`

### Android App Setup

1. **Configure API Base URL**
   ```java
   // Update ApiClient.java
   private static final String BASE_URL = "http://your_ip:8080/api/";
   ```

2. **Add Google Maps API Key**
   ```xml
   <!-- Update AndroidManifest.xml -->
   <meta-data
       android:name="com.google.android.geo.API_KEY"
       android:value="YOUR_GOOGLE_MAPS_API_KEY" />
   ```

3. **Build and Install**
   - Open project in Android Studio
   - Build and run on device/emulator

## API Endpoints

### Authentication
- `POST /api/auth/signup` - User registration
- `POST /api/auth/send-otp` - Send OTP for login
- `POST /api/auth/login` - Login with OTP verification

### Properties
- `GET /api/properties/all` - Get all properties
- `POST /api/properties/create/{sellerId}` - Create property
- `GET /api/properties/seller/{sellerId}` - Get seller properties
- `GET /api/properties/nearby` - Get nearby properties

### Inquiries
- `POST /api/inquiries/create/{customerId}` - Create inquiry
- `GET /api/inquiries/customer/{customerId}` - Get customer inquiries
- `GET /api/inquiries/seller/{sellerId}` - Get seller inquiries

### Admin
- `GET /api/admin/users/login-report` - User login report
- `GET /api/admin/inquiries/closed-report` - Closed inquiries report

## Key Features Implementation

### Terms & Conditions
- Sellers must self-update property details
- Sellers verify inquiry authenticity
- Platform provides inquiry connections only
- 3-month inquiry response period

### Security Features
- Mobile OTP verification
- JWT token authentication
- Input validation and sanitization
- SQL injection prevention

### Location Features
- GPS-based property search
- Nearby property suggestions
- Google Maps integration
- Location-based filtering

## Testing

### Backend Testing
```bash
cd backend
mvn test
```

### API Testing
Use tools like Postman to test API endpoints:
1. Register user via `/auth/signup`
2. Send OTP via `/auth/send-otp`
3. Login via `/auth/login`
4. Create property via `/properties/create/{sellerId}`

## Deployment

### Backend Deployment
1. Build JAR file: `mvn clean package`
2. Deploy to server: `java -jar target/property-listing-backend-1.0.0.jar`

### Android App Deployment
1. Generate signed APK in Android Studio
2. Distribute via Google Play Store or direct installation

## Contributing

1. Fork the repository
2. Create feature branch
3. Make changes
4. Test thoroughly
5. Submit pull request

## License

This project is licensed under the MIT License.

## Support

For support and questions:
- Create GitHub issues
- Contact development team
- Check documentation

---

**Note**: This is a complete property listing platform with all essential features for connecting property sellers and buyers. The system includes proper authentication, inquiry management, and admin reporting capabilities.
