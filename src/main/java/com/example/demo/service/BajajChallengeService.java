package com.example.demo.service;

import com.example.demo.dto.SqlQueryRequest;
import com.example.demo.dto.WebhookGenerationRequest;
import com.example.demo.dto.WebhookGenerationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Service to handle Bajaj Finserv Health API Challenge workflow
 * Executes the complete webhook flow automatically on startup
 */
@Service
public class BajajChallengeService {

    private static final Logger logger = LoggerFactory.getLogger(BajajChallengeService.class);
    private static final String WEBHOOK_GENERATION_URL = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

    private final RestTemplate restTemplate;
    private final String userName;
    private final String regNo;
    private final String userEmail;

    public BajajChallengeService(
            RestTemplate restTemplate,
            @Value("${bajaj.user.name}") String userName,
            @Value("${bajaj.user.regNo}") String regNo,
            @Value("${bajaj.user.email}") String userEmail) {
        this.restTemplate = restTemplate;
        this.userName = userName;
        this.regNo = regNo;
        this.userEmail = userEmail;
    }

    /**
     * Execute the complete Bajaj challenge workflow
     */
    public void executeChallengeFlow() {
        logger.info("========================================");
        logger.info("Starting Bajaj Finserv Health API Challenge");
        logger.info("========================================");

        try {
            // STEP 1: Generate webhook and get JWT token
            WebhookGenerationResponse webhookResponse = generateWebhook();
            
            if (webhookResponse == null || webhookResponse.getWebhook() == null || webhookResponse.getAccessToken() == null) {
                logger.error("Failed to generate webhook. Response is null or incomplete.");
                return;
            }

            logger.info("Webhook URL: {}", webhookResponse.getWebhook());
            logger.info("Access Token received: {}...", webhookResponse.getAccessToken().substring(0, Math.min(20, webhookResponse.getAccessToken().length())));

            // STEP 2: Determine question based on registration number
            int questionNumber = determineQuestion(regNo);
            logger.info("Question Number: {}", questionNumber);

            // STEP 3: Get SQL query for the question
            String sqlQuery = getSqlQueryForQuestion(questionNumber);
            logger.info("SQL Query: {}", sqlQuery);

            // STEP 4: Submit SQL query to webhook
            submitSqlQuery(webhookResponse.getWebhook(), webhookResponse.getAccessToken(), sqlQuery);

            logger.info("========================================");
            logger.info("Challenge completed successfully!");
            logger.info("========================================");

        } catch (Exception e) {
            logger.error("Error during challenge execution: {}", e.getMessage(), e);
        }
    }

    /**
     * STEP 1: Generate webhook URL and JWT token
     */
    private WebhookGenerationResponse generateWebhook() {
        logger.info("STEP 1: Generating webhook...");
        
        WebhookGenerationRequest request = new WebhookGenerationRequest(userName, regNo, userEmail);
        logger.info("Request: {}", request);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<WebhookGenerationRequest> entity = new HttpEntity<>(request, headers);
            
            ResponseEntity<WebhookGenerationResponse> response = restTemplate.exchange(
                    WEBHOOK_GENERATION_URL,
                    HttpMethod.POST,
                    entity,
                    WebhookGenerationResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                logger.info("Webhook generated successfully!");
                return response.getBody();
            } else {
                logger.error("Unexpected response status: {}", response.getStatusCode());
                return null;
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("HTTP Error during webhook generation: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            return null;
        } catch (Exception e) {
            logger.error("Error generating webhook: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * STEP 2: Determine question based on registration number
     * Odd last 2 digits → Question 1
     * Even last 2 digits → Question 2
     */
    private int determineQuestion(String regNo) {
        logger.info("STEP 2: Determining question from registration number...");
        
        if (regNo == null || regNo.length() < 2) {
            logger.warn("Invalid registration number. Defaulting to Question 1");
            return 1;
        }

        String lastTwoDigits = regNo.substring(regNo.length() - 2);
        int lastTwoDigitsInt = Integer.parseInt(lastTwoDigits);
        
        logger.info("Last 2 digits of regNo: {}", lastTwoDigits);
        
        if (lastTwoDigitsInt % 2 == 0) {
            logger.info("Even number detected → Question 2");
            return 2;
        } else {
            logger.info("Odd number detected → Question 1");
            return 1;
        }
    }

    /**
     * STEP 3: Get SQL query based on question number
     * Replace these with actual SQL queries for your challenge
     */
    private String getSqlQueryForQuestion(int questionNumber) {
        logger.info("STEP 3: Preparing SQL query for Question {}...", questionNumber);
        
        if (questionNumber == 1) {
            // Question 1 SQL Query
            // Example: Find all employees with salary > 50000
            return "SELECT employee_id, first_name, last_name, salary " +
                   "FROM employees " +
                   "WHERE salary > 50000 " +
                   "ORDER BY salary DESC;";
        } else {
            // Question 2 SQL Query
            // Example: Find departments with more than 5 employees
            return "SELECT d.department_id, d.department_name, COUNT(e.employee_id) as employee_count " +
                   "FROM departments d " +
                   "JOIN employees e ON d.department_id = e.department_id " +
                   "GROUP BY d.department_id, d.department_name " +
                   "HAVING COUNT(e.employee_id) > 5 " +
                   "ORDER BY employee_count DESC;";
        }
    }

    /**
     * STEP 4: Submit SQL query to webhook URL with JWT authentication
     */
    private void submitSqlQuery(String webhookUrl, String accessToken, String sqlQuery) {
        logger.info("STEP 4: Submitting SQL query to webhook...");
        
        SqlQueryRequest request = new SqlQueryRequest(sqlQuery);
        logger.info("Request: {}", request);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);
            
            HttpEntity<SqlQueryRequest> entity = new HttpEntity<>(request, headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                    webhookUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            logger.info("Response Status: {}", response.getStatusCode());
            logger.info("Response Body: {}", response.getBody());

            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("SQL query submitted successfully!");
            } else {
                logger.warn("Unexpected response status: {}", response.getStatusCode());
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("HTTP Error during SQL submission: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error("Error submitting SQL query: {}", e.getMessage(), e);
        }
    }
}
