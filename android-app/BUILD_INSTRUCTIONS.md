# Android APK Build Instructions

## Quick Build with Android Studio (Recommended)

### Prerequisites
- Android Studio (latest version)
- Android SDK 34
- Java 11 or Java 17 (Java 24 causes compatibility issues)

### Steps
1. Open Android Studio
2. Click "Open an existing project"
3. Navigate to `C:\Users\yoges\CascadeProjects\PropertyListingApp\android-app`
4. Wait for Gradle sync to complete
5. Go to **Build** → **Build Bundle(s) / APK(s)** → **Build APK(s)**
6. APK will be generated in `app/build/outputs/apk/debug/app-debug.apk`

## Command Line Build (Alternative)

### Fix Java Version Issue
Your system has Java 24, but Android Gradle Plugin requires Java 11-19. 

**Option 1: Install Java 17**
```powershell
# Download and install Java 17 from Oracle or OpenJDK
# Set JAVA_HOME to Java 17 installation path
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
```

**Option 2: Use Android Studio's Embedded JDK**
```powershell
# Set JAVA_HOME to Android Studio's JDK
$env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"
```

### Build Commands
```powershell
# Navigate to project directory
cd C:\Users\yoges\CascadeProjects\PropertyListingApp\android-app

# Clean and build debug APK
.\gradlew.bat clean assembleDebug

# Build release APK (requires signing)
.\gradlew.bat clean assembleRelease
```

## APK Location
After successful build:
- **Debug APK**: `app\build\outputs\apk\debug\app-debug.apk`
- **Release APK**: `app\build\outputs\apk\release\app-release.apk`

## Troubleshooting

### Common Issues
1. **Java Version**: Use Java 11-19, not Java 24
2. **Android SDK**: Ensure SDK 34 is installed
3. **Gradle Sync**: Let Android Studio sync all dependencies
4. **Build Tools**: Ensure latest Android build tools are installed

### Alternative Build Methods
1. **Android Studio GUI**: Most reliable method
2. **Command Line**: Requires proper Java version
3. **CI/CD**: Use GitHub Actions or similar with Java 17

## App Configuration

### Backend URL
Update the backend URL in `ApiClient.java`:
```java
private static final String BASE_URL = "http://your-backend-ip:8080/api/";
```

### Google Maps API Key
Add your Google Maps API key in `AndroidManifest.xml`:
```xml
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="YOUR_GOOGLE_MAPS_API_KEY" />
```

## Testing the APK

1. Enable **Developer Options** on your Android device
2. Enable **USB Debugging**
3. Install APK: `adb install app-debug.apk`
4. Or transfer APK to device and install manually

## App Features Included

✅ User signup/login with OTP
✅ Property browsing and search
✅ Property details and inquiry
✅ Profile management
✅ Terms & conditions
✅ Modern Material Design UI
✅ API integration with backend

The app is ready for testing and deployment!
