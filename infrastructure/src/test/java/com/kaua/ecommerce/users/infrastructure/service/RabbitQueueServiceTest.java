package com.kaua.ecommerce.users.infrastructure.service;

import com.kaua.ecommerce.users.AmqpTest;
import com.kaua.ecommerce.users.application.account.mail.create.CreateMailQueueCommand;
import com.kaua.ecommerce.users.application.gateways.QueueGateway;
import com.kaua.ecommerce.users.domain.accounts.mail.AccountMailType;
import com.kaua.ecommerce.users.domain.utils.RandomStringUtils;
import com.kaua.ecommerce.users.infrastructure.configurations.annotations.EmailQueue;
import com.kaua.ecommerce.users.infrastructure.configurations.json.Json;
import com.kaua.ecommerce.users.infrastructure.services.impl.RabbitQueueService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

@AmqpTest
public class RabbitQueueServiceTest {

    public static final String LISTENER = "email.send";

    @Autowired
    @EmailQueue
    private QueueGateway publisher;

    @Autowired
    private RabbitListenerTestHarness harness;

    @Test
    void shouldSendQueueMessage() throws InterruptedException {
        final var aMail = CreateMailQueueCommand.with(
                RandomStringUtils.generateValue(36),
                "Teste",
                "teste@teste.com",
                AccountMailType.ACCOUNT_CONFIRMATION.name()
        );

        final var expectedMessage = Json.writeValueAsString(aMail);

        this.publisher.send(aMail);

        final var invocationData =
                harness.getNextInvocationDataFor(LISTENER, 1, TimeUnit.SECONDS);

        Assertions.assertNotNull(invocationData);
        Assertions.assertNotNull(invocationData.getArguments());

        final var actualMessage = invocationData.getArguments()[0].toString();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void shouldSendQueueMessageButThrowsException() {
        final var rabbitOperations = Mockito.mock(RabbitOperations.class);

        Mockito.doThrow(new RuntimeException("Simulated Exception"))
                .when(rabbitOperations)
                .convertAndSend(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                Mockito.any(CorrelationData.class));

        final var queueService = new RabbitQueueService("test", "teste", rabbitOperations);

        final var aMail = CreateMailQueueCommand.with(
                RandomStringUtils.generateValue(36),
                "Teste",
                "teste@teste.com",
                AccountMailType.ACCOUNT_CONFIRMATION.name()
        );

        Assertions.assertThrows(RuntimeException.class, () -> queueService.send(aMail));
    }
}