package com.kaua.ecommerce.users.infrastructure.configurations;

import com.kaua.ecommerce.users.application.gateways.QueueGateway;
import com.kaua.ecommerce.users.infrastructure.services.local.InMemoryQueueService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class QueueConfig {

    @Bean
    @Qualifier("mail")
    @Profile({"development", "test"})
    public QueueGateway localQueueGateway() {
        return new InMemoryQueueService();
    }
}
