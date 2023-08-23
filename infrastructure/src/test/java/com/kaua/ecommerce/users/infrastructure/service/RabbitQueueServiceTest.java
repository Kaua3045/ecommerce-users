package com.kaua.ecommerce.users.infrastructure.service;

import com.kaua.ecommerce.users.AmqpTest;
import com.kaua.ecommerce.users.application.account.mail.create.CreateMailQueueCommand;
import com.kaua.ecommerce.users.application.gateways.QueueGateway;
import com.kaua.ecommerce.users.domain.accounts.mail.AccountMailType;
import com.kaua.ecommerce.users.domain.utils.RandomStringUtils;
import com.kaua.ecommerce.users.infrastructure.configurations.annotations.EmailQueue;
import com.kaua.ecommerce.users.infrastructure.configurations.json.Json;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

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
    public void shouldSendMessage() throws InterruptedException {
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
}

@Component
class EmailSendListener {

    @RabbitListener(id = RabbitQueueServiceTest.LISTENER, queues = "${amqp.queues.email-queue.routing-key}")
    public void onEmailConfirmationSend(@Payload final String message) {
        System.out.println(message);
    }
}
