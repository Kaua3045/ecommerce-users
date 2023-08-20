package com.kaua.ecommerce.users.infrastructure.services.local;


import com.kaua.ecommerce.users.infrastructure.configurations.json.Json;
import com.kaua.ecommerce.users.infrastructure.services.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InMemoryEventService implements EventService {

    private static final Logger log = LoggerFactory.getLogger(InMemoryEventService.class);

    @Override
    public void send(Object event) {
        log.debug("Event was observed: {}", Json.writeValueAsString(event));
    }
}
