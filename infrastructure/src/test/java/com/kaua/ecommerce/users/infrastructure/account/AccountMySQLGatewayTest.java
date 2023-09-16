package com.kaua.ecommerce.users.infrastructure.account;

import com.kaua.ecommerce.users.IntegrationTest;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.accounts.AccountID;
import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.domain.roles.RoleTypes;
import com.kaua.ecommerce.users.infrastructure.accounts.AccountMySQLGateway;
import com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountJpaEntity;
import com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountJpaRepository;
import com.kaua.ecommerce.users.infrastructure.roles.persistence.RoleJpaEntity;
import com.kaua.ecommerce.users.infrastructure.roles.persistence.RoleJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class AccountMySQLGatewayTest {

    @Autowired
    private AccountMySQLGateway accountGateway;

    @Autowired
    private AccountJpaRepository accountRepository;

    @Autowired
    private RoleJpaRepository roleRepository;

    @Test
    void givenAValidAccount_whenCallCreate_shouldReturnANewAccount() {
        final var aFirstName = "Fulano";
        final var aLastName = "Silva";
        final var aEmail = "teste@teste.com";
        final var aPassword = "1234567Ab";
        final var aRole = Role.newRole("Ceo", null, RoleTypes.EMPLOYEES, false);

        Account aAccount = Account.newAccount(aFirstName, aLastName, aEmail, aPassword, aRole);

        roleRepository.saveAndFlush(RoleJpaEntity.toEntity(aRole));

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
        Assertions.assertEquals(aAccount.getRole(), actualAccount.getRole());
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
        Assertions.assertEquals(aAccount.getRole().getId().getValue(), actualEntity.getRoleJpaEntity().getId());
        Assertions.assertEquals(aAccount.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertEquals(aAccount.getUpdatedAt(), actualEntity.getUpdatedAt());
    }

    @Test
    void givenAValidEmailButNotExistis_whenCallExistsByEmail_shouldReturnFalse() {
        final var aEmail = "teste@teste.com";

        Assertions.assertEquals(0, accountRepository.count());
        Assertions.assertFalse(accountGateway.existsByEmail(aEmail));
    }

    @Test
    void givenAValidExistingEmail_whenCallExistsByEmail_shouldReturnTrue() {
        final var aFirstName = "Fulano";
        final var aLastName = "Silva";
        final var aEmail = "teste@teste.com";
        final var aPassword = "1234567Ab";
        final var aRole = Role.newRole("Ceo", null, RoleTypes.EMPLOYEES, false);

        Account aAccount = Account.newAccount(aFirstName, aLastName, aEmail, aPassword, aRole);

        roleRepository.saveAndFlush(RoleJpaEntity.toEntity(aRole));

        Assertions.assertEquals(0, accountRepository.count());
        accountRepository.save(AccountJpaEntity.toEntity(aAccount));
        Assertions.assertEquals(1, accountRepository.count());

        Assertions.assertTrue(accountGateway.existsByEmail(aEmail));
    }

    @Test
    void givenAPrePersistedAccountAndValidAccountId_whenCallFindById_shouldReturnAccount() {
        final var aFirstName = "Fulano";
        final var aLastName = "Silva";
        final var aEmail = "teste@teste.com";
        final var aPassword = "1234567Ab";
        final var aRole = Role.newRole("Ceo", null, RoleTypes.EMPLOYEES, false);

        Account aAccount = Account.newAccount(aFirstName, aLastName, aEmail, aPassword, aRole);

        roleRepository.saveAndFlush(RoleJpaEntity.toEntity(aRole));

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
        Assertions.assertEquals(aAccount.getRole(), actualAccount.getRole());
        Assertions.assertEquals(aAccount.getCreatedAt(), actualAccount.getCreatedAt());
        Assertions.assertEquals(aAccount.getUpdatedAt(), actualAccount.getUpdatedAt());
    }

    @Test
    void givenAValidAccountIdButNotStored_whenCallFindById_shouldReturnEmpty() {
        Assertions.assertEquals(0, accountRepository.count());

        final var actualAccount = accountGateway.findById(AccountID.from("empty").getValue());

        Assertions.assertTrue(actualAccount.isEmpty());
    }

    @Test
    void givenAValidAccount_whenCallUpdate_shouldReturnAUpdatedAccount() {
        final var aFirstName = "Fulano";
        final var aLastName = "Silva";
        final var aEmail = "teste@teste.com";
        final var aPassword = "1234567Ab";
        final var aAvatarUrl = "http://local/teste.png";
        final var aRole = Role.newRole("Ceo", null, RoleTypes.EMPLOYEES, false);

        Account aAccount = Account.newAccount(aFirstName, aLastName, aEmail, "1234567Ab*", aRole);

        roleRepository.saveAndFlush(RoleJpaEntity.toEntity(aRole));

        Assertions.assertEquals(0, accountRepository.count());

        accountRepository.save(AccountJpaEntity.toEntity(aAccount));

        Assertions.assertEquals(1, accountRepository.count());

        final var aAccountWithPasswordUpdated = aAccount.changePassword(aPassword);
        final var aAccountUpdatedWithPasswordAndAvatarUrl = aAccountWithPasswordUpdated.changeAvatarUrl(aAvatarUrl);

        final var actualAccount = this.accountGateway.update(aAccountUpdatedWithPasswordAndAvatarUrl);

        Assertions.assertEquals(aAccount.getId(), actualAccount.getId());
        Assertions.assertEquals(aFirstName, actualAccount.getFirstName());
        Assertions.assertEquals(aLastName, actualAccount.getLastName());
        Assertions.assertEquals(aEmail, actualAccount.getEmail());
        Assertions.assertEquals(aPassword, actualAccount.getPassword());
        Assertions.assertEquals(aAvatarUrl, actualAccount.getAvatarUrl());
        Assertions.assertEquals(aAccount.getMailStatus(), actualAccount.getMailStatus());
        Assertions.assertEquals(aAccount.getRole(), actualAccount.getRole());
        Assertions.assertEquals(aAccount.getCreatedAt(), actualAccount.getCreatedAt());
        Assertions.assertEquals(aAccount.getUpdatedAt(), actualAccount.getUpdatedAt());

        final var actualEntity = accountRepository.findById(actualAccount.getId().getValue()).get();

        Assertions.assertEquals(aAccount.getId().getValue(), actualEntity.getId());
        Assertions.assertEquals(aFirstName, actualEntity.getFirstName());
        Assertions.assertEquals(aLastName, actualEntity.getLastName());
        Assertions.assertEquals(aEmail, actualEntity.getEmail());
        Assertions.assertEquals(aPassword, actualEntity.getPassword());
        Assertions.assertEquals(aAvatarUrl, actualEntity.getAvatarUrl());
        Assertions.assertEquals(aAccount.getMailStatus(), actualEntity.getMailStatus());
        Assertions.assertEquals(aAccount.getRole().getId().getValue(), actualEntity.getRoleJpaEntity().getId());
        Assertions.assertEquals(aAccount.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertEquals(aAccount.getUpdatedAt(), actualEntity.getUpdatedAt());
    }

    @Test
    void givenAPrePersistedAccountAndValidEmail_whenCallFindByEmail_shouldReturnAccount() {
        final var aFirstName = "Fulano";
        final var aLastName = "Silva";
        final var aEmail = "teste@teste.com";
        final var aPassword = "1234567Ab";
        final var aRole = Role.newRole("Ceo", null, RoleTypes.EMPLOYEES, false);

        Account aAccount = Account.newAccount(aFirstName, aLastName, aEmail, aPassword, aRole);

        roleRepository.saveAndFlush(RoleJpaEntity.toEntity(aRole));

        Assertions.assertEquals(0, accountRepository.count());

        accountRepository.save(AccountJpaEntity.toEntity(aAccount));

        Assertions.assertEquals(1, accountRepository.count());

        final var actualAccount = accountGateway.findByEmail(aEmail).get();

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
    void givenAnInvalidEmailNotStored_whenCallFindByEmail_shouldReturnEmpty() {
        Assertions.assertEquals(0, accountRepository.count());

        final var actualAccount = accountGateway.findByEmail("empty");

        Assertions.assertTrue(actualAccount.isEmpty());
    }

    @Test
    void givenAPrePersistedAccount_whenCallDeleteById_shouldBeOk() {
        final var aRole = Role.newRole("Ceo", null, RoleTypes.EMPLOYEES, false);
        final var aAccount = Account.newAccount(
                "teste",
                "testes",
                "testes@teste.com",
                "1234567Ab",
                aRole
        );
        final var aId = aAccount.getId().getValue();

        roleRepository.saveAndFlush(RoleJpaEntity.toEntity(aRole));
        accountRepository.saveAndFlush(AccountJpaEntity.toEntity(aAccount));

        Assertions.assertEquals(1, accountRepository.count());

        Assertions.assertDoesNotThrow(() -> accountGateway.deleteById(aId));

        Assertions.assertEquals(0, accountRepository.count());
    }

    @Test
    void givenAnNotPrePersistedAccount_whenCallDeleteById_shouldBeOk() {
        final var aId = "123";

        Assertions.assertEquals(0, accountRepository.count());

        Assertions.assertDoesNotThrow(() -> accountGateway.deleteById(aId));

        Assertions.assertEquals(0, accountRepository.count());
    }
}
