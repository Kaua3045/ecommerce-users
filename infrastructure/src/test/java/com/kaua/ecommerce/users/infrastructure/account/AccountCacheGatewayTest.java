package com.kaua.ecommerce.users.infrastructure.account;

import com.kaua.ecommerce.users.CacheGatewayTest;
import com.kaua.ecommerce.users.config.CacheTestConfiguration;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.accounts.AccountID;
import com.kaua.ecommerce.users.domain.permissions.Permission;
import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.domain.roles.RolePermission;
import com.kaua.ecommerce.users.domain.roles.RoleTypes;
import com.kaua.ecommerce.users.infrastructure.accounts.AccountCacheGateway;
import com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountCacheEntity;
import com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountCacheRepository;
import com.kaua.ecommerce.users.infrastructure.permissions.persistence.PermissionJpaEntity;
import com.kaua.ecommerce.users.infrastructure.permissions.persistence.PermissionJpaRepository;
import com.kaua.ecommerce.users.infrastructure.roles.persistence.RoleJpaEntity;
import com.kaua.ecommerce.users.infrastructure.roles.persistence.RoleJpaRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Set;

@Testcontainers
@CacheGatewayTest
public class AccountCacheGatewayTest extends CacheTestConfiguration {

    @Container
    private static final GenericContainer<?> redis = new GenericContainer<>(
            DockerImageName.parse("redis:alpine"))
            .withExposedPorts(6379);

    @DynamicPropertySource
    public static void redisProperties(final DynamicPropertyRegistry propertySources) {
        propertySources.add("redis.host", redis::getHost);
        propertySources.add("redis.port", redis::getFirstMappedPort);
    }

//    @BeforeEach
//    void setup() {
//        init();
//    }
//
//    @AfterEach
//    void cleanUp() {
//        stop();
//    }

    @Autowired
    private AccountCacheGateway accountCacheGateway;

    @Autowired
    private AccountCacheRepository accountRepository;

    @Autowired
    private RoleJpaRepository roleRepository;

    @Autowired
    private PermissionJpaRepository permissionRepository;

