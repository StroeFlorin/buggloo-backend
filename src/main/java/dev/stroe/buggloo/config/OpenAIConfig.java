package dev.stroe.buggloo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;

/**
 * Configuration properties for OpenAI integration.
 */
@Configuration
@ConfigurationProperties(prefix = "openai")
@Validated
public class OpenAIConfig {

    @NotBlank(message = "OpenAI API key is required")
    private String apiKey;

    private int maxTokens = 2048;

    // Getters and setters
    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
    
    public int getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(int maxTokens) {
        this.maxTokens = maxTokens;
    }
} 