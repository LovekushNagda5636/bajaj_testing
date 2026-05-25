# 🏗️ Application Architecture

## System Architecture Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                     SPRING BOOT APPLICATION                      │
│                                                                  │
│  ┌────────────────────────────────────────────────────────┐    │
│  │              DemoApplication.java                       │    │
│  │              (Main Entry Point)                         │    │
│  └────────────────────────────────────────────────────────┘    │
│                            │                                     │
│                            │ Starts                              │
│                            ↓                                     │
│  ┌────────────────────────────────────────────────────────┐    │
│  │         ChallengeRunner.java                            │    │
│  │         (CommandLineRunner)                             │    │
│  │         - Executes automatically on startup             │    │
│  └────────────────────────────────────────────────────────┘    │
│                            │                                     │
│                            │ Calls                               │
│                            ↓                                     │
│  ┌────────────────────────────────────────────────────────┐    │
│  │      BajajChallengeService.java                         │    │
│  │      (Core Business Logic)                              │    │
│  │                                                          │    │
│  │  ┌──────────────────────────────────────────────────┐  │    │
│  │  │  executeChallengeFlow()                          │  │    │
│  │  │  ├─ generateWebhook()                            │  │    │
│  │  │  ├─ determineQuestion()                          │  │    │
│  │  │  ├─ getSqlQueryForQuestion()                     │  │    │
│  │  │  └─ submitSqlQuery()                             │  │    │
│  │  └──────────────────────────────────────────────────┘  │    │
│  └────────────────────────────────────────────────────────┘    │
│                            │                                     │
│                            │ Uses                                │
│                            ↓                                     │
│  ┌────────────────────────────────────────────────────────┐    │
│  │         RestTemplate                                    │    │
│  │         (HTTP Client)                                   │    │
│  │         - Configured in RestTemplateConfig.java        │    │
│  └────────────────────────────────────────────────────────┘    │
│                            │                                     │
└────────────────────────────┼─────────────────────────────────────┘
                             │
                             │ HTTP Requests
                             ↓
┌─────────────────────────────────────────────────────────────────┐
│                    BAJAJ FINSERV API                             │
│                                                                  │
│  ┌────────────────────────────────────────────────────────┐    │
│  │  Endpoint 1: Generate Webhook                           │    │
│  │  POST /hiring/generateWebhook/JAVA                      │    │
│  │  Returns: {webhook, accessToken}                        │    │
│  └────────────────────────────────────────────────────────┘    │
│                                                                  │
│  ┌────────────────────────────────────────────────────────┐    │
│  │  Endpoint 2: Webhook URL (Dynamic)                      │    │
│  │  POST <webhook_url>                                     │    │
│  │  Headers: Authorization: Bearer <JWT>                   │    │
│  │  Body: {finalQuery: "SQL"}                              │    │
│  └────────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────────┘
```

---

## Component Interaction Flow

```
┌──────────────┐
│   User       │
│   Starts     │
│   App        │
└──────┬───────┘
       │
       ↓
┌──────────────────────────────────────────────────────────┐
│  Spring Boot Initialization                              │
│  - Loads application.properties                          │
│  - Creates RestTemplate bean                             │
│  - Initializes BajajChallengeService                     │
│  - Runs CommandLineRunner                                │
└──────┬───────────────────────────────────────────────────┘
       │
       ↓
┌──────────────────────────────────────────────────────────┐
│  ChallengeRunner.run()                                   │
│  - Calls bajajChallengeService.executeChallengeFlow()   │
└──────┬───────────────────────────────────────────────────┘
       │
       ↓
┌──────────────────────────────────────────────────────────┐
│  STEP 1: generateWebhook()                               │
│  ┌────────────────────────────────────────────────────┐  │
│  │  1. Create WebhookGenerationRequest                │  │
│  │     - name, regNo, email from properties           │  │
│  │  2. Send POST to Bajaj API                         │  │
│  │  3. Receive WebhookGenerationResponse              │  │
│  │     - webhook URL                                  │  │
│  │     - JWT access token                             │  │
│  └────────────────────────────────────────────────────┘  │
└──────┬───────────────────────────────────────────────────┘
       │
       ↓
┌──────────────────────────────────────────────────────────┐
│  STEP 2: determineQuestion()                             │
│  ┌────────────────────────────────────────────────────┐  │
│  │  1. Extract last 2 digits from regNo               │  │
│  │  2. Check if odd or even                           │  │
│  │  3. Return question number (1 or 2)                │  │
│  └────────────────────────────────────────────────────┘  │
└──────┬───────────────────────────────────────────────────┘
       │
       ↓
