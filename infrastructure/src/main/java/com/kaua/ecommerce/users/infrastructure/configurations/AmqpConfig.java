package com.kaua.ecommerce.users.infrastructure.configurations;

import com.kaua.ecommerce.users.infrastructure.configurations.annotations.AccountCreatedEvent;
import com.kaua.ecommerce.users.infrastructure.configurations.annotations.AccountCreatedGenerateMailCodeEvent;
import com.kaua.ecommerce.users.infrastructure.configurations.annotations.AccountEvents;
import com.kaua.ecommerce.users.infrastructure.configurations.properties.amqp.QueueProperties;
import org.springframework.amqp.core.*;
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
    @ConfigurationProperties("amqp.queues.account-created-generate-confirmation-code-mail")
    @AccountCreatedGenerateMailCodeEvent
    public QueueProperties accountCreatedGenerateConfirmationMailQueueProperties() {
        return new QueueProperties();
    }

    @Configuration
    static class Admin {

        @Bean
        @AccountEvents
        public Exchange accountEventsExchange(@AccountCreatedEvent final QueueProperties props) {
            return new FanoutExchange(props.getExchange());
        }

        @Bean
        @AccountCreatedEvent
        public Queue accountCreatedQueue(@AccountCreatedEvent final QueueProperties props) {
            return new Queue(props.getQueue());
        }

        @Bean
        @AccountCreatedGenerateMailCodeEvent
        public Queue accountCreatedGenerateMailCodeEventQueue(
                @AccountCreatedGenerateMailCodeEvent final QueueProperties props
        ) {
            return new Queue(props.getQueue());
        }

        @Bean
        @AccountCreatedEvent
        public Binding accountCreatedBinding(
                @AccountEvents FanoutExchange exchange,
                @AccountCreatedEvent Queue queue
        ) {
            return BindingBuilder.bind(queue).to(exchange);
        }

        @Bean
        @AccountCreatedGenerateMailCodeEvent
        public Binding accountCreatedGenerateMailCodeEventBinding(
                @AccountEvents FanoutExchange exchange,
                @AccountCreatedGenerateMailCodeEvent Queue queue
        ) {
            return BindingBuilder.bind(queue).to(exchange);
        }
    }
}
