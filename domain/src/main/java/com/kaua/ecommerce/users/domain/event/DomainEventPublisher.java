package com.kaua.ecommerce.users.domain.event;

@FunctionalInterface
public interface DomainEventPublisher {

    <T extends DomainEvent> void publish(T event, String routingKey);
}