┌──────────────────────────────────────────────────────────┐
│  STEP 3: getSqlQueryForQuestion()                        │
│  ┌────────────────────────────────────────────────────┐  │
│  │  1. Check question number                          │  │
│  │  2. Return appropriate SQL query string            │  │
│  │     - Question 1 query for odd                     │  │
│  │     - Question 2 query for even                    │  │
│  └────────────────────────────────────────────────────┘  │
└──────┬───────────────────────────────────────────────────┘
       │
       ↓
┌──────────────────────────────────────────────────────────┐
│  STEP 4: submitSqlQuery()                                │
│  ┌────────────────────────────────────────────────────┐  │
│  │  1. Create SqlQueryRequest with query              │  │
│  │  2. Set Authorization header with JWT              │  │
│  │  3. POST to webhook URL                            │  │
│  │  4. Receive response                               │  │
│  │  5. Log success/failure                            │  │
│  └────────────────────────────────────────────────────┘  │
└──────┬───────────────────────────────────────────────────┘
       │
       ↓
┌──────────────────────────────────────────────────────────┐
│  Application Completes                                   │
│  - Logs "Challenge completed successfully!"              │
│  - Application exits                                     │
└──────────────────────────────────────────────────────────┘
```

---

## Class Diagram

```
┌─────────────────────────────────────────────────────────┐
│              DemoApplication                             │
│  ─────────────────────────────────────────────────────  │
│  + main(String[] args): void                            │
└─────────────────────────────────────────────────────────┘
                         │
                         │ starts
                         ↓
┌─────────────────────────────────────────────────────────┐
│         ChallengeRunner                                  │
│         implements CommandLineRunner                     │
│  ─────────────────────────────────────────────────────  │
│  - bajajChallengeService: BajajChallengeService         │
│  ─────────────────────────────────────────────────────  │
│  + run(String... args): void                            │
└─────────────────────────────────────────────────────────┘
                         │
                         │ uses
                         ↓
┌─────────────────────────────────────────────────────────┐
│         BajajChallengeService                            │
│         @Service                                         │
│  ─────────────────────────────────────────────────────  │
│  - restTemplate: RestTemplate                           │
│  - userName: String                                     │
│  - regNo: String                                        │
│  - userEmail: String                                    │
│  ─────────────────────────────────────────────────────  │
│  + executeChallengeFlow(): void                         │
│  - generateWebhook(): WebhookGenerationResponse         │
│  - determineQuestion(String): int                       │
│  - getSqlQueryForQuestion(int): String                  │
│  - submitSqlQuery(String, String, String): void         │
└─────────────────────────────────────────────────────────┘
                         │
                         │ uses
                         ↓
┌─────────────────────────────────────────────────────────┐
│         RestTemplateConfig                               │
│         @Configuration                                   │
│  ─────────────────────────────────────────────────────  │
│  + restTemplate(): RestTemplate                         │
└─────────────────────────────────────────────────────────┘
```

---

## Data Transfer Objects (DTOs)

```
┌─────────────────────────────────────────────────────────┐
│      WebhookGenerationRequest                            │
│  ─────────────────────────────────────────────────────  │
│  - name: String                                         │
│  - regNo: String                                        │
│  - email: String                                        │
│  ─────────────────────────────────────────────────────  │
│  + getName(): String                                    │
│  + setName(String): void                                │
│  + getRegNo(): String                                   │
│  + setRegNo(String): void                               │
│  + getEmail(): String                                   │
│  + setEmail(String): void                               │
└─────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────┐
│      WebhookGenerationResponse                           │
│  ─────────────────────────────────────────────────────  │
│  - webhook: String                                      │
│  - accessToken: String                                  │
│  ─────────────────────────────────────────────────────  │
│  + getWebhook(): String                                 │
│  + setWebhook(String): void                             │
│  + getAccessToken(): String                             │
│  + setAccessToken(String): void                         │
└─────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────┐
│      SqlQueryRequest                                     │
│  ─────────────────────────────────────────────────────  │
│  - finalQuery: String                                   │
│  ─────────────────────────────────────────────────────  │
│  + getFinalQuery(): String                              │
│  + setFinalQuery(String): void                          │
└─────────────────────────────────────────────────────────┘
```

---

## HTTP Request/Response Flow

### Request 1: Generate Webhook

```
┌─────────────────────────────────────────────────────────┐
│  REQUEST                                                 │
│  ─────────────────────────────────────────────────────  │
│  Method: POST                                           │
│  URL: https://bfhldevapigw.healthrx.co.in/             │
│       hiring/generateWebhook/JAVA                       │
│  Headers:                                               │
│    Content-Type: application/json                       │
│  Body:                                                  │
│    {                                                    │
│      "name": "John Doe",                                │
│      "regNo": "12345",                                  │
│      "email": "john@example.com"                        │
│    }                                                    │
└─────────────────────────────────────────────────────────┘
                         │
                         ↓
