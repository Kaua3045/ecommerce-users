package com.kaua.ecommerce.users.application.account.mail.create;

import com.kaua.ecommerce.users.IntegrationTest;
import com.kaua.ecommerce.users.application.gateways.AccountMailGateway;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.accounts.mail.AccountMailType;
import com.kaua.ecommerce.users.domain.utils.InstantUtils;
import com.kaua.ecommerce.users.domain.utils.RandomStringUtils;
import com.kaua.ecommerce.users.infrastructure.accounts.mail.persistence.AccountMailJpaRepository;
import com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountJpaEntity;
import com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@IntegrationTest
public class CreateAccountMailUseCaseIT {

    @Autowired
    private CreateAccountMailUseCase useCase;

    @Autowired
    private AccountMailJpaRepository accountMailRepository;

    @Autowired
    private AccountJpaRepository accountJpaRepository;

    @SpyBean
    private AccountMailGateway accountMailGateway;

    @Test
    public void givenAValidCommand_whenCallCreateAccountMail_thenShouldReturneAnAccountMail() {
        // given
        final var aAccount = Account.newAccount(
                "Fulano",
                "Silveira",
                "teste@teste.com",
                "1234567Ab"
        );
        final var aToken = RandomStringUtils.generateValue(36);
        final var aAccountId = aAccount.getId().getValue();
        final var aType = AccountMailType.ACCOUNT_CONFIRMATION;
        final var aSubject = "Account Confirmation";
        final var aExpiresAt = InstantUtils.now().plus(1, ChronoUnit.HOURS);

        this.accountJpaRepository.save(AccountJpaEntity.toEntity(aAccount));

        Assertions.assertEquals(0, accountMailRepository.count());

        final var aCommand = CreateAccountMailCommand.with(
                aAccountId,
                aToken,
                aType,
                aSubject,
                aExpiresAt
        );

        // when
        final var aOutput = useCase.execute(aCommand).getRight();

        // then
        Assertions.assertNotNull(aOutput);
        Assertions.assertNotNull(aOutput.id());

        Assertions.assertEquals(1, accountMailRepository.count());

        final var actualAccountMail = accountMailRepository.findById(aOutput.id()).get();

        Assertions.assertEquals(aOutput.id(), actualAccountMail.getId());
        Assertions.assertEquals(aToken, actualAccountMail.getToken());
        Assertions.assertEquals(aAccountId, actualAccountMail.getAccount().getId());
        Assertions.assertEquals(aType, actualAccountMail.getType());
        Assertions.assertEquals(aExpiresAt, actualAccountMail.getExpiresAt());
        Assertions.assertNotNull(actualAccountMail.getCreatedAt());
        Assertions.assertNotNull(actualAccountMail.getUpdatedAt());
    }

    @Test
    public void givenAnInvalidToken_whenCallCreateAccountMail_thenShouldReturneAnError() {
        // given
        final var aAccount = Account.newAccount(
                "Fulano",
                "Silveira",
                "teste@teste.com",
                "1234567Ab"
        );
        final var aToken = "";
        final var aAccountId = aAccount.getId().getValue();
        final var aType = AccountMailType.ACCOUNT_CONFIRMATION;
        final var aSubject = "Account Confirmation";
        final var aExpiresAt = InstantUtils.now().plus(1, ChronoUnit.HOURS);
        final var expectedErrorMessage = "'token' should not be null or blank";

        this.accountJpaRepository.save(AccountJpaEntity.toEntity(aAccount));

        Assertions.assertEquals(0, accountMailRepository.count());

        final var aCommand = CreateAccountMailCommand.with(
                aAccountId,
                aToken,
                aType,
                aSubject,
                aExpiresAt
        );

        // when
        final var aOutput = useCase.execute(aCommand).getLeft();

        // then
        Assertions.assertEquals(expectedErrorMessage, aOutput.getErrors().get(0).message());
        Mockito.verify(accountMailGateway, Mockito.never()).create(Mockito.any());
    }

    @Test
    public void givenAnInvalidTokenLengthMoreThan36_whenCallCreateAccountMail_thenShouldReturneAnError() {
        // given
        final var aAccount = Account.newAccount(
                "Fulano",
                "Silveira",
                "teste@teste.com",
                "1234567Ab"
        );
        final var aToken = RandomStringUtils.generateValue(37);
        final var aAccountId = aAccount.getId().getValue();
        final var aType = AccountMailType.ACCOUNT_CONFIRMATION;
        final var aSubject = "Account Confirmation";
        final var aExpiresAt = InstantUtils.now().plus(1, ChronoUnit.HOURS);
        final var expectedErrorMessage = "'token' should not be greater than 36";

        this.accountJpaRepository.save(AccountJpaEntity.toEntity(aAccount));

        Assertions.assertEquals(0, accountMailRepository.count());

        final var aCommand = CreateAccountMailCommand.with(
                aAccountId,
                aToken,
                aType,
                aSubject,
                aExpiresAt
        );

        // when
        final var aOutput = useCase.execute(aCommand).getLeft();

        // then
        Assertions.assertEquals(expectedErrorMessage, aOutput.getErrors().get(0).message());
        Mockito.verify(accountMailGateway, Mockito.never()).create(Mockito.any());
    }

    @Test
    public void givenAnInvalidType_whenCallCreateAccountMail_thenShouldReturneAnError() {
        // given
        final var aAccount = Account.newAccount(
                "Fulano",
                "Silveira",
                "teste@teste.com",
                "1234567Ab"
        );
        final var aToken = RandomStringUtils.generateValue(36);
        final var aAccountId = aAccount.getId().getValue();
        final AccountMailType aType = null;
        final var aSubject = "Account Confirmation";
        final var aExpiresAt = InstantUtils.now().plus(1, ChronoUnit.HOURS);
        final var expectedErrorMessage = "'type' should not be null";

        this.accountJpaRepository.save(AccountJpaEntity.toEntity(aAccount));

        Assertions.assertEquals(0, accountMailRepository.count());

        final var aCommand = CreateAccountMailCommand.with(
                aAccountId,
                aToken,
                aType,
                aSubject,
                aExpiresAt
        );

        // when
        final var aOutput = useCase.execute(aCommand).getLeft();

        // then
        Assertions.assertEquals(expectedErrorMessage, aOutput.getErrors().get(0).message());
        Mockito.verify(accountMailGateway, Mockito.never()).create(Mockito.any());
    }

    @Test
    public void givenAnInvalidExpiresAt_whenCallCreateAccountMail_thenShouldReturneAnError() {
        // given
        final var aAccount = Account.newAccount(
                "Fulano",
                "Silveira",
                "teste@teste.com",
                "1234567Ab"
        );
        final var aToken = RandomStringUtils.generateValue(36);
        final var aAccountId = aAccount.getId().getValue();
        final var aType = AccountMailType.ACCOUNT_CONFIRMATION;
        final var aSubject = "Account Confirmation";
        final Instant aExpiresAt = null;
        final var expectedErrorMessage = "'expiresAt' should not be null";

        this.accountJpaRepository.save(AccountJpaEntity.toEntity(aAccount));

        Assertions.assertEquals(0, accountMailRepository.count());

        final var aCommand = CreateAccountMailCommand.with(
                aAccountId,
                aToken,
                aType,
                aSubject,
                aExpiresAt
        );

        // when
        final var aOutput = useCase.execute(aCommand).getLeft();

        // then
        Assertions.assertEquals(expectedErrorMessage, aOutput.getErrors().get(0).message());
        Mockito.verify(accountMailGateway, Mockito.never()).create(Mockito.any());
    }

    @Test
    public void givenAnInvalidExpiresAtBeforeNow_whenCallCreateAccountMail_thenShouldReturneAnError() {
        // given
        final var aAccount = Account.newAccount(
                "Fulano",
                "Silveira",
                "teste@teste.com",
                "1234567Ab"
        );
        final var aToken = RandomStringUtils.generateValue(36);
        final var aAccountId = aAccount.getId().getValue();
        final var aType = AccountMailType.ACCOUNT_CONFIRMATION;
        final var aSubject = "Account Confirmation";
        final var aExpiresAt = InstantUtils.now().minus(1, ChronoUnit.HOURS);
        final var expectedErrorMessage = "'expiresAt' should not be before now";

        this.accountJpaRepository.save(AccountJpaEntity.toEntity(aAccount));

        Assertions.assertEquals(0, accountMailRepository.count());

        final var aCommand = CreateAccountMailCommand.with(
                aAccountId,
                aToken,
                aType,
                aSubject,
                aExpiresAt
        );

        // when
        final var aOutput = useCase.execute(aCommand).getLeft();

        // then
        Assertions.assertEquals(expectedErrorMessage, aOutput.getErrors().get(0).message());
        Mockito.verify(accountMailGateway, Mockito.never()).create(Mockito.any());
    }
}
