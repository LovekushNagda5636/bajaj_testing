# Bajaj Finserv Health API Challenge - Usage Guide

## 🚀 Quick Start

### Step 1: Configure Your Details

Open `src/main/resources/application.properties` and update:

```properties
bajaj.user.name=Your Full Name
bajaj.user.regNo=Your Registration Number
bajaj.user.email=your.email@example.com
```

### Step 2: Update SQL Queries

Open `src/main/java/com/example/demo/service/BajajChallengeService.java` and locate the `getSqlQueryForQuestion` method (around line 130).

**For Question 1 (Odd registration number):**
```java
if (questionNumber == 1) {
    return "YOUR_ACTUAL_SQL_QUERY_FOR_QUESTION_1";
}
```

**For Question 2 (Even registration number):**
```java
else {
    return "YOUR_ACTUAL_SQL_QUERY_FOR_QUESTION_2";
}
```

### Step 3: Run the Application

```bash
cd demo
mvn spring-boot:run
```

Or build and run the JAR:

```bash
mvn clean package
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

## 📋 Understanding the Flow

### What Happens When You Run?

1. **Application Starts**
   - Spring Boot initializes
   - `ChallengeRunner` executes automatically

2. **STEP 1: Generate Webhook**
   ```
   POST https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA
   Body: {"name":"...", "regNo":"...", "email":"..."}
   ```
   - Receives webhook URL and JWT token

3. **STEP 2: Determine Question**
   - Extracts last 2 digits from registration number
   - Odd → Question 1
   - Even → Question 2

4. **STEP 3: Prepare SQL Query**
   - Selects appropriate query based on question number

5. **STEP 4: Submit to Webhook**
   ```
   POST <webhook_url>
   Headers: Authorization: Bearer <jwt_token>
   Body: {"finalQuery":"YOUR_SQL_QUERY"}
   ```

## 📊 Expected Console Output

```
========================================
Starting Bajaj Finserv Health API Challenge
========================================
STEP 1: Generating webhook...
Request: WebhookGenerationRequest{name='John Doe', regNo='12345', email='john.doe@example.com'}
Webhook generated successfully!
Webhook URL: https://bfhldevapigw.healthrx.co.in/webhook/...
Access Token received: eyJhbGciOiJIUzI1NiIs...

STEP 2: Determining question from registration number...
Last 2 digits of regNo: 45
Odd number detected → Question 1
Question Number: 1

STEP 3: Preparing SQL query for Question 1...
SQL Query: SELECT employee_id, first_name, last_name, salary FROM employees WHERE salary > 50000 ORDER BY salary DESC;

STEP 4: Submitting SQL query to webhook...
Request: SqlQueryRequest{finalQuery='SELECT employee_id...'}
Response Status: 200 OK
Response Body: {"status":"success","message":"Query received"}
SQL query submitted successfully!

========================================
Challenge completed successfully!
========================================
```

## 🔍 Troubleshooting

### Issue: Connection Timeout

**Symptom:**
```
Error generating webhook: Connection timed out
```

**Solution:**
- Check your internet connection
- Verify the Bajaj API endpoint is accessible
- Try increasing timeout in `RestTemplateConfig.java`

### Issue: 401 Unauthorized

**Symptom:**
```
HTTP Error during SQL submission: 401 - Unauthorized
```

**Solution:**
- Verify your name, regNo, and email are correct
- Ensure the JWT token was received in Step 1
- Check if the token has expired (run again)

### Issue: 400 Bad Request

**Symptom:**
```
HTTP Error during webhook generation: 400 - Bad Request
```

**Solution:**
- Verify your registration number format
- Ensure email is valid
- Check that all fields in application.properties are filled

### Issue: SQL Query Not Accepted

**Symptom:**
```
Response Status: 400 BAD_REQUEST
Response Body: {"error":"Invalid SQL query"}
```

**Solution:**
- Review your SQL query syntax
- Ensure the query matches the question requirements
- Check for missing semicolons or quotes

## 🛠️ Customization

### Change Timeout Values

Edit `src/main/java/com/example/demo/config/RestTemplateConfig.java`:

```java
factory.setConnectTimeout(20000); // 20 seconds
factory.setReadTimeout(60000);    // 60 seconds
```

### Add More Logging

Edit `src/main/resources/application.properties`:

```properties
logging.level.com.example.demo=DEBUG
logging.level.org.springframework.web.client=DEBUG
```

### Disable Auto-Execution

If you want to prevent automatic execution on startup, comment out the `@Component` annotation in `ChallengeRunner.java`:

```java
// @Component
public class ChallengeRunner implements CommandLineRunner {
    // ...
}
```

## 📝 SQL Query Examples

### Question 1 Example (Odd RegNo)
```sql
SELECT 
    e.employee_id,
    e.first_name,
    e.last_name,
    e.salary,
    d.department_name
FROM employees e
JOIN departments d ON e.department_id = d.department_id
WHERE e.salary > 50000
ORDER BY e.salary DESC;
```

### Question 2 Example (Even RegNo)
```sql
SELECT 
    d.department_id,
    d.department_name,
    COUNT(e.employee_id) as employee_count,
    AVG(e.salary) as avg_salary
FROM departments d
LEFT JOIN employees e ON d.department_id = e.department_id
GROUP BY d.department_id, d.department_name
HAVING COUNT(e.employee_id) > 5
ORDER BY employee_count DESC;
```

## 🧪 Testing Different Scenarios

### Test with Odd Registration Number
```properties
bajaj.user.regNo=12345  # Last 2 digits: 45 (odd)
```
Expected: Question 1 will be selected

### Test with Even Registration Number
```properties
bajaj.user.regNo=12346  # Last 2 digits: 46 (even)
```
Expected: Question 2 will be selected

## 📦 Building for Submission

### Create Executable JAR

```bash
mvn clean package
```

The JAR will be created at: `target/demo-0.0.1-SNAPSHOT.jar`

### Run the JAR

```bash
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

### Verify JAR Contents

```bash
jar tf target/demo-0.0.1-SNAPSHOT.jar
```

## 🔐 Security Notes

- **Never commit** your actual registration number or email to version control
- The JWT token is temporary and expires after use
- No sensitive data is stored in the application

## 📚 Project Structure Explained

```
demo/
├── src/main/java/com/example/demo/
│   ├── DemoApplication.java           # Main Spring Boot application
│   ├── config/
│   │   └── RestTemplateConfig.java    # HTTP client configuration
│   ├── dto/
│   │   ├── WebhookGenerationRequest.java   # Request to generate webhook
│   │   ├── WebhookGenerationResponse.java  # Response with webhook & JWT
│   │   └── SqlQueryRequest.java            # SQL query submission
│   ├── service/
│   │   └── BajajChallengeService.java      # Core business logic
│   └── runner/
│       └── ChallengeRunner.java            # Startup execution
└── src/main/resources/
    └── application.properties          # Configuration
```

## 🎯 Key Points to Remember

1. ✅ **No Database Required** - SQL queries are sent as strings only
2. ✅ **No REST Controllers** - Application runs automatically on startup
3. ✅ **No Frontend** - Pure backend client application
4. ✅ **Automatic Execution** - Uses CommandLineRunner
5. ✅ **JWT Authentication** - Token received from Bajaj API
6. ✅ **Question Selection** - Based on registration number parity

## 📞 Support

If you encounter issues:
1. Check the console logs for detailed error messages
2. Verify all configuration in `application.properties`
3. Ensure Java 17 and Maven are properly installed
4. Review the troubleshooting section above

---

**Good luck with your Bajaj Finserv Health API Challenge! 🚀**