┌─────────────────────────────────────────────────────────┐
│  RESPONSE                                                │
│  ─────────────────────────────────────────────────────  │
│  Status: 200 OK                                         │
│  Body:                                                  │
│    {                                                    │
│      "webhook": "https://bfhldevapigw.../webhook/...",  │
│      "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI..."   │
│    }                                                    │
└─────────────────────────────────────────────────────────┘
```

### Request 2: Submit SQL Query

```
┌─────────────────────────────────────────────────────────┐
│  REQUEST                                                 │
│  ─────────────────────────────────────────────────────  │
│  Method: POST                                           │
│  URL: <webhook_url_from_previous_response>              │
│  Headers:                                               │
│    Content-Type: application/json                       │
│    Authorization: Bearer <jwt_token>                    │
│  Body:                                                  │
│    {                                                    │
│      "finalQuery": "SELECT * FROM employees..."         │
│    }                                                    │
└─────────────────────────────────────────────────────────┘
                         │
                         ↓
┌─────────────────────────────────────────────────────────┐
│  RESPONSE                                                │
│  ─────────────────────────────────────────────────────  │
│  Status: 200 OK                                         │
│  Body:                                                  │
│    {                                                    │
│      "status": "success",                               │
│      "message": "Query received and validated"          │
│    }                                                    │
└─────────────────────────────────────────────────────────┘
```

---

## Configuration Flow

```
┌─────────────────────────────────────────────────────────┐
│  application.properties                                  │
│  ─────────────────────────────────────────────────────  │
│  bajaj.user.name=John Doe                               │
│  bajaj.user.regNo=12345                                 │
│  bajaj.user.email=john@example.com                      │
└─────────────────────────────────────────────────────────┘
                         │
                         │ @Value injection
                         ↓
┌─────────────────────────────────────────────────────────┐
│  BajajChallengeService                                   │
│  ─────────────────────────────────────────────────────  │
│  @Value("${bajaj.user.name}")                           │
│  private String userName;                               │
│                                                          │
│  @Value("${bajaj.user.regNo}")                          │
│  private String regNo;                                  │
│                                                          │
│  @Value("${bajaj.user.email}")                          │
│  private String userEmail;                              │
└─────────────────────────────────────────────────────────┘
```

---

## Error Handling Flow

```
┌─────────────────────────────────────────────────────────┐
│  Try-Catch Blocks in BajajChallengeService              │
└─────────────────────────────────────────────────────────┘
                         │
                         ↓
┌─────────────────────────────────────────────────────────┐
│  HTTP Errors                                             │
│  ├─ HttpClientErrorException (4xx)                      │
│  │  └─ Log error with status and response body          │
│  ├─ HttpServerErrorException (5xx)                      │
│  │  └─ Log error with status and response body          │
│  └─ Generic Exception                                   │
│     └─ Log error message and stack trace                │
└─────────────────────────────────────────────────────────┘
                         │
                         ↓
┌─────────────────────────────────────────────────────────┐
│  Logging                                                 │
│  ├─ INFO: Normal flow execution                         │
│  ├─ WARN: Unexpected but handled situations             │
│  └─ ERROR: Failures and exceptions                      │
└─────────────────────────────────────────────────────────┘
```

---

## Dependency Injection

```
┌─────────────────────────────────────────────────────────┐
│  Spring Container                                        │
│  ─────────────────────────────────────────────────────  │
│  ┌───────────────────────────────────────────────────┐  │
│  │  RestTemplateConfig                               │  │
│  │  @Configuration                                   │  │
│  │  ├─ Creates RestTemplate bean                     │  │
│  │  └─ Configures timeouts                           │  │
│  └───────────────────────────────────────────────────┘  │
│                         │                                │
│                         ↓                                │
│  ┌───────────────────────────────────────────────────┐  │
│  │  BajajChallengeService                            │  │
│  │  @Service                                         │  │
│  │  ├─ Injects RestTemplate                          │  │
│  │  ├─ Injects @Value properties                     │  │
│  │  └─ Business logic methods                        │  │
│  └───────────────────────────────────────────────────┘  │
│                         │                                │
│                         ↓                                │
│  ┌───────────────────────────────────────────────────┐  │
│  │  ChallengeRunner                                  │  │
│  │  @Component                                       │  │
│  │  ├─ Implements CommandLineRunner                 │  │
│  │  ├─ Injects BajajChallengeService                │  │
│  │  └─ Executes on startup                          │  │
│  └───────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────┘
```

---

## Logging Architecture

```
┌─────────────────────────────────────────────────────────┐
│  SLF4J (Logging Facade)                                  │
└─────────────────────────────────────────────────────────┘
                         │
                         ↓
