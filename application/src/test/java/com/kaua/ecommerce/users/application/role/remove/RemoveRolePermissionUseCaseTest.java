package com.kaua.ecommerce.users.application.role.remove;

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

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.argThat;

@ExtendWith(MockitoExtension.class)
public class RemoveRolePermissionUseCaseTest {

    @Mock
    private RoleGateway roleGateway;

    @InjectMocks
    private DefaultRemoveRolePermissionUseCase useCase;

    @Test
    void givenAValidCommandWithRoleIdAndPermissionId_whenCallRemovePermission_shouldBeOk() {
        // given
        final var aRole = Role.newRole("ceo", null, RoleTypes.EMPLOYEES, true);
        final var aRolePermissionOne = RolePermission.newRolePermission(PermissionID.unique(), "teste");
        final var aRolePermissionTwo = RolePermission.newRolePermission(PermissionID.unique(), "testes");
        aRole.addPermissions(Set.of(aRolePermissionOne, aRolePermissionTwo));

        final var aId = aRole.getId().getValue();
        final var aRolePermissionName = aRolePermissionOne.getPermissionName();

        final var aCommand = RemoveRolePermissionCommand.with(aId, aRolePermissionName);

        // when
        Mockito.when(roleGateway.findById(aId)).thenReturn(Optional.of(aRole));
        Mockito.when(roleGateway.update(aRole)).thenAnswer(returnsFirstArg());

        Assertions.assertDoesNotThrow(() -> useCase.execute(aCommand));

        // then
        Mockito.verify(roleGateway, Mockito.times(1))
                .findById(aId);
        Mockito.verify(roleGateway, Mockito.times(1)).update(argThat(cmd ->
                Objects.equals(aId, cmd.getId().getValue()) &&
                Objects.equals(1, cmd.getPermissions().size()) &&
                Objects.equals(aRole.getName(), cmd.getName()) &&
                Objects.equals(aRole.getDescription(), cmd.getDescription()) &&
                Objects.equals(aRole.getRoleType(), cmd.getRoleType()) &&
                Objects.equals(aRole.isDefault(), cmd.isDefault())));
    }

    @Test
    void givenAnInvalidCommandWithPermissionIdNotExists_whenCallRemovePermission_shouldBeOk() {
        // given
        final var aRole = Role.newRole("ceo", null, RoleTypes.EMPLOYEES, true);
        final var aRolePermissionOne = RolePermission.newRolePermission(PermissionID.unique(), "teste");
        aRole.addPermissions(Set.of(aRolePermissionOne));

        final var aId = aRole.getId().getValue();
        final var aRolePermissionName = "create-user";

        final var aCommand = RemoveRolePermissionCommand.with(aId, aRolePermissionName);

        // when
        Mockito.when(roleGateway.findById(aId)).thenReturn(Optional.of(aRole));
        Mockito.when(roleGateway.update(aRole)).thenAnswer(returnsFirstArg());

        Assertions.assertDoesNotThrow(() -> useCase.execute(aCommand));

        // then
        Mockito.verify(roleGateway, Mockito.times(1))
                .findById(aId);
        Mockito.verify(roleGateway, Mockito.times(1)).update(argThat(cmd ->
                Objects.equals(aId, cmd.getId().getValue()) &&
                        Objects.equals(1, cmd.getPermissions().size()) &&
                        Objects.equals(aRole.getName(), cmd.getName()) &&
                        Objects.equals(aRole.getDescription(), cmd.getDescription()) &&
                        Objects.equals(aRole.getRoleType(), cmd.getRoleType()) &&
                        Objects.equals(aRole.isDefault(), cmd.isDefault())));
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
        Mockito.when(roleGateway.findById(Mockito.anyString())).thenReturn(Optional.empty());

        final var aResult = Assertions.assertThrows(NotFoundException.class,
                () -> useCase.execute(aCommnad));

        // then
        Assertions.assertEquals(expectedErrorMessage, aResult.getMessage());

        Mockito.verify(roleGateway, Mockito.times(1)).findById(Mockito.anyString());
        Mockito.verify(roleGateway, Mockito.times(0)).update(Mockito.any());
    }
}
