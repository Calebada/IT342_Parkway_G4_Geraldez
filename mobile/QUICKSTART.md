# Quick Start Guide - ParkWay Mobile App

## For Android Studio Iguana Users

### Step 1: Prepare Workspace
1. Open the project in Android Studio
2. Click **File → Open** 
3. Navigate to `IT342_Parkway_G4_Geraldez/mobile`
4. Click **OK**

### Step 2: Configure Backend Connection

The mobile app is configured to connect to your backend at `http://10.0.2.2:8080` (Android emulator default).

#### If using Android Emulator:
- No changes needed - `10.0.2.2` is the automatic bridge to localhost

#### If using Physical Device:
1. Find your PC's IP address
   - Windows: `ipconfig` in Command Prompt → IPv4 Address (e.g., 192.168.1.x)
2. Edit: `app/src/main/java/com/parkway/demo/api/RetrofitClient.kt`
3. Change: 
   ```kotlin
   private const val BASE_URL = "http://YOUR_PC_IP:8080"
   ```

### Step 3: Build & Run

**On Android Studio:**
1. Click **Build → Make Project**
2. Wait for build to complete
3. Click **Run** button (or Shift + F10)
4. Select device/emulator
5. Click **OK**

**From Terminal (Optional):**
```bash
cd mobile
./gradlew build
./gradlew assembleDebug
./gradlew installDebug
```

### Step 4: Test the Application

#### Ensure Backend is Running
```bash
cd backend
mvn spring-boot:run
```

#### Test Flow
1. **Splash Screen** - Shows for 2 seconds
2. **Login Screen** - If not logged in
3. **Registration** - Click "Sign up" to create account
   - Enter: John, Doe, john@example.com, password123
4. **Dashboard** - After successful login
5. **Logout** - Click button to exit

## Common Issues & Solutions

### Issue: Cannot connect to backend
**Solution**: 
- Verify backend is running: `http://localhost:8080/api/users`
- Check `RetrofitClient.kt` - ensure correct IP/PORT
- For emulator, must use `10.0.2.2` not `localhost`

### Issue: Gradle sync fails
**Solution**:
```bash
./gradlew clean
./gradlew --refresh-dependencies
```

### Issue: Build fails with missing resources
**Solution**:
- Clean project: **Build → Clean Project**
- Rebuild: **Build → Make Project**

### Issue: App crashes on emulator
**Solution**:
- Check `android:usesCleartextTraffic="true"` in AndroidManifest.xml
- Verify cleartext traffic enabled in XML config

## Android Studio Features Used

- **Kotlin** - Modern Android language
- **AndroidX** - Latest Android libraries
- **Material Components** - Modern UI design
- **Retrofit** - HTTP requests
- **Coroutines** - Async operations
- **SharedPreferences** - Local storage

## Project Files Overview

| File | Purpose |
|------|---------|
| `build.gradle` | Project and app dependencies |
| `AndroidManifest.xml` | App configuration and permissions |
| `RetrofitClient.kt` | Backend API client configuration |
| `LoginActivity.kt` | Login screen logic |
| `RegisterActivity.kt` | Registration screen logic |
| `DashboardActivity.kt` | Main app screen after login |
| `PreferencesManager.kt` | Session and user data storage |
| `ValidationUtils.kt` | Input validation logic |
| `activity_login.xml` | Login UI layout |
| `activity_register.xml` | Registration UI layout |
| `activity_dashboard.xml` | Dashboard UI layout |

## APK Generation

To generate APK for distribution:

1. Click **Build → Generate Signed Bundle / APK**
2. Select **APK**
3. Create or select keystore
4. Fill in credentials
5. Select **release** build variant
6. Finish

APK will be in: `app/release/app-release.apk`

## Next Steps

After getting login/registration working:

1. **Add Parking Search** - Browse available parking spots
2. **Add Bookings** - Reserve parking spaces
3. **Add Payments** - Implement payment processing
4. **Add Profile** - User settings and preferences
5. **Add Navigation** - Map integration for directions

## Useful Keyboard Shortcuts

| Shortcut | Action |
|----------|--------|
| `Shift + F10` | Run app |
| `Ctrl + Shift + F10` | Run with configuration |
| `Ctrl + B` | Go to definition |
| `Alt + Enter` | Quick fix |
| `Ctrl + /` | Comment/uncomment |
| `Ctrl + D` | Duplicate line |
| `Shift + Delete` | Delete line |

## Android Studio Tips

- **Logcat**: View app logs (View → Tool Windows → Logcat)
- **Layout Preview**: See XML live preview while editing
- **Virtual Device Manager**: Manage emulators (Tools → Device Manager)
- **Profiler**: Monitor performance (View → Tool Windows → Profiler)

---

**Need Help?** Check the full README.md for detailed documentation.
