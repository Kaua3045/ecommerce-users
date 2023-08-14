package com.kaua.ecommerce.users.application.account.create;

import com.kaua.ecommerce.users.IntegrationTest;
import com.kaua.ecommerce.users.application.gateways.AccountGateway;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.accounts.AccountMailStatus;
import com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountJpaEntity;
import com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

@IntegrationTest
public class CreateAccountUseCaseIT {

    @Autowired
    private CreateAccountUseCase useCase;

    @Autowired
    private AccountJpaRepository accountRepository;

    @SpyBean
    private AccountGateway accountGateway;

    @Test
    public void givenAValidCommand_whenCallCreateAccount_thenShouldReturneAnAccountId() {
        // given
        final var aFirstName = "Fulano";
        final var aLastName = "Silveira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "1234567Ab";

        Assertions.assertEquals(0, accountRepository.count());

        final var aCommand = CreateAccountCommand.with(aFirstName, aLastName, aEmail, aPassword);

        // when
        final var aOutput = useCase.execute(aCommand).getRight();

        // then
        Assertions.assertNotNull(aOutput);
        Assertions.assertNotNull(aOutput.id());

        Assertions.assertEquals(1, accountRepository.count());

        final var actualAccount = accountRepository.findById(aOutput.id()).get();

        Assertions.assertEquals(aOutput.id(), actualAccount.getId());
        Assertions.assertEquals(aFirstName, actualAccount.getFirstName());
        Assertions.assertEquals(aLastName, actualAccount.getLastName());
        Assertions.assertEquals(aEmail, actualAccount.getEmail());
        Assertions.assertEquals(aPassword, actualAccount.getPassword());
        Assertions.assertEquals(AccountMailStatus.WAITING_CONFIRMATION, actualAccount.getMailStatus());
        Assertions.assertNull(actualAccount.getAvatarUrl());
        Assertions.assertNotNull(actualAccount.getCreatedAt());
        Assertions.assertNotNull(actualAccount.getUpdatedAt());
    }

    @Test
    public void givenAnInvalidFirstName_whenCallCreateAccount_thenShouldReturnAnError() {
        // given
        final var aFirstName = "";
        final var aLastName = "Silveira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "1234567Ab";
        final var expectedErrorMessage = "'firstName' should not be null or blank";
        final var expectedErrorCount = 1;

        Assertions.assertEquals(0, accountRepository.count());

        final var aCommand = CreateAccountCommand.with(aFirstName, aLastName, aEmail, aPassword);

        // when
        final var aNotification = useCase.execute(aCommand).getLeft();

        // then
        Assertions.assertEquals(expectedErrorCount, aNotification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, aNotification.getErrors().get(0).message());
        Assertions.assertEquals(0, accountRepository.count());

        Mockito.verify(accountGateway, Mockito.never()).create(Mockito.any());
    }

    @Test
    public void givenAnInvalidLastName_whenCallCreateAccount_thenShouldReturnAnError() {
        // given
        final var aFirstName = "Fulano";
        final String aLastName = null;
        final var aEmail = "teste@teste.com";
        final var aPassword = "1234567Ab";
        final var expectedErrorMessage = "'lastName' should not be null or blank";
        final var expectedErrorCount = 1;

        Assertions.assertEquals(0, accountRepository.count());

        final var aCommand = CreateAccountCommand.with(aFirstName, aLastName, aEmail, aPassword);

        // when
        final var aNotification = useCase.execute(aCommand).getLeft();

        // then
        Assertions.assertEquals(expectedErrorCount, aNotification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, aNotification.getErrors().get(0).message());
        Assertions.assertEquals(0, accountRepository.count());

        Mockito.verify(accountGateway, Mockito.never()).create(Mockito.any());
    }

    @Test
    public void givenAnInvalidEmail_whenCallCreateAccount_thenShouldReturnAnError() {
        // given
        final var aFirstName = "Fulano";
        final var aLastName = "Silveira";
        final var aEmail = "";
        final var aPassword = "1234567Ab";
        final var expectedErrorMessage = "'email' should not be null or blank";
        final var expectedErrorCount = 1;

        Assertions.assertEquals(0, accountRepository.count());

        final var aCommand = CreateAccountCommand.with(aFirstName, aLastName, aEmail, aPassword);

        // when
        final var aNotification = useCase.execute(aCommand).getLeft();

        // then
        Assertions.assertEquals(expectedErrorCount, aNotification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, aNotification.getErrors().get(0).message());
        Assertions.assertEquals(0, accountRepository.count());

        Mockito.verify(accountGateway, Mockito.never()).create(Mockito.any());
    }

    @Test
    public void givenAnInvalidPassword_whenCallCreateAccount_thenShouldReturnAnError() {
        // given
        final var aFirstName = "Fulano";
        final var aLastName = "Silveira";
        final var aEmail = "teste@teste.com";
        final String aPassword = null;
        final var expectedErrorMessage = "'password' should not be null or blank";
        final var expectedErrorCount = 1;

        Assertions.assertEquals(0, accountRepository.count());

        final var aCommand = CreateAccountCommand.with(aFirstName, aLastName, aEmail, aPassword);

        // when
        final var aNotification = useCase.execute(aCommand).getLeft();

        // then
        Assertions.assertEquals(expectedErrorCount, aNotification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, aNotification.getErrors().get(0).message());
        Assertions.assertEquals(0, accountRepository.count());

        Mockito.verify(accountGateway, Mockito.never()).create(Mockito.any());
    }

    @Test
    public void givenAnExistingEmail_whenCallCreateAccount_thenShouldReturnAnError() {
        // given
        final var aFirstName = "Fulano";
        final var aLastName = "Silveira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "1234567Ab";
        final var expectedErrorMessage = "'email' already exists";
        final var expectedErrorCount = 1;

        Assertions.assertEquals(0, accountRepository.count());
        accountRepository.save(
                AccountJpaEntity.toEntity(Account.newAccount(
                        "Test",
                        "Teste",
                        "teste@teste.com",
                        "1234567Ab")));
        Assertions.assertEquals(1, accountRepository.count());

        final var aCommand = CreateAccountCommand.with(aFirstName, aLastName, aEmail, aPassword);

        // when
        final var aNotification = useCase.execute(aCommand).getLeft();

        // then
        Assertions.assertEquals(expectedErrorCount, aNotification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, aNotification.getErrors().get(0).message());

        Assertions.assertEquals(1, accountRepository.count());

        Mockito.verify(accountGateway, Mockito.times(1))
                .existsByEmail(aEmail);
        Mockito.verify(accountGateway, Mockito.never())
                .create(Mockito.any());
    }
}
