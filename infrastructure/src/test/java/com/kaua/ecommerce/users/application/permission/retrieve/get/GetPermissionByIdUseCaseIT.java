package com.kaua.ecommerce.users.application.permission.retrieve.get;

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
public class GetPermissionByIdUseCaseIT {

    @Autowired
    private GetPermissionByIdUseCase getPermissionByIdUseCase;

    @Autowired
    private PermissionJpaRepository permissionRepository;

    @Test
    void givenAValidId_whenCallGetPermissionById_shouldReturnPermission() {
        final var aName = "create-admin-user";
        final var aDescription = "Permission to create admin user";

        final var aPermission = Permission.newPermission(aName, aDescription);
        final var aId = aPermission.getId().getValue();

        permissionRepository.saveAndFlush(PermissionJpaEntity.toEntity(aPermission));

        final var actualPermission = this.getPermissionByIdUseCase.execute(GetPermissionByIdCommand.with(aId));

        Assertions.assertEquals(aId, actualPermission.id());
        Assertions.assertEquals(aName, actualPermission.name());
        Assertions.assertEquals(aDescription, actualPermission.description());
        Assertions.assertEquals(aPermission.getCreatedAt().toString(), actualPermission.createdAt());
        Assertions.assertEquals(aPermission.getUpdatedAt().toString(), actualPermission.updatedAt());
    }

    @Test
    void givenAnInvalidId_whenCallGetPermissionById_shouldThrowNotFoundException() {
        final var expectedErrorMessage = "Permission with id 123 was not found";
        final var aId = PermissionID.from("123").getValue();

        final var actualException = Assertions.assertThrows(NotFoundException.class,
                () -> this.getPermissionByIdUseCase.execute(GetPermissionByIdCommand.with(aId)));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
