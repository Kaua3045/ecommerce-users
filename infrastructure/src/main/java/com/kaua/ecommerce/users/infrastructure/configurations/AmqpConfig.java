package com.kaua.ecommerce.users.infrastructure.configurations;

import com.kaua.ecommerce.users.infrastructure.configurations.annotations.AccountCreatedEvent;
import com.kaua.ecommerce.users.infrastructure.configurations.annotations.AccountCreatedGenerateMailCodeEvent;
import com.kaua.ecommerce.users.infrastructure.configurations.properties.amqp.QueueProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfig {

    @Bean
    @ConfigurationProperties("amqp.queues.account-created")
    @AccountCreatedEvent
    public QueueProperties accountCreatedQueueProperties() {
        return new QueueProperties();
    }

    @Bean
    @ConfigurationProperties("amqp.queues.account-generate-confirmation-code-mail")
    @AccountCreatedGenerateMailCodeEvent
    public QueueProperties accountGenerateConfirmationMailQueueProperties() {
        return new QueueProperties();
    }
}
