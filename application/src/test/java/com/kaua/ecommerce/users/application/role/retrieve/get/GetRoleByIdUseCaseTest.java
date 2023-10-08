package com.kaua.ecommerce.users.application.role.retrieve.get;

import com.kaua.ecommerce.users.application.gateways.RoleGateway;
import com.kaua.ecommerce.users.domain.exceptions.NotFoundException;
import com.kaua.ecommerce.users.domain.permissions.PermissionID;
import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.domain.roles.RolePermission;
import com.kaua.ecommerce.users.domain.roles.RoleTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class GetRoleByIdUseCaseTest {

    @Mock
    private RoleGateway roleGateway;

    @InjectMocks
    private DefaultGetRoleByIdUseCase useCase;

    @Test
    void givenAValidCommand_whenCallGetRoleById_thenShouldReturneAnRole() {
        // given
        final var aRolePermission = RolePermission.newRolePermission(PermissionID.unique(), "create-user");

        final var aName = "Ceo";
        final var aDescription = "Chief executive officer";
        final var aRoleType = RoleTypes.EMPLOYEES;
        final var aIsDefault = false;
        final var aPermissions = Set.of(aRolePermission);

        final var aRole = Role.newRole(aName, aDescription, aRoleType, aIsDefault);
        aRole.addPermissions(aPermissions);

        final var aCommand = GetRoleByIdCommand.with(aRole.getId().getValue());

        // when
        Mockito.when(roleGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(aRole));

        final var aOutput = Assertions.assertDoesNotThrow(() -> useCase.execute(aCommand));

        // then
        Assertions.assertNotNull(aOutput);
        Assertions.assertEquals(aOutput.id(), aRole.getId().getValue());
        Assertions.assertEquals(aOutput.name(), aRole.getName());
        Assertions.assertEquals(aOutput.description(), aRole.getDescription());
        Assertions.assertEquals(aOutput.roleType(), aRole.getRoleType().name());
        Assertions.assertEquals(aOutput.isDefault(), aRole.isDefault());
        Assertions.assertEquals(aOutput.createdAt(), aRole.getCreatedAt().toString());
        Assertions.assertEquals(aOutput.updatedAt(), aRole.getUpdatedAt().toString());
        Assertions.assertEquals(1, aOutput.permissions().size());

        Mockito.verify(roleGateway, Mockito.times(1))
                .findById(aRole.getId().getValue());
    }

    @Test
    void givenAnInvalidId_whenCallGetRoleById_thenShouldThrowNotFoundException() {
        // given
        final var expectedErrorMessage = "Role with id 123 was not found";

        final var aCommand = GetRoleByIdCommand.with("123");

        // when
        Mockito.when(roleGateway.findById(Mockito.any()))
                .thenReturn(Optional.empty());

        final var aOutput = Assertions.assertThrows(NotFoundException.class,
                () -> useCase.execute(aCommand));

        // then
        Assertions.assertEquals(expectedErrorMessage, aOutput.getMessage());

        Mockito.verify(roleGateway, Mockito.times(1))
                .findById(Mockito.any());
    }
}
