package com.kaua.ecommerce.users.infrastructure.account;

import com.kaua.ecommerce.users.IntegrationTest;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.accounts.AccountID;
import com.kaua.ecommerce.users.domain.pagination.SearchQuery;
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

import java.util.List;

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

    @Test
    void givenPrePersistedAccounts_whenCallFindAll_shouldReturnPaginated() {
        final var aRole = Role.newRole("Ceo", null, RoleTypes.EMPLOYEES, false);
        roleRepository.save(RoleJpaEntity.toEntity(aRole));

        final var aPage = 0;
        final var aPerPage = 1;
        final var aTotalItems = 2;
        final var aTotalPages = 2;

        final var aAccountOne = Account.newAccount(
                "one",
                "ones",
                "one.ones@test.com",
                "1234567Ab*",
                aRole
        );

        final var aAccountTwo = Account.newAccount(
                "two",
                "twos",
                "two.twos@test.com",
                "1234567Ab*",
                aRole
        );

        Assertions.assertEquals(0, accountRepository.count());

        accountRepository.saveAll(List.of(
                AccountJpaEntity.toEntity(aAccountOne),
                AccountJpaEntity.toEntity(aAccountTwo)
        ));

        Assertions.assertEquals(2, accountRepository.count());

        final var aQuery = new SearchQuery(0, 1, "", "firstName", "asc");
        final var aResult = accountGateway.findAll(aQuery);

        Assertions.assertEquals(aPage, aResult.currentPage());
        Assertions.assertEquals(aPerPage, aResult.perPage());
        Assertions.assertEquals(aTotalItems, aResult.totalItems());
        Assertions.assertEquals(aTotalPages, aResult.totalPages());
        Assertions.assertEquals(aPerPage, aResult.items().size());

        Assertions.assertEquals(aAccountOne.getId().getValue(), aResult.items().get(0).getId().getValue());
    }

    @Test
    void givenEmptyAccountsTable_whenCallFindAll_shouldReturnEmptyPage() {
        final var aPage = 0;
        final var aPerPage = 1;
        final var aTotalItems = 0;
        final var aTotalPages = 0;

        Assertions.assertEquals(0, accountRepository.count());

        final var aQuery = new SearchQuery(0, 1, "", "firstName", "asc");
        final var aResult = accountGateway.findAll(aQuery);

        Assertions.assertEquals(aPage, aResult.currentPage());
        Assertions.assertEquals(aPerPage, aResult.perPage());
        Assertions.assertEquals(aTotalItems, aResult.totalItems());
        Assertions.assertEquals(aTotalPages, aResult.totalPages());
        Assertions.assertEquals(0, aResult.items().size());
    }

    @Test
    void givenFollowPagination_whenCallFindAllWithPageOne_shouldReturnPaginated() {
        var aPage = 0;
        final var aPerPage = 1;
        final var aTotalItems = 3;
        final var aTotalPages = 3;

        final var aRoleUser = Role.newRole("User", null, RoleTypes.COMMON, true);

        final var aAccountOne = Account.newAccount(
                "one",
                "ones",
                "one.ones@test.com",
                "1234567Ab*",
                aRoleUser
        );

        final var aAccountTwo = Account.newAccount(
                "two",
                "twos",
                "two.twos@test.com",
                "1234567Ab*",
                aRoleUser
        );

        final var aAccountThree = Account.newAccount(
                "z",
                "z",
                "zzzz.zzzz@test.com",
                "1234567Ab*",
                aRoleUser
        );

        roleRepository.save(RoleJpaEntity.toEntity(aRoleUser));

        Assertions.assertEquals(0, accountRepository.count());

        accountRepository.saveAll(List.of(
                AccountJpaEntity.toEntity(aAccountOne),
                AccountJpaEntity.toEntity(aAccountTwo),
                AccountJpaEntity.toEntity(aAccountThree)
        ));

        Assertions.assertEquals(3, accountRepository.count());

        // Page 0
        var aQuery = new SearchQuery(0, 1, "", "firstName", "asc");
        var aResult = accountGateway.findAll(aQuery);

        Assertions.assertEquals(aPage, aResult.currentPage());
        Assertions.assertEquals(aPerPage, aResult.perPage());
        Assertions.assertEquals(aTotalItems, aResult.totalItems());
        Assertions.assertEquals(aTotalPages, aResult.totalPages());
        Assertions.assertEquals(aPerPage, aResult.items().size());

        Assertions.assertEquals(aAccountOne.getId().getValue(), aResult.items().get(0).getId().getValue());

        // Page 1
        aPage = 1;

        aQuery = new SearchQuery(1, 1, "", "firstName", "asc");
        aResult = accountGateway.findAll(aQuery);

        Assertions.assertEquals(aPage, aResult.currentPage());
        Assertions.assertEquals(aPerPage, aResult.perPage());
        Assertions.assertEquals(aTotalItems, aResult.totalItems());
        Assertions.assertEquals(aTotalPages, aResult.totalPages());
        Assertions.assertEquals(aPerPage, aResult.items().size());

        Assertions.assertEquals(aAccountTwo.getId().getValue(), aResult.items().get(0).getId().getValue());

        // Page 2
        aPage = 2;

        aQuery = new SearchQuery(2, 1, "", "firstName", "asc");
        aResult = accountGateway.findAll(aQuery);

        Assertions.assertEquals(aPage, aResult.currentPage());
        Assertions.assertEquals(aPerPage, aResult.perPage());
        Assertions.assertEquals(aTotalItems, aResult.totalItems());
        Assertions.assertEquals(aTotalPages, aResult.totalPages());
        Assertions.assertEquals(aPerPage, aResult.items().size());

        Assertions.assertEquals(aAccountThree.getId(), aResult.items().get(0).getId());
    }

    @Test
    void givenPrePersistedAccountsAndFulAsTerm_whenCallFindAll_shouldReturnPaginated() {
        final var aPage = 0;
        final var aPerPage = 1;
        final var aTotalItems = 1;
        final var aTotalPages = 1;

        final var aRoleUser = Role.newRole("User", null, RoleTypes.COMMON, true);

        final var aAccountOne = Account.newAccount(
                "fulano",
                "fulaninho",
                "one.ones@test.com",
                "1234567Ab*",
                aRoleUser
        );

        final var aAccountTwo = Account.newAccount(
                "two",
                "twos",
                "two.twos@test.com",
                "1234567Ab*",
                aRoleUser
        );

        roleRepository.save(RoleJpaEntity.toEntity(aRoleUser));

        Assertions.assertEquals(0, accountRepository.count());

        accountRepository.saveAll(List.of(
                AccountJpaEntity.toEntity(aAccountOne),
                AccountJpaEntity.toEntity(aAccountTwo)
        ));

        Assertions.assertEquals(2, accountRepository.count());

        final var aQuery = new SearchQuery(0, 1, "ful", "firstName", "asc");
        final var aResult = accountGateway.findAll(aQuery);

        Assertions.assertEquals(aPage, aResult.currentPage());
        Assertions.assertEquals(aPerPage, aResult.perPage());
        Assertions.assertEquals(aTotalItems, aResult.totalItems());
        Assertions.assertEquals(aTotalPages, aResult.totalPages());
        Assertions.assertEquals(aPerPage, aResult.items().size());

        Assertions.assertEquals(aAccountOne.getId(), aResult.items().get(0).getId());
    }
}
