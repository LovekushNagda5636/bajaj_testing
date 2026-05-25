# 🚀 Bajaj Challenge - Quick Setup Checklist

## Before You Start

### ✅ Prerequisites Installed

- [ ] **Java 17** installed
  ```bash
  java -version
  # Should show: java version "17.x.x"
  ```

- [ ] **Maven** installed
  ```bash
  mvn -version
  # Should show: Apache Maven 3.x.x
  ```

- [ ] **Internet connection** active (required for API calls)

## 📝 Configuration Steps

### Step 1: Update Personal Details

File: `src/main/resources/application.properties`

- [ ] Replace `bajaj.user.name` with your full name
- [ ] Replace `bajaj.user.regNo` with your registration number
- [ ] Replace `bajaj.user.email` with your email address

```properties
bajaj.user.name=Your Full Name Here
bajaj.user.regNo=Your Registration Number
bajaj.user.email=your.email@example.com
```

### Step 2: Update SQL Queries

File: `src/main/java/com/example/demo/service/BajajChallengeService.java`

Method: `getSqlQueryForQuestion` (around line 130)

- [ ] Replace Question 1 SQL query (for odd registration numbers)
- [ ] Replace Question 2 SQL query (for even registration numbers)
- [ ] Verify both queries end with semicolon (`;`)
- [ ] Test query syntax if possible

### Step 3: Verify Project Structure

- [ ] All files are in correct locations
- [ ] No compilation errors
- [ ] `pom.xml` is intact

## 🧪 Testing

### Compile Test

```bash
cd demo
mvn clean compile
```

- [ ] Build succeeds with "BUILD SUCCESS"
- [ ] No compilation errors

### Package Test

```bash
mvn clean package
```

- [ ] Build succeeds
- [ ] JAR file created in `target/` directory
- [ ] File: `target/demo-0.0.1-SNAPSHOT.jar` exists

## 🎯 Execution

### Run with Maven

```bash
mvn spring-boot:run
```

### Or Run JAR Directly

```bash
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

## 📊 Expected Behavior

- [ ] Application starts without errors
- [ ] Logs show "Starting Bajaj Finserv Health API Challenge"
- [ ] STEP 1: Webhook generation succeeds
- [ ] STEP 2: Question number is determined
- [ ] STEP 3: SQL query is prepared
- [ ] STEP 4: Query is submitted to webhook
- [ ] Logs show "Challenge completed successfully!"
- [ ] Application exits

## 🔍 Verification Checklist

### Console Output Should Show:

- [ ] ✅ Webhook URL received
- [ ] ✅ Access token received (partial display)
- [ ] ✅ Question number (1 or 2)
- [ ] ✅ SQL query displayed
- [ ] ✅ Response status: 200 OK
- [ ] ✅ Success message

### If You See Errors:

#### Connection Timeout
- [ ] Check internet connection
- [ ] Verify Bajaj API is accessible
- [ ] Try again after a few minutes

#### 401 Unauthorized
- [ ] Verify registration details are correct
- [ ] Check email format is valid
- [ ] Ensure registration number is correct

#### 400 Bad Request
- [ ] Review SQL query syntax
- [ ] Check for missing semicolons
- [ ] Verify query matches question requirements

## 📦 Final Submission Checklist

- [ ] Application runs successfully
- [ ] Correct question is selected based on registration number
- [ ] SQL query is appropriate for the question
- [ ] Response from webhook is successful (200 OK)
- [ ] All logs are clear and show success

## 🎓 Understanding Your Registration Number

### Odd Last 2 Digits → Question 1
Examples: 01, 03, 05, 07, 09, 11, 13, ..., 97, 99

### Even Last 2 Digits → Question 2
Examples: 00, 02, 04, 06, 08, 10, 12, ..., 96, 98

**Your Registration Number:** `_______`

**Last 2 Digits:** `___`

**Question Number:** `___` (1 or 2)

## 📚 Quick Reference

| File | Purpose |
|------|---------|
| `application.properties` | Your personal details |
| `BajajChallengeService.java` | SQL queries (line ~130) |
| `README.md` | Complete documentation |
| `USAGE_GUIDE.md` | Detailed usage instructions |
| `SQL_QUERIES.md` | SQL query examples and tips |

## 🆘 Need Help?

1. **Check logs** - They show detailed error messages
2. **Review README.md** - Complete documentation
3. **Check USAGE_GUIDE.md** - Troubleshooting section
4. **Verify SQL_QUERIES.md** - Query syntax examples

## ✨ Pro Tips

- 💡 Test your SQL query syntax before running
- 💡 Keep your registration number handy
- 💡 Run the application in a terminal to see all logs
- 💡 Save the console output for reference
- 💡 Double-check your email format

## 🎯 Ready to Run?

Once all checkboxes above are ✅, you're ready to execute:

```bash
cd demo
mvn spring-boot:run
```

---

**Good luck with your Bajaj Finserv Health API Challenge! 🚀**

**Remember:** This application simulates the exact 2024 Bajaj webhook challenge flow automatically!
