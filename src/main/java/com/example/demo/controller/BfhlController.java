package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@CrossOrigin
public class BfhlController {

    @GetMapping("/bfhl")
    public Map<String, Integer> get() {
        Map<String, Integer> response = new HashMap<>();
        response.put("operation_code", 1);
        return response;
    }

    @PostMapping("/bfhl")
    public Map<String, Object> post(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        
        response.put("is_success", true);
        response.put("user_id", "lovekush_nagda_08022003");
        response.put("email", "lovekushnagda231252@acropolis.in");
        response.put("roll_number", "0827CY231039");
        
        List<String> data = (List<String>) request.get("data");
        List<String> numbers = new ArrayList<>();
        List<String> alphabets = new ArrayList<>();
        String highestLower = "";
        boolean hasPrime = false;
        
        for (String item : data) {
            if (item.matches("\\d+")) {
                numbers.add(item);
                int num = Integer.parseInt(item);
                if (isPrime(num)) hasPrime = true;
            } else if (item.length() == 1 && Character.isLetter(item.charAt(0))) {
                alphabets.add(item);
                if (Character.isLowerCase(item.charAt(0))) {
                    if (highestLower.isEmpty() || item.charAt(0) > highestLower.charAt(0)) {
                        highestLower = item;
                    }
                }
            }
        }
        
        response.put("numbers", numbers);
        response.put("alphabets", alphabets);
        response.put("highest_lowercase_alphabet", highestLower.isEmpty() ? new ArrayList<>() : Arrays.asList(highestLower));
        response.put("is_prime_found", hasPrime);
        
        String fileB64 = (String) request.get("file_b64");
        if (fileB64 != null && !fileB64.isEmpty()) {
            try {
                byte[] decoded = Base64.getDecoder().decode(fileB64);
                response.put("file_valid", true);
                response.put("file_mime_type", getMimeType(decoded));
                response.put("file_size_kb", String.format("%.2f", decoded.length / 1024.0));
            } catch (Exception e) {
                response.put("file_valid", false);
            }
        } else {
            response.put("file_valid", false);
        }
        
        return response;
    }
    
    private boolean isPrime(int n) {
        if (n <= 1) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;
        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }
    
    private String getMimeType(byte[] bytes) {
        if (bytes.length < 4) return "application/octet-stream";
        if (bytes[0] == (byte) 0x89 && bytes[1] == 0x50) return "image/png";
        if (bytes[0] == (byte) 0xFF && bytes[1] == (byte) 0xD8) return "image/jpeg";
        if (bytes[0] == 0x47 && bytes[1] == 0x49) return "image/gif";
        if (bytes[0] == 0x25 && bytes[1] == 0x50) return "application/pdf";
        return "application/octet-stream";
    }
}
