package com.example.demo.service;

import com.example.demo.dto.BfhlRequest;
import com.example.demo.dto.BfhlResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BfhlService {

    @Value("${bfhl.user.name:Lovekush Nagda}")
    private String userName;

    @Value("${bfhl.user.dob:05062003}")
    private String userDob;

    @Value("${bfhl.user.email:lovekushnagad231252@acropolis.in}")
    private String userEmail;

    @Value("${bfhl.user.rollNumber:0827CY231039}")
    private String rollNumber;

    public BfhlResponse processRequest(BfhlRequest request) {
        BfhlResponse response = new BfhlResponse();
        
        response.setIs_success(true);
        response.setUser_id(userName.toLowerCase().replace(" ", "_") + "_" + userDob);
        response.setEmail(userEmail);
        response.setRoll_number(rollNumber);

        List<String> numbers = new ArrayList<>();
        List<String> alphabets = new ArrayList<>();
        List<String> lowercaseAlphabets = new ArrayList<>();
        boolean primeFound = false;

        if (request.getData() != null) {
            for (String item : request.getData()) {
                if (item == null || item.isEmpty()) continue;

                if (isNumeric(item)) {
                    numbers.add(item);
                    if (isPrime(Long.parseLong(item))) {
                        primeFound = true;
                    }
                } else if (item.length() == 1 && Character.isLetter(item.charAt(0))) {
                    alphabets.add(item);
                    if (Character.isLowerCase(item.charAt(0))) {
                        lowercaseAlphabets.add(item);
                    }
                }
            }
        }

        response.setNumbers(numbers);
        response.setAlphabets(alphabets);
        response.setIs_prime_found(primeFound);
        response.setHighest_lowercase_alphabet(
            lowercaseAlphabets.isEmpty() ? new ArrayList<>() : 
            Collections.singletonList(Collections.max(lowercaseAlphabets))
        );

        if (request.getFile_b64() != null && !request.getFile_b64().trim().isEmpty()) {
            try {
                byte[] decoded = Base64.getDecoder().decode(request.getFile_b64());
                response.setFile_valid(true);
                response.setFile_mime_type(detectMimeType(decoded));
                response.setFile_size_kb(String.format("%.2f", decoded.length / 1024.0));
            } catch (Exception e) {
                response.setFile_valid(false);
            }
        } else {
            response.setFile_valid(false);
        }

        return response;
    }

    private boolean isNumeric(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isPrime(long num) {
        if (num <= 1) return false;
        if (num == 2) return true;
        if (num % 2 == 0) return false;
        for (long i = 3; i * i <= num; i += 2) {
            if (num % i == 0) return false;
        }
        return true;
    }

    private String detectMimeType(byte[] bytes) {
        if (bytes.length < 4) return "application/octet-stream";
        
        if (bytes[0] == (byte) 0x89 && bytes[1] == 0x50 && bytes[2] == 0x4E && bytes[3] == 0x47) 
            return "image/png";
        if (bytes[0] == (byte) 0xFF && bytes[1] == (byte) 0xD8 && bytes[2] == (byte) 0xFF) 
            return "image/jpeg";
        if (bytes[0] == 0x47 && bytes[1] == 0x49 && bytes[2] == 0x46) 
            return "image/gif";
        if (bytes[0] == 0x25 && bytes[1] == 0x50 && bytes[2] == 0x44 && bytes[3] == 0x46) 
            return "application/pdf";
        
        return "application/octet-stream";
    }
}
