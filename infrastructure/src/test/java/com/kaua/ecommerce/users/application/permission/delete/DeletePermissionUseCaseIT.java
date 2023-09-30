package com.kaua.ecommerce.users.application.permission.delete;

import com.kaua.ecommerce.users.IntegrationTest;
import com.kaua.ecommerce.users.domain.permissions.Permission;
import com.kaua.ecommerce.users.domain.permissions.PermissionID;
import com.kaua.ecommerce.users.infrastructure.permissions.persistence.PermissionJpaEntity;
import com.kaua.ecommerce.users.infrastructure.permissions.persistence.PermissionJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class DeletePermissionUseCaseIT {

    @Autowired
    private DeletePermissionUseCase deletePermissionUseCase;

    @Autowired
    private PermissionJpaRepository permissionRepository;

    @Test
    void givenAValidId_whenCallDeletePermission_shouldBeOk() {
        final var aPermission = Permission.newPermission("create-role", "create a new role");
        final var aId = aPermission.getId().getValue();

        permissionRepository.saveAndFlush(PermissionJpaEntity.toEntity(aPermission));

        Assertions.assertEquals(1, permissionRepository.count());

        Assertions.assertDoesNotThrow(() -> this.deletePermissionUseCase.execute(DeletePermissionCommand.with(aId)));

        Assertions.assertEquals(0, permissionRepository.count());
    }

    @Test
    void givenAInvalidId_whenCallDeletePermission_shouldBeOk() {
        final var aId = PermissionID.from("123").getValue();

        Assertions.assertEquals(0, permissionRepository.count());

        Assertions.assertDoesNotThrow(() -> this.deletePermissionUseCase.execute(DeletePermissionCommand.with(aId)));

        Assertions.assertEquals(0, permissionRepository.count());
    }
}
