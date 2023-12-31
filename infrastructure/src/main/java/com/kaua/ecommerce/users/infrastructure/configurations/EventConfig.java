package com.kaua.ecommerce.users.infrastructure.configurations;

import com.kaua.ecommerce.users.infrastructure.configurations.annotations.AccountCreatedEvent;
import com.kaua.ecommerce.users.infrastructure.configurations.annotations.AccountEvents;
import com.kaua.ecommerce.users.infrastructure.configurations.properties.amqp.QueueProperties;
import com.kaua.ecommerce.users.infrastructure.services.EventService;
import com.kaua.ecommerce.users.infrastructure.services.impl.RabbitEventService;
import com.kaua.ecommerce.users.infrastructure.services.local.InMemoryEventService;
import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class EventConfig {

    @Bean
    @AccountEvents
    @Profile({"development"})
    public EventService localEventService() {
        return new InMemoryEventService();
    }

    @Bean
    @AccountEvents
    @ConditionalOnMissingBean
    public EventService rabbitEventService(
            @AccountCreatedEvent final QueueProperties props,
            final RabbitOperations ops
    ) {
        return new RabbitEventService(props.getExchange(), ops);
    }
}
