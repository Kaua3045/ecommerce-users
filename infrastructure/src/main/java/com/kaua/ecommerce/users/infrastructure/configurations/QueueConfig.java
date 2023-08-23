package com.kaua.ecommerce.users.infrastructure.configurations;

import com.kaua.ecommerce.users.application.gateways.QueueGateway;
import com.kaua.ecommerce.users.infrastructure.configurations.annotations.EmailQueue;
import com.kaua.ecommerce.users.infrastructure.configurations.properties.amqp.QueueProperties;
import com.kaua.ecommerce.users.infrastructure.services.impl.RabbitQueueService;
import com.kaua.ecommerce.users.infrastructure.services.local.InMemoryQueueService;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class QueueConfig {

    @Bean
    @ConfigurationProperties("amqp.queues.email-queue")
    @EmailQueue
    public QueueProperties mailQueueProperties() {
        return new QueueProperties();
    }

    @Bean
    @EmailQueue
    @Profile("development")
    public QueueGateway inMemoryMailQueueService() {
        return new InMemoryQueueService();
    }

    @Bean
    @EmailQueue
    @ConditionalOnMissingBean
    public QueueGateway rabbitMailQueueService(
            @EmailQueue QueueProperties props,
            final RabbitOperations ops
    ) {
        return new RabbitQueueService(props.getExchange(), props.getRoutingKey(), ops);
    }

    @Configuration
    static class Admin {

        @Bean
        @EmailQueue
        public Exchange emailExchange(@EmailQueue final QueueProperties props) {
            return new DirectExchange(props.getExchange());
        }

        @Bean
        @EmailQueue
        public Queue emailQueue(@EmailQueue final QueueProperties props) {
            return new Queue(props.getQueue());
        }

        @Bean
        @EmailQueue
        public Binding emailBinding(
                @EmailQueue DirectExchange exchange,
                @EmailQueue Queue queue,
                @EmailQueue final QueueProperties props
        ) {
            return BindingBuilder.bind(queue).to(exchange).with(props.getRoutingKey());
        }
    }
}
