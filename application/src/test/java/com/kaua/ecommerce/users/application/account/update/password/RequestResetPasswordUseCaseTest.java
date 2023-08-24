package com.kaua.ecommerce.users.application.account.update.password;

import com.kaua.ecommerce.users.application.account.mail.create.CreateAccountMailUseCase;
import com.kaua.ecommerce.users.application.gateways.AccountGateway;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.accounts.mail.AccountMailType;
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

@ExtendWith(MockitoExtension.class)
public class RequestResetPasswordUseCaseTest {

    @Mock
    private AccountGateway accountGateway;

    @Mock
    private CreateAccountMailUseCase createAccountMailUseCase;

    @InjectMocks
    private DefaultRequestResetPasswordUseCase useCase;

    @Test
    public void givenAValidCommand_whenCallRequestResetPassowrd_thenShouldDoesNotThrow() {
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

        final var aCommand = RequestResetPasswordCommand.with(aEmail);

        // when
        Mockito.when(accountGateway.findByEmail(Mockito.any()))
                .thenReturn(Optional.of(aAccount));

        Assertions.assertDoesNotThrow(() -> useCase.execute(aCommand));

        Mockito.verify(accountGateway, Mockito.times(1))
                .findByEmail(aEmail);
        Mockito.verify(createAccountMailUseCase, Mockito.times(1))
                .execute(Mockito.argThat(cmd ->
                        Objects.equals(aAccount.getId().getValue(), cmd.accountId()) &&
                        Objects.equals(AccountMailType.PASSWORD_RESET, cmd.type())
                ));
    }

    @Test
    public void givenAnInvalidCommand_whenCallRequestResetPassowrd_thenShouldThrowException() {
        // given
        final var aEmail = "teste@teste.com";
        final var expectedErrorMessage = "Account with id teste@teste.com was not found";

        final var aCommand = RequestResetPasswordCommand.with(aEmail);

        // when
        Mockito.when(accountGateway.findByEmail(Mockito.any()))
                .thenReturn(Optional.empty());

        final var aException = Assertions.assertThrows(NotFoundException.class,
                () -> useCase.execute(aCommand));

        Assertions.assertEquals(expectedErrorMessage, aException.getMessage());

        Mockito.verify(accountGateway, Mockito.times(1))
                .findByEmail(aEmail);
        Mockito.verify(createAccountMailUseCase, Mockito.times(0))
                .execute(Mockito.any());
    }
}
