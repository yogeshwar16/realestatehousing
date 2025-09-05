# Property Listing iOS App

A native iOS application for property listing and management, built with Swift and UIKit.

## Features

- **User Authentication**: Mobile OTP-based signup and login
- **Property Browsing**: Search and filter properties by type, location, and price
- **Property Details**: Detailed property information with images and seller contact
- **Inquiry System**: Submit inquiries for properties with terms acceptance
- **Profile Management**: Update user profile information
- **Terms & Conditions**: Built-in terms and conditions screen

## Architecture

### Project Structure
```
PropertyListing/
├── App/
│   ├── AppDelegate.swift
│   ├── SceneDelegate.swift
│   └── Info.plist
├── Models/
│   ├── User.swift
│   ├── Property.swift
│   ├── Inquiry.swift
│   └── APIResponse.swift
├── Services/
│   └── APIService.swift
├── Utils/
│   ├── UserManager.swift
│   └── Extensions.swift
├── Controllers/
│   ├── SplashViewController.swift
│   ├── SignupViewController.swift
│   ├── LoginViewController.swift
│   ├── DashboardViewController.swift
│   ├── PropertyDetailViewController.swift
│   ├── InquiryViewController.swift
│   ├── ProfileViewController.swift
│   ├── TermsConditionsViewController.swift
│   └── MainTabBarController.swift
├── Views/
│   ├── PropertyCollectionViewCell.swift
│   └── PropertyCollectionViewCell.xib
└── Resources/
    ├── LaunchScreen.storyboard
    └── Assets.xcassets/
```

### Key Components

#### Models
- **User**: User entity with authentication and profile data
- **Property**: Property listing with details, images, and seller info
- **Inquiry**: Inquiry requests with terms acceptance
- **APIResponse**: Generic API response wrapper

#### Services
- **APIService**: Singleton service for REST API communication
- **UserManager**: User session and authentication management

#### Controllers
- **Authentication Flow**: Splash → Signup/Login → Dashboard
- **Main Flow**: Dashboard (Property List) → Property Details → Inquiry
- **Profile Flow**: Profile Management → Terms & Conditions

## Setup Instructions

### Prerequisites
- Xcode 14.0 or later
- iOS 13.0 or later
- Swift 5.0 or later

### Configuration

1. **Backend URL**: Update the base URL in `APIService.swift`:
```swift
private let baseURL = "http://your-backend-url:8080/api"
```

2. **Info.plist**: Configure required permissions:
   - Location access for property search
   - Camera and photo library for property images
   - Network security settings for HTTP requests

3. **App Icons**: Add app icon images to `Assets.xcassets/AppIcon.appiconset/`

### Build and Run

1. Open `PropertyListing.xcodeproj` in Xcode
2. Select target device or simulator
3. Build and run the project (⌘+R)

## API Integration

The app integrates with the Property Listing backend API:

### Authentication Endpoints
- `POST /auth/signup` - User registration
- `POST /auth/send-otp` - Send OTP for login
- `POST /auth/login` - Login with OTP
- `PUT /auth/user/{mobileNumber}` - Update user profile

### Property Endpoints
- `GET /properties` - Get all properties
- `GET /properties/search` - Search properties with filters
- `POST /properties` - Create new property (sellers only)

### Inquiry Endpoints
- `POST /inquiries` - Create property inquiry

## UI/UX Design

### Color Scheme
- **Primary**: Blue (#1947D1) - Navigation, buttons, accents
- **Secondary**: Orange (#FF6B35) - Action buttons, highlights
- **Background**: White/System backgrounds
- **Text**: System label colors for accessibility

### Typography
- **Headings**: System font, bold weights
- **Body**: System font, regular/medium weights
- **Captions**: System font, light weights

### Components
- **Cards**: Rounded corners, subtle shadows
- **Buttons**: Rounded, filled with primary/secondary colors
- **Text Fields**: Bordered, with icon prefixes
- **Collection Views**: Grid layout for property listings

## Key Features Implementation

### Authentication Flow
1. **Splash Screen**: App intro with animated logo
2. **Signup**: Form validation, terms acceptance, API integration
3. **Login**: Mobile number input, OTP verification, session management

### Property Browsing
1. **Dashboard**: Collection view with property cards
2. **Search & Filter**: Property type, location, price range filters
3. **Property Details**: Full property information, seller contact

### Inquiry System
1. **Inquiry Form**: Property context, custom message, terms acceptance
2. **API Integration**: Secure inquiry submission with user authentication
3. **Validation**: Input validation, terms acceptance enforcement

### Profile Management
1. **Profile View**: Editable user information
2. **Update API**: Profile update with validation
3. **Session Management**: Login/logout functionality

## Error Handling

- Network error handling with user-friendly messages
- Form validation with inline error display
- API error response parsing and display
- Loading states with activity indicators

## Security

- JWT token-based authentication (if implemented on backend)
- Input validation and sanitization
- Secure storage of user session data
- HTTPS communication (configure in production)

## Future Enhancements

- Push notifications for inquiry responses
- Image upload for property listings
- Map integration for property locations
- Favorites and saved searches
- Chat system for buyer-seller communication
- Advanced property filters and sorting

## Testing

- Unit tests for models and services
- UI tests for critical user flows
- API integration tests
- Device compatibility testing

## Deployment

1. **Development**: Use development backend URL
2. **Staging**: Configure staging environment
3. **Production**: 
   - Update backend URL to production
   - Configure HTTPS and security settings
   - Submit to App Store Connect

## Support

For technical support or questions:
- Email: support@propertylistingapp.com
- Documentation: Check backend API documentation
- Issues: Report bugs and feature requests
