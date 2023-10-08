package com.kaua.ecommerce.users.infrastructure.amqp;

import com.kaua.ecommerce.users.AmqpTest;
import com.kaua.ecommerce.users.application.usecases.account.mail.create.CreateAccountMailCommand;
import com.kaua.ecommerce.users.application.usecases.account.mail.create.CreateAccountMailOutput;
import com.kaua.ecommerce.users.application.usecases.account.mail.create.CreateAccountMailUseCase;
import com.kaua.ecommerce.users.application.either.Either;
import com.kaua.ecommerce.users.application.gateways.AccountGateway;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.accounts.AccountCreatedEvent;
import com.kaua.ecommerce.users.domain.accounts.mail.AccountMailType;
import com.kaua.ecommerce.users.domain.exceptions.NotFoundException;
import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.domain.roles.RoleTypes;
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

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@AmqpTest
public class AccountCreatedListenerTest {

    @Autowired
    private TestRabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitListenerTestHarness harness;

    @MockBean
    private CreateAccountMailUseCase useCase;

    @MockBean
    private AccountGateway accountGateway;

    @Autowired
    @AccountCreatedGenerateMailCodeEvent
    private QueueProperties queueProperties;

    @Test
    void givenAccount_whenCallsListener_shouldCallUseCase() throws InterruptedException {
        final var aAccount = Account.newAccount(
                "teste",
                "testes",
                "teste@testes.com",
                "123456789Ab",
                Role.newRole("ceo", null, RoleTypes.EMPLOYEES, false)
        );
        final var aAccountCreatedEvent = new AccountCreatedEvent(
                aAccount.getId().getValue(),
                aAccount.getFirstName(),
                aAccount.getLastName(),
                aAccount.getEmail()
        );

        final var expectedMessage = Json.writeValueAsString(aAccountCreatedEvent);

        Mockito.when(accountGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(aAccount));
        Mockito.when(useCase.execute(Mockito.any()))
                .thenReturn(Either.right(CreateAccountMailOutput.from("123456")));

        this.rabbitTemplate.convertAndSend(queueProperties.getQueue(), expectedMessage);

        final var invocationData =
                harness.getNextInvocationDataFor(
                        AccountMailGenerateCodeListener.ACCOUNT_MAIL_GENERATE_CODE_LISTENER,
                        1, TimeUnit.SECONDS);

        Assertions.assertNotNull(invocationData);
        Assertions.assertNotNull(invocationData.getArguments());

        final var actualMessage = invocationData.getArguments()[0].toString();
        Assertions.assertEquals(expectedMessage, actualMessage);

        final var cmdCaptor = ArgumentCaptor.forClass(CreateAccountMailCommand.class);
        Mockito.verify(useCase, Mockito.times(1)).execute(cmdCaptor.capture());

        final var actualCommand = cmdCaptor.getValue();
        Assertions.assertEquals(aAccount.getId().getValue(), actualCommand.account().getId().getValue());
        Assertions.assertEquals(AccountMailType.ACCOUNT_CONFIRMATION, actualCommand.type());
    }

    @Test
    void givenAnInvalidAccount_whenCallsListener_shouldThrowNotFoundException() {
        final var expectedErrorMessage = "Account with id 123456789 was not found";

        final var createAccountMailUseCase = Mockito.mock(CreateAccountMailUseCase.class);
        final var accountGateway = Mockito.mock(AccountGateway.class);

        final var listener = new AccountMailGenerateCodeListener(
                createAccountMailUseCase,
                accountGateway);

        final var aMessage = "{\"id\":\"123456789\",\"firstName\":\"teste\",\"lastName\":\"testes\",\"email\":\"teste@teste.com\"}";

        Mockito.when(accountGateway.findById(Mockito.any()))
                .thenReturn(Optional.empty());

        final var aException = Assertions.assertThrows(
                NotFoundException.class,
                () -> listener.onGenerateAccountMailCode(aMessage));

        Assertions.assertEquals(expectedErrorMessage, aException.getMessage());

        Mockito.verify(createAccountMailUseCase, Mockito.times(0))
                .execute(Mockito.any());
    }
}
