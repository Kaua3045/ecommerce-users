package com.kaua.ecommerce.users.infrastructure.configurations;

import com.kaua.ecommerce.users.infrastructure.configurations.annotations.AccountCreatedEvent;
import com.kaua.ecommerce.users.infrastructure.services.EventService;
import com.kaua.ecommerce.users.infrastructure.services.local.InMemoryEventService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class EventConfig {

    @Bean
    @AccountCreatedEvent
    @Profile({"development", "test"})
    public EventService localEventService() {
        return new InMemoryEventService();
    }
}
