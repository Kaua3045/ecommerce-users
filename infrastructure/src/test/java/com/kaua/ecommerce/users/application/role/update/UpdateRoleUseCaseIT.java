package com.kaua.ecommerce.users.application.role.update;

import com.kaua.ecommerce.users.CacheGatewayTest;
import com.kaua.ecommerce.users.application.gateways.RoleGateway;
import com.kaua.ecommerce.users.application.usecases.role.update.UpdateRoleCommand;
import com.kaua.ecommerce.users.application.usecases.role.update.UpdateRoleUseCase;
import com.kaua.ecommerce.users.config.CacheTestConfiguration;
import com.kaua.ecommerce.users.domain.exceptions.NotFoundException;
import com.kaua.ecommerce.users.domain.permissions.Permission;
import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.domain.roles.RoleID;
import com.kaua.ecommerce.users.domain.roles.RoleTypes;
import com.kaua.ecommerce.users.infrastructure.permissions.persistence.PermissionJpaEntity;
import com.kaua.ecommerce.users.infrastructure.permissions.persistence.PermissionJpaRepository;
import com.kaua.ecommerce.users.infrastructure.roles.persistence.RoleJpaEntity;
import com.kaua.ecommerce.users.infrastructure.roles.persistence.RoleJpaRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Set;

@Testcontainers
@CacheGatewayTest
public class UpdateRoleUseCaseIT extends CacheTestConfiguration {

    @Container
    private static final GenericContainer<?> redis = new GenericContainer<>(
            DockerImageName.parse("redis:7-alpine"))
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
    private UpdateRoleUseCase updateRoleUseCase;

    @Autowired
    private RoleJpaRepository roleRepository;

    @Autowired
    private PermissionJpaRepository permissionRepository;

    @SpyBean
    private RoleGateway roleGateway;

    @Test
    void givenAValidCommandWithDescriptionAndPermissions_whenCallUpdateRole_shouldReturnRoleId() {
        final var aPermission = Permission.newPermission("create-user", "Create User");
        final var aRole = Role.newRole("user", null, RoleTypes.COMMON, false);

        roleRepository.saveAndFlush(RoleJpaEntity.toEntity(aRole));
        permissionRepository.saveAndFlush(PermissionJpaEntity.toEntity(aPermission));

        final var aName = "ceo";
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = RoleTypes.EMPLOYEES;
        final var aIsDefault = true;
        final var aPermissions = Set.of(aPermission.getId().getValue());
        final var aId = aRole.getId().getValue();

        final var aCommand = UpdateRoleCommand.with(aId, aName, aDescription, aRoleType.name(), aIsDefault, aPermissions);

        Assertions.assertEquals(1, roleRepository.count());

        final var actualResult = this.updateRoleUseCase.execute(aCommand).getRight();

        Assertions.assertNotNull(actualResult);
        Assertions.assertNotNull(actualResult.id());

        Assertions.assertEquals(1, roleRepository.count());

        final var actualRole = roleRepository.findById(actualResult.id()).get();

        Assertions.assertEquals(aName, actualRole.getName());
        Assertions.assertEquals(aDescription, actualRole.getDescription());
        Assertions.assertEquals(aRoleType, actualRole.getRoleType());
        Assertions.assertEquals(aIsDefault, actualRole.isDefault());
        Assertions.assertEquals(1, actualRole.getPermissions().size());
        Assertions.assertEquals(aRole.getCreatedAt(), actualRole.getCreatedAt());
        Assertions.assertTrue(aRole.getUpdatedAt().isBefore(actualRole.getUpdatedAt()));
    }

    @Test
    void givenAValidCommandWithNullDescription_whenCallUpdateRole_shouldReturnRoleId() {
        final var aRole = Role.newRole("user", null, RoleTypes.COMMON, false);

        roleRepository.saveAndFlush(RoleJpaEntity.toEntity(aRole));

        final var aName = "ceo";
        final String aDescription = null;
        final var aRoleType = RoleTypes.EMPLOYEES;
        final var aIsDefault = false;
        final Set<String> aPermissions = null;
        final var aId = aRole.getId().getValue();

        final var aCommand = UpdateRoleCommand.with(aId, aName, aDescription, aRoleType.name(), aIsDefault, aPermissions);

        Assertions.assertEquals(1, roleRepository.count());

        final var actualResult = this.updateRoleUseCase.execute(aCommand).getRight();

        Assertions.assertNotNull(actualResult);
        Assertions.assertNotNull(actualResult.id());

        Assertions.assertEquals(1, roleRepository.count());

        final var actualRole = roleRepository.findById(actualResult.id()).get();

        Assertions.assertEquals(aName, actualRole.getName());
        Assertions.assertEquals(aDescription, actualRole.getDescription());
        Assertions.assertEquals(aRoleType, actualRole.getRoleType());
        Assertions.assertEquals(aIsDefault, actualRole.isDefault());
        Assertions.assertEquals(0, actualRole.getPermissions().size());
        Assertions.assertEquals(aRole.getCreatedAt(), actualRole.getCreatedAt());
        Assertions.assertTrue(aRole.getUpdatedAt().isBefore(actualRole.getUpdatedAt()));
    }

    @Test
    void givenAnInvalidName_whenCallUpdateRole_thenShouldReturnOldRoleName() {
        final var aRole = Role.newRole("user", null, RoleTypes.COMMON, false);

        roleRepository.saveAndFlush(RoleJpaEntity.toEntity(aRole));

        final var aName = "";
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = RoleTypes.EMPLOYEES;
        final var aIsDefault = false;
        final Set<String> aPermissions = null;
        final var aId = aRole.getId().getValue();

        final var aCommand = UpdateRoleCommand.with(aId, aName, aDescription, aRoleType.name(), aIsDefault, aPermissions);

        Assertions.assertEquals(1, roleRepository.count());

        final var aResult = this.updateRoleUseCase.execute(aCommand).getRight();

        Assertions.assertNotNull(aResult);
        Assertions.assertNotNull(aResult.id());

        Mockito.verify(roleGateway, Mockito.times(1)).update(Mockito.any());
    }

    @Test
    void givenACommandWithInvalidID_whenCallUpdateRole_shouldThrowNotFoundException() {
        final var aName = "ceo";
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = RoleTypes.EMPLOYEES;
        final var aIsDefault = false;
        final Set<String> aPermissions = null;
        final var aId = RoleID.from("123").getValue();

        final var expectedErrorMessage = "Role with id 123 was not found";

        final var aCommand = UpdateRoleCommand.with(aId, aName, aDescription, aRoleType.name(), aIsDefault, aPermissions);

        final var actualException = Assertions.assertThrows(NotFoundException.class,
                () -> this.updateRoleUseCase.execute(aCommand));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