    @Test
    void givenAValidAccount_whenCallSave_shouldReturnAccountSavedInCache() {
        final var aFirstName = "Fulano";
        final var aLastName = "Silva";
        final var aEmail = "teste@teste.com";
        final var aPassword = "1234567Ab";
        final var aRole = Role.newRole("Ceo", null, RoleTypes.EMPLOYEES, false);
        final var aPermission = Permission.newPermission("create-user", null);
        final var aRolePermission = RolePermission.newRolePermission(aPermission.getId(), aPermission.getName());

        aRole.addPermissions(Set.of(aRolePermission));

        Account aAccount = Account.newAccount(aFirstName, aLastName, aEmail, aPassword, aRole);

        permissionRepository.save(PermissionJpaEntity.toEntity(aPermission));

        roleRepository.save(RoleJpaEntity.toEntity(aRole));

        Assertions.assertEquals(0, accountRepository.count());

        accountCacheGateway.save(aAccount);

        final var actualAccount = accountCacheGateway.get(aAccount.getId().getValue()).get();

        Assertions.assertEquals(1, accountRepository.count());

        Assertions.assertEquals(aAccount.getId(), actualAccount.getId());
        Assertions.assertEquals(aFirstName, actualAccount.getFirstName());
        Assertions.assertEquals(aLastName, actualAccount.getLastName());
        Assertions.assertEquals(aEmail, actualAccount.getEmail());
        Assertions.assertNull(actualAccount.getAvatarUrl());
        Assertions.assertEquals(aAccount.getMailStatus(), actualAccount.getMailStatus());
        Assertions.assertEquals(aAccount.getRole(), actualAccount.getRole());
        Assertions.assertEquals(aRolePermission.getPermissionName(), actualAccount.getRole().getPermissions().stream().findFirst().get().getPermissionName());
        Assertions.assertEquals(aAccount.getCreatedAt(), actualAccount.getCreatedAt());
        Assertions.assertEquals(aAccount.getUpdatedAt(), actualAccount.getUpdatedAt());

        final var actualEntity = accountRepository.findById(actualAccount.getId().getValue()).get();

        Assertions.assertEquals(aAccount.getId().getValue(), actualEntity.getId());
        Assertions.assertEquals(aFirstName, actualEntity.getFirstName());
        Assertions.assertEquals(aLastName, actualEntity.getLastName());
        Assertions.assertEquals(aEmail, actualEntity.getEmail());
        Assertions.assertNull(actualEntity.getAvatarUrl());
        Assertions.assertEquals(aAccount.getMailStatus(), actualEntity.getMailStatus());
        Assertions.assertEquals(aAccount.getRole().getId().getValue(), actualEntity.getRole().getId());
        Assertions.assertEquals(aAccount.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertEquals(aAccount.getUpdatedAt(), actualEntity.getUpdatedAt());
    }

    @Test
    void givenAPrePersistedAccountAndValidAccountId_whenCallGetInCache_shouldReturnAccount() {
        final var aFirstName = "Fulano";
        final var aLastName = "Silva";
        final var aEmail = "teste@teste.com";
        final var aPassword = "1234567Ab";
        final var aRole = Role.newRole("Ceo", null, RoleTypes.EMPLOYEES, false);

        Account aAccount = Account.newAccount(aFirstName, aLastName, aEmail, aPassword, aRole);

        roleRepository.save(RoleJpaEntity.toEntity(aRole));

        Assertions.assertEquals(0, accountRepository.count());

        accountRepository.save(AccountCacheEntity.toEntity(aAccount));

        Assertions.assertEquals(1, accountRepository.count());

        final var actualAccount = accountCacheGateway.get(aAccount.getId().getValue()).get();

        Assertions.assertEquals(aAccount.getId(), actualAccount.getId());
        Assertions.assertEquals(aFirstName, actualAccount.getFirstName());
        Assertions.assertEquals(aLastName, actualAccount.getLastName());
        Assertions.assertEquals(aEmail, actualAccount.getEmail());
        Assertions.assertNull(actualAccount.getAvatarUrl());
        Assertions.assertEquals(aAccount.getMailStatus(), actualAccount.getMailStatus());
        Assertions.assertEquals(aAccount.getRole(), actualAccount.getRole());
        Assertions.assertEquals(aAccount.getCreatedAt(), actualAccount.getCreatedAt());
        Assertions.assertEquals(aAccount.getUpdatedAt(), actualAccount.getUpdatedAt());
    }

    @Test
    void givenAValidAccountIdButNotStored_whenCallGetInCache_shouldReturnEmpty() {
        Assertions.assertEquals(0, accountRepository.count());

        final var actualAccount = accountCacheGateway.get(AccountID.from("empty").getValue());

        Assertions.assertTrue(actualAccount.isEmpty());
    }

    @Test
    void givenAValidAccount_whenCallSave_shouldReturnAUpdatedAccount() {
        final var aFirstName = "Fulano";
        final var aLastName = "Silva";
        final var aEmail = "teste@teste.com";
        final var aPassword = "1234567Ab";
        final var aAvatarUrl = "http://local/teste.png";
        final var aRole = Role.newRole("Ceo", null, RoleTypes.EMPLOYEES, false);

        Account aAccount = Account.newAccount(aFirstName, aLastName, aEmail, "1234567Ab*", aRole);

        roleRepository.save(RoleJpaEntity.toEntity(aRole));

        Assertions.assertEquals(0, accountRepository.count());

        accountRepository.save(AccountCacheEntity.toEntity(aAccount));

        Assertions.assertEquals(1, accountRepository.count());

        final var aAccountWithPasswordUpdated = aAccount.changePassword(aPassword);
        final var aAccountUpdatedWithPasswordAndAvatarUrl = aAccountWithPasswordUpdated.changeAvatarUrl(aAvatarUrl);

        this.accountCacheGateway.save(aAccountUpdatedWithPasswordAndAvatarUrl);

        final var actualAccount = this.accountCacheGateway.get(aAccount.getId().getValue()).get();

        Assertions.assertEquals(aAccount.getId(), actualAccount.getId());
        Assertions.assertEquals(aFirstName, actualAccount.getFirstName());
        Assertions.assertEquals(aLastName, actualAccount.getLastName());
        Assertions.assertEquals(aEmail, actualAccount.getEmail());
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
        Assertions.assertEquals(aAvatarUrl, actualEntity.getAvatarUrl());
        Assertions.assertEquals(aAccount.getMailStatus(), actualEntity.getMailStatus());
        Assertions.assertEquals(aAccount.getRole().getId().getValue(), actualEntity.getRole().getId());
        Assertions.assertEquals(aAccount.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertEquals(aAccount.getUpdatedAt(), actualEntity.getUpdatedAt());
    }

    @Test
    void givenAPrePersistedAccount_whenCallDeleteInCache_shouldBeOk() {
        final var aRole = Role.newRole("Ceo", null, RoleTypes.EMPLOYEES, false);
        final var aAccount = Account.newAccount(
                "teste",
                "testes",
                "testes@teste.com",
                "1234567Ab",
                aRole
        );
        final var aId = aAccount.getId().getValue();

        roleRepository.save(RoleJpaEntity.toEntity(aRole));
        accountRepository.save(AccountCacheEntity.toEntity(aAccount));

        Assertions.assertEquals(1, accountRepository.count());

        Assertions.assertDoesNotThrow(() -> accountCacheGateway.delete(aId));

        Assertions.assertEquals(0, accountRepository.count());
    }

    @Test
    void givenAnNotPrePersistedAccount_whenCallDeleteInCache_shouldBeOk() {
        final var aId = "123";

        Assertions.assertEquals(0, accountRepository.count());

        Assertions.assertDoesNotThrow(() -> accountCacheGateway.delete(aId));

        Assertions.assertEquals(0, accountRepository.count());
    }
}
