package com.kaua.ecommerce.users.infrastructure.services.local;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaua.ecommerce.users.application.gateways.QueueGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InMemoryQueueService implements QueueGateway {

    private static final Logger log = LoggerFactory.getLogger(InMemoryQueueService.class);

    private final ObjectMapper mapper;

    public InMemoryQueueService(final ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void send(Object aPayload) {
        try {
            log.debug("Sending message to queue: {}", mapper.writeValueAsString(aPayload));
        } catch (Exception e) {
            log.error("Error on send message to queue", e);
        }
    }
}
