package com.example.demo.dto;

/**
 * DTO for webhook generation response
 * Contains webhook URL and JWT access token
 */
public class WebhookGenerationResponse {
    private String webhook;
    private String accessToken;

    public WebhookGenerationResponse() {
    }

    public WebhookGenerationResponse(String webhook, String accessToken) {
        this.webhook = webhook;
        this.accessToken = accessToken;
    }

    public String getWebhook() {
        return webhook;
    }

    public void setWebhook(String webhook) {
        this.webhook = webhook;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String toString() {
        return "WebhookGenerationResponse{" +
                "webhook='" + webhook + '\'' +
                ", accessToken='" + accessToken + '\'' +
                '}';
    }
}
