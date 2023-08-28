package com.kaua.ecommerce.users.infrastructure.services.impl;

import com.kaua.ecommerce.users.infrastructure.configurations.json.Json;
import com.kaua.ecommerce.users.infrastructure.services.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.UncategorizedAmqpException;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitOperations;

import java.util.Objects;

public class RabbitEventService implements EventService {

    private static final Logger log = LoggerFactory.getLogger(RabbitEventService.class);

    private final String exchange;
    private final RabbitOperations ops;

    public RabbitEventService(
            final String exchange,
            final RabbitOperations ops
    ) {
        this.exchange = Objects.requireNonNull(exchange);
        this.ops = Objects.requireNonNull(ops);
    }

    @Override
    public void send(final Object event, final String routingKey) {
        try {
            final var aCorrelationData = new CorrelationData();
            this.ops.convertAndSend(this.exchange, routingKey, Json.writeValueAsString(event), aCorrelationData);
        } catch (final UncategorizedAmqpException e) {
            // save event in dynamodb to retry later
            log.error("Error sending message: {}", e.getMessage());
            throw e;
        }
    }
}
