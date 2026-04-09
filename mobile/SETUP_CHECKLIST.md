# Android Studio Import & Setup Checklist

Use this checklist to ensure proper setup of the ParkWay Mobile App in Android Studio.

## Pre-Setup

- [ ] Android Studio (Iguana or later) is installed
- [ ] Java 17+ is installed
- [ ] Android SDK 34+ is installed
- [ ] Internet connection available (for Gradle download)
- [ ] Backup of previous projects

## Step 1: Open Project in Android Studio

- [ ] Launch Android Studio
- [ ] Click **File → Open**
- [ ] Navigate to `C:\IT342_Parkway_G4_Geraldez\mobile`
- [ ] Select the **mobile** folder
- [ ] Click **OK**

## Step 2: Wait for Gradle Sync

- [ ] Gradle sync starts automatically
- [ ] Monitor progress in bottom status bar
- [ ] Build runs successfully (no red errors)
- [ ] "Build Successful" message appears
- [ ] Gradle files are parsed correctly

## Step 3: Review Project Structure

Verify these folders exist in Project view:

**Left sidebar - Project structure:**
- [ ] `app/` folder visible
- [ ] `src/` under app
- [ ] `main/` under src
- [ ] `java/` folder visible
- [ ] `res/` folder visible
- [ ] `AndroidManifest.xml` file visible

**Code structure in `java/com/parkway/demo/`:**
- [ ] `activity/` folder (contains 4 .kt files)
- [ ] `api/` folder (contains 2 .kt files)
- [ ] `model/` folder (contains 1 .kt file)
- [ ] `utils/` folder (contains 2 .kt files)

**Resources in `res/`:**
- [ ] `layout/` folder (contains 4 .xml files)
- [ ] `values/` folder (contains 4 .xml files)
- [ ] `drawable/` folder (contains 6 .xml files)
- [ ] `xml/` folder (contains 2 .xml files)

## Step 4: Check Gradle Configuration

- [ ] Open `build.gradle` (app level)
- [ ] Verify `compileSdk 34`
- [ ] Verify targetSdk `34`
- [ ] Check dependencies are loaded:
  - [ ] androidX.core-ktx
  - [ ] androidX.appcompat
  - [ ] material components
  - [ ] retrofit2
  - [ ] gson
  - [ ] coroutines
- [ ] No error marks (red X) in Gradle files

## Step 5: Configure Backend URL

**Important**: This must be done before running the app!

- [ ] Navigate to: `app/src/main/java/com/parkway/demo/api/RetrofitClient.kt`
- [ ] Find line: `private const val BASE_URL = "http://10.0.2.2:8080"`

Choose your setup:

**For Android Emulator** (default):
  - [ ] Keep `BASE_URL = "http://10.0.2.2:8080"` (no change needed)

**For Physical Device**:
  - [ ] Open Command Prompt: `ipconfig`
  - [ ] Find your PC's IPv4 address (e.g., 192.168.1.100)
  - [ ] Update: `private const val BASE_URL = "http://YOUR_IP:8080"`
  - [ ] Example: `private const val BASE_URL = "http://192.168.1.100:8080"`

## Step 6: Verify Backend is Running

**Before running the app, verify backend is accessible:**

Option A - Using Browser:
- [ ] Open: `http://localhost:8080/api/users`
- [ ] Should return empty array or user data (not error)

Option B - Using Command Prompt:
- [ ] Run: `curl http://localhost:8080/api/users`
- [ ] Should return valid JSON response

Option C - Check from terminal:
```bash
cd backend
mvn spring-boot:run
```
- [ ] Backend starts without errors
- [ ] "Started ParkwayApplication" message appears

## Step 7: Verify AndroidManifest.xml

- [ ] Open: `app/src/main/AndroidManifest.xml`
- [ ] Verify permissions:
  - [ ] `<uses-permission android:name="android.permission.INTERNET" />`
  - [ ] `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />`
- [ ] Verify activities are listed:
  - [ ] `SplashActivity` with MAIN intent
  - [ ] `LoginActivity`
  - [ ] `RegisterActivity`
  - [ ] `DashboardActivity`
- [ ] Verify: `android:usesCleartextTraffic="true"`

## Step 8: Build the Project

- [ ] Click **Build → Clean Project**
- [ ] Wait for clean to complete (progress bar)
- [ ] Click **Build → Make Project**
- [ ] Wait for build to complete
- [ ] Verify: "Build Successful" message in status bar
- [ ] No error messages (red text) in Build Output

## Step 9: Run the App

**Device Setup:**

**For Android Emulator**:
- [ ] Go to **Tools → Device Manager**
- [ ] Create or select an emulator (API 24+)
- [ ] Click play button to start emulator
- [ ] Wait for emulator to fully boot (home screen visible)

**For Physical Device**:
- [ ] Connect device via USB cable
- [ ] Enable USB debugging on device
- [ ] Device appears in device list in Android Studio

