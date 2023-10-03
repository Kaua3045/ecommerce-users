package com.kaua.ecommerce.users.application.permission.update;

import com.kaua.ecommerce.users.IntegrationTest;
import com.kaua.ecommerce.users.domain.exceptions.NotFoundException;
import com.kaua.ecommerce.users.domain.permissions.Permission;
import com.kaua.ecommerce.users.domain.permissions.PermissionID;
import com.kaua.ecommerce.users.infrastructure.permissions.persistence.PermissionJpaEntity;
import com.kaua.ecommerce.users.infrastructure.permissions.persistence.PermissionJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class UpdatePermissionUseCaseIT {

    @Autowired
    private UpdatePermissionUseCase updatePermissionUseCase;

    @Autowired
    private PermissionJpaRepository permissionRepository;

    @Test
    void givenAValidCommand_whenCallUpdatePermission_shouldReturnPermissionId() {
        final var aName = "create-admin-user";
        final var aPermission = Permission.newPermission(aName, null);

        permissionRepository.saveAndFlush(PermissionJpaEntity.toEntity(aPermission));

        final var aDescription = "Create an admin user";
        final var aId = aPermission.getId().getValue();

        final var aCommand = UpdatePermissionCommand.with(aId, aDescription);

        Assertions.assertEquals(1, permissionRepository.count());

        final var actualResult = this.updatePermissionUseCase.execute(aCommand).getRight();

        Assertions.assertNotNull(actualResult);
        Assertions.assertNotNull(actualResult.id());

        Assertions.assertEquals(1, permissionRepository.count());

        final var actualRole = permissionRepository.findById(actualResult.id()).get();

        Assertions.assertEquals(aName, actualRole.getName());
        Assertions.assertEquals(aDescription, actualRole.getDescription());
        Assertions.assertEquals(aPermission.getCreatedAt(), actualRole.getCreatedAt());
        Assertions.assertTrue(aPermission.getUpdatedAt().isBefore(actualRole.getUpdatedAt()));
    }

    @Test
    void givenAValidCommandWithNullDescription_whenCallUpdatePermission_shouldReturnPermissionId() {
        final var aName = "create-admin-user";
        final var aPermission = Permission.newPermission(aName, "Create an admin user");

        permissionRepository.saveAndFlush(PermissionJpaEntity.toEntity(aPermission));

        final String aDescription = null;
        final var aId = aPermission.getId().getValue();

        final var aCommand = UpdatePermissionCommand.with(aId, aDescription);

        Assertions.assertEquals(1, permissionRepository.count());

        final var actualResult = this.updatePermissionUseCase.execute(aCommand).getRight();

        Assertions.assertNotNull(actualResult);
        Assertions.assertNotNull(actualResult.id());

        Assertions.assertEquals(1, permissionRepository.count());

        final var actualRole = permissionRepository.findById(actualResult.id()).get();

        Assertions.assertEquals(aName, actualRole.getName());
        Assertions.assertEquals(aDescription, actualRole.getDescription());
        Assertions.assertEquals(aPermission.getCreatedAt(), actualRole.getCreatedAt());
        Assertions.assertTrue(aPermission.getUpdatedAt().isBefore(actualRole.getUpdatedAt()));
    }

    @Test
    void givenACommandWithInvalidID_whenCallUpdatePermission_shouldThrowNotFoundException() {
        final var aDescription = "Create an admin user";
        final var aId = PermissionID.from("123").getValue();

        final var expectedErrorMessage = "Permission with id 123 was not found";

        final var aCommand = UpdatePermissionCommand.with(aId, aDescription);

        final var actualException = Assertions.assertThrows(NotFoundException.class,
                () -> this.updatePermissionUseCase.execute(aCommand));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
