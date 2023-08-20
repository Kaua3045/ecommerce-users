package com.kaua.ecommerce.users.infrastructure.services;

@FunctionalInterface
public interface EventService {

    void send(Object event);
}