┌─────────────────────────────────────────────────────────┐
│  Logback (Implementation)                                │
│  ─────────────────────────────────────────────────────  │
│  Configuration from application.properties:              │
│  - logging.level.com.example.demo=INFO                  │
│  - logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss}...   │
└─────────────────────────────────────────────────────────┘
                         │
                         ↓
┌─────────────────────────────────────────────────────────┐
│  Console Output                                          │
│  ─────────────────────────────────────────────────────  │
│  2026-05-25 22:30:00 - Starting Bajaj Challenge...      │
│  2026-05-25 22:30:01 - STEP 1: Generating webhook...    │
│  2026-05-25 22:30:02 - Webhook generated successfully!  │
│  ...                                                     │
└─────────────────────────────────────────────────────────┘
```

---

## Build & Packaging Architecture

```
┌─────────────────────────────────────────────────────────┐
│  Maven Build Process                                     │
└─────────────────────────────────────────────────────────┘
                         │
                         ↓
┌─────────────────────────────────────────────────────────┐
│  1. Clean Phase                                          │
│     - Deletes target/ directory                          │
└─────────────────────────────────────────────────────────┘
                         │
                         ↓
┌─────────────────────────────────────────────────────────┐
│  2. Compile Phase                                        │
│     - Compiles Java source files                         │
│     - Copies resources                                   │
└─────────────────────────────────────────────────────────┘
                         │
                         ↓
┌─────────────────────────────────────────────────────────┐
│  3. Test Phase (Skipped in our build)                   │
│     - Would run unit tests                               │
└─────────────────────────────────────────────────────────┘
                         │
                         ↓
┌─────────────────────────────────────────────────────────┐
│  4. Package Phase                                        │
│     - Creates JAR file                                   │
│     - Spring Boot repackages with dependencies           │
└─────────────────────────────────────────────────────────┘
                         │
                         ↓
┌─────────────────────────────────────────────────────────┐
│  Output: demo-0.0.1-SNAPSHOT.jar                         │
│  ─────────────────────────────────────────────────────  │
│  Structure:                                              │
│  ├─ BOOT-INF/                                            │
│  │  ├─ classes/ (compiled application classes)          │
│  │  └─ lib/ (all dependencies)                          │
│  ├─ META-INF/                                            │
│  └─ org/springframework/boot/loader/ (Spring loader)    │
└─────────────────────────────────────────────────────────┘
```

---

## Runtime Execution Architecture

```
┌─────────────────────────────────────────────────────────┐
│  java -jar demo-0.0.1-SNAPSHOT.jar                       │
└─────────────────────────────────────────────────────────┘
                         │
                         ↓
┌─────────────────────────────────────────────────────────┐
│  Spring Boot Loader                                      │
│  - Extracts nested JARs                                  │
│  - Sets up classpath                                     │
└─────────────────────────────────────────────────────────┘
                         │
                         ↓
┌─────────────────────────────────────────────────────────┐
│  Spring Application Context                              │
│  - Component scanning                                    │
│  - Bean creation                                         │
│  - Dependency injection                                  │
└─────────────────────────────────────────────────────────┘
                         │
                         ↓
┌─────────────────────────────────────────────────────────┐
│  CommandLineRunner Execution                             │
│  - ChallengeRunner.run() is called                       │
│  - Challenge flow executes                               │
└─────────────────────────────────────────────────────────┘
                         │
                         ↓
┌─────────────────────────────────────────────────────────┐
│  Application Shutdown                                    │
│  - Logs completion message                               │
│  - Exits with status code 0                              │
└─────────────────────────────────────────────────────────┘
```

---

## Key Design Patterns Used

### 1. **Dependency Injection**
- Spring manages all bean creation and wiring
- Constructor injection for required dependencies

### 2. **DTO Pattern**
- Separate objects for data transfer
- Clean separation between API contracts and business logic

### 3. **Service Layer Pattern**
- Business logic encapsulated in service classes
- Clear separation of concerns

### 4. **Configuration Pattern**
- Externalized configuration in application.properties
- @Configuration classes for bean setup

### 5. **Template Method Pattern**
- RestTemplate provides template for HTTP operations
- Consistent error handling and request/response processing

---

**This architecture ensures:**
- ✅ Clean separation of concerns
- ✅ Easy to test and maintain
- ✅ Follows Spring Boot best practices
- ✅ Scalable and extensible design
