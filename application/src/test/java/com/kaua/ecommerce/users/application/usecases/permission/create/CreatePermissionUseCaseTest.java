package com.kaua.ecommerce.users.application.usecases.permission.create;

import com.kaua.ecommerce.users.application.gateways.PermissionGateway;
import com.kaua.ecommerce.users.application.usecases.permission.create.CreatePermissionCommand;
import com.kaua.ecommerce.users.application.usecases.permission.create.CreatePermissionOutput;
import com.kaua.ecommerce.users.application.usecases.permission.create.DefaultCreatePermissionUseCase;
import com.kaua.ecommerce.users.domain.utils.IdUtils;
import com.kaua.ecommerce.users.domain.utils.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.argThat;

@ExtendWith(MockitoExtension.class)
public class CreatePermissionUseCaseTest {

    @Mock
    private PermissionGateway permissionGateway;

    @InjectMocks
    private DefaultCreatePermissionUseCase useCase;

    @Test
    void givenAValidCommandWithNullDescription_whenCallCreatePermission_shouldReturnPermissionId() {
        // given
        final var aName = "create-role";
        final String aDescription = null;

        final var aCommnad = CreatePermissionCommand.with(aName, aDescription);

        // when
        Mockito.when(permissionGateway.existsByName(Mockito.anyString())).thenReturn(false);
        Mockito.when(permissionGateway.create(Mockito.any())).thenAnswer(returnsFirstArg());

        final var aResult = useCase.execute(aCommnad).getRight();

        // then
        Assertions.assertNotNull(aResult);
        Assertions.assertNotNull(aResult.id());

        Mockito.verify(permissionGateway, Mockito.times(1)).existsByName(Mockito.anyString());
        Mockito.verify(permissionGateway, Mockito.times(1)).create(argThat(cmd ->
                Objects.equals(aName, cmd.getName()) &&
                Objects.equals(aDescription, cmd.getDescription()) &&
                Objects.nonNull(cmd.getId()) &&
                Objects.nonNull(cmd.getCreatedAt()) &&
                Objects.nonNull(cmd.getUpdatedAt())
        ));
    }

    @Test
    void givenAValidCommandWithDescription_whenCallCreatePermission_shouldReturnPermissionId() {
        // given
        final var aName = "create-role";
        final var aDescription = "Create role permission";

        final var aCommnad = CreatePermissionCommand.with(aName, aDescription);

        // when
        Mockito.when(permissionGateway.existsByName(Mockito.anyString())).thenReturn(false);
        Mockito.when(permissionGateway.create(Mockito.any())).thenAnswer(returnsFirstArg());

        final var aResult = useCase.execute(aCommnad).getRight();

        // then
        Assertions.assertNotNull(aResult);
        Assertions.assertNotNull(aResult.id());

        Mockito.verify(permissionGateway, Mockito.times(1)).existsByName(Mockito.anyString());
        Mockito.verify(permissionGateway, Mockito.times(1)).create(argThat(cmd ->
                Objects.equals(aName, cmd.getName()) &&
                        Objects.equals(aDescription, cmd.getDescription()) &&
                        Objects.nonNull(cmd.getId()) &&
                        Objects.nonNull(cmd.getCreatedAt()) &&
                        Objects.nonNull(cmd.getUpdatedAt())
        ));
    }

