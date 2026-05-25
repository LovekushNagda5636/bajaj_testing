# Bajaj Finserv Health API Challenge - Java Spring Boot

This is a Java Spring Boot application that simulates the **2024 Bajaj Finserv Health API webhook challenge flow**. The application automatically executes the complete workflow on startup without requiring any manual API calls.

## 🎯 Challenge Flow

The application performs the following steps automatically:

### **STEP 1: Generate Webhook**
- Sends POST request to: `https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA`
- Request body contains: name, registration number, and email
- Receives: webhook URL and JWT access token

### **STEP 2: Determine Question**
- Extracts last 2 digits from registration number
- **Odd last 2 digits** → Question 1
- **Even last 2 digits** → Question 2

### **STEP 3: Prepare SQL Query**
- Selects appropriate SQL query based on question number
- Query is stored as a string (NOT executed)

### **STEP 4: Submit to Webhook**
- Sends POST request to the webhook URL received in Step 1
- Includes JWT token in Authorization header
- Body contains: `{"finalQuery": "YOUR_SQL_QUERY"}`

## 🛠️ Tech Stack

- **Java 17**
- **Spring Boot 4.0.6**
- **Maven**
- **RestTemplate** for HTTP requests
- **Jackson** for JSON processing

## 📁 Project Structure

```
demo/
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── DemoApplication.java          # Main application class
│   │   │   ├── config/
│   │   │   │   └── RestTemplateConfig.java   # RestTemplate configuration
│   │   │   ├── dto/
│   │   │   │   ├── WebhookGenerationRequest.java
│   │   │   │   ├── WebhookGenerationResponse.java
│   │   │   │   └── SqlQueryRequest.java
│   │   │   ├── service/
│   │   │   │   └── BajajChallengeService.java # Core business logic
│   │   │   └── runner/
│   │   │       └── ChallengeRunner.java       # Startup execution
│   │   └── resources/
│   │       └── application.properties         # Configuration
│   └── test/
├── pom.xml
└── README.md
```

## ⚙️ Configuration

Before running the application, update `src/main/resources/application.properties`:

```properties
# Your full name
bajaj.user.name=John Doe

# Your registration number
bajaj.user.regNo=12345

# Your email address
bajaj.user.email=john.doe@example.com
```

## 🚀 How to Run

### Option 1: Using Maven

```bash
cd demo
mvn spring-boot:run
```

### Option 2: Build and Run JAR

```bash
cd demo
mvn clean package
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

## 📝 SQL Queries

The application includes example SQL queries for both questions. **Update these in `BajajChallengeService.java`** based on your actual challenge requirements:

### Question 1 (Odd registration number)
```java
private String getSqlQueryForQuestion(int questionNumber) {
    if (questionNumber == 1) {
        return "YOUR_QUESTION_1_SQL_QUERY";
    }
    // ...
}
```

### Question 2 (Even registration number)
```java
private String getSqlQueryForQuestion(int questionNumber) {
    // ...
    else {
        return "YOUR_QUESTION_2_SQL_QUERY";
    }
}
```

## 📊 Expected Output

When you run the application, you'll see logs like:

```
========================================
Starting Bajaj Finserv Health API Challenge
========================================
STEP 1: Generating webhook...
Request: WebhookGenerationRequest{name='John Doe', regNo='12345', email='john.doe@example.com'}
Webhook generated successfully!
Webhook URL: https://...
Access Token received: eyJhbGciOiJIUzI1NiIs...

STEP 2: Determining question from registration number...
Last 2 digits of regNo: 45
Odd number detected → Question 1
Question Number: 1

STEP 3: Preparing SQL query for Question 1...
SQL Query: SELECT employee_id, first_name...

STEP 4: Submitting SQL query to webhook...
Request: SqlQueryRequest{finalQuery='SELECT employee_id...'}
Response Status: 200 OK
Response Body: {...}
SQL query submitted successfully!

========================================
Challenge completed successfully!
========================================
```

## ⚠️ Important Notes

- **NO database required** - SQL queries are sent as strings only
- **NO REST controllers** - Application runs automatically on startup
- **NO frontend** - Pure backend client application
- **NO authentication endpoints** - Uses JWT from Bajaj API
- The application will exit after completing the workflow

## 🔧 Troubleshooting

### Connection Timeout
- Check your internet connection
- Verify the Bajaj API endpoint is accessible

### Invalid Response
- Ensure your name, regNo, and email are correct in `application.properties`
- Check logs for detailed error messages

### Build Errors
- Ensure Java 17 is installed: `java -version`
- Ensure Maven is installed: `mvn -version`

## 📦 Dependencies

All dependencies are managed in `pom.xml`:
- `spring-boot-starter-web` - For RestTemplate and HTTP support
- `spring-boot-starter-test` - For testing (optional)
- `jackson-databind` - For JSON serialization/deserialization

## 🎓 Learning Points

This application demonstrates:
- ✅ Spring Boot CommandLineRunner for startup execution
- ✅ RestTemplate for HTTP client operations
- ✅ JWT Bearer token authentication
- ✅ DTO pattern for request/response handling
- ✅ Service layer architecture
- ✅ Externalized configuration with application.properties
- ✅ Proper logging with SLF4J
- ✅ Error handling for HTTP operations

## 📄 License

This is a challenge submission project for educational purposes.

---

**Built with ❤️ for Bajaj Finserv Health API Challenge**
