package com.kaua.ecommerce.users.infrastructure.service.local;

import com.kaua.ecommerce.users.domain.accounts.AccountCreatedEvent;
import com.kaua.ecommerce.users.infrastructure.services.local.InMemoryQueueService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InMemoryQueueServiceTest {

    private InMemoryQueueService target = new InMemoryQueueService();

    @Test
    void givenAValidObject_whenCallSendQueue_shouldDoesNotThrow() {
        final var aEvent = new AccountCreatedEvent(
                "123",
                "test",
                "testes",
                "teste@test.com"
        );

        Assertions.assertDoesNotThrow(() -> this.target.send(aEvent));
    }
}
