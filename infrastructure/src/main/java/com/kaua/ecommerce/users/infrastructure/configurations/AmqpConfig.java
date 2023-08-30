package com.kaua.ecommerce.users.infrastructure.configurations;

import com.kaua.ecommerce.users.infrastructure.configurations.annotations.AccountCreatedEvent;
import com.kaua.ecommerce.users.infrastructure.configurations.annotations.AccountCreatedGenerateMailCodeEvent;
import com.kaua.ecommerce.users.infrastructure.configurations.annotations.AccountDeleteEvent;
import com.kaua.ecommerce.users.infrastructure.configurations.annotations.AccountEvents;
import com.kaua.ecommerce.users.infrastructure.configurations.properties.amqp.QueueProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

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

    @Bean
    @ConfigurationProperties("amqp.queues.account-deleted")
    @AccountDeleteEvent
    public QueueProperties accountDeletedQueueProperties() {
        return new QueueProperties();
    }

    @Configuration
    static class Admin {

        private static final Logger log = LoggerFactory.getLogger(Admin.class);

        @Bean
        @Profile({"production", "test-integration"})
        public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
            final var template = new RabbitTemplate(connectionFactory);
            template.setMandatory(true);
            template.setReturnsCallback(returned -> {
                log.info("Message returned: {}, body: {}", returned, new String(returned.getMessage().getBody()));
                // falhou, se retorno é porque falhou, pronto!
            });
            template.setConfirmCallback((correlationData, ack, cause) -> {
                if (!ack) {
                    log.info("Message failed: {}, cause: {}, ack: {}", correlationData, cause, ack);
                    // falhou a publicação no rabbitmq (timeout ou erro)
                }
                log.debug("Message confirmed: {}, ack: {}", correlationData, ack);
                // foi entregue (pode não ter sido enviada para uma routingkey)
            });
            return template;
        }

        @Bean
        @AccountEvents
        public Exchange accountEventsExchange(@AccountCreatedEvent final QueueProperties props) {
            return new TopicExchange(props.getExchange());
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
                @AccountEvents TopicExchange exchange,
                @AccountCreatedEvent Queue queue,
                @AccountCreatedEvent final QueueProperties props
        ) {
            return BindingBuilder.bind(queue).to(exchange).with(props.getRoutingKey());
        }

        @Bean
        @AccountCreatedGenerateMailCodeEvent
        public Binding accountCreatedGenerateMailCodeEventBinding(
                @AccountEvents TopicExchange exchange,
                @AccountCreatedGenerateMailCodeEvent Queue queue,
                @AccountCreatedGenerateMailCodeEvent final QueueProperties props
        ) {
            return BindingBuilder.bind(queue).to(exchange).with(props.getRoutingKey());
        }

        @Bean
        @AccountDeleteEvent
        public Queue accountDeletedQueue(@AccountDeleteEvent final QueueProperties props) {
            return new Queue(props.getQueue());
        }

        @Bean
        @AccountDeleteEvent
        public Binding accountDeletedBinding(
                @AccountEvents TopicExchange exchange,
                @AccountDeleteEvent Queue queue,
                @AccountDeleteEvent final QueueProperties props
        ) {
            return BindingBuilder.bind(queue).to(exchange).with(props.getRoutingKey());
        }
    }
}
