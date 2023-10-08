package com.kaua.ecommerce.users.application.role.create;

import com.kaua.ecommerce.users.IntegrationTest;
import com.kaua.ecommerce.users.application.gateways.RoleGateway;
import com.kaua.ecommerce.users.application.usecases.role.create.CreateRoleCommand;
import com.kaua.ecommerce.users.application.usecases.role.create.CreateRoleUseCase;
import com.kaua.ecommerce.users.domain.permissions.Permission;
import com.kaua.ecommerce.users.domain.roles.RoleTypes;
import com.kaua.ecommerce.users.infrastructure.permissions.persistence.PermissionJpaEntity;
import com.kaua.ecommerce.users.infrastructure.permissions.persistence.PermissionJpaRepository;
import com.kaua.ecommerce.users.infrastructure.roles.persistence.RoleJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;
import java.util.Set;

@IntegrationTest
public class CreateRoleUseCaseIT {

    @Autowired
    private CreateRoleUseCase createRoleUseCase;

    @Autowired
    private RoleJpaRepository roleRepository;

    @Autowired
    private PermissionJpaRepository permissionRepository;

    @SpyBean
    private RoleGateway roleGateway;

    @Test
    void givenAValidCommandWithDescriptionAndPermissions_whenCallCreateRole_shouldReturnRoleId() {
        final var aPermissionOne = Permission.newPermission("permission-one", "Permission One");
        final var aPermissionTwo = Permission.newPermission("permission-two", "Permission Two");
        permissionRepository.saveAll(List.of(PermissionJpaEntity.toEntity(aPermissionOne),
                PermissionJpaEntity.toEntity(aPermissionTwo)));

        final var aName = "ceo";
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = RoleTypes.EMPLOYEES;
        final var aIsDefault = false;
        final var aPermissions = Set.of(aPermissionOne.getId().getValue(), aPermissionTwo.getId().getValue());

        Assertions.assertEquals(0, roleRepository.count());

        final var aCommand = new CreateRoleCommand(aName, aDescription, aRoleType.name(), aIsDefault, aPermissions);

        final var actualResult = this.createRoleUseCase.execute(aCommand).getRight();

        Assertions.assertNotNull(actualResult);
        Assertions.assertNotNull(actualResult.id());

        Assertions.assertEquals(1, roleRepository.count());

        final var actualRole = roleRepository.findById(actualResult.id()).get();

        Assertions.assertEquals(aName, actualRole.getName());
        Assertions.assertEquals(aDescription, actualRole.getDescription());
        Assertions.assertEquals(aRoleType, actualRole.getRoleType());
        Assertions.assertEquals(aIsDefault, actualRole.isDefault());
        Assertions.assertEquals(2, actualRole.getPermissions().size());
        Assertions.assertNotNull(actualRole.getCreatedAt());
        Assertions.assertNotNull(actualRole.getUpdatedAt());

        Mockito.verify(roleGateway, Mockito.times(1)).create(Mockito.any());
    }

    @Test
    void givenAValidCommandWithNullDescriptionAndNullPermissions_whenCallCreateRole_shouldReturnRoleId() {
        final var aName = "ceo";
        final String aDescription = null;
        final var aRoleType = RoleTypes.EMPLOYEES;
        final var aIsDefault = false;
        final Set<String> aPermissions = null;

        Assertions.assertEquals(0, roleRepository.count());

        final var aCommand = new CreateRoleCommand(aName, aDescription, aRoleType.name(), aIsDefault, aPermissions);

        final var actualResult = this.createRoleUseCase.execute(aCommand).getRight();

        Assertions.assertNotNull(actualResult);
        Assertions.assertNotNull(actualResult.id());

        Assertions.assertEquals(1, roleRepository.count());

        final var actualRole = roleRepository.findById(actualResult.id()).get();

        Assertions.assertEquals(aName, actualRole.getName());
        Assertions.assertEquals(aDescription, actualRole.getDescription());
        Assertions.assertEquals(aRoleType, actualRole.getRoleType());
        Assertions.assertEquals(aIsDefault, actualRole.isDefault());
        Assertions.assertEquals(0, actualRole.getPermissions().size());
        Assertions.assertNotNull(actualRole.getCreatedAt());
        Assertions.assertNotNull(actualRole.getUpdatedAt());

        Mockito.verify(roleGateway, Mockito.times(1)).create(Mockito.any());
    }

    @Test
    void givenAnInvalidName_whenCallCreateRole_thenShouldReturnDomainException() {
        final String aName = null;
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = "employees";
        final var aIsDefault = false;
        final Set<String> aPermissions = null;
        final var expectedErrorMessage = "'name' should not be null or blank";
        final var expectedErrorCount = 1;

        Assertions.assertEquals(0, roleRepository.count());

        final var aCommand = new CreateRoleCommand(aName, aDescription, aRoleType, aIsDefault, aPermissions);

        final var notification = this.createRoleUseCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());

        Assertions.assertEquals(0, roleRepository.count());

        Mockito.verify(roleGateway, Mockito.times(0)).create(Mockito.any());
    }
}