    @Test
    void givenAnExistingPermissionName_whenCallCreatePermission_shouldReturnDomainException() {
        // given
        final var aName = "create-role";
        final var aDescription = "Create role permission";
        final var expectedErrorMessage = "Permission already exists";
        final var expectedErrorCount = 1;

        final var aCommnad = CreatePermissionCommand.with(aName, aDescription);

        // when
        Mockito.when(permissionGateway.existsByName(Mockito.anyString())).thenReturn(true);

        final var aResult = useCase.execute(aCommnad).getLeft();

        // then
        Assertions.assertEquals(expectedErrorMessage, aResult.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aResult.getErrors().size());

        Mockito.verify(permissionGateway, Mockito.times(1)).existsByName(Mockito.anyString());
        Mockito.verify(permissionGateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    void givenAnInvalidNameNull_whenCallCreatePermission_shouldReturnDomainException() {
        // given
        final String aName = null;
        final var aDescription = "Create role permission";
        final var expectedErrorMessage = "'name' should not be null or blank";
        final var expectedErrorCount = 1;

        final var aCommnad = CreatePermissionCommand.with(aName, aDescription);

        // when
        final var aResult = useCase.execute(aCommnad).getLeft();

        // then
        Assertions.assertEquals(expectedErrorMessage, aResult.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aResult.getErrors().size());

        Mockito.verify(permissionGateway, Mockito.times(1)).existsByName(Mockito.any());
        Mockito.verify(permissionGateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    void givenAnInvalidNameBlank_whenCallCreatePermission_shouldReturnDomainException() {
        // given
        final var aName = "";
        final var aDescription = "Create role permission";
        final var expectedErrorMessage = "'name' should not be null or blank";
        final var expectedErrorCount = 1;

        final var aCommnad = CreatePermissionCommand.with(aName, aDescription);

        // when
        Mockito.when(permissionGateway.existsByName(Mockito.anyString())).thenReturn(false);

        final var aResult = useCase.execute(aCommnad).getLeft();

        // then
        Assertions.assertEquals(expectedErrorMessage, aResult.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aResult.getErrors().size());

        Mockito.verify(permissionGateway, Mockito.times(1)).existsByName(Mockito.anyString());
        Mockito.verify(permissionGateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    void givenAnInvalidNameLengthLessThan3_whenCallCreatePermission_shouldReturnDomainException() {
        // given
        final var aName = "cr ";
        final var aDescription = "Create role permission";
        final var expectedErrorMessage = "'name' must be between 3 and 50 characters";
        final var expectedErrorCount = 1;

        final var aCommnad = CreatePermissionCommand.with(aName, aDescription);

        // when
        Mockito.when(permissionGateway.existsByName(Mockito.anyString())).thenReturn(false);

        final var aResult = useCase.execute(aCommnad).getLeft();

        // then
        Assertions.assertEquals(expectedErrorMessage, aResult.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aResult.getErrors().size());

        Mockito.verify(permissionGateway, Mockito.times(1)).existsByName(Mockito.anyString());
        Mockito.verify(permissionGateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    void givenAnInvalidNameLengthMoreThan50_whenCallCreatePermission_shouldReturnDomainException() {
        // given
        final var aName = RandomStringUtils.generateValue(51);
        final String aDescription = null;
        final var expectedErrorMessage = "'name' must be between 3 and 50 characters";
        final var expectedErrorCount = 1;

        final var aCommnad = CreatePermissionCommand.with(aName, aDescription);

        // when
        Mockito.when(permissionGateway.existsByName(Mockito.anyString())).thenReturn(false);

        final var aResult = useCase.execute(aCommnad).getLeft();

        // then
        Assertions.assertEquals(expectedErrorMessage, aResult.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aResult.getErrors().size());

        Mockito.verify(permissionGateway, Mockito.times(1)).existsByName(Mockito.anyString());
        Mockito.verify(permissionGateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    void givenAnInvalidDescriptionLengthMoreThan255_whenCallCreatePermission_shouldReturnDomainException() {
        // given
        // given
        final var aName = "create-role";
        final var aDescription = RandomStringUtils.generateValue(256);
        final var expectedErrorMessage = "'description' must be between 0 and 255 characters";
        final var expectedErrorCount = 1;

        final var aCommnad = CreatePermissionCommand.with(aName, aDescription);

        // when
        Mockito.when(permissionGateway.existsByName(Mockito.anyString())).thenReturn(false);

        final var aResult = useCase.execute(aCommnad).getLeft();

        // then
        Assertions.assertEquals(expectedErrorMessage, aResult.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aResult.getErrors().size());

        Mockito.verify(permissionGateway, Mockito.times(1)).existsByName(Mockito.anyString());
        Mockito.verify(permissionGateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    void givenAValidStringId_whenCallCreatePermissionOutputFrom_shouldReturnCreatePermissionOutputInstance() {
        final var aId = IdUtils.generate();

        final var aResult = CreatePermissionOutput.from(aId);

        Assertions.assertNotNull(aResult);
        Assertions.assertEquals(aId, aResult.id());
        Assertions.assertInstanceOf(CreatePermissionOutput.class, aResult);
    }
}
