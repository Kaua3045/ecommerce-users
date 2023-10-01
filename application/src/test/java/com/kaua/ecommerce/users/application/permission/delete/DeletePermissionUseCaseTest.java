package com.kaua.ecommerce.users.application.permission.delete;

import com.kaua.ecommerce.users.application.gateways.PermissionGateway;
import com.kaua.ecommerce.users.domain.permissions.Permission;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DeletePermissionUseCaseTest {

    @Mock
    private PermissionGateway permissionGateway;

    @InjectMocks
    private DefaultDeletePermissionUseCase useCase;

    @Test
    void givenAValidCommandWithPermissionId_whenCallDeleteById_shouldBeOk() {
        // given
        final var aPermission = Permission.newPermission("create-role", "Create Role");
        final var aId = aPermission.getId().getValue();

        final var aCommand = DeletePermissionCommand.with(aId);

        // when
        Mockito.doNothing().when(permissionGateway).deleteById(aId);

        Assertions.assertDoesNotThrow(() -> useCase.execute(aCommand));

        // then
        Mockito.verify(permissionGateway, Mockito.times(1))
                .deleteById(aId);
    }

    @Test
    void givenAnInvalidCommandWithAccountIdNotExists_whenCallDeleteById_shouldBeOk() {
        // given
        final var aId = "123";

        final var aCommand = DeletePermissionCommand.with(aId);

        // when
        Mockito.doNothing().when(permissionGateway).deleteById(aId);

        Assertions.assertDoesNotThrow(() -> useCase.execute(aCommand));

        // then
        Mockito.verify(permissionGateway, Mockito.times(1))
                .deleteById(aId);
    }
}
