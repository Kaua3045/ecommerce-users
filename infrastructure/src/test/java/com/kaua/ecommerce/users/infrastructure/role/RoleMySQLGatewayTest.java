package com.kaua.ecommerce.users.infrastructure.role;

import com.kaua.ecommerce.users.CacheGatewayTest;
import com.kaua.ecommerce.users.config.CacheTestConfiguration;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.pagination.SearchQuery;
import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.domain.roles.RoleID;
import com.kaua.ecommerce.users.domain.roles.RoleTypes;
import com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountCacheEntity;
import com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountCacheRepository;
import com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountJpaEntity;
import com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountJpaRepository;
import com.kaua.ecommerce.users.infrastructure.roles.RoleMySQLGateway;
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

import java.util.List;
import java.util.Set;

@Testcontainers
@CacheGatewayTest
public class RoleMySQLGatewayTest extends CacheTestConfiguration {

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
    private RoleMySQLGateway roleGateway;

    @Autowired
    private RoleJpaRepository roleRepository;

    @Autowired
    private AccountJpaRepository accountJpaRepository;

    @Autowired
    private AccountCacheRepository accountCacheRepository;

    @Test
    void givenAValidRoleWithDescription_whenCallCreate_shouldReturnANewRole() {
        final var aName = "ceo";
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = RoleTypes.EMPLOYEES;
        final var aIsDefault = false;

        final var aRole = Role.newRole(aName, aDescription, aRoleType, aIsDefault);

        Assertions.assertEquals(0, roleRepository.count());

        final var actualRole = roleGateway.create(aRole);

        Assertions.assertEquals(1, roleRepository.count());

        Assertions.assertEquals(aRole.getId(), actualRole.getId());
        Assertions.assertEquals(aRole.getName(), actualRole.getName());
        Assertions.assertEquals(aRole.getDescription(), actualRole.getDescription());
        Assertions.assertEquals(aRole.getRoleType(), actualRole.getRoleType());
        Assertions.assertEquals(aRole.isDefault(), actualRole.isDefault());
        Assertions.assertEquals(aRole.getCreatedAt(), actualRole.getCreatedAt());
        Assertions.assertEquals(aRole.getUpdatedAt(), actualRole.getUpdatedAt());

        final var actualEntity = roleRepository.findById(actualRole.getId().getValue()).get();

        Assertions.assertEquals(aRole.getId().getValue(), actualEntity.getId());
        Assertions.assertEquals(aName, actualEntity.getName());
        Assertions.assertEquals(aDescription, actualEntity.getDescription());
        Assertions.assertEquals(aRoleType, actualEntity.getRoleType());
        Assertions.assertEquals(aIsDefault, actualEntity.isDefault());
        Assertions.assertEquals(aRole.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertEquals(aRole.getUpdatedAt(), actualEntity.getUpdatedAt());
    }

    @Test
    void givenAValidRoleWithNullDescription_whenCallCreate_shouldReturnANewRole() {
        final var aName = "ceo";
        final String aDescription = null;
        final var aRoleType = RoleTypes.EMPLOYEES;
        final var aIsDefault = false;

        final var aRole = Role.newRole(aName, aDescription, aRoleType, aIsDefault);

        Assertions.assertEquals(0, roleRepository.count());

        final var actualRole = roleGateway.create(aRole);

        Assertions.assertEquals(1, roleRepository.count());

        Assertions.assertEquals(aRole.getId(), actualRole.getId());
        Assertions.assertEquals(aRole.getName(), actualRole.getName());
        Assertions.assertEquals(aRole.getDescription(), actualRole.getDescription());
        Assertions.assertEquals(aRole.getRoleType(), actualRole.getRoleType());
        Assertions.assertEquals(aRole.isDefault(), actualRole.isDefault());
        Assertions.assertEquals(aRole.getCreatedAt(), actualRole.getCreatedAt());
        Assertions.assertEquals(aRole.getUpdatedAt(), actualRole.getUpdatedAt());

        final var actualEntity = roleRepository.findById(actualRole.getId().getValue()).get();

        Assertions.assertEquals(aRole.getId().getValue(), actualEntity.getId());
        Assertions.assertEquals(aName, actualEntity.getName());
        Assertions.assertEquals(aDescription, actualEntity.getDescription());
        Assertions.assertEquals(aRoleType, actualEntity.getRoleType());
        Assertions.assertEquals(aIsDefault, actualEntity.isDefault());
        Assertions.assertEquals(aRole.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertEquals(aRole.getUpdatedAt(), actualEntity.getUpdatedAt());
    }


    @Test
    void givenAValidNameButNotExistis_whenCallExistsByName_shouldReturnFalse() {
        final var aName = "ceo";

        Assertions.assertEquals(0, roleRepository.count());
        Assertions.assertFalse(roleRepository.existsByName(aName));
    }

    @Test
    void givenAValidExistingName_whenCallExistsByName_shouldReturnTrue() {
        final var aName = "ceo";
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = RoleTypes.EMPLOYEES;
        final var aIsDefault = false;

        final var aRole = Role.newRole(aName, aDescription, aRoleType, aIsDefault);

        Assertions.assertEquals(0, roleRepository.count());
        roleRepository.save(RoleJpaEntity.toEntity(aRole));
        Assertions.assertEquals(1, roleRepository.count());

        Assertions.assertTrue(roleGateway.existsByName(aName));
    }

    @Test
    void givenAValidRole_whenCallUpdate_shouldReturnAUpdatedRole() {
        final var aName = "ceo";
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = RoleTypes.EMPLOYEES;
        final var aIsDefault = false;

        Role aRole = Role.newRole("User", "Common user", RoleTypes.COMMON, true);

        Assertions.assertEquals(0, roleRepository.count());

        roleRepository.save(RoleJpaEntity.toEntity(aRole));

        Assertions.assertEquals(1, roleRepository.count());

        final var aRoleUpdatedDate = aRole.getUpdatedAt();

        final var aRoleUpdated = aRole.update(aName, aDescription, aRoleType, aIsDefault, null);

        final var actualRole = this.roleGateway.update(aRoleUpdated);

        Assertions.assertEquals(aRole.getId(), actualRole.getId());
        Assertions.assertEquals(aName, actualRole.getName());
        Assertions.assertEquals(aDescription, actualRole.getDescription());
        Assertions.assertEquals(aRoleType, actualRole.getRoleType());
        Assertions.assertEquals(aIsDefault, actualRole.isDefault());
        Assertions.assertEquals(aRole.getCreatedAt(), actualRole.getCreatedAt());
        Assertions.assertTrue(actualRole.getUpdatedAt().isAfter(aRoleUpdatedDate));

        final var actualEntity = roleRepository.findById(actualRole.getId().getValue()).get();

        Assertions.assertEquals(aRole.getId().getValue(), actualEntity.getId());
        Assertions.assertEquals(aName, actualEntity.getName());
        Assertions.assertEquals(aDescription, actualEntity.getDescription());
        Assertions.assertEquals(aRoleType, actualEntity.getRoleType());
        Assertions.assertEquals(aIsDefault, actualEntity.isDefault());
        Assertions.assertEquals(aRole.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertTrue(actualEntity.getUpdatedAt().isAfter(aRoleUpdatedDate));
    }

    @Test
    void givenAPrePersistedRoleAndValidRoleId_whenCallFindById_shouldReturnRole() {
        final var aName = "ceo";
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = RoleTypes.EMPLOYEES;
        final var aIsDefault = false;

        Role aRole = Role.newRole(aName, aDescription, aRoleType, aIsDefault);

        Assertions.assertEquals(0, roleRepository.count());

        roleRepository.save(RoleJpaEntity.toEntity(aRole));

        Assertions.assertEquals(1, roleRepository.count());

        final var actualRole = roleGateway.findById(aRole.getId().getValue()).get();

        Assertions.assertEquals(aRole.getId(), actualRole.getId());
        Assertions.assertEquals(aRole.getName(), actualRole.getName());
        Assertions.assertEquals(aRole.getDescription(), actualRole.getDescription());
        Assertions.assertEquals(aRole.getRoleType(), actualRole.getRoleType());
        Assertions.assertEquals(aRole.isDefault(), actualRole.isDefault());
        Assertions.assertEquals(aRole.getCreatedAt(), actualRole.getCreatedAt());
        Assertions.assertEquals(aRole.getUpdatedAt(), actualRole.getUpdatedAt());
    }

    @Test
    void givenAValidRoleIdButNotStored_whenCallFindById_shouldReturnEmpty() {
        Assertions.assertEquals(0, roleRepository.count());

        final var actualRole = roleGateway.findById(RoleID.from("empty").getValue());

        Assertions.assertTrue(actualRole.isEmpty());
    }

    @Test
    void givenAPrePersistedRoleAndAccountUsingRoleAndAccountSavedInCache_whenCallDeleteById_shouldBeOk() {
        roleRepository.saveAndFlush(
                RoleJpaEntity.toEntity(Role
                        .newRole("User", "Common user", RoleTypes.COMMON, true)));

        final var aRole = Role.newRole("Admin", null, RoleTypes.COMMON, false);
        final var aAccount = Account.newAccount(
                "teste",
                "testes",
                "teste@teste.com",
                "123456Ab*",
                aRole
        );

        final var aId = aRole.getId().getValue();

        roleRepository.saveAndFlush(RoleJpaEntity.toEntity(aRole));
        accountJpaRepository.saveAndFlush(AccountJpaEntity.toEntity(aAccount));
        accountCacheRepository.save(AccountCacheEntity.toEntity(aAccount));

        Assertions.assertEquals(2, roleRepository.count());
        Assertions.assertEquals(1, accountJpaRepository.count());
        Assertions.assertEquals(1, accountCacheRepository.count());

        Assertions.assertDoesNotThrow(() -> roleGateway.deleteById(aId));

        Assertions.assertEquals(1, roleRepository.count());
        Assertions.assertEquals(1, accountJpaRepository.count());
        Assertions.assertEquals(0, accountCacheRepository.count());
    }

    @Test
    void givenAPrePersistedRoleAndAccountNotUsingRole_whenCallDeleteById_shouldBeOk() {
        final var aDefaultRole = Role
                .newRole("User", "Common user", RoleTypes.COMMON, true);
        final var aRole = Role.newRole("Admin", null, RoleTypes.COMMON, false);
        final var aAccount = Account.newAccount(
                "teste",
                "testes",
                "teste@teste.com",
                "123456Ab*",
                aRole
        );

        final var aId = aDefaultRole.getId().getValue();

        roleRepository.saveAllAndFlush(Set.of(RoleJpaEntity.toEntity(aRole), RoleJpaEntity.toEntity(aDefaultRole)));
        accountJpaRepository.saveAndFlush(AccountJpaEntity.toEntity(aAccount));
        accountCacheRepository.save(AccountCacheEntity.toEntity(aAccount));

        Assertions.assertEquals(2, roleRepository.count());
        Assertions.assertEquals(1, accountJpaRepository.count());
        Assertions.assertEquals(1, accountCacheRepository.count());

        Assertions.assertDoesNotThrow(() -> roleGateway.deleteById(aId));

        Assertions.assertEquals(1, roleRepository.count());
        Assertions.assertEquals(1, accountJpaRepository.count());
        Assertions.assertEquals(1, accountCacheRepository.count());
    }

    @Test
    void givenAnNotPrePersistedRole_whenCallDeleteById_shouldBeOk() {
        final var aId = "123";

        Assertions.assertEquals(0, roleRepository.count());

        Assertions.assertDoesNotThrow(() -> roleGateway.deleteById(aId));

        Assertions.assertEquals(0, roleRepository.count());
    }

    @Test
    void givenPrePersistedRoles_whenCallFindAll_shouldReturnPaginated() {
        final var aPage = 0;
        final var aPerPage = 1;
        final var aTotalItems = 3;
        final var aTotalPages = 3;

        final var aRoleUser = Role.newRole("User", null, RoleTypes.COMMON, true);
        final var aRoleAdmin = Role.newRole("Admin", "Admin user", RoleTypes.EMPLOYEES, false);
        final var aRoleCeo = Role.newRole("CEO", "Chief Executive Officer", RoleTypes.EMPLOYEES, false);

        Assertions.assertEquals(0, roleRepository.count());

        roleRepository.saveAllAndFlush(List.of(
                RoleJpaEntity.toEntity(aRoleUser),
                RoleJpaEntity.toEntity(aRoleAdmin),
                RoleJpaEntity.toEntity(aRoleCeo)
        ));

        Assertions.assertEquals(3, roleRepository.count());

        final var aQuery = new SearchQuery(0, 1, "", "name", "asc");
        final var aResult = roleGateway.findAll(aQuery);

        Assertions.assertEquals(aPage, aResult.currentPage());
        Assertions.assertEquals(aPerPage, aResult.perPage());
        Assertions.assertEquals(aTotalItems, aResult.totalItems());
        Assertions.assertEquals(aTotalPages, aResult.totalPages());
        Assertions.assertEquals(aPerPage, aResult.items().size());

        Assertions.assertEquals(aRoleAdmin.getId(), aResult.items().get(0).getId());
    }

    @Test
    void givenEmptyRolesTable_whenCallFindAll_shouldReturnEmptyPage() {
        final var aPage = 0;
        final var aPerPage = 1;
        final var aTotalItems = 0;
        final var aTotalPages = 0;

        Assertions.assertEquals(0, roleRepository.count());

        final var aQuery = new SearchQuery(0, 1, "", "name", "asc");
        final var aResult = roleGateway.findAll(aQuery);

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
        final var aRoleAdmin = Role.newRole("Admin", "Admin user", RoleTypes.EMPLOYEES, false);
        final var aRoleCeo = Role.newRole("CEO", "Chief Executive Officer", RoleTypes.EMPLOYEES, false);

        Assertions.assertEquals(0, roleRepository.count());

        roleRepository.saveAllAndFlush(List.of(
                RoleJpaEntity.toEntity(aRoleUser),
                RoleJpaEntity.toEntity(aRoleAdmin),
                RoleJpaEntity.toEntity(aRoleCeo)
        ));

        Assertions.assertEquals(3, roleRepository.count());

        // Page 0
        var aQuery = new SearchQuery(0, 1, "", "name", "asc");
        var aResult = roleGateway.findAll(aQuery);

        Assertions.assertEquals(aPage, aResult.currentPage());
        Assertions.assertEquals(aPerPage, aResult.perPage());
        Assertions.assertEquals(aTotalItems, aResult.totalItems());
        Assertions.assertEquals(aTotalPages, aResult.totalPages());
        Assertions.assertEquals(aPerPage, aResult.items().size());

        Assertions.assertEquals(aRoleAdmin.getId(), aResult.items().get(0).getId());

        // Page 1
        aPage = 1;

        aQuery = new SearchQuery(1, 1, "", "name", "asc");
        aResult = roleGateway.findAll(aQuery);

        Assertions.assertEquals(aPage, aResult.currentPage());
        Assertions.assertEquals(aPerPage, aResult.perPage());
        Assertions.assertEquals(aTotalItems, aResult.totalItems());
        Assertions.assertEquals(aTotalPages, aResult.totalPages());
        Assertions.assertEquals(aPerPage, aResult.items().size());

        Assertions.assertEquals(aRoleCeo.getId(), aResult.items().get(0).getId());

        // Page 2
        aPage = 2;

        aQuery = new SearchQuery(2, 1, "", "name", "asc");
        aResult = roleGateway.findAll(aQuery);

        Assertions.assertEquals(aPage, aResult.currentPage());
        Assertions.assertEquals(aPerPage, aResult.perPage());
        Assertions.assertEquals(aTotalItems, aResult.totalItems());
        Assertions.assertEquals(aTotalPages, aResult.totalPages());
        Assertions.assertEquals(aPerPage, aResult.items().size());

        Assertions.assertEquals(aRoleUser.getId(), aResult.items().get(0).getId());
    }

    @Test
    void givenPrePersistedRolesAndCeAsTerm_whenCallFindAll_shouldReturnPaginated() {
        final var aPage = 0;
        final var aPerPage = 1;
        final var aTotalItems = 1;
        final var aTotalPages = 1;

        final var aRoleUser = Role.newRole("User", null, RoleTypes.COMMON, true);
        final var aRoleAdmin = Role.newRole("Admin", "Admin user", RoleTypes.EMPLOYEES, false);
        final var aRoleCeo = Role.newRole("CEO", "Chief Executive Officer", RoleTypes.EMPLOYEES, false);

        Assertions.assertEquals(0, roleRepository.count());

        roleRepository.saveAllAndFlush(List.of(
                RoleJpaEntity.toEntity(aRoleUser),
                RoleJpaEntity.toEntity(aRoleAdmin),
                RoleJpaEntity.toEntity(aRoleCeo)
        ));

        Assertions.assertEquals(3, roleRepository.count());

        final var aQuery = new SearchQuery(0, 1, "ce", "name", "asc");
        final var aResult = roleGateway.findAll(aQuery);

        Assertions.assertEquals(aPage, aResult.currentPage());
        Assertions.assertEquals(aPerPage, aResult.perPage());
        Assertions.assertEquals(aTotalItems, aResult.totalItems());
        Assertions.assertEquals(aTotalPages, aResult.totalPages());
        Assertions.assertEquals(aPerPage, aResult.items().size());

        Assertions.assertEquals(aRoleCeo.getId(), aResult.items().get(0).getId());
    }

    @Test
    void givenAPrePersistedRoleAndValidRoleId_whenCallFindDefaultRoleTrue_shouldReturnRole() {
        final var aName = "ceo";
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = RoleTypes.EMPLOYEES;
        final var aIsDefault = true;

        Role aRole = Role.newRole(aName, aDescription, aRoleType, aIsDefault);

        Assertions.assertEquals(0, roleRepository.count());

        roleRepository.save(RoleJpaEntity.toEntity(aRole));

        Assertions.assertEquals(1, roleRepository.count());

        final var actualRole = roleGateway.findDefaultRole().get();

        Assertions.assertEquals(aRole.getId(), actualRole.getId());
        Assertions.assertEquals(aRole.getName(), actualRole.getName());
        Assertions.assertEquals(aRole.getDescription(), actualRole.getDescription());
        Assertions.assertEquals(aRole.getRoleType(), actualRole.getRoleType());
        Assertions.assertEquals(aRole.isDefault(), actualRole.isDefault());
        Assertions.assertEquals(aRole.getCreatedAt(), actualRole.getCreatedAt());
        Assertions.assertEquals(aRole.getUpdatedAt(), actualRole.getUpdatedAt());
    }
}
