package com.kaua.ecommerce.users.infrastructure.configurations;

import com.kaua.ecommerce.users.infrastructure.configurations.annotations.AccountCreatedEvent;
import com.kaua.ecommerce.users.infrastructure.configurations.annotations.AccountCreatedGenerateMailCodeEvent;
import com.kaua.ecommerce.users.infrastructure.configurations.annotations.AccountDeleteEvent;
import com.kaua.ecommerce.users.infrastructure.configurations.annotations.AccountEvents;
import com.kaua.ecommerce.users.infrastructure.configurations.properties.amqp.QueueProperties;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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

        @Bean
        @Profile("production")
        public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
            final var template = new RabbitTemplate(connectionFactory);
            template.setMandatory(true);
            template.setReturnsCallback(returned -> {
                System.out.println("Message returned");
                System.out.println(returned.getMessage());
                // TODO: falhou, se retorno é porque falhou, pronto!
            });
            template.setConfirmCallback((correlationData, ack, cause) -> {
                System.out.println("Message confirmed");
                System.out.println(correlationData);
                System.out.println(ack);
                System.out.println(cause);
                // TODO: foi entregue (pode não ter sido enviada para uma routingkey)
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
