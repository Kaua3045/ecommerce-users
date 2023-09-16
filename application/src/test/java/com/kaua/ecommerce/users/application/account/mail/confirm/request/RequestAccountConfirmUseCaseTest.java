package com.kaua.ecommerce.users.application.account.mail.confirm.request;

import com.kaua.ecommerce.users.application.account.mail.create.CreateAccountMailOutput;
import com.kaua.ecommerce.users.application.account.mail.create.CreateAccountMailUseCase;
import com.kaua.ecommerce.users.application.either.Either;
import com.kaua.ecommerce.users.application.gateways.AccountGateway;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.accounts.mail.AccountMailType;
import com.kaua.ecommerce.users.domain.exceptions.NotFoundException;
import com.kaua.ecommerce.users.domain.roles.RoleID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class RequestAccountConfirmUseCaseTest {

    @Mock
    private AccountGateway accountGateway;

    @Mock
    private CreateAccountMailUseCase createAccountMailUseCase;

    @InjectMocks
    private DefaultRequestAccountConfirmUseCase useCase;

    @Test
    void givenAValidCommand_whenCallRequestAccountConfirmationCode_thenShouldReturnAccountMailId() {
        // given
        final var aFirstName = "Fulano";
        final var aLastName = "Silveira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "1234567Ab";
        final var aRoleId = RoleID.unique();
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                aPassword,
                aRoleId
        );

        final var aCommand = RequestAccountConfirmCommand.with(aAccount.getId().getValue());

        // when
        Mockito.when(accountGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(aAccount));
        Mockito.when(createAccountMailUseCase.execute(Mockito.any()))
                .thenReturn(Either.right(CreateAccountMailOutput.from("123123123123")));

        final var aOutput = useCase.execute(aCommand).getRight();

        Assertions.assertNotNull(aOutput);
        Assertions.assertNotNull(aOutput.id());

        Mockito.verify(accountGateway, Mockito.times(1))
                .findById(aAccount.getId().getValue());
        Mockito.verify(createAccountMailUseCase, Mockito.times(1))
                .execute(Mockito.argThat(cmd ->
                        Objects.equals(aAccount, cmd.account()) &&
                                Objects.equals(AccountMailType.ACCOUNT_CONFIRMATION, cmd.type())
                ));
    }

    @Test
    void givenAnInvalidCommand_whenCallRequestAccountConfirmationCode_thenShouldThrowException() {
        // given
        final var aId = "12341242421";
        final var expectedErrorMessage = "Account with id 12341242421 was not found";

        final var aCommand = RequestAccountConfirmCommand.with(aId);

        // when
        Mockito.when(accountGateway.findById(Mockito.any()))
                .thenReturn(Optional.empty());

        final var aException = Assertions.assertThrows(NotFoundException.class,
                () -> useCase.execute(aCommand));

        Assertions.assertEquals(expectedErrorMessage, aException.getMessage());

        Mockito.verify(accountGateway, Mockito.times(1))
                .findById(aId);
        Mockito.verify(createAccountMailUseCase, Mockito.times(0))
                .execute(Mockito.any());
    }
}
