package com.kaua.ecommerce.users.application.account.mail.confirm;

import com.kaua.ecommerce.users.IntegrationTest;
import com.kaua.ecommerce.users.application.gateways.AccountMailGateway;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.accounts.mail.AccountMail;
import com.kaua.ecommerce.users.domain.accounts.mail.AccountMailType;
import com.kaua.ecommerce.users.domain.exceptions.NotFoundException;
import com.kaua.ecommerce.users.domain.utils.InstantUtils;
import com.kaua.ecommerce.users.domain.utils.RandomStringUtils;
import com.kaua.ecommerce.users.infrastructure.accounts.mail.persistence.AccountMailJpaEntity;
import com.kaua.ecommerce.users.infrastructure.accounts.mail.persistence.AccountMailJpaRepository;
import com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountJpaEntity;
import com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.temporal.ChronoUnit;

@IntegrationTest
public class ConfirmAccountMailUseCaseIT {

    @Autowired
    private ConfirmAccountMailUseCase useCase;

    @Autowired
    private AccountMailJpaRepository accountMailRepository;

    @Autowired
    private AccountJpaRepository accountJpaRepository;

    @SpyBean
    private AccountMailGateway accountMailGateway;

    @Test
    public void givenAValidCommand_whenCallConfirmAccount_thenShouldReturneTrue() {
        // given
        final var aAccount = Account.newAccount(
                "Fulano",
                "Silveira",
                "teste@teste.com",
                "1234567Ab"
        );
        final var aToken = RandomStringUtils.generateValue(36);

        this.accountJpaRepository.saveAndFlush(AccountJpaEntity.toEntity(aAccount));
        this.accountMailRepository.saveAndFlush(AccountMailJpaEntity.toEntity(AccountMail.newAccountMail(
                aToken,
                AccountMailType.ACCOUNT_CONFIRMATION,
                aAccount,
                InstantUtils.now().plus(1, ChronoUnit.HOURS)
        )));

        Assertions.assertEquals(1, accountMailRepository.count());

        final var aCommand = ConfirmAccountMailCommand.with(aToken);

        // when
        final var aOutput = useCase.execute(aCommand).getRight();

        // then
        Assertions.assertTrue(aOutput);
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
        final var expectedErrorMessage = "AccountMail with id empty was not found";

        this.accountJpaRepository.save(AccountJpaEntity.toEntity(aAccount));

        Assertions.assertEquals(0, accountMailRepository.count());

        final var aCommand = ConfirmAccountMailCommand.with("empty");

        // when
        final var aOutput = Assertions.assertThrows(NotFoundException.class,
                () -> useCase.execute(aCommand).getLeft());

        // then
        Assertions.assertEquals(expectedErrorMessage, aOutput.getMessage());
        Mockito.verify(accountMailGateway, Mockito.never()).deleteById(Mockito.any());
    }

    @Test
    public void givenAValidCommandButExpiredToken_whenCallConfirmAccount_thenShouldReturneAnError() {
        // given
        final var aAccount = Account.newAccount(
                "Fulano",
                "Silveira",
                "teste@teste.com",
                "1234567Ab"
        );
        final var aToken = RandomStringUtils.generateValue(36);
        final var expectedErrorMessage = "Token expired";

        this.accountJpaRepository.saveAndFlush(AccountJpaEntity.toEntity(aAccount));
        this.accountMailRepository.saveAndFlush(AccountMailJpaEntity.toEntity(AccountMail.newAccountMail(
                aToken,
                AccountMailType.ACCOUNT_CONFIRMATION,
                aAccount,
                InstantUtils.now().minus(1, ChronoUnit.HOURS)
        )));

        Assertions.assertEquals(1, accountMailRepository.count());

        final var aCommand = ConfirmAccountMailCommand.with(aToken);

        // when
        final var aOutput = useCase.execute(aCommand).getLeft();

        // then
        Assertions.assertEquals(expectedErrorMessage, aOutput.getErrors().get(0).message());
    }
}
