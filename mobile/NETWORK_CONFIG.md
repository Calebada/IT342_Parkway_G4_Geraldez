# Network Configuration Guide

## Backend URL Configuration for Different Scenarios

Your ParkWay backend runs on `http://localhost:8080`. However, the mobile app needs different configurations based on where it's running.

## Quick Reference

| Scenario | URL | File to Edit |
|----------|-----|--------------|
| Android Emulator (Windows/Mac/Linux) | `http://10.0.2.2:8080` | RetrofitClient.kt |
| Physical Device (Same Network) | `http://192.168.1.x:8080` | RetrofitClient.kt |
| Android Studio Emulator API 28+ | `http://10.0.2.2:8080` | RetrofitClient.kt |
| Genymotion Emulator | `http://10.0.2.2:8080` | RetrofitClient.kt |

## Configuration Steps

### Step 1: Locate Configuration File

**File**: `app/src/main/java/com/parkway/demo/api/RetrofitClient.kt`

### Step 2: Find the BASE_URL Line

```kotlin
object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080"  // ← Change this
```

### Step 3: Replace with Your Configuration

#### For Android Emulator (DEFAULT - No changes needed)
```kotlin
private const val BASE_URL = "http://10.0.2.2:8080"
```

#### For Physical Device
First, find your PC's IP:

**Windows**:
1. Open Command Prompt
2. Type: `ipconfig`
3. Look for "IPv4 Address" (e.g., 192.168.1.100)

**Mac/Linux**:
1. Open Terminal
2. Type: `ifconfig`
3. Look for "inet" address

Then update:
```kotlin
private const val BASE_URL = "http://192.168.1.100:8080"  // Replace with your IP
```

#### For Testing Backend on Different Port
```kotlin
private const val BASE_URL = "http://10.0.2.2:9000"  // If backend runs on port 9000
```

### Step 4: Verify Backend Connection

After changing URL, verify backend is accessible:

**From Android Studio Terminal**:
```bash
cd mobile
./gradlew clean build
```

**Test the endpoint manually** (Windows Command Prompt):
```bash
curl http://10.0.2.2:8080/api/users
```

If successful, you'll see an empty array or user data.

## Special Cases

### Docker Container as Backend
If backend runs in Docker:
- Docker Host: `http://host.docker.internal:8080`
- Docker Network: `http://backend:8080`

### Remote Server (Cloud Deployment)
```kotlin
private const val BASE_URL = "https://api.parkway.com"
```
- Use HTTPS for production
- Enable cleartext traffic only for development

### Multiple Environments

Create different build variants:

**build.gradle**:
```gradle
buildTypes {
    debug {
        buildConfigField "String", "API_URL", "\"http://10.0.2.2:8080\""
    }
    release {
        buildConfigField "String", "API_URL", "\"https://api.parkway.com\""
    }
}
```

Then use in code:
```kotlin
private const val BASE_URL = BuildConfig.API_URL
```

## Troubleshooting Connection Issues

### Issue: Connection Refused
```
java.net.ConnectException: Failed to connect
```

**Solutions**:
- Verify backend is running: `http://localhost:8080/api/users`
- Check firewall allows port 8080
- Verify correct IP/PORT in RetrofitClient.kt
- Wait 30 seconds after starting backend

### Issue: Timeout
```
java.net.SocketTimeoutException: connect timed out
```

**Solutions**:
- Increase timeout in RetrofitClient.kt:
  ```kotlin
  .connectTimeout(30, TimeUnit.SECONDS)
  ```
- Check network connectivity
- Verify backend is responsive

### Issue: Invalid Address
```
java.net.UnknownHostException: Unable to resolve host
```

**Solutions**:
- Hostname/IP is incorrect
- Network connectivity issue
- DNS not resolving

### Issue: SSL Certificate Error (HTTPS)
```
javax.net.ssl.SSLHandshakeException
```

**Solutions** (Development Only):
```kotlin
val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
    override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
    override fun getAcceptedIssuers(): Array<X509Certificate>? = null
})

val sslContext = SSLContext.getInstance("SSL")
sslContext.init(null, trustAllCerts, java.security.SecureRandom())
val httpsClient = OkHttpClient.Builder()
    .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
    .build()
```

## Network Debugging

### Enable Logging

Edit `RetrofitClient.kt`:

```kotlin
private fun getLoggingInterceptor(): HttpLoggingInterceptor {
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BODY  // ← More verbose
    return logging
}
```

Then view in Logcat:
- Window → Tool Windows → Logcat
- Filter: "OkHttp"

### View Network Requests
```
**→ POST http://10.0.2.2:8080/api/users/login
**   Content-Type: application/json
**   Content-Length: 45
**
**   {"email":"test@example.com","password":"12345"}
**
**← 200 OK
**   application/json; charset=utf-8
**   Content-Length: 120
**
**   {"userID":1,"firstname":"John","email":"test@example.com",...}
```

### Using Charles or Fiddler Proxy

1. Set Charles as system proxy
2. Configure in RetrofitClient:
   ```kotlin
   .proxy(Proxy(Proxy.Type.HTTP, InetSocketAddress("127.0.0.1", 8888)))
   ```

## Production Deployment

### HTTPS/TLS Configuration
```kotlin
private const val BASE_URL = "https://api.parkway.com"

private val httpClient = OkHttpClient.Builder()
    .certificatePinner(
        CertificatePinner.Builder()
            .add("api.parkway.com", "sha256/AAAAAAAAAAAAAAAAAAAAAA...")
            .build()
    )
    .build()
```

### Environment Configuration
```kotlin
object RemoteConfig {
    val BASE_URL = when (BuildConfig.FLAVOR) {
        "production" -> "https://api.parkway.com"
        "staging" -> "https://staging-api.parkway.com"
        else -> "http://10.0.2.2:8080"
    }
}
```

## Reference

### Standard Ports
- HTTP: 80
- HTTPS: 443
- Spring Boot Default: 8080

### Emulator Special Addresses
- Localhost: `10.0.2.2`
- Host IP on network: `10.0.2.2`
- First DNS: `10.0.2.3`

### Network Timeouts (In RetrofitClient)
```kotlin
.connectTimeout(15, TimeUnit.SECONDS)
.readTimeout(15, TimeUnit.SECONDS)
.writeTimeout(15, TimeUnit.SECONDS)
```

---

**Questions?** Check the Logcat for detailed error messages. Android Studio's Logcat is your best debugging tool!
