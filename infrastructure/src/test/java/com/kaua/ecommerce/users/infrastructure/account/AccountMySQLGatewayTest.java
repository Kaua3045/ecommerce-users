package com.kaua.ecommerce.users.infrastructure.account;

import com.kaua.ecommerce.users.MySQLGatewayTest;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.accounts.AccountID;
import com.kaua.ecommerce.users.infrastructure.accounts.AccountMySQLGateway;
import com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountJpaEntity;
import com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@MySQLGatewayTest
public class AccountMySQLGatewayTest {

    @Autowired
    private AccountMySQLGateway accountGateway;

    @Autowired
    private AccountJpaRepository accountRepository;

    @Test
    public void givenAValidAccount_whenCallCreate_shouldReturnANewAccount() {
        final var aFirstName = "Fulano";
        final var aLastName = "Silva";
        final var aEmail = "teste@teste.com";
        final var aPassword = "1234567Ab";

        Account aAccount = Account.newAccount(aFirstName, aLastName, aEmail, aPassword);

        Assertions.assertEquals(0, accountRepository.count());

        final var actualAccount = accountGateway.create(aAccount);

        Assertions.assertEquals(1, accountRepository.count());

        Assertions.assertEquals(aAccount.getId(), actualAccount.getId());
        Assertions.assertEquals(aFirstName, actualAccount.getFirstName());
        Assertions.assertEquals(aLastName, actualAccount.getLastName());
        Assertions.assertEquals(aEmail, actualAccount.getEmail());
        Assertions.assertEquals(aPassword, actualAccount.getPassword());
        Assertions.assertNull(actualAccount.getAvatarUrl());
        Assertions.assertEquals(aAccount.getMailStatus(), actualAccount.getMailStatus());
        Assertions.assertEquals(aAccount.getCreatedAt(), actualAccount.getCreatedAt());
        Assertions.assertEquals(aAccount.getUpdatedAt(), actualAccount.getUpdatedAt());

        final var actualEntity = accountRepository.findById(actualAccount.getId().getValue()).get();

        Assertions.assertEquals(aAccount.getId().getValue(), actualEntity.getId());
        Assertions.assertEquals(aFirstName, actualEntity.getFirstName());
        Assertions.assertEquals(aLastName, actualEntity.getLastName());
        Assertions.assertEquals(aEmail, actualEntity.getEmail());
        Assertions.assertEquals(aPassword, actualEntity.getPassword());
        Assertions.assertNull(actualEntity.getAvatarUrl());
        Assertions.assertEquals(aAccount.getMailStatus(), actualEntity.getMailStatus());
        Assertions.assertEquals(aAccount.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertEquals(aAccount.getUpdatedAt(), actualEntity.getUpdatedAt());
    }

    @Test
    public void givenAValidEmailButNotExistis_whenCallExistsByEmail_shouldReturnFalse() {
        final var aEmail = "teste@teste.com";

        Assertions.assertEquals(0, accountRepository.count());
        Assertions.assertFalse(accountGateway.existsByEmail(aEmail));
    }

    @Test
    public void givenAValidExistingEmail_whenCallExistsByEmail_shouldReturnTrue() {
        final var aFirstName = "Fulano";
        final var aLastName = "Silva";
        final var aEmail = "teste@teste.com";
        final var aPassword = "1234567Ab";

        Account aAccount = Account.newAccount(aFirstName, aLastName, aEmail, aPassword);

        Assertions.assertEquals(0, accountRepository.count());
        accountRepository.save(AccountJpaEntity.toEntity(aAccount));
        Assertions.assertEquals(1, accountRepository.count());

        Assertions.assertTrue(accountGateway.existsByEmail(aEmail));
    }

    @Test
    public void givenAPrePersistedAccountAndValidAccountId_whenCallFindById_shouldReturnAccount() {
        final var aFirstName = "Fulano";
        final var aLastName = "Silva";
        final var aEmail = "teste@teste.com";
        final var aPassword = "1234567Ab";

        Account aAccount = Account.newAccount(aFirstName, aLastName, aEmail, aPassword);

        Assertions.assertEquals(0, accountRepository.count());

        accountRepository.save(AccountJpaEntity.toEntity(aAccount));

        Assertions.assertEquals(1, accountRepository.count());

        final var actualAccount = accountGateway.findById(aAccount.getId().getValue()).get();

        Assertions.assertEquals(aAccount.getId(), actualAccount.getId());
        Assertions.assertEquals(aFirstName, actualAccount.getFirstName());
        Assertions.assertEquals(aLastName, actualAccount.getLastName());
        Assertions.assertEquals(aEmail, actualAccount.getEmail());
        Assertions.assertEquals(aPassword, actualAccount.getPassword());
        Assertions.assertNull(actualAccount.getAvatarUrl());
        Assertions.assertEquals(aAccount.getMailStatus(), actualAccount.getMailStatus());
        Assertions.assertEquals(aAccount.getCreatedAt(), actualAccount.getCreatedAt());
        Assertions.assertEquals(aAccount.getUpdatedAt(), actualAccount.getUpdatedAt());
    }

    @Test
    public void givenAValidAccountIdNotStored_whenCallFindById_shouldReturnEmpty() {
        Assertions.assertEquals(0, accountRepository.count());

        final var actualAccount = accountGateway.findById(AccountID.from("empty").getValue());

        Assertions.assertTrue(actualAccount.isEmpty());
    }
}
