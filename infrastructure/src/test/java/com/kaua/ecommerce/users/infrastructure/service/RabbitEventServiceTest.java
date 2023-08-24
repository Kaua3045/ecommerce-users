package com.kaua.ecommerce.users.infrastructure.service;

import com.kaua.ecommerce.users.AmqpTest;
import com.kaua.ecommerce.users.infrastructure.configurations.annotations.AccountCreatedEvent;
import com.kaua.ecommerce.users.infrastructure.configurations.json.Json;
import com.kaua.ecommerce.users.infrastructure.services.EventService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

@AmqpTest
public class RabbitEventServiceTest {

    public static final String LISTENER = "account.created";

    @Autowired
    @AccountCreatedEvent
    private EventService publisher;

    @Autowired
    private RabbitListenerTestHarness harness;

    @Test
    void shouldSendMessage() throws InterruptedException {
        final var aEvent = new com.kaua.ecommerce.users.domain.accounts.AccountCreatedEvent(
                "123",
                "teste",
                "testes",
                "testes@teste.com"
        );

        final var expectedMessage = Json.writeValueAsString(aEvent);

        this.publisher.send(aEvent);

        final var invocationData =
                harness.getNextInvocationDataFor(LISTENER, 1, TimeUnit.SECONDS);

        Assertions.assertNotNull(invocationData);
        Assertions.assertNotNull(invocationData.getArguments());

        final var actualMessage = invocationData.getArguments()[0].toString();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }
}