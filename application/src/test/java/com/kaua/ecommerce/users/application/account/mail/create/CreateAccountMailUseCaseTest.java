package com.kaua.ecommerce.users.application.account.mail.create;

import com.kaua.ecommerce.users.application.gateways.AccountGateway;
import com.kaua.ecommerce.users.application.gateways.AccountMailGateway;
import com.kaua.ecommerce.users.application.gateways.QueueGateway;
import com.kaua.ecommerce.users.domain.accounts.Account;
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

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;

@ExtendWith(MockitoExtension.class)
public class CreateAccountMailUseCaseTest {

    @Mock
    private AccountMailGateway accountMailGateway;

    @Mock
    private AccountGateway accountGateway;

    @Mock
    private QueueGateway queueGateway;

    @InjectMocks
    private DefaultCreateAccountMailUseCase useCase;

    @Test
    public void givenAValidCommand_whenCallCreateAccountMail_thenShouldReturneAnAccountMail() {
        // given
        final var aToken = RandomStringUtils.generateValue(36);
        final var aAccount = Account.newAccount(
                "Fulano",
                "Silveira",
                "teste@teste.com",
                "1234567Ab"
        );
        final var aType = AccountMailType.ACCOUNT_CONFIRMATION;
        final var aSubject = "Account confirmation";
        final var aExpirestAt = InstantUtils.now().plus(10, ChronoUnit.MINUTES);

        final var aCommand = CreateAccountMailCommand.with(
                aAccount.getId().getValue(),
                aToken,
                aType,
                aSubject,
                aExpirestAt
        );

        // when
        Mockito.when(accountGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(aAccount));
        Mockito.when(accountMailGateway.findAllByAccountId(Mockito.any()))
                .thenReturn(List.of());
        Mockito.when(accountMailGateway.create(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        final var aOutput = useCase.execute(aCommand).getRight();

        // then
        Assertions.assertNotNull(aOutput);

        Mockito.verify(accountGateway, Mockito.times(1))
                .findById(Mockito.any());
        Mockito.verify(accountMailGateway, Mockito.times(1))
                .findAllByAccountId(Mockito.any());
        Mockito.verify(accountMailGateway, Mockito.times(1))
                .create(Mockito.argThat(aAccountMail ->
                        Objects.equals(aToken, aAccountMail.getToken()) &&
                        Objects.equals(aAccount.getId(), aAccountMail.getAccount().getId()) &&
                        Objects.equals(aType, aAccountMail.getType()) &&
                        Objects.equals(aExpirestAt, aAccountMail.getExpiresAt()) &&
                        Objects.nonNull(aAccountMail.getId()) &&
                        Objects.nonNull(aAccountMail.getCreatedAt()) &&
                        Objects.nonNull(aAccountMail.getUpdatedAt())
                ));
    }

    @Test
    public void givenAnInvalidToken_whenCallCreateAccountMail_thenShouldReturnAnError() {
        // given
        final var aToken = "";
        final var aAccount = Account.newAccount(
                "Fulano",
                "Silveira",
                "teste@teste.com",
                "1234567Ab"
        );
        final var aType = AccountMailType.ACCOUNT_CONFIRMATION;
        final var aSubject = "Account confirmation";
        final var aExpirestAt = InstantUtils.now().plus(10, ChronoUnit.MINUTES);
        final var expectedErrorMessage = "'token' should not be null or blank";
        final var expectedErrorCount = 1;

        final var aCommand = CreateAccountMailCommand.with(
                aAccount.getId().getValue(),
                aToken,
                aType,
                aSubject,
                aExpirestAt
        );

        // when
        Mockito.when(accountGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(aAccount));

        final var aNotification = useCase.execute(aCommand).getLeft();

        // then
        Assertions.assertEquals(expectedErrorCount, aNotification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, aNotification.getErrors().get(0).message());

        Mockito.verify(accountGateway, Mockito.times(1))
                .findById(Mockito.any());
        Mockito.verify(accountMailGateway, Mockito.times(1))
                .findAllByAccountId(Mockito.any());
        Mockito.verify(accountMailGateway, Mockito.never())
                .create(Mockito.any());
    }

    @Test
    public void givenAnInvalidTokenLenghtMoreThan36_whenCallCreateAccountMail_thenShouldReturnAnError() {
        // given
        final var aToken = RandomStringUtils.generateValue(37);
        final var aAccount = Account.newAccount(
                "Fulano",
                "Silveira",
                "teste@teste.com",
                "1234567Ab"
        );
        final var aType = AccountMailType.ACCOUNT_CONFIRMATION;
        final var aSubject = "Account confirmation";
        final var aExpirestAt = InstantUtils.now().plus(10, ChronoUnit.MINUTES);
        final var expectedErrorMessage = "'token' should not be greater than 36";
        final var expectedErrorCount = 1;

        final var aCommand = CreateAccountMailCommand.with(
                aAccount.getId().getValue(),
                aToken,
                aType,
                aSubject,
                aExpirestAt
        );

        // when
        Mockito.when(accountGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(aAccount));

        final var aNotification = useCase.execute(aCommand).getLeft();

        // then
        Assertions.assertEquals(expectedErrorCount, aNotification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, aNotification.getErrors().get(0).message());

        Mockito.verify(accountGateway, Mockito.times(1))
                .findById(Mockito.any());
        Mockito.verify(accountMailGateway, Mockito.times(1))
                .findAllByAccountId(Mockito.any());
        Mockito.verify(accountMailGateway, Mockito.never())
                .create(Mockito.any());
    }

    @Test
    public void givenAnInvalidAccountIdIsNull_whenCallCreateAccountMail_thenShouldReturnAnError() {
        // given
        final var aToken = RandomStringUtils.generateValue(36);
        final String aAccount = null;
        final var aType = AccountMailType.ACCOUNT_CONFIRMATION;
        final var aSubject = "Account confirmation";
        final var aExpirestAt = InstantUtils.now().plus(10, ChronoUnit.MINUTES);
        final var expectedErrorMessage = "'accountId' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = CreateAccountMailCommand.with(
                aAccount,
                aToken,
                aType,
                aSubject,
                aExpirestAt
        );

        // when
        final var aNotification = useCase.execute(aCommand).getLeft();

        // then
        Assertions.assertEquals(expectedErrorCount, aNotification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, aNotification.getErrors().get(0).message());

        Mockito.verify(accountGateway, Mockito.times(0))
                .findById(Mockito.any());
        Mockito.verify(accountMailGateway, Mockito.times(0))
                .findAllByAccountId(Mockito.any());
        Mockito.verify(accountMailGateway, Mockito.never())
                .create(Mockito.any());
    }

    @Test
    public void givenAnInvalidAccount_whenCallCreateAccountMail_thenShouldReturnAnError() {
        // given
        final var aToken = RandomStringUtils.generateValue(36);
        final var aAccount = "84045cfc-705f-4418-bf4b-155d5d11f69f";
        final var aType = AccountMailType.ACCOUNT_CONFIRMATION;
        final var aSubject = "Account confirmation";
        final var aExpirestAt = InstantUtils.now().plus(10, ChronoUnit.MINUTES);
        final var expectedErrorMessage = "Account with id 84045cfc-705f-4418-bf4b-155d5d11f69f was not found";

        final var aCommand = CreateAccountMailCommand.with(
                aAccount,
                aToken,
                aType,
                aSubject,
                aExpirestAt
        );

        // when
        Mockito.when(accountGateway.findById(Mockito.any()))
                .thenReturn(Optional.empty());

        final var aNotFound = Assertions.assertThrows(NotFoundException.class,
                () -> useCase.execute(aCommand));

        // then
        Assertions.assertEquals(expectedErrorMessage, aNotFound.getMessage());

        Mockito.verify(accountGateway, Mockito.times(1))
                .findById(Mockito.any());
        Mockito.verify(accountMailGateway, Mockito.times(0))
                .findAllByAccountId(Mockito.any());
        Mockito.verify(accountMailGateway, Mockito.never())
                .create(Mockito.any());
    }

    @Test
    public void givenAnInvalidAccountMailType_whenCallCreateAccountMail_thenShouldReturnAnError() {
        // given
        final var aToken = RandomStringUtils.generateValue(36);
        final var aAccount = Account.newAccount(
                "Fulano",
                "Silveira",
                "teste@teste.com",
                "1234567Ab"
        );
        final AccountMailType aType = null;
        final var aSubject = "Account confirmation";
        final var aExpirestAt = InstantUtils.now().plus(10, ChronoUnit.MINUTES);
        final var expectedErrorMessage = "'type' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = CreateAccountMailCommand.with(
                aAccount.getId().getValue(),
                aToken,
                aType,
                aSubject,
                aExpirestAt
        );

        // when
        Mockito.when(accountGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(aAccount));

        final var aNotification = useCase.execute(aCommand).getLeft();

        // then
        Assertions.assertEquals(expectedErrorCount, aNotification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, aNotification.getErrors().get(0).message());

        Mockito.verify(accountGateway, Mockito.times(1))
                .findById(Mockito.any());
        Mockito.verify(accountMailGateway, Mockito.times(1))
                .findAllByAccountId(Mockito.any());
        Mockito.verify(accountMailGateway, Mockito.never())
                .create(Mockito.any());
    }

    @Test
    public void givenAnInvalidExpiresAt_whenCallCreateAccountMail_thenShouldReturnAnError() {
        // given
        final var aToken = RandomStringUtils.generateValue(36);
        final var aAccount = Account.newAccount(
                "Fulano",
                "Silveira",
                "teste@teste.com",
                "1234567Ab"
        );
        final var aType = AccountMailType.ACCOUNT_CONFIRMATION;
        final var aSubject = "Account confirmation";
        final Instant aExpirestAt = null;
        final var expectedErrorMessage = "'expiresAt' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = CreateAccountMailCommand.with(
                aAccount.getId().getValue(),
                aToken,
                aType,
                aSubject,
                aExpirestAt
        );

        // when
        Mockito.when(accountGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(aAccount));

        final var aNotification = useCase.execute(aCommand).getLeft();

        // then
        Assertions.assertEquals(expectedErrorCount, aNotification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, aNotification.getErrors().get(0).message());

        Mockito.verify(accountGateway, Mockito.times(1))
                .findById(Mockito.any());
        Mockito.verify(accountMailGateway, Mockito.times(1))
                .findAllByAccountId(Mockito.any());
        Mockito.verify(accountMailGateway, Mockito.never())
                .create(Mockito.any());
    }

    @Test
    public void givenAnInvalidExpiresAtBeforeNow_whenCallCreateAccountMail_thenShouldReturnAnError() {
        // given
        final var aToken = RandomStringUtils.generateValue(36);
        final var aAccount = Account.newAccount(
                "Fulano",
                "Silveira",
                "teste@teste.com",
                "1234567Ab"
        );
        final var aType = AccountMailType.ACCOUNT_CONFIRMATION;
        final var aSubject = "Account confirmation";
        final var aExpirestAt = InstantUtils.now().minus(10, ChronoUnit.MINUTES);
        final var expectedErrorMessage = "'expiresAt' should not be before now";
        final var expectedErrorCount = 1;

        final var aCommand = CreateAccountMailCommand.with(
                aAccount.getId().getValue(),
                aToken,
                aType,
                aSubject,
                aExpirestAt
        );

        // when
        Mockito.when(accountGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(aAccount));

        final var aNotification = useCase.execute(aCommand).getLeft();

        // then
        Assertions.assertEquals(expectedErrorCount, aNotification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, aNotification.getErrors().get(0).message());

        Mockito.verify(accountGateway, Mockito.times(1))
                .findById(Mockito.any());
        Mockito.verify(accountMailGateway, Mockito.times(1))
                .findAllByAccountId(Mockito.any());
        Mockito.verify(accountMailGateway, Mockito.never())
                .create(Mockito.any());
    }

    @Test
    public void givenAValidCommandButExistingMailWithType_whenCallCreateAccountMail_thenShouldDeleteExistMailAndReturneAnAccountMail() {
        // given
        final var aToken = RandomStringUtils.generateValue(36);
        final var aAccount = Account.newAccount(
                "Fulano",
                "Silveira",
                "teste@teste.com",
                "1234567Ab"
        );
        final var aType = AccountMailType.ACCOUNT_CONFIRMATION;
        final var aSubject = "Account confirmation";
        final var aExpirestAt = InstantUtils.now().plus(10, ChronoUnit.MINUTES);

        final var aCommand = CreateAccountMailCommand.with(
                aAccount.getId().getValue(),
                aToken,
                aType,
                aSubject,
                aExpirestAt
        );

        // when
        Mockito.when(accountGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(aAccount));
        Mockito.when(accountMailGateway.findAllByAccountId(Mockito.any()))
                .thenReturn(List.of(
                        AccountMail.newAccountMail(
                        RandomStringUtils.generateValue(36),
                        AccountMailType.ACCOUNT_CONFIRMATION,
                        aAccount,
                        InstantUtils.now().plus(10, ChronoUnit.MINUTES)
                ), AccountMail.newAccountMail(
                        RandomStringUtils.generateValue(36),
                        AccountMailType.PASSWORD_RESET,
                        aAccount,
                        InstantUtils.now().plus(10, ChronoUnit.MINUTES)
                )));
        Mockito.when(accountMailGateway.create(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        final var aOutput = useCase.execute(aCommand).getRight();

        // then
        Assertions.assertNotNull(aOutput);

        Mockito.verify(accountGateway, Mockito.times(1))
                .findById(Mockito.any());
        Mockito.verify(accountMailGateway, Mockito.times(1))
                .findAllByAccountId(Mockito.any());
        Mockito.verify(accountMailGateway, Mockito.times(1))
                .create(Mockito.argThat(aAccountMail ->
                        Objects.equals(aToken, aAccountMail.getToken()) &&
                                Objects.equals(aAccount, aAccountMail.getAccount()) &&
                                Objects.equals(aType, aAccountMail.getType()) &&
                                Objects.equals(aExpirestAt, aAccountMail.getExpiresAt()) &&
                                Objects.nonNull(aAccountMail.getId()) &&
                                Objects.nonNull(aAccountMail.getCreatedAt()) &&
                                Objects.nonNull(aAccountMail.getUpdatedAt())
                ));
    }
}
