package com.kaua.ecommerce.users.application.permission.update;

import com.kaua.ecommerce.users.application.gateways.PermissionGateway;
import com.kaua.ecommerce.users.domain.exceptions.NotFoundException;
import com.kaua.ecommerce.users.domain.permissions.Permission;
import com.kaua.ecommerce.users.domain.utils.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.argThat;

@ExtendWith(MockitoExtension.class)
public class UpdatePermissionUseCaseTest {

    @Mock
    private PermissionGateway permissionGateway;

    @InjectMocks
    private DefaultUpdatePermissionUseCase useCase;

    @Test
    void givenAValidCommand_whenCallUpdatePermission_shouldReturnPermissionId() {
        // given
        final var aName = "create-role";
        final var aDescription = "Create a new role";
        final var aPermission = Permission.newPermission(aName, aDescription);

        final var aCommnad = UpdatePermissionCommand.with(
                aPermission.getId().getValue(),
                aDescription
        );

        // when
        Mockito.when(permissionGateway.findById(Mockito.anyString())).thenReturn(Optional.of(aPermission));
        Mockito.when(permissionGateway.update(Mockito.any())).thenAnswer(returnsFirstArg());

        final var aResult = useCase.execute(aCommnad).getRight();

        // then
        Assertions.assertNotNull(aResult);
        Assertions.assertNotNull(aResult.id());

        Mockito.verify(permissionGateway, Mockito.times(1)).findById(Mockito.anyString());
        Mockito.verify(permissionGateway, Mockito.times(1)).update(argThat(cmd ->
                Objects.equals(aName, cmd.getName()) &&
                Objects.equals(aDescription, cmd.getDescription()) &&
                Objects.nonNull(cmd.getId()) &&
                Objects.nonNull(cmd.getCreatedAt()) &&
                Objects.nonNull(cmd.getUpdatedAt())
        ));
    }

    @Test
    void givenAValidCommandWithDescriptionNull_whenCallUpdatePermission_shouldReturnPermissionId() {
        // given
        final var aName = "create-role";
        final String aDescription = null;
        final var aPermission = Permission.newPermission(aName, aDescription);

        final var aCommnad = UpdatePermissionCommand.with(
                aPermission.getId().getValue(),
                aDescription
        );

        // when
        Mockito.when(permissionGateway.findById(Mockito.anyString())).thenReturn(Optional.of(aPermission));
        Mockito.when(permissionGateway.update(Mockito.any())).thenAnswer(returnsFirstArg());

        final var aResult = useCase.execute(aCommnad).getRight();

        // then
        Assertions.assertNotNull(aResult);
        Assertions.assertNotNull(aResult.id());

        Mockito.verify(permissionGateway, Mockito.times(1)).findById(Mockito.anyString());
        Mockito.verify(permissionGateway, Mockito.times(1)).update(argThat(cmd ->
                Objects.equals(aName, cmd.getName()) &&
                        Objects.equals(aDescription, cmd.getDescription()) &&
                        Objects.nonNull(cmd.getId()) &&
                        Objects.nonNull(cmd.getCreatedAt()) &&
                        Objects.nonNull(cmd.getUpdatedAt())
        ));
    }

    @Test
    void givenAnInvalidPermissionId_whenCallUpdatePermission_shouldReturnNotFoundException() {
        // given
        final var aId = "123";
        final var aName = "create-role";
        final var aDescription = "Create a new role";

        final var expectedErrorMessage = "Permission with id 123 was not found";

        final var aCommnad = UpdatePermissionCommand.with(aId, aDescription);

        // when
        Mockito.when(permissionGateway.findById(Mockito.anyString())).thenReturn(Optional.empty());

        final var aResult = Assertions.assertThrows(NotFoundException.class,
                () -> useCase.execute(aCommnad));

        // then
        Assertions.assertEquals(expectedErrorMessage, aResult.getMessage());

        Mockito.verify(permissionGateway, Mockito.times(1)).findById(Mockito.anyString());
        Mockito.verify(permissionGateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    void givenAnInvalidDescriptionLengthMoreThan255_whenCallUpdatePermission_shouldReturnDomainException() {
        // given
        final var aName = "create-role";
        final var aDescription = RandomStringUtils.generateValue(256);
        final var aPermission = Permission.newPermission(aName, aDescription);

        final var expectedErrorMessage = "'description' must be between 0 and 255 characters";
        final var expectedErrorCount = 1;

        final var aCommnad = UpdatePermissionCommand.with(
                aPermission.getId().getValue(),
                aDescription
        );

        // when
        Mockito.when(permissionGateway.findById(Mockito.anyString())).thenReturn(Optional.of(aPermission));

        final var aResult = useCase.execute(aCommnad).getLeft();

        // then
        Assertions.assertEquals(expectedErrorMessage, aResult.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aResult.getErrors().size());

        Mockito.verify(permissionGateway, Mockito.times(1)).findById(Mockito.anyString());
        Mockito.verify(permissionGateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    void givenAValidStringId_whenCallUpdatePermissionOutputFrom_shouldReturnUpdatePermissionOutputInstance() {
        final var aPermission = Permission.newPermission("create-role", "Create a new role");

        final var aResult = UpdatePermissionOutput.from(aPermission);

        Assertions.assertNotNull(aResult);
        Assertions.assertEquals(aPermission.getId().getValue(), aResult.id());
        Assertions.assertInstanceOf(UpdatePermissionOutput.class, aResult);
    }
}
