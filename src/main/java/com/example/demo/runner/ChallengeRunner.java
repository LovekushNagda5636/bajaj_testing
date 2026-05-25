package com.example.demo.runner;

import com.example.demo.service.BajajChallengeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * CommandLineRunner to execute the Bajaj challenge flow automatically on startup
 * This runs after the Spring application context is fully initialized
 */
@Component
public class ChallengeRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ChallengeRunner.class);
    private final BajajChallengeService bajajChallengeService;

    public ChallengeRunner(BajajChallengeService bajajChallengeService) {
        this.bajajChallengeService = bajajChallengeService;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Application started. Executing Bajaj challenge flow...");
        bajajChallengeService.executeChallengeFlow();
    }
}
