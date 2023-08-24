package com.kaua.ecommerce.users.application.account.mail.confirm;

import com.kaua.ecommerce.users.application.gateways.AccountGateway;
import com.kaua.ecommerce.users.application.gateways.AccountMailGateway;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.accounts.AccountMailStatus;
import com.kaua.ecommerce.users.domain.accounts.mail.AccountMail;
import com.kaua.ecommerce.users.domain.accounts.mail.AccountMailType;
import com.kaua.ecommerce.users.domain.exceptions.NotFoundException;
import com.kaua.ecommerce.users.domain.utils.InstantUtils;
import com.kaua.ecommerce.users.domain.utils.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;

@ExtendWith(MockitoExtension.class)
public class ConfirmAccountMailUseCaseTest {

    @Mock
    private AccountMailGateway accountMailGateway;

    @Mock
    private AccountGateway accountGateway;

    @InjectMocks
    private DefaultConfirmAccountMailUseCase useCase;

    @Test
    void givenAValidCommand_whenCallConfirmAccount_thenShouldReturneTrue() {
        // given
        final var aToken = RandomStringUtils.generateValue(36);
        final var aAccount = Account.newAccount(
                "Fulano",
                "Silveira",
                "teste@teste.com",
                "1234567Ab"
        );
        final var aType = AccountMailType.ACCOUNT_CONFIRMATION;
        final var aExpirestAt = InstantUtils.now().plus(10, ChronoUnit.MINUTES);
        final var aAccountMail = AccountMail.newAccountMail(
                aToken,
                aType,
                aAccount,
                aExpirestAt
        );

        final var aCommand = ConfirmAccountMailCommand.with(aToken);

        // when
        Mockito.when(accountMailGateway.findByToken(Mockito.any()))
                .thenReturn(Optional.of(aAccountMail));
        Mockito.when(accountGateway.update(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        final var aOutput = useCase.execute(aCommand).getRight();

        // then
        Assertions.assertTrue(aOutput);

        Mockito.verify(accountMailGateway, Mockito.times(1))
                .findByToken(Mockito.any());
        Mockito.verify(accountGateway, Mockito.times(1))
                .update(Mockito.argThat(account ->
                        Objects.nonNull(account.getId()) &&
                                Objects.equals(AccountMailStatus.CONFIRMED, account.getMailStatus()) &&
                                Objects.equals(aAccount.getFirstName(), account.getFirstName()) &&
                                Objects.equals(aAccount.getLastName(), account.getLastName()) &&
                                Objects.equals(aAccount.getEmail(), account.getEmail()) &&
                                Objects.equals(aAccount.getPassword(), account.getPassword()) &&
                                Objects.isNull(account.getAvatarUrl()) &&
                                Objects.nonNull(account.getCreatedAt()) &&
                                Objects.nonNull(account.getUpdatedAt())
                ));
        Mockito.verify(accountMailGateway, Mockito.times(1))
                .deleteById(Mockito.any());
    }

    @Test
    void givenAnInvalidCommand_whenCallConfirmAccount_thenShouldThrowsDomainException() {
        // given
        final var expectedErrorMessage = "Token expired";
        final var expectedErrorCount = 1;

        final var aToken = RandomStringUtils.generateValue(36);
        final var aAccount = Account.newAccount(
                "Fulano",
                "Silveira",
                "teste@teste.com",
                "1234567Ab"
        );
        final var aType = AccountMailType.ACCOUNT_CONFIRMATION;
        final var aExpirestAt = InstantUtils.now().minus(1, ChronoUnit.HOURS);
        final var aAccountMail = AccountMail.newAccountMail(
                aToken,
                aType,
                aAccount,
                aExpirestAt
        );

        final var aCommand = ConfirmAccountMailCommand.with(aToken);

        // when
        Mockito.when(accountMailGateway.findByToken(Mockito.any()))
                .thenReturn(Optional.of(aAccountMail));

        final var aOutput = useCase.execute(aCommand).getLeft();

        // then
        Assertions.assertEquals(expectedErrorMessage, aOutput.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aOutput.getErrors().size());

        Mockito.verify(accountMailGateway, Mockito.times(1))
                .findByToken(Mockito.any());
        Mockito.verify(accountGateway, Mockito.times(0))
                .update(Mockito.any());
        Mockito.verify(accountMailGateway, Mockito.times(0))
                .deleteById(Mockito.any());
    }

    @Test
    void givenAValidCommandWithAccountMailNotPersisted_whenCallConfirmAccount_thenShouldReturnNotFoundException() {
        // given
        final var expectedErrorMessage = "AccountMail with id empty was not found";

        final var aCommand = ConfirmAccountMailCommand.with("empty");

        // when
        Mockito.when(accountMailGateway.findByToken(Mockito.any()))
                .thenReturn(Optional.empty());

        final var aOutput = Assertions.assertThrows(NotFoundException.class,
                () -> useCase.execute(aCommand));

        // then
        Assertions.assertEquals(expectedErrorMessage, aOutput.getMessage());

        Mockito.verify(accountMailGateway, Mockito.times(1))
                .findByToken(Mockito.any());
        Mockito.verify(accountGateway, Mockito.times(0))
                .update(Mockito.any());
        Mockito.verify(accountMailGateway, Mockito.times(0))
                .deleteById(Mockito.any());
    }
}
