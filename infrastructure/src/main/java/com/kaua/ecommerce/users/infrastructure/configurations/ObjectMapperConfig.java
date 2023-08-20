package com.kaua.ecommerce.users.infrastructure.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaua.ecommerce.users.infrastructure.configurations.json.Json;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return Json.mapper();
    }
}
