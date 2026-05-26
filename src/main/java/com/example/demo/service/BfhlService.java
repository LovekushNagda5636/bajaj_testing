package com.example.demo.service;

import com.example.demo.dto.BfhlRequest;
import com.example.demo.dto.BfhlResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BfhlService {

    public BfhlResponse processData(BfhlRequest request) {
        BfhlResponse response = new BfhlResponse();
        
        response.setIs_success(true);
        response.setUser_id("lovekush_nagda_08022003");
        response.setEmail("lovekushnagda231252@acropolis.in");
        response.setRoll_number("0827CY231039");
        
        List<String> oddNumbers = new ArrayList<>();
        List<String> evenNumbers = new ArrayList<>();
        List<String> alphabets = new ArrayList<>();
        List<String> specialChars = new ArrayList<>();
        List<Character> alphaChars = new ArrayList<>();
        int sum = 0;
        
        for (String item : request.getData()) {
            if (item.matches("\\d+")) {
                int num = Integer.parseInt(item);
                sum += num;
                if (num % 2 == 0) {
                    evenNumbers.add(item);
                } else {
                    oddNumbers.add(item);
                }
            } else if (item.length() == 1 && Character.isLetter(item.charAt(0))) {
                alphabets.add(item.toUpperCase());
                alphaChars.add(item.charAt(0));
            } else {
                specialChars.add(item);
            }
        }
        
        response.setOdd_numbers(oddNumbers);
        response.setEven_numbers(evenNumbers);
        response.setAlphabets(alphabets);
        response.setSpecial_characters(specialChars);
        response.setSum(String.valueOf(sum));
        response.setConcat_string(buildConcatString(alphaChars));
        
        return response;
    }
    
    private String buildConcatString(List<Character> chars) {
        if (chars.isEmpty()) return "";
        
        StringBuilder result = new StringBuilder();
        for (int i = chars.size() - 1; i >= 0; i--) {
            char c = chars.get(i);
            int pos = chars.size() - 1 - i;
            if (pos % 2 == 0) {
                result.append(Character.toUpperCase(c));
            } else {
                result.append(Character.toLowerCase(c));
            }
        }
        return result.toString();
    }
}
