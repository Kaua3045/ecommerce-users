package com.kaua.ecommerce.users.application.usecases.role.create;

import com.kaua.ecommerce.users.application.gateways.PermissionGateway;
import com.kaua.ecommerce.users.application.gateways.RoleGateway;
import com.kaua.ecommerce.users.domain.exceptions.DomainException;
import com.kaua.ecommerce.users.domain.permissions.Permission;
import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.domain.roles.RoleTypes;
import com.kaua.ecommerce.users.domain.utils.IdUtils;
import com.kaua.ecommerce.users.domain.utils.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.argThat;

@ExtendWith(MockitoExtension.class)
public class CreateRoleUseCaseTest {

    @Mock
    private RoleGateway roleGateway;

    @Mock
    private PermissionGateway permissionGateway;

    @InjectMocks
    private DefaultCreateRoleUseCase useCase;

    @Test
    void givenAValidCommandWithNullDescriptionAndNullPermissions_whenCallCreateRole_shouldReturnRoleId() {
        // given
        final var aName = "ceo";
        final String aDescription = null;
        final var aRoleType = "employees";
        final var aIsDefault = true;
        final Set<String> aPermissions = null;

        final var aCommnad = CreateRoleCommand.with(aName, aDescription, aRoleType, aIsDefault, aPermissions);

        // when
        Mockito.when(roleGateway.existsByName(Mockito.anyString())).thenReturn(false);
        Mockito.when(roleGateway.create(Mockito.any())).thenAnswer(returnsFirstArg());

        final var aResult = useCase.execute(aCommnad).getRight();

        // then
        Assertions.assertNotNull(aResult);
        Assertions.assertNotNull(aResult.id());

        Mockito.verify(roleGateway, Mockito.times(1)).existsByName(Mockito.anyString());
        Mockito.verify(roleGateway, Mockito.times(1)).create(argThat(cmd ->
                Objects.equals(aName, cmd.getName()) &&
                Objects.equals(aDescription, cmd.getDescription()) &&
                Objects.equals(aRoleType, cmd.getRoleType().name().toLowerCase()) &&
                Objects.nonNull(cmd.getId()) &&
                Objects.nonNull(cmd.getCreatedAt()) &&
                Objects.nonNull(cmd.getUpdatedAt()) &&
                        Objects.equals(aIsDefault, cmd.isDefault()) &&
                        Objects.equals(0, cmd.getPermissions().size())
        ));
    }

    @Test
    void givenAValidCommandWithNullDescriptionAndEmptyPermissions_whenCallCreateRole_shouldReturnRoleId() {
        // given
        final var aName = "ceo";
        final String aDescription = null;
        final var aRoleType = "employees";
        final var aIsDefault = true;
        final Set<String> aPermissions = Collections.emptySet();

        final var aCommnad = CreateRoleCommand.with(aName, aDescription, aRoleType, aIsDefault, aPermissions);

        // when
        Mockito.when(roleGateway.existsByName(Mockito.anyString())).thenReturn(false);
        Mockito.when(roleGateway.create(Mockito.any())).thenAnswer(returnsFirstArg());

        final var aResult = useCase.execute(aCommnad).getRight();

        // then
        Assertions.assertNotNull(aResult);
        Assertions.assertNotNull(aResult.id());

        Mockito.verify(roleGateway, Mockito.times(1)).existsByName(Mockito.anyString());
        Mockito.verify(roleGateway, Mockito.times(1)).create(argThat(cmd ->
                Objects.equals(aName, cmd.getName()) &&
                        Objects.equals(aDescription, cmd.getDescription()) &&
                        Objects.equals(aRoleType, cmd.getRoleType().name().toLowerCase()) &&
                        Objects.nonNull(cmd.getId()) &&
                        Objects.nonNull(cmd.getCreatedAt()) &&
                        Objects.nonNull(cmd.getUpdatedAt()) &&
                        Objects.equals(aIsDefault, cmd.isDefault()) &&
                        Objects.equals(0, cmd.getPermissions().size())
        ));
    }

    @Test
    void givenAValidCommandWithDescriptionAndPermissions_whenCallCreateRole_shouldReturnRoleId() {
        // given
        final var aPermissionOne = Permission.newPermission("one", "Permission One");
        final var aPermissionTwo = Permission.newPermission("two", null);

        final var aName = "ceo";
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = "employees";
        final var aIsDefault = false;
        final var aPermissions = Set.of(aPermissionOne.getId().getValue(), aPermissionTwo.getId().getValue());

        final var aCommnad = CreateRoleCommand.with(aName, aDescription, aRoleType, aIsDefault, aPermissions);

        // when
        Mockito.when(roleGateway.existsByName(Mockito.anyString())).thenReturn(false);
        Mockito.when(permissionGateway.findAllByIds(Mockito.any()))
                .thenReturn(Set.of(aPermissionOne, aPermissionTwo));
        Mockito.when(roleGateway.create(Mockito.any())).thenAnswer(returnsFirstArg());

        final var aResult = useCase.execute(aCommnad).getRight();

        // then
        Assertions.assertNotNull(aResult);
        Assertions.assertNotNull(aResult.id());

        Mockito.verify(roleGateway, Mockito.times(1)).existsByName(Mockito.anyString());
        Mockito.verify(permissionGateway, Mockito.times(1)).findAllByIds(Mockito.any());
        Mockito.verify(roleGateway, Mockito.times(1)).create(argThat(cmd ->
                Objects.equals(aName, cmd.getName()) &&
                        Objects.equals(aDescription, cmd.getDescription()) &&
                        Objects.equals(aRoleType, cmd.getRoleType().name().toLowerCase()) &&
                        Objects.nonNull(cmd.getId()) &&
                        Objects.nonNull(cmd.getCreatedAt()) &&
                        Objects.nonNull(cmd.getUpdatedAt()) &&
                        Objects.equals(aIsDefault, cmd.isDefault()) &&
                        Objects.equals(2, cmd.getPermissions().size()) &&
                        Objects.nonNull(cmd.getPermissions().stream().filter(permission -> permission.getPermissionName().equals(aPermissionOne.getName())).findFirst().get())
        ));
    }

    @Test
    void givenAnExistingRoleName_whenCallCreateRole_shouldReturnDomainException() {
        // given
        final var aName = "ceo";
        final var aDescription = RandomStringUtils.generateValue(36);
        final var aRoleType = "employees";
        final var aIsDefault = false;
        final var aPermissions = Set.<String>of();
        final var expectedErrorMessage = "Role already exists";
        final var expectedErrorCount = 1;

        final var aCommnad = CreateRoleCommand.with(aName, aDescription, aRoleType, aIsDefault, aPermissions);

        // when
        Mockito.when(roleGateway.existsByName(Mockito.anyString())).thenReturn(true);

        final var aResult = useCase.execute(aCommnad).getLeft();

        // then
        Assertions.assertEquals(expectedErrorMessage, aResult.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aResult.getErrors().size());

        Mockito.verify(roleGateway, Mockito.times(1)).existsByName(Mockito.anyString());
        Mockito.verify(permissionGateway, Mockito.times(0)).findAllByIds(Mockito.any());
        Mockito.verify(roleGateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    void givenAnIsDefaultTrueButExistingOtherDefaultRole_whenCallCreateRole_shouldReturnDomainException() {
        // given
        final var aName = "ceo";
        final var aDescription = RandomStringUtils.generateValue(36);
        final var aRoleType = "employees";
        final var aIsDefault = true;
        final var aPermissions = Set.<String>of();
        final var aDefaultRole = Role.newRole("User", null, RoleTypes.COMMON, true);
        final var expectedErrorMessage = "Default role already exists";
        final var expectedErrorCount = 1;

        final var aCommnad = CreateRoleCommand.with(aName, aDescription, aRoleType, aIsDefault, aPermissions);

        // when
        Mockito.when(roleGateway.existsByName(Mockito.anyString())).thenReturn(false);
        Mockito.when(roleGateway.findDefaultRole()).thenReturn(Optional.of(aDefaultRole));

        final var aResult = useCase.execute(aCommnad).getLeft();

        // then
        Assertions.assertEquals(expectedErrorMessage, aResult.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aResult.getErrors().size());

        Mockito.verify(roleGateway, Mockito.times(1)).existsByName(Mockito.anyString());
        Mockito.verify(roleGateway, Mockito.times(1)).findDefaultRole();
        Mockito.verify(permissionGateway, Mockito.times(0)).findAllByIds(Mockito.any());
        Mockito.verify(roleGateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    void givenAnInvalidNameNull_whenCallCreateRole_shouldReturnDomainException() {
        // given
        final String aName = null;
        final var aDescription = RandomStringUtils.generateValue(100);
        final var aRoleType = "employees";
        final var aIsDefault = false;
        final var aPermissions = Set.<String>of();
        final var expectedErrorMessage = "'name' should not be null or blank";
        final var expectedErrorCount = 1;

        final var aCommnad = CreateRoleCommand.with(aName, aDescription, aRoleType, aIsDefault, aPermissions);

        // when
        final var aResult = useCase.execute(aCommnad).getLeft();

        // then
        Assertions.assertEquals(expectedErrorMessage, aResult.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aResult.getErrors().size());

        Mockito.verify(roleGateway, Mockito.times(1)).existsByName(Mockito.any());
        Mockito.verify(permissionGateway, Mockito.times(0)).findAllByIds(Mockito.any());
        Mockito.verify(roleGateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    void givenAnInvalidNameBlank_whenCallCreateRole_shouldReturnDomainException() {
        // given
        final var aName = "";
        final var aDescription = RandomStringUtils.generateValue(100);
        final var aRoleType = "employees";
        final var aIsDefault = false;
        final var aPermissions = Set.<String>of();
        final var expectedErrorMessage = "'name' should not be null or blank";
        final var expectedErrorCount = 1;

        final var aCommnad = CreateRoleCommand.with(aName, aDescription, aRoleType, aIsDefault, aPermissions);

        // when
        final var aResult = useCase.execute(aCommnad).getLeft();

        // then
        Assertions.assertEquals(expectedErrorMessage, aResult.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aResult.getErrors().size());

        Mockito.verify(roleGateway, Mockito.times(1)).existsByName(Mockito.anyString());
        Mockito.verify(permissionGateway, Mockito.times(0)).findAllByIds(Mockito.any());
        Mockito.verify(roleGateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    void givenAnInvalidNameLengthLessThan3_whenCallCreateRole_shouldReturnDomainException() {
        // given
        final var aName = "ce ";
        final String aDescription = null;
        final var aRoleType = "employees";
        final var aIsDefault = false;
        final var aPermissions = Set.<String>of();
        final var expectedErrorMessage = "'name' must be between 3 and 50 characters";
        final var expectedErrorCount = 1;

        final var aCommnad = CreateRoleCommand.with(aName, aDescription, aRoleType, aIsDefault, aPermissions);

        // when
        Mockito.when(roleGateway.existsByName(Mockito.anyString())).thenReturn(false);
        final var aResult = useCase.execute(aCommnad).getLeft();

        // then
        Assertions.assertEquals(expectedErrorMessage, aResult.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aResult.getErrors().size());

        Mockito.verify(roleGateway, Mockito.times(1)).existsByName(Mockito.anyString());
        Mockito.verify(permissionGateway, Mockito.times(0)).findAllByIds(Mockito.any());
        Mockito.verify(roleGateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    void givenAnInvalidNameLengthMoreThan50_whenCallCreateRole_shouldReturnDomainException() {
        // given
        final var aName = RandomStringUtils.generateValue(52);
        final String aDescription = null;
        final var aRoleType = "employees";
        final var aIsDefault = false;
        final var aPermissions = Set.<String>of();
        final var expectedErrorMessage = "'name' must be between 3 and 50 characters";
        final var expectedErrorCount = 1;

        final var aCommnad = CreateRoleCommand.with(aName, aDescription, aRoleType, aIsDefault, aPermissions);

        // when
        Mockito.when(roleGateway.existsByName(Mockito.anyString())).thenReturn(false);
        final var aResult = useCase.execute(aCommnad).getLeft();

        // then
        Assertions.assertEquals(expectedErrorMessage, aResult.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aResult.getErrors().size());

        Mockito.verify(roleGateway, Mockito.times(1)).existsByName(Mockito.anyString());
        Mockito.verify(permissionGateway, Mockito.times(0)).findAllByIds(Mockito.any());
        Mockito.verify(roleGateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    void givenAnInvalidDescriptionLengthMoreThan255_whenCallCreateRole_shouldReturnDomainException() {
        // given
        final var aName = "ceo";
        final var aDescription = RandomStringUtils.generateValue(256);
        final var aRoleType = "employees";
        final var aIsDefault = false;
        final var aPermissions = Set.<String>of();
        final var expectedErrorMessage = "'description' must be between 0 and 255 characters";
        final var expectedErrorCount = 1;

        final var aCommnad = CreateRoleCommand.with(aName, aDescription, aRoleType, aIsDefault, aPermissions);

        // when
        Mockito.when(roleGateway.existsByName(Mockito.anyString())).thenReturn(false);
        final var aResult = useCase.execute(aCommnad).getLeft();

        // then
        Assertions.assertEquals(expectedErrorMessage, aResult.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aResult.getErrors().size());

        Mockito.verify(roleGateway, Mockito.times(1)).existsByName(Mockito.anyString());
        Mockito.verify(permissionGateway, Mockito.times(0)).findAllByIds(Mockito.any());
        Mockito.verify(roleGateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    void givenAnInvalidRoleTypeNull_whenCallCreateRole_shouldReturnDomainException() {
        // given
        final var aName = "ceo";
        final var aDescription = "Chief Executive Officer";
        final String aRoleType = null;
        final var aIsDefault = false;
        final var aPermissions = Set.<String>of();
        final var expectedErrorMessage = "RoleType not found, role types available: [COMMON, EMPLOYEES]";
        final var expectedErrorCount = 1;

        final var aCommnad = CreateRoleCommand.with(aName, aDescription, aRoleType, aIsDefault, aPermissions);

        // when
        Mockito.when(roleGateway.existsByName(Mockito.anyString())).thenReturn(false);

        final var aException = Assertions.assertThrows(DomainException.class,
                () -> useCase.execute(aCommnad));

        // then
        Assertions.assertEquals(expectedErrorMessage, aException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aException.getErrors().size());

        Mockito.verify(roleGateway, Mockito.times(1)).existsByName(Mockito.anyString());
        Mockito.verify(permissionGateway, Mockito.times(0)).findAllByIds(Mockito.any());
        Mockito.verify(roleGateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    void givenAValidStringId_whenCallCreateRoleOutputFrom_shouldReturnCreateRoleOutputInstance() {
        final var aId = IdUtils.generate();

        final var aResult = CreateRoleOutput.from(aId);

        Assertions.assertNotNull(aResult);
        Assertions.assertEquals(aId, aResult.id());
        Assertions.assertInstanceOf(CreateRoleOutput.class, aResult);
    }

    @Test
    void givenAValidCommandWithDescriptionAndTwoPermissionsIdsButOneNotValid_whenCallCreateRole_shouldReturnRoleWithExistingsPermissions() {
        // given
        final var aPermissionOne = Permission.newPermission("one", "Permission One");
        final var aPermissionTwo = Permission.newPermission("two", null);

        final var aName = "ceo";
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = "employees";
        final var aIsDefault = false;
        final var aPermissions = Set.of(aPermissionOne.getId().getValue(), aPermissionTwo.getId().getValue());

        final var aCommnad = CreateRoleCommand.with(aName, aDescription, aRoleType, aIsDefault, aPermissions);

        // when
        Mockito.when(roleGateway.existsByName(Mockito.anyString())).thenReturn(false);
        Mockito.when(permissionGateway.findAllByIds(Mockito.any()))
                .thenReturn(Set.of(aPermissionOne));
        Mockito.when(roleGateway.create(Mockito.any())).thenAnswer(returnsFirstArg());

        final var aResult = useCase.execute(aCommnad).getRight();

        // then
        Assertions.assertNotNull(aResult);
        Assertions.assertNotNull(aResult.id());

        Mockito.verify(roleGateway, Mockito.times(1)).existsByName(Mockito.anyString());
        Mockito.verify(permissionGateway, Mockito.times(1)).findAllByIds(Mockito.any());
        Mockito.verify(roleGateway, Mockito.times(1)).create(argThat(cmd ->
                Objects.equals(aName, cmd.getName()) &&
                        Objects.equals(aDescription, cmd.getDescription()) &&
                        Objects.equals(aRoleType, cmd.getRoleType().name().toLowerCase()) &&
                        Objects.nonNull(cmd.getId()) &&
                        Objects.nonNull(cmd.getCreatedAt()) &&
                        Objects.nonNull(cmd.getUpdatedAt()) &&
                        Objects.equals(aIsDefault, cmd.isDefault()) &&
                        Objects.equals(1, cmd.getPermissions().size())
        ));
    }
}
