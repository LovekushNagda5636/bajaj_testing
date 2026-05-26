package com.example.demo.controller;

import com.example.demo.dto.BfhlRequest;
import com.example.demo.dto.BfhlResponse;
import com.example.demo.service.BfhlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class BfhlController {

    @Autowired
    private BfhlService bfhlService;

    @PostMapping("/bfhl")
    public ResponseEntity<BfhlResponse> processData(@RequestBody BfhlRequest request) {
        BfhlResponse response = bfhlService.processData(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
