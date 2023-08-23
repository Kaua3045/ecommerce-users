package com.kaua.ecommerce.users.infrastructure.configurations;

import com.kaua.ecommerce.users.infrastructure.configurations.annotations.AccountCreatedEvent;
import com.kaua.ecommerce.users.infrastructure.services.EventService;
import com.kaua.ecommerce.users.infrastructure.services.impl.SnsEventService;
import com.kaua.ecommerce.users.infrastructure.services.local.InMemoryEventService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.services.sns.SnsClient;

@Configuration
public class EventConfig {

    @Value("${aws.sns.topic.arn}")
    private String ACCOUNT_TOPIC_ARN;

    @Bean
    @AccountCreatedEvent
    @Profile({"development", "test"})
    public EventService localEventService() {
        return new InMemoryEventService();
    }

    @Bean
    @AccountCreatedEvent
    @Profile("production")
    public EventService snsEventService() {
        return new SnsEventService("created", ACCOUNT_TOPIC_ARN, SnsClient.create());
    }
}
