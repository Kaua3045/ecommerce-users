package com.kaua.ecommerce.users.infrastructure.services.local;


import com.kaua.ecommerce.users.application.gateways.QueueGateway;
import com.kaua.ecommerce.users.infrastructure.configurations.json.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InMemoryQueueService implements QueueGateway {

    private static final Logger log = LoggerFactory.getLogger(InMemoryQueueService.class);

    @Override
    public void send(Object event) {
        log.debug("Queue message was observed: {}", Json.writeValueAsString(event));
    }
}
