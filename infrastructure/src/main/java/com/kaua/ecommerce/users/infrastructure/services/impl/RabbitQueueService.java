package com.kaua.ecommerce.users.infrastructure.services.impl;

import com.kaua.ecommerce.users.application.gateways.QueueGateway;
import com.kaua.ecommerce.users.infrastructure.configurations.json.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitOperations;

import java.util.Objects;

public class RabbitQueueService implements QueueGateway {

    private static final Logger log = LoggerFactory.getLogger(RabbitQueueService.class);

    private final String exchange;
    private final String routingKey;
    private final RabbitOperations ops;

    public RabbitQueueService(
            final String exchange,
            final String routingKey,
            final RabbitOperations ops
    ) {
        this.exchange = Objects.requireNonNull(exchange);
        this.routingKey = Objects.requireNonNull(routingKey);
        this.ops = Objects.requireNonNull(ops);
    }

    @Override
    public void send(final Object event) {
        try {
            final var aCorrelationData = new CorrelationData();
            this.ops.convertAndSend(this.exchange, this.routingKey, Json.writeValueAsString(event), aCorrelationData);
        } catch (final Exception e) {
            log.error("Error sending message", e);
            throw e;
        }
    }
}
