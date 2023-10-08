package com.kaua.ecommerce.users.application.role.remove;

import com.kaua.ecommerce.users.IntegrationTest;
import com.kaua.ecommerce.users.domain.exceptions.NotFoundException;
import com.kaua.ecommerce.users.domain.permissions.Permission;
import com.kaua.ecommerce.users.domain.permissions.PermissionID;
import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.domain.roles.RolePermission;
import com.kaua.ecommerce.users.domain.roles.RoleTypes;
import com.kaua.ecommerce.users.infrastructure.permissions.persistence.PermissionJpaEntity;
import com.kaua.ecommerce.users.infrastructure.permissions.persistence.PermissionJpaRepository;
import com.kaua.ecommerce.users.infrastructure.roles.persistence.RoleJpaEntity;
import com.kaua.ecommerce.users.infrastructure.roles.persistence.RoleJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

@IntegrationTest
public class RemoveRolePermissionUseCaseIT {

    @Autowired
    private RemoveRolePermissionUseCase useCase;

    @Autowired
    private RoleJpaRepository roleRepository;

    @Autowired
    private PermissionJpaRepository permissionRepository;

    @Test
    void givenAValidCommandWithRoleIdAndPermissionId_whenCallRemovePermission_shouldBeOk() {
        // given
        final var aPermissionOne = Permission.newPermission("teste", null);
        final var aPermissionTwo = Permission.newPermission("create", "create");
        final var aRole = Role.newRole("ceo", null, RoleTypes.EMPLOYEES, true);

        final var aRolePermissionOne = RolePermission.newRolePermission(aPermissionOne.getId(), aPermissionOne.getName());
        final var aRolePermissionTwo = RolePermission.newRolePermission(aPermissionTwo.getId(), aPermissionTwo.getName());

        aRole.addPermissions(Set.of(aRolePermissionOne, aRolePermissionTwo));

        permissionRepository.saveAllAndFlush(List.of(
                PermissionJpaEntity.toEntity(aPermissionOne),
                PermissionJpaEntity.toEntity(aPermissionTwo)));

        roleRepository.saveAndFlush(RoleJpaEntity.toEntity(aRole));

        final var aId = aRole.getId().getValue();
        final var aRolePermissionName = aRolePermissionOne.getPermissionName();

        final var aCommand = RemoveRolePermissionCommand.with(aId, aRolePermissionName);

        // when
        Assertions.assertDoesNotThrow(() -> useCase.execute(aCommand));

        // then
        final var aRoleEntity = roleRepository.findById(aId).get();

        Assertions.assertEquals(1, aRoleEntity.getRolePermissions().size());
    }

    @Test
    void givenAnInvalidCommandWithPermissionIdNotExists_whenCallRemovePermission_shouldBeOk() {
        // given
        final var aPermissionOne = Permission.newPermission("teste", null);
        final var aRole = Role.newRole("ceo", null, RoleTypes.EMPLOYEES, true);
        final var aRolePermissionOne = RolePermission.newRolePermission(aPermissionOne.getId(), aPermissionOne.getName());

        aRole.addPermissions(Set.of(aRolePermissionOne));

        permissionRepository.saveAndFlush(PermissionJpaEntity.toEntity(aPermissionOne));
        roleRepository.saveAndFlush(RoleJpaEntity.toEntity(aRole));

        final var aId = aRole.getId().getValue();
        final var aRolePermissionName = "create-user-admin";

        final var aCommand = RemoveRolePermissionCommand.with(aId, aRolePermissionName);

        // when
        Assertions.assertDoesNotThrow(() -> useCase.execute(aCommand));

        // then
        final var aRoleEntity = roleRepository.findById(aId).get();

        Assertions.assertEquals(1, aRoleEntity.getRolePermissions().size());
    }

    @Test
    void givenAnInvalidRoleId_whenCallRemovePermission_shouldReturnNotFoundException() {
        // given
        final var aRolePermissionOne = RolePermission.newRolePermission(PermissionID.unique(), "teste");
        final var aRolePermissionName = aRolePermissionOne.getPermissionName();
        final var aId = "123";

        final var expectedErrorMessage = "Role with id 123 was not found";

        final var aCommnad = RemoveRolePermissionCommand.with(aId, aRolePermissionName);

        // when
        final var aResult = Assertions.assertThrows(NotFoundException.class,
                () -> useCase.execute(aCommnad));

        // then
        Assertions.assertEquals(expectedErrorMessage, aResult.getMessage());
    }
}
