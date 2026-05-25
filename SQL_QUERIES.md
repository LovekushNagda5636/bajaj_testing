# SQL Queries Reference

## ⚠️ IMPORTANT: Replace These Queries

The queries in `BajajChallengeService.java` are **EXAMPLES ONLY**. You must replace them with the actual SQL queries for your specific challenge questions.

## 📍 Where to Update

File: `src/main/java/com/example/demo/service/BajajChallengeService.java`

Method: `getSqlQueryForQuestion(int questionNumber)`

Line: ~130

## 🔢 Question Selection Logic

```
Last 2 digits of Registration Number:
- ODD  (01, 03, 05, ..., 97, 99) → Question 1
- EVEN (00, 02, 04, ..., 96, 98) → Question 2
```

## 📝 Current Example Queries

### Question 1 (Odd Registration Number)
```java
return "SELECT employee_id, first_name, last_name, salary " +
       "FROM employees " +
       "WHERE salary > 50000 " +
       "ORDER BY salary DESC;";
```

### Question 2 (Even Registration Number)
```java
return "SELECT d.department_id, d.department_name, COUNT(e.employee_id) as employee_count " +
       "FROM departments d " +
       "JOIN employees e ON d.department_id = e.department_id " +
       "GROUP BY d.department_id, d.department_name " +
       "HAVING COUNT(e.employee_id) > 5 " +
       "ORDER BY employee_count DESC;";
```

## 🎯 How to Replace

1. Open `BajajChallengeService.java`
2. Find the `getSqlQueryForQuestion` method
3. Replace the return statements with your actual queries

### Example:

```java
private String getSqlQueryForQuestion(int questionNumber) {
    logger.info("STEP 3: Preparing SQL query for Question {}...", questionNumber);
    
    if (questionNumber == 1) {
        // ⬇️ REPLACE THIS WITH YOUR ACTUAL QUESTION 1 QUERY
        return "SELECT * FROM your_table WHERE your_condition;";
    } else {
        // ⬇️ REPLACE THIS WITH YOUR ACTUAL QUESTION 2 QUERY
        return "SELECT * FROM your_other_table WHERE your_other_condition;";
    }
}
```

## 💡 SQL Query Tips

### Multi-line Queries
For better readability, use string concatenation:

```java
return "SELECT " +
       "    column1, " +
       "    column2, " +
       "    column3 " +
       "FROM table_name " +
       "WHERE condition = 'value' " +
       "ORDER BY column1 DESC;";
```

### Complex Queries with JOINs
```java
return "SELECT " +
       "    t1.id, " +
       "    t1.name, " +
       "    t2.description " +
       "FROM table1 t1 " +
       "INNER JOIN table2 t2 ON t1.id = t2.table1_id " +
       "WHERE t1.status = 'active' " +
       "ORDER BY t1.created_at DESC;";
```

### Queries with Aggregations
```java
return "SELECT " +
       "    category, " +
       "    COUNT(*) as total_count, " +
       "    AVG(price) as avg_price, " +
       "    MAX(price) as max_price " +
       "FROM products " +
       "GROUP BY category " +
       "HAVING COUNT(*) > 10 " +
       "ORDER BY total_count DESC;";
```

### Subqueries
```java
return "SELECT " +
       "    employee_id, " +
       "    employee_name, " +
       "    salary " +
       "FROM employees " +
       "WHERE salary > (SELECT AVG(salary) FROM employees) " +
       "ORDER BY salary DESC;";
```

## ✅ Query Validation Checklist

Before running the application, ensure your SQL query:

- [ ] Has correct syntax (no missing commas, parentheses, etc.)
- [ ] Ends with a semicolon (`;`)
- [ ] Uses correct table and column names
- [ ] Has proper JOIN conditions (if using JOINs)
- [ ] Includes necessary WHERE clauses
- [ ] Has appropriate ORDER BY clause
- [ ] Uses correct aggregate functions (if needed)
- [ ] Has valid GROUP BY and HAVING clauses (if needed)

## 🧪 Testing Your Query

If you have access to a database, test your query first:

```sql
-- Test in your SQL client
SELECT * FROM your_table LIMIT 10;

-- Then copy the working query to Java
```

## 🚨 Common Mistakes to Avoid

### ❌ Missing Semicolon
```java
return "SELECT * FROM employees"  // WRONG - no semicolon
```

### ✅ Correct
```java
return "SELECT * FROM employees;"  // CORRECT
```

### ❌ Unescaped Quotes
```java
return "SELECT * FROM employees WHERE name = "John";"  // WRONG
```

### ✅ Correct
```java
return "SELECT * FROM employees WHERE name = 'John';"  // CORRECT
```

### ❌ Missing String Concatenation
```java
return "SELECT * FROM employees
        WHERE salary > 50000;"  // WRONG - Java doesn't support multi-line strings like this
```

### ✅ Correct
```java
return "SELECT * FROM employees " +
       "WHERE salary > 50000;"  // CORRECT
```

## 📋 Template for Your Queries

Copy this template and fill in your actual queries:

```java
private String getSqlQueryForQuestion(int questionNumber) {
    logger.info("STEP 3: Preparing SQL query for Question {}...", questionNumber);
    
    if (questionNumber == 1) {
        // Question 1: [DESCRIBE YOUR QUESTION HERE]
        // Example: Find all employees with salary greater than 50000
        return "YOUR_ACTUAL_SQL_QUERY_HERE;";
        
    } else {
        // Question 2: [DESCRIBE YOUR QUESTION HERE]
        // Example: Find departments with more than 5 employees
        return "YOUR_ACTUAL_SQL_QUERY_HERE;";
    }
}
```

## 🎓 Remember

- The SQL query is **NOT executed** by this application
- It's sent as a **string** to the Bajaj API webhook
- The Bajaj backend will execute and validate your query
- Make sure your query matches the exact requirements of your challenge question

---

**Update your queries and good luck! 🚀**
