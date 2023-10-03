package com.kaua.ecommerce.users.application.permission.retrieve.get;

import com.kaua.ecommerce.users.application.gateways.PermissionGateway;
import com.kaua.ecommerce.users.domain.exceptions.NotFoundException;
import com.kaua.ecommerce.users.domain.permissions.Permission;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class GetPermissionByIdUseCaseTest {

    @Mock
    private PermissionGateway permissionGateway;

    @InjectMocks
    private DefaultGetPermissionByIdUseCase useCase;

    @Test
    void givenAValidCommand_whenCallGetPermissionById_thenShouldReturneAnPermission() {
        // given
        final var aName = "create-role";
        final var aDescription = "Permission to create a role";
        final var aPermission = Permission.newPermission(aName, aDescription);

        final var aCommand = GetPermissionByIdCommand.with(aPermission.getId().getValue());

        // when
        Mockito.when(permissionGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(aPermission));

        final var aOutput = Assertions.assertDoesNotThrow(() -> useCase.execute(aCommand));

        // then
        Assertions.assertNotNull(aOutput);
        Assertions.assertEquals(aOutput.id(), aPermission.getId().getValue());
        Assertions.assertEquals(aOutput.name(), aPermission.getName());
        Assertions.assertEquals(aOutput.description(), aPermission.getDescription());
        Assertions.assertEquals(aOutput.createdAt(), aPermission.getCreatedAt().toString());
        Assertions.assertEquals(aOutput.updatedAt(), aPermission.getUpdatedAt().toString());

        Mockito.verify(permissionGateway, Mockito.times(1))
                .findById(aPermission.getId().getValue());
    }

    @Test
    void givenAnInvalidId_whenCallGetPermissionById_thenShouldThrowNotFoundException() {
        // given
        final var expectedErrorMessage = "Permission with id 123 was not found";

        final var aCommand = GetPermissionByIdCommand.with("123");

        // when
        Mockito.when(permissionGateway.findById(Mockito.any()))
                .thenReturn(Optional.empty());

        final var aOutput = Assertions.assertThrows(NotFoundException.class,
                () -> useCase.execute(aCommand));

        // then
        Assertions.assertEquals(expectedErrorMessage, aOutput.getMessage());

        Mockito.verify(permissionGateway, Mockito.times(1))
                .findById(Mockito.any());
    }
}
