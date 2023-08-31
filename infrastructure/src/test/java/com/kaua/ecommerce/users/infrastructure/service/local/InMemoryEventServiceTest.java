package com.kaua.ecommerce.users.infrastructure.service.local;

import com.kaua.ecommerce.users.domain.accounts.AccountCreatedEvent;
import com.kaua.ecommerce.users.infrastructure.services.local.InMemoryEventService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InMemoryEventServiceTest {

    private InMemoryEventService target = new InMemoryEventService();

    @Test
    void givenAValidObject_whenCallSendEvent_shouldDoesNotThrow() {
        final var aRoutingKey = "account.created";
        final var aEvent = new AccountCreatedEvent(
                "123",
                "test",
                "testes",
                "teste@test.com"
        );

        Assertions.assertDoesNotThrow(() -> this.target.send(aEvent, aRoutingKey));
    }
}