**Run the App:**
- [ ] Click **Run** button (green play icon)
- [ ] Or press **Shift + F10**
- [ ] Select device from popup dialog
- [ ] Click **OK**
- [ ] Wait for app to compile and deploy (1-2 minutes)
- [ ] App should launch on device

## Step 10: Verify App Launch

- [ ] App starts without crashes
- [ ] Splash screen shows ParkWay logo
- [ ] Progress indicator spins for 2 seconds
- [ ] After 2 seconds, redirected to Login screen
- [ ] If already logged in: redirected to Dashboard

## Step 11: Test Login Functionality

**Create Test Account:**
- [ ] Click "Sign up" link on Login screen
- [ ] Fill in fields:
  - [ ] Username: testuser
  - [ ] First Name: John
  - [ ] Last Name: Doe
  - [ ] Email: john@example.com
  - [ ] Password: password123
  - [ ] Confirm: password123
- [ ] Click Register button
- [ ] Success message should appear
- [ ] After 2 seconds, auto-redirect to Login

**Login with Test Account:**
- [ ] Enter Email: john@example.com
- [ ] Enter Password: password123
- [ ] Click Login button
- [ ] Success message should appear
- [ ] After 2 seconds, redirected to Dashboard
- [ ] Dashboard shows "Welcome to ParkWay"
- [ ] Dashboard shows your name: "John Doe"

## Step 12: Test Logout

- [ ] From Dashboard, click logout button (top right)
- [ ] Redirected back to Login screen
- [ ] User data is cleared from preferences

## Step 13: Review Logcat for Errors

- [ ] Open: **View → Tool Windows → Logcat**
- [ ] Filter: type "OkHttp" or "parkway"
- [ ] Verify no red error messages
- [ ] Should see network requests to backend
- [ ] Response codes should be 200-201 (success)

## Troubleshooting Checklist

If you encounter issues, check:

**Build Issues:**
- [ ] Gradle cache cleared: `./gradlew clean`
- [ ] All dependencies downloaded
- [ ] Java version is 17+
- [ ] Android SDK 34 is installed

**Runtime Issues:**
- [ ] Backend server is running on port 8080
- [ ] Correct URL in RetrofitClient.kt
- [ ] For emulator: using `10.0.2.2` not `localhost`
- [ ] For device: using actual PC IP address
- [ ] Firewall allows port 8080
- [ ] No proxy interference

**App Crashes:**
- [ ] Check Logcat for exception details
- [ ] Verify AndroidManifest.xml is valid
- [ ] Ensure all resources exist in res folder
- [ ] Check cleartext traffic is enabled

**Network Issues:**
- [ ] Ping backend: `ping localhost` or `ping YOUR_IP`
- [ ] Test curl: `curl http://localhost:8080/api/users`
- [ ] Check firewall settings
- [ ] Verify backend is fully started

## Final Verification

- [ ] ✅ Project opens without errors
- [ ] ✅ Gradle sync succeeds
- [ ] ✅ Project builds successfully
- [ ] ✅ App runs on emulator/device
- [ ] ✅ Login screen displays correctly
- [ ] ✅ Registration works
- [ ] ✅ Login with credentials works
- [ ] ✅ Dashboard appears after login
- [ ] ✅ Logout works
- [ ] ✅ No crashes or errors in Logcat

## Next Steps After Verification

1. **Review Code**
   - [ ] Open LoginActivity.kt
   - [ ] Review network requests
   - [ ] Understand validation flow

2. **Explore App**
   - [ ] Test form validation
   - [ ] Test error messages
   - [ ] Check response handling

3. **Study Architecture**
   - [ ] Review RetrofitClient setup
   - [ ] Understand PreferencesManager
   - [ ] Study ValidationUtils

4. **Read Documentation**
   - [ ] Read README.md
   - [ ] Review QUICKSTART.md
   - [ ] Check KOTLIN_BEST_PRACTICES.md

## Documentation Files to Review

**Essential:**
1. [ ] `README.md` - Main documentation
2. [ ] `QUICKSTART.md` - Quick setup guide

**Reference:**
3. [ ] `KOTLIN_BEST_PRACTICES.md` - Code patterns
4. [ ] `NETWORK_CONFIG.md` - Backend URLs
5. [ ] `PROJECT_SUMMARY.md` - What was built
6. [ ] `DIRECTORY_STRUCTURE.txt` - File organization

## Support Information

If you encounter issues:

1. **Check Logcat**: View → Tool Windows → Logcat
2. **Read Error Messages**: They usually indicate the problem
3. **Review Documentation**: Check NETWORK_CONFIG.md or QUICKSTART.md
4. **Clean & Rebuild**: Build → Clean → Make Project
5. **Restart Android Studio**: Sometimes fixes caching issues

---

## Completion Status

When all checkboxes are marked, your setup is complete and ready for development!

**Setup Date**: _____________  
**Verified By**: _____________  
**Notes**: _________________________________________________________________

---

**Good luck with your ParkWay Mobile App development! 🚀**
