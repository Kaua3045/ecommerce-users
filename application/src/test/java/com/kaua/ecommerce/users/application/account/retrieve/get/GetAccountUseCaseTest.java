package com.kaua.ecommerce.users.application.account.retrieve.get;

import com.kaua.ecommerce.users.application.gateways.AccountGateway;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.accounts.AccountMailStatus;
import com.kaua.ecommerce.users.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;

@ExtendWith(MockitoExtension.class)
public class GetAccountUseCaseTest {

    @Mock
    private AccountGateway accountGateway;

    @InjectMocks
    private DefaultGetAccountUseCase useCase;

    @Test
    void givenAValidCommand_whenCallGetByIdAccount_thenShouldReturneAnAccount() {
        // given
        final var aFirstName = "Fulano";
        final var aLastName = "Silveira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "1234567Ab";
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                aPassword
        );

        final var aCommand = GetAccountCommand.with(aAccount.getId().getValue());

        // when
        Mockito.when(accountGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(aAccount));

        final var aOutput = Assertions.assertDoesNotThrow(() -> useCase.execute(aCommand));

        // then
        Assertions.assertNotNull(aOutput);
        Assertions.assertEquals(aOutput.id(), aAccount.getId().getValue());
        Assertions.assertEquals(aOutput.firstName(), aAccount.getFirstName());
        Assertions.assertEquals(aOutput.lastName(), aAccount.getLastName());
        Assertions.assertEquals(aOutput.email(), aAccount.getEmail());
        Assertions.assertEquals(aOutput.mailStatus(), aAccount.getMailStatus().name());
        Assertions.assertEquals(aOutput.avatarUrl(), aAccount.getAvatarUrl());
        Assertions.assertEquals(aOutput.createdAt(), aAccount.getCreatedAt().toString());
        Assertions.assertEquals(aOutput.updatedAt(), aAccount.getUpdatedAt().toString());

        Mockito.verify(accountGateway, Mockito.times(1))
                .findById(aAccount.getId().getValue());
    }

    @Test
    void givenAnInvalidId_whenCallGetByIdAccount_thenShouldThrowNotFoundException() {
        // given
        final var expectedErrorMessage = "Account with id 123 was not found";

        final var aCommand = GetAccountCommand.with("123");

        // when
        Mockito.when(accountGateway.findById(Mockito.any()))
                .thenReturn(Optional.empty());

        final var aOutput = Assertions.assertThrows(NotFoundException.class,
                () -> useCase.execute(aCommand));

        // then
        Assertions.assertEquals(expectedErrorMessage, aOutput.getMessage());

        Mockito.verify(accountGateway, Mockito.times(1))
                .findById(Mockito.any());
    }
}
