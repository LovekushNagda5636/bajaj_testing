package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Bajaj Finserv Health API Challenge Application
 * 
 * This application simulates the 2024 Bajaj webhook challenge flow:
 * 1. Generates webhook URL and JWT token on startup
 * 2. Determines question based on registration number
 * 3. Prepares SQL query solution
 * 4. Submits SQL query to webhook with JWT authentication
 * 
 * No controllers or endpoints - runs automatically on startup
 */
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
