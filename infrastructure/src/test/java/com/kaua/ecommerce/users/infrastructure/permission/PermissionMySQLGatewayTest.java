package com.kaua.ecommerce.users.infrastructure.permission;

import com.kaua.ecommerce.users.CacheGatewayTest;
import com.kaua.ecommerce.users.config.CacheTestConfiguration;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.pagination.SearchQuery;
import com.kaua.ecommerce.users.domain.permissions.Permission;
import com.kaua.ecommerce.users.domain.permissions.PermissionID;
import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.domain.roles.RolePermission;
import com.kaua.ecommerce.users.domain.roles.RoleTypes;
import com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountCacheEntity;
import com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountCacheRepository;
import com.kaua.ecommerce.users.infrastructure.permissions.PermissionMySQLGateway;
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

import java.util.List;
import java.util.Set;

@Testcontainers
@CacheGatewayTest
public class PermissionMySQLGatewayTest extends CacheTestConfiguration {

    @Container
    private static final GenericContainer<?> redis = new GenericContainer<>(
            DockerImageName.parse("redis:6-alpine"))
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void redisProperties(final DynamicPropertyRegistry propertySources) {
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
    private PermissionMySQLGateway permissionGateway;

    @Autowired
    private PermissionJpaRepository permissionRepository;

    @Autowired
    private AccountCacheRepository accountCacheRepository;

    @Autowired
    private RoleJpaRepository roleJpaRepository;

    @Test
    void givenAValidPermissionWithDescription_whenCallCreate_shouldReturnANewPermission() {
        final var aName = "create-role";
        final var aDescription = "Create a new role";

        final var aPermission = Permission.newPermission(aName, aDescription);

        Assertions.assertEquals(0, permissionRepository.count());

        final var actualPermission = permissionGateway.create(aPermission);

        Assertions.assertEquals(1, permissionRepository.count());

        Assertions.assertEquals(aPermission.getId(), actualPermission.getId());
        Assertions.assertEquals(aPermission.getName(), actualPermission.getName());
        Assertions.assertEquals(aPermission.getDescription(), actualPermission.getDescription());
        Assertions.assertEquals(aPermission.getCreatedAt(), actualPermission.getCreatedAt());
        Assertions.assertEquals(aPermission.getUpdatedAt(), actualPermission.getUpdatedAt());

        final var actualEntity = permissionRepository.findById(actualPermission.getId().getValue()).get();

        Assertions.assertEquals(aPermission.getId().getValue(), actualEntity.getId());
        Assertions.assertEquals(aName, actualEntity.getName());
        Assertions.assertEquals(aDescription, actualEntity.getDescription());
        Assertions.assertEquals(aPermission.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertEquals(aPermission.getUpdatedAt(), actualEntity.getUpdatedAt());
    }

    @Test
    void givenAValidPermissionWithNullDescription_whenCallCreate_shouldReturnANewPermission() {
        final var aName = "create-role";
        final String aDescription = null;

        final var aPermission = Permission.newPermission(aName, aDescription);

        Assertions.assertEquals(0, permissionRepository.count());

        final var actualPermission = permissionGateway.create(aPermission);

        Assertions.assertEquals(1, permissionRepository.count());

        Assertions.assertEquals(aPermission.getId(), actualPermission.getId());
        Assertions.assertEquals(aPermission.getName(), actualPermission.getName());
        Assertions.assertEquals(aPermission.getDescription(), actualPermission.getDescription());
        Assertions.assertEquals(aPermission.getCreatedAt(), actualPermission.getCreatedAt());
        Assertions.assertEquals(aPermission.getUpdatedAt(), actualPermission.getUpdatedAt());

        final var actualEntity = permissionRepository.findById(actualPermission.getId().getValue()).get();

        Assertions.assertEquals(aPermission.getId().getValue(), actualEntity.getId());
        Assertions.assertEquals(aName, actualEntity.getName());
        Assertions.assertEquals(aDescription, actualEntity.getDescription());
        Assertions.assertEquals(aPermission.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertEquals(aPermission.getUpdatedAt(), actualEntity.getUpdatedAt());
    }


    @Test
    void givenAValidNameButNotExists_whenCallExistsByName_shouldReturnFalse() {
        final var aName = "create-role";

        Assertions.assertEquals(0, permissionRepository.count());
        Assertions.assertFalse(permissionGateway.existsByName(aName));
    }

    @Test
    void givenAValidExistingName_whenCallExistsByName_shouldReturnTrue() {
        final var aName = "create-role";
        final var aDescription = "Create a new role";

        final var aPermission = Permission.newPermission(aName, aDescription);

        Assertions.assertEquals(0, permissionRepository.count());
        permissionRepository.save(PermissionJpaEntity.toEntity(aPermission));
        Assertions.assertEquals(1, permissionRepository.count());

        Assertions.assertTrue(permissionGateway.existsByName(aName));
    }

    @Test
    void givenAPrePersistedPermission_whenCallDeleteById_shouldBeOk() {
        final var aRole = Role.newRole("admin", "Admin", RoleTypes.EMPLOYEES, false);
        final var aAccount = Account.newAccount(
                "teste",
                "testes",
                "teste@testes.com",
                "1234567Ab*",
                aRole
        );
        final var aPermission = Permission.newPermission("create-role", "Create a new role");
        final var aId = aPermission.getId().getValue();

        aRole.addPermissions(Set.of(RolePermission.newRolePermission(aPermission.getId(), aPermission.getName())));
        permissionRepository.saveAndFlush(PermissionJpaEntity.toEntity(aPermission));
        roleJpaRepository.saveAndFlush(RoleJpaEntity.toEntity(aRole));
        accountCacheRepository.save(AccountCacheEntity.toEntity(aAccount));

        Assertions.assertEquals(1, permissionRepository.count());

        Assertions.assertDoesNotThrow(() -> permissionGateway.deleteById(aId));

        Assertions.assertEquals(0, permissionRepository.count());
    }

    @Test
    void givenAnNotPrePersistedPermission_whenCallDeleteById_shouldBeOk() {
        final var aId = "123";

        Assertions.assertEquals(0, permissionRepository.count());

        Assertions.assertDoesNotThrow(() -> permissionGateway.deleteById(aId));

        Assertions.assertEquals(0, permissionRepository.count());
    }

    @Test
    void givenAPrePersistedPermissionAndValidPermissionId_whenCallFindById_shouldReturnPermission() {
        final var aName = "create-role";
        final var aDescription = "Create a new role";

        Permission aPermission = Permission.newPermission(aName, aDescription);

        Assertions.assertEquals(0, permissionRepository.count());

        permissionRepository.save(PermissionJpaEntity.toEntity(aPermission));

        Assertions.assertEquals(1, permissionRepository.count());

        final var actualPermission = permissionGateway.findById(aPermission.getId().getValue()).get();

        Assertions.assertEquals(aPermission.getId(), actualPermission.getId());
        Assertions.assertEquals(aPermission.getName(), actualPermission.getName());
        Assertions.assertEquals(aPermission.getDescription(), actualPermission.getDescription());
        Assertions.assertEquals(aPermission.getCreatedAt(), actualPermission.getCreatedAt());
        Assertions.assertEquals(aPermission.getUpdatedAt(), actualPermission.getUpdatedAt());
    }

    @Test
    void givenAValidPermissionIdButNotStored_whenCallFindById_shouldReturnEmpty() {
        Assertions.assertEquals(0, permissionRepository.count());

        final var actualPermission = permissionGateway.findById(PermissionID.from("empty").getValue());

        Assertions.assertTrue(actualPermission.isEmpty());
    }

    @Test
    void givenAValidPermission_whenCallUpdate_shouldReturnAUpdatedPermission() {
        final var aName = "create-role";
        final var aDescription = "Create a new role";

        final var aPermission = Permission.newPermission(aName, null);

        Assertions.assertEquals(0, permissionRepository.count());

        permissionRepository.save(PermissionJpaEntity.toEntity(aPermission));

        Assertions.assertEquals(1, permissionRepository.count());

        final var aPermissionUpdatedDate = aPermission.getUpdatedAt();

        final var aPermissionUpdated = aPermission.update(aDescription);

        final var actualPermission = this.permissionGateway.update(aPermissionUpdated);

        Assertions.assertEquals(aPermission.getId(), actualPermission.getId());
        Assertions.assertEquals(aName, actualPermission.getName());
        Assertions.assertEquals(aDescription, actualPermission.getDescription());
        Assertions.assertEquals(aPermission.getCreatedAt(), actualPermission.getCreatedAt());
        Assertions.assertTrue(actualPermission.getUpdatedAt().isAfter(aPermissionUpdatedDate));

        final var actualEntity = permissionRepository.findById(actualPermission.getId().getValue()).get();

        Assertions.assertEquals(aPermission.getId().getValue(), actualEntity.getId());
        Assertions.assertEquals(aName, actualEntity.getName());
        Assertions.assertEquals(aDescription, actualEntity.getDescription());
        Assertions.assertEquals(aPermission.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertTrue(actualEntity.getUpdatedAt().isAfter(aPermissionUpdatedDate));
    }

    @Test
    void givenPrePersistedPermission_whenCallFindAll_shouldReturnPaginated() {
        final var aPage = 0;
        final var aPerPage = 1;
        final var aTotalItems = 3;
        final var aTotalPages = 3;

        final var aCustomerPermission = Permission.newPermission("customer-all", "Customer all");
        final var aAdminPermission = Permission.newPermission("admin-all", "Admin all");
        final var aCeoPermission = Permission.newPermission("ceo-all", null);

        Assertions.assertEquals(0, permissionRepository.count());

        permissionRepository.saveAllAndFlush(List.of(
                PermissionJpaEntity.toEntity(aCustomerPermission),
                PermissionJpaEntity.toEntity(aAdminPermission),
                PermissionJpaEntity.toEntity(aCeoPermission)
        ));

        Assertions.assertEquals(3, permissionRepository.count());

        final var aQuery = new SearchQuery(0, 1, "", "name", "asc");
        final var aResult = permissionGateway.findAll(aQuery);

        Assertions.assertEquals(aPage, aResult.currentPage());
        Assertions.assertEquals(aPerPage, aResult.perPage());
        Assertions.assertEquals(aTotalItems, aResult.totalItems());
        Assertions.assertEquals(aTotalPages, aResult.totalPages());
        Assertions.assertEquals(aPerPage, aResult.items().size());

        Assertions.assertEquals(aAdminPermission.getId(), aResult.items().get(0).getId());
    }

    @Test
    void givenEmptyPermissionsTable_whenCallFindAll_shouldReturnEmptyPage() {
        final var aPage = 0;
        final var aPerPage = 1;
        final var aTotalItems = 0;
        final var aTotalPages = 0;

        Assertions.assertEquals(0, permissionRepository.count());

        final var aQuery = new SearchQuery(0, 1, "", "name", "asc");
        final var aResult = permissionGateway.findAll(aQuery);

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

        final var aCustomerPermission = Permission.newPermission("customer-all", "Customer all");
        final var aAdminPermission = Permission.newPermission("admin-all", "Admin all");
        final var aCeoPermission = Permission.newPermission("ceo-all", null);

        Assertions.assertEquals(0, permissionRepository.count());

        permissionRepository.saveAllAndFlush(List.of(
                PermissionJpaEntity.toEntity(aCustomerPermission),
                PermissionJpaEntity.toEntity(aAdminPermission),
                PermissionJpaEntity.toEntity(aCeoPermission)
        ));

        Assertions.assertEquals(3, permissionRepository.count());

        // Page 0
        var aQuery = new SearchQuery(0, 1, "", "name", "asc");
        var aResult = permissionGateway.findAll(aQuery);

        Assertions.assertEquals(aPage, aResult.currentPage());
        Assertions.assertEquals(aPerPage, aResult.perPage());
        Assertions.assertEquals(aTotalItems, aResult.totalItems());
        Assertions.assertEquals(aTotalPages, aResult.totalPages());
        Assertions.assertEquals(aPerPage, aResult.items().size());

        Assertions.assertEquals(aAdminPermission.getId(), aResult.items().get(0).getId());

        // Page 1
        aPage = 1;

        aQuery = new SearchQuery(1, 1, "", "name", "asc");
        aResult = permissionGateway.findAll(aQuery);

        Assertions.assertEquals(aPage, aResult.currentPage());
        Assertions.assertEquals(aPerPage, aResult.perPage());
        Assertions.assertEquals(aTotalItems, aResult.totalItems());
        Assertions.assertEquals(aTotalPages, aResult.totalPages());
        Assertions.assertEquals(aPerPage, aResult.items().size());

        Assertions.assertEquals(aCeoPermission.getId(), aResult.items().get(0).getId());

        // Page 2
        aPage = 2;

        aQuery = new SearchQuery(2, 1, "", "name", "asc");
        aResult = permissionGateway.findAll(aQuery);

        Assertions.assertEquals(aPage, aResult.currentPage());
        Assertions.assertEquals(aPerPage, aResult.perPage());
        Assertions.assertEquals(aTotalItems, aResult.totalItems());
        Assertions.assertEquals(aTotalPages, aResult.totalPages());
        Assertions.assertEquals(aPerPage, aResult.items().size());

        Assertions.assertEquals(aCustomerPermission.getId(), aResult.items().get(0).getId());
    }

    @Test
    void givenPrePersistedPermissionsAndCustAsTerm_whenCallFindAll_shouldReturnPaginated() {
        final var aPage = 0;
        final var aPerPage = 1;
        final var aTotalItems = 1;
        final var aTotalPages = 1;

        final var aCustomerPermission = Permission.newPermission("customer-all", "Customer all");
        final var aAdminPermission = Permission.newPermission("admin-all", "Admin all");
        final var aCeoPermission = Permission.newPermission("ceo-all", null);

        Assertions.assertEquals(0, permissionRepository.count());

        permissionRepository.saveAllAndFlush(List.of(
                PermissionJpaEntity.toEntity(aCustomerPermission),
                PermissionJpaEntity.toEntity(aAdminPermission),
                PermissionJpaEntity.toEntity(aCeoPermission)
        ));

        Assertions.assertEquals(3, permissionRepository.count());

        final var aQuery = new SearchQuery(0, 1, "cust", "name", "asc");
        final var aResult = permissionGateway.findAll(aQuery);

        Assertions.assertEquals(aPage, aResult.currentPage());
        Assertions.assertEquals(aPerPage, aResult.perPage());
        Assertions.assertEquals(aTotalItems, aResult.totalItems());
        Assertions.assertEquals(aTotalPages, aResult.totalPages());
        Assertions.assertEquals(aPerPage, aResult.items().size());

        Assertions.assertEquals(aCustomerPermission.getId(), aResult.items().get(0).getId());
    }

    @Test
    void givenTwoPermissionsAndOnePersisted_whenCallFindAllByIds_shouldReturnPersistedPermission() {
        final var aPermission = Permission.newPermission("customer-all", "Customer all");

        final var aTotal = 1;
        final var aId = aPermission.getId().getValue();

        Assertions.assertEquals(0, permissionRepository.count());

        permissionRepository.saveAndFlush(PermissionJpaEntity.toEntity(aPermission));

        Assertions.assertEquals(1, permissionRepository.count());

        final var actualPermission = permissionGateway.findAllByIds(Set.of(aId, "123"));

        Assertions.assertEquals(aTotal, actualPermission.size());
        Assertions.assertTrue(actualPermission.contains(aPermission));
    }
}
