package com.example.demo.dto;

/**
 * DTO for webhook generation request
 * Sent to Bajaj API to generate webhook URL and JWT token
 */
public class WebhookGenerationRequest {
    private String name;
    private String regNo;
    private String email;

    public WebhookGenerationRequest() {
    }

    public WebhookGenerationRequest(String name, String regNo, String email) {
        this.name = name;
        this.regNo = regNo;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "WebhookGenerationRequest{" +
                "name='" + name + '\'' +
                ", regNo='" + regNo + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
