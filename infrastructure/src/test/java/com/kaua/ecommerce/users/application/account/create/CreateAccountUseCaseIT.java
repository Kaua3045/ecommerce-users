package com.kaua.ecommerce.users.application.account.create;

import com.kaua.ecommerce.users.IntegrationTest;
import com.kaua.ecommerce.users.domain.accounts.AccountMailStatus;
import com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class CreateAccountUseCaseIT {

    @Autowired
    private CreateAccountUseCase useCase;

    @Autowired
    private AccountJpaRepository accountRepository;

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

        final var actualAccount = accountRepository.findById(aOutput.id().getValue()).get();

        Assertions.assertEquals(aOutput.id().getValue(), actualAccount.getId());
        Assertions.assertEquals(aFirstName, actualAccount.getFirstName());
        Assertions.assertEquals(aLastName, actualAccount.getLastName());
        Assertions.assertEquals(aEmail, actualAccount.getEmail());
        Assertions.assertEquals(aPassword, actualAccount.getPassword());
        Assertions.assertEquals(AccountMailStatus.WAITING_CONFIRMATION, actualAccount.getMailStatus());
        Assertions.assertNull(actualAccount.getAvatarUrl());
        Assertions.assertNotNull(actualAccount.getCreatedAt());
        Assertions.assertNotNull(actualAccount.getUpdatedAt());
    }
}
