package com.sse.emiter.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OpenAIConfig {
    @Value("${openai.api.base.url}")
    private String baseUrl;

    @Value("${openai.api.key}")
    private String apiKey;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .rootUri(baseUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
    }
}
