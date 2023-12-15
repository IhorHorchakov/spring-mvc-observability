package com.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestClientConfig {
    @Value("${rental.manager.base.url}")
    private String rentalManagerBaseUrl;

    @Bean("RentalManagerRestTemplate")
    RestTemplate rentalManagerRestTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .rootUri(rentalManagerBaseUrl)
                .build();
    }
}
