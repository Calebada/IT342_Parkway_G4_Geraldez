# ParkWay Mobile Application

This is an Android mobile application for the ParkWay parking management system, developed with **Kotlin** and **XML** layouts.

## Features

### ✅ Implemented
- **User Registration**: Create new account with validation
- **User Login**: Authenticate users with email and password
- **Dashboard**: Welcome screen after successful login
- **Authentication**: Session management using SharedPreferences
- **Backend Integration**: Connected to Phase 1 backend APIs
- **Input Validation**: Client-side validation for all forms
- **Error Handling**: User-friendly error messages
- **Responsive Design**: Material Design UI matching web app

### 🔄 Coming Soon
- View and book parking spaces
- My bookings management
- User profile and preferences
- Payment integration
- Real-time parking availability
- Navigation to parking locations

## Architecture

The application follows best practices with:
- **MVVM Pattern**: Clean separation of concerns
- **Retrofit**: HTTP client for API communication
- **Coroutines**: Asynchronous operations
- **SharedPreferences**: Local data persistence
- **Material Design**: Modern UI components

## Project Structure

```
mobile/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/parkway/demo/
│   │   │   │   ├── activity/          # Activities (Login, Register, Dashboard)
│   │   │   │   ├── api/              # Retrofit API client
│   │   │   │   ├── model/            # Data models
│   │   │   │   └── utils/            # Utility classes
│   │   │   ├── res/
│   │   │   │   ├── layout/           # XML layouts
│   │   │   │   ├── values/           # Colors, strings, styles
│   │   │   │   ├── drawable/         # Icons and shapes
│   │   │   │   └── xml/              # Configuration files
│   │   │   └── AndroidManifest.xml
│   ├── build.gradle
│   └── proguard-rules.pro
├── build.gradle
└── settings.gradle
```

## Setup Instructions

### Prerequisites
- Android Studio (Iguana or later)
- Java 17+
- Android SDK 34+
- Gradle 8.1+

### Installation

1. **Open Project in Android Studio**
   ```bash
   git clone <repository-url>
   cd IT342_Parkway_G4_Geraldez/mobile
   ```

2. **Open with Android Studio**
   - File → Open → Select the `mobile` folder
   - Wait for Gradle sync to complete

3. **Configure Backend URL**
   - Edit `app/src/main/java/com/parkway/demo/api/RetrofitClient.kt`
   - Change `BASE_URL` if your backend is not on `localhost:8080`
   
   For **emulator**:
   ```kotlin
   private const val BASE_URL = "http://10.0.2.2:8080"
   ```
   
   For **physical device**:
   ```kotlin
   private const val BASE_URL = "http://192.168.x.x:8080"  // Replace with your PC's IP
   ```

4. **Build the Project**
   - Build → Make Project
   - Wait for build to complete

5. **Run the Application**
   
   **Option A: Android Emulator**
   - Select emulator from device dropdown
   - Click Run (Shift + F10)
   
   **Option B: Physical Device**
   - Connect device via USB
   - Enable USB debugging
   - Click Run (Shift + F10)

## API Endpoints

The app communicates with these backend endpoints:

### Authentication
- **POST** `/api/users/register` - Register new user
- **POST** `/api/users/login` - Login user

### Request/Response Models

#### Registration Request
```json
{
  "firstname": "John",
  "lastname": "Doe",
  "email": "john@example.com",
  "password": "password123"
}
```

#### Login Request
```json
{
  "email": "john@example.com",
  "password": "password123"
}
```

#### Login Response
```json
{
  "userID": 1,
  "firstname": "John",
  "lastname": "Doe",
  "email": "john@example.com",
  "role": "user"
}
```

## Testing

### Manual Testing Credentials
```
Email: testuser@example.com
Password: password123
```

### Test Cases

1. **Login with valid credentials**
   - Launch app
   - Enter valid email and password
   - Verify successful login and navigation to Dashboard

2. **Login with invalid credentials**
   - Enter wrong email or password
   - Verify error message displays

3. **Registration with valid data**
   - Click "Sign up" link
   - Fill all fields correctly
   - Verify success message and redirect to login

4. **Registration validation**
   - Test with empty fields
   - Test with invalid email format
   - Test with mismatched passwords
   - Verify appropriate error messages

5. **Session persistence**
   - Login successfully
   - Close app and reopen
   - Verify still logged in on Dashboard

6. **Logout**
   - Click logout button on Dashboard
   - Verify redirect to Login screen
   - Verify user data cleared

## Troubleshooting

### Network Connection Issues
- **Android Emulator**: Use `10.0.2.2` instead of `localhost`
- **Physical Device**: Ensure both phone and PC are on same network
- **Firewall**: Check if port 8080 is open

### Build Errors
```bash
# Clean and rebuild
./gradlew clean build
```

### Connection Refused
- Verify backend is running on port 8080
- Check `BASE_URL` in `RetrofitClient.kt`
- Verify firewall settings

### Gradle Issues
```bash
# Update gradle wrapper
./gradlew wrapper --gradle-version=8.1
```

## Dependencies

- **AndroidX**: Core functionality
- **Material Design**: UI components
- **Retrofit 2**: HTTP client
- **Gson**: JSON serialization
- **Coroutines**: Async operations
- **OkHttp**: HTTP interceptor for logging

## Design System

### Colors
- **Primary**: Cyan (#00BCD4)
- **Dark Cyan**: #0097A7
- **Light Cyan**: #B2EBF2
- **Error Red**: #FF4444
- **Success Green**: #4CAF50

### Typography
- **Headings**: 24-32sp, Bold
- **Body**: 14-16sp, Regular
- **Small Text**: 12sp, Regular

## Screenshots

### Login Screen
- Email input field
- Password input field
- Login button
- Register link

### Registration Screen
- Username field
- First name field
- Last name field
- Email field
- Password field
- Confirm password field
- Register button
- Login link

### Dashboard
- Welcome message with user name
- Logout button
- Feature cards (Coming Soon)

## Development Guide

### Adding New Features

1. **Create new Activity**
   ```kotlin
   class NewFeatureActivity : AppCompatActivity() {
       override fun onCreate(savedInstanceState: Bundle?) {
           super.onCreate(savedInstanceState)
           setContentView(R.layout.activity_new_feature)
       }
   }
   ```

2. **Add layout XML**
   ```xml
   <!-- res/layout/activity_new_feature.xml -->
   <LinearLayout>
       <!-- Your layout here -->
   </LinearLayout>
   ```

3. **Register in AndroidManifest.xml**
   ```xml
   <activity android:name=".activity.NewFeatureActivity" />
   ```

## Version History

- **v1.0** - Initial release with login and registration

## Contributing

To contribute:
1. Create feature branch
2. Make changes
3. Test thoroughly
4. Submit pull request

## License

This project is part of IT342 Group 4 by Parkway Team.

## Support

For issues or questions, contact the development team or create an issue in the repository.

---

**Last Updated**: April 2026  
**Maintainer**: Parkway Development Team
