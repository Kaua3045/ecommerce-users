package com.kaua.ecommerce.users.application.permission.create;

import com.kaua.ecommerce.users.IntegrationTest;
import com.kaua.ecommerce.users.application.gateways.PermissionGateway;
import com.kaua.ecommerce.users.application.usecases.permission.create.CreatePermissionCommand;
import com.kaua.ecommerce.users.application.usecases.permission.create.CreatePermissionUseCase;
import com.kaua.ecommerce.users.infrastructure.permissions.persistence.PermissionJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

@IntegrationTest
public class CreatePermissionUseCaseIT {

    @Autowired
    private CreatePermissionUseCase createPermissionUseCase;

    @Autowired
    private PermissionJpaRepository permissionRepository;

    @SpyBean
    private PermissionGateway permissionGateway;

    @Test
    void givenAValidCommandWithDescription_whenCallCreatePermission_shouldReturnPermissionId() {
        final var aName = "create-role";
        final var aDescription = "Create a new role";

        Assertions.assertEquals(0, permissionRepository.count());

        final var aCommand = CreatePermissionCommand.with(aName, aDescription);

        final var actualResult = this.createPermissionUseCase.execute(aCommand).getRight();

        Assertions.assertNotNull(actualResult);
        Assertions.assertNotNull(actualResult.id());

        Assertions.assertEquals(1, permissionRepository.count());

        final var actualPermission = permissionRepository.findById(actualResult.id()).get();

        Assertions.assertEquals(aName, actualPermission.getName());
        Assertions.assertEquals(aDescription, actualPermission.getDescription());
        Assertions.assertNotNull(actualPermission.getCreatedAt());
        Assertions.assertNotNull(actualPermission.getUpdatedAt());
    }

    @Test
    void givenAValidCommandWithNullDescription_whenCallCreatePermission_shouldReturnPermissionId() {
        final var aName = "create-role";
        final String aDescription = null;

        Assertions.assertEquals(0, permissionRepository.count());

        final var aCommand = CreatePermissionCommand.with(aName, aDescription);

        final var actualResult = this.createPermissionUseCase.execute(aCommand).getRight();

        Assertions.assertNotNull(actualResult);
        Assertions.assertNotNull(actualResult.id());

        Assertions.assertEquals(1, permissionRepository.count());

        final var actualPermission = permissionRepository.findById(actualResult.id()).get();

        Assertions.assertEquals(aName, actualPermission.getName());
        Assertions.assertEquals(aDescription, actualPermission.getDescription());
        Assertions.assertNotNull(actualPermission.getCreatedAt());
        Assertions.assertNotNull(actualPermission.getUpdatedAt());
    }

    @Test
    void givenAnInvalidName_whenCallCreatePermission_thenShouldReturnDomainException() {
        final String aName = null;
        final var aDescription = "Create a new role";
        final var expectedErrorMessage = "'name' should not be null or blank";
        final var expectedErrorCount = 1;

        Assertions.assertEquals(0, permissionRepository.count());

        final var aCommand = CreatePermissionCommand.with(aName, aDescription);

        final var notification = this.createPermissionUseCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());

        Assertions.assertEquals(0, permissionRepository.count());

        Mockito.verify(permissionGateway, Mockito.times(0)).create(Mockito.any());
    }
}
