package com.kaua.ecommerce.users.infrastructure.services.impl;

import com.kaua.ecommerce.users.application.gateways.QueueGateway;
import com.kaua.ecommerce.users.infrastructure.configurations.json.Json;
import org.springframework.amqp.rabbit.core.RabbitOperations;

import java.util.Objects;

public class RabbitQueueService implements QueueGateway {

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
        this.ops.convertAndSend(this.exchange, this.routingKey, Json.writeValueAsString(event));
    }
}
