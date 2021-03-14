package com.rtsystems.webservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${exchange.api.url}")
    private String baseUrl;

    @Bean
    public WebClient getWebClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}