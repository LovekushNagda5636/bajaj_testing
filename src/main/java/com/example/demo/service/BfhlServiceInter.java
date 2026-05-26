package com.example.demo.service;

import com.example.demo.dto.BfhlRequest;
import com.example.demo.dto.BfhlResponse;

public interface BfhlServiceInter {
    BfhlResponse processData(BfhlRequest request);
}
