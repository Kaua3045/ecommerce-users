package com.kaua.ecommerce.users.application.gateways;

public interface QueueGateway {

    void send(Object aPayload);
}
