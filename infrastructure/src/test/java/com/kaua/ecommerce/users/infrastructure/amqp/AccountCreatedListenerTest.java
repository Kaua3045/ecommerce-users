package com.kaua.ecommerce.users.infrastructure.amqp;

import com.kaua.ecommerce.users.AmqpTest;
import com.kaua.ecommerce.users.application.account.mail.create.CreateAccountMailCommand;
import com.kaua.ecommerce.users.application.account.mail.create.CreateAccountMailOutput;
import com.kaua.ecommerce.users.application.account.mail.create.CreateAccountMailUseCase;
import com.kaua.ecommerce.users.application.either.Either;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.accounts.AccountCreatedEvent;
import com.kaua.ecommerce.users.infrastructure.configurations.annotations.AccountCreatedGenerateMailCodeEvent;
import com.kaua.ecommerce.users.infrastructure.configurations.json.Json;
import com.kaua.ecommerce.users.infrastructure.configurations.properties.amqp.QueueProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.amqp.rabbit.test.TestRabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.concurrent.TimeUnit;

@AmqpTest
public class AccountCreatedListenerTest {

    @Autowired
    private TestRabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitListenerTestHarness harness;

    @MockBean
    private CreateAccountMailUseCase useCase;

    @Autowired
    @AccountCreatedGenerateMailCodeEvent
    private QueueProperties queueProperties;

    @Test
    public void givenAccount_whenCallsListener_shouldCallUseCase() throws InterruptedException {
        final var aAccount = Account.newAccount(
                "teste",
                "testes",
                "teste@testes.com",
                "123456789Ab"
        );
        final var aAccountCreatedEvent = new AccountCreatedEvent(
                aAccount.getId().getValue(),
                aAccount.getFirstName(),
                aAccount.getLastName(),
                aAccount.getEmail()
        );

        final var expectedMessage = Json.writeValueAsString(aAccountCreatedEvent);

        Mockito.when(useCase.execute(Mockito.any()))
                .thenReturn(Either.right(CreateAccountMailOutput.from("123456")));

        this.rabbitTemplate.convertAndSend(queueProperties.getQueue(), expectedMessage);

        final var invocationData =
                harness.getNextInvocationDataFor(
                        AccountMailGenerateCodeListener.ACCOUNT_MAIL_GENERATE_CODE_LISTENER,
                        1, TimeUnit.SECONDS);

        System.out.println(invocationData);
        Assertions.assertNotNull(invocationData);
        Assertions.assertNotNull(invocationData.getArguments());

        final var actualMessage = invocationData.getArguments()[0].toString();
        Assertions.assertEquals(expectedMessage, actualMessage);

        final var cmdCaptor = ArgumentCaptor.forClass(CreateAccountMailCommand.class);
        Mockito.verify(useCase, Mockito.times(1)).execute(cmdCaptor.capture());

        final var actualCommand = cmdCaptor.getValue();
        System.out.println(actualCommand);
    }
}
