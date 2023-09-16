package com.kaua.ecommerce.users.application.role.update;

import com.kaua.ecommerce.users.application.gateways.RoleGateway;
import com.kaua.ecommerce.users.domain.exceptions.DomainException;
import com.kaua.ecommerce.users.domain.exceptions.NotFoundException;
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

import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.argThat;

@ExtendWith(MockitoExtension.class)
public class UpdateRoleUseCaseTest {

    @Mock
    private RoleGateway roleGateway;

    @InjectMocks
    private DefaultUpdateRoleUseCase useCase;

    @Test
    void givenAValidCommandWithNullDescription_whenCallUpdateRole_shouldReturnRoleId() {
        // given
        final var aName = "ceo";
        final String aDescription = null;
        final var aRoleType = "employees";
        final var aIsDefault = false;
        final var aRole = Role.newRole("User", "Common User", RoleTypes.COMMON, true);

        final var aCommnad = UpdateRoleCommand.with(
                aRole.getId().getValue(),
                aName,
                aDescription,
                aRoleType,
                aIsDefault
        );

        // when
        Mockito.when(roleGateway.findById(Mockito.anyString())).thenReturn(Optional.of(aRole));
        Mockito.when(roleGateway.update(Mockito.any())).thenAnswer(returnsFirstArg());

        final var aResult = useCase.execute(aCommnad).getRight();

        // then
        Assertions.assertNotNull(aResult);
        Assertions.assertNotNull(aResult.id());

        Mockito.verify(roleGateway, Mockito.times(1)).findById(Mockito.anyString());
        Mockito.verify(roleGateway, Mockito.times(1)).update(argThat(cmd ->
                Objects.equals(aName, cmd.getName()) &&
                Objects.equals(aDescription, cmd.getDescription()) &&
                Objects.equals(aRoleType, cmd.getRoleType().name().toLowerCase()) &&
                Objects.nonNull(cmd.getId()) &&
                Objects.nonNull(cmd.getCreatedAt()) &&
                Objects.nonNull(cmd.getUpdatedAt()) &&
                Objects.equals(aIsDefault, cmd.isDefault())
        ));
    }

    @Test
    void givenAValidCommandWithDescription_whenCallUpdateRole_shouldReturnRoleId() {
        // given
        final var aName = "ceo";
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = "employees";
        final var aIsDefault = false;
        final var aRole = Role.newRole("User", "Common User", RoleTypes.COMMON, true);

        final var aCommnad = UpdateRoleCommand.with(
                aRole.getId().getValue(),
                aName,
                aDescription,
                aRoleType,
                aIsDefault
        );

        // when
        Mockito.when(roleGateway.findById(Mockito.anyString())).thenReturn(Optional.of(aRole));
        Mockito.when(roleGateway.update(Mockito.any())).thenAnswer(returnsFirstArg());

        final var aResult = useCase.execute(aCommnad).getRight();

        // then
        Assertions.assertNotNull(aResult);
        Assertions.assertNotNull(aResult.id());

        Mockito.verify(roleGateway, Mockito.times(1)).findById(Mockito.anyString());
        Mockito.verify(roleGateway, Mockito.times(1)).update(argThat(cmd ->
                Objects.equals(aName, cmd.getName()) &&
                        Objects.equals(aDescription, cmd.getDescription()) &&
                        Objects.equals(aRoleType, cmd.getRoleType().name().toLowerCase()) &&
                        Objects.nonNull(cmd.getId()) &&
                        Objects.nonNull(cmd.getCreatedAt()) &&
                        Objects.nonNull(cmd.getUpdatedAt()) &&
                        Objects.equals(aIsDefault, cmd.isDefault())
        ));
    }

    @Test
    void givenAnInvalidRoleId_whenCallUpdateRole_shouldReturnNotFoundException() {
        // given
        final var aId = "123";
        final var aName = "ceo";
        final var aDescription = RandomStringUtils.generateValue(36);
        final var aRoleType = "employees";
        final var aIsDefault = false;

        final var expectedErrorMessage = "Role with id 123 was not found";

        final var aCommnad = UpdateRoleCommand.with(aId, aName, aDescription, aRoleType, aIsDefault);

        // when
        Mockito.when(roleGateway.findById(Mockito.anyString())).thenReturn(Optional.empty());

        final var aResult = Assertions.assertThrows(NotFoundException.class,
                () -> useCase.execute(aCommnad));

        // then
        Assertions.assertEquals(expectedErrorMessage, aResult.getMessage());

        Mockito.verify(roleGateway, Mockito.times(1)).findById(Mockito.anyString());
        Mockito.verify(roleGateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    void givenAnIsDefaultTrueButExistingDefaultRole_whenCallUpdateRole_shouldReturnDomainException() {
        // given
        final var aId = "123";
        final var aName = "ceo";
        final var aDescription = RandomStringUtils.generateValue(36);
        final var aRoleType = "employees";
        final var aIsDefault = true;
        final var aRole = Role.newRole("User", "Common User", RoleTypes.COMMON, true);

        final var expectedErrorMessage = "Default role already exists";

        final var aCommnad = UpdateRoleCommand.with(aId, aName, aDescription, aRoleType, aIsDefault);

        // when
        Mockito.when(roleGateway.findDefaultRole()).thenReturn(Optional.of(aRole));

        final var aResult = useCase.execute(aCommnad).getLeft();

        // then
        Assertions.assertEquals(expectedErrorMessage, aResult.getErrors().get(0).message());

        Mockito.verify(roleGateway, Mockito.times(1)).findDefaultRole();
        Mockito.verify(roleGateway, Mockito.times(0)).findById(Mockito.anyString());
        Mockito.verify(roleGateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    void givenAnInvalidNameNull_whenCallUpdateRole_shouldReturnDomainException() {
        // given
        final String aName = null;
        final var aDescription = RandomStringUtils.generateValue(100);
        final var aRoleType = "employees";
        final var aIsDefault = false;
        final var aRole = Role.newRole("User", "Common User", RoleTypes.COMMON, true);

        final var expectedErrorMessage = "'name' should not be null or blank";
        final var expectedErrorCount = 1;

        final var aCommnad = UpdateRoleCommand.with(
                aRole.getId().getValue(),
                aName,
                aDescription,
                aRoleType,
                aIsDefault
        );

        // when
        Mockito.when(roleGateway.findById(Mockito.anyString())).thenReturn(Optional.of(aRole));

        final var aResult = useCase.execute(aCommnad).getLeft();

        // then
        Assertions.assertEquals(expectedErrorMessage, aResult.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aResult.getErrors().size());

        Mockito.verify(roleGateway, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(roleGateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    void givenAnInvalidNameBlank_whenCallUpdateRole_shouldReturnDomainException() {
        // given
        final var aName = "";
        final var aDescription = RandomStringUtils.generateValue(100);
        final var aRoleType = "employees";
        final var aIsDefault = false;
        final var aRole = Role.newRole("User", "Common User", RoleTypes.COMMON, true);

        final var expectedErrorMessage = "'name' should not be null or blank";
        final var expectedErrorCount = 1;

        final var aCommnad = UpdateRoleCommand.with(
                aRole.getId().getValue(),
                aName,
                aDescription,
                aRoleType,
                aIsDefault
        );

        // when
        Mockito.when(roleGateway.findById(Mockito.anyString())).thenReturn(Optional.of(aRole));

        final var aResult = useCase.execute(aCommnad).getLeft();

        // then
        Assertions.assertEquals(expectedErrorMessage, aResult.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aResult.getErrors().size());

        Mockito.verify(roleGateway, Mockito.times(1)).findById(Mockito.anyString());
        Mockito.verify(roleGateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    void givenAnInvalidNameLengthLessThan3_whenCallUpdateRole_shouldReturnDomainException() {
        // given
        final var aName = "ce ";
        final String aDescription = null;
        final var aRoleType = "employees";
        final var aIsDefault = false;
        final var aRole = Role.newRole("User", "Common User", RoleTypes.COMMON, true);

        final var expectedErrorMessage = "'name' must be between 3 and 50 characters";
        final var expectedErrorCount = 1;

        final var aCommnad = UpdateRoleCommand.with(
                aRole.getId().getValue(),
                aName,
                aDescription,
                aRoleType,
                aIsDefault
        );

        // when
        Mockito.when(roleGateway.findById(Mockito.anyString())).thenReturn(Optional.of(aRole));

        final var aResult = useCase.execute(aCommnad).getLeft();

        // then
        Assertions.assertEquals(expectedErrorMessage, aResult.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aResult.getErrors().size());

        Mockito.verify(roleGateway, Mockito.times(1)).findById(Mockito.anyString());
        Mockito.verify(roleGateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    void givenAnInvalidNameLengthMoreThan50_whenCallUpdateRole_shouldReturnDomainException() {
        // given
        final var aName = RandomStringUtils.generateValue(52);
        final String aDescription = null;
        final var aRoleType = "employees";
        final var aIsDefault = false;
        final var aRole = Role.newRole("User", "Common User", RoleTypes.COMMON, true);

        final var expectedErrorMessage = "'name' must be between 3 and 50 characters";
        final var expectedErrorCount = 1;

        final var aCommnad = UpdateRoleCommand.with(
                aRole.getId().getValue(),
                aName,
                aDescription,
                aRoleType,
                aIsDefault
        );

        // when
        Mockito.when(roleGateway.findById(Mockito.anyString())).thenReturn(Optional.of(aRole));

        final var aResult = useCase.execute(aCommnad).getLeft();

        // then
        Assertions.assertEquals(expectedErrorMessage, aResult.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aResult.getErrors().size());

        Mockito.verify(roleGateway, Mockito.times(1)).findById(Mockito.anyString());
        Mockito.verify(roleGateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    void givenAnInvalidDescriptionLengthMoreThan255_whenCallUpdateRole_shouldReturnDomainException() {
        // given
        final var aName = "ceo";
        final var aDescription = RandomStringUtils.generateValue(256);
        final var aRoleType = "employees";
        final var aIsDefault = false;
        final var aRole = Role.newRole("User", "Common User", RoleTypes.COMMON, true);

        final var expectedErrorMessage = "'description' must be between 0 and 255 characters";
        final var expectedErrorCount = 1;

        final var aCommnad = UpdateRoleCommand.with(
                aRole.getId().getValue(),
                aName,
                aDescription,
                aRoleType,
                aIsDefault
        );

        // when
        Mockito.when(roleGateway.findById(Mockito.anyString())).thenReturn(Optional.of(aRole));

        final var aResult = useCase.execute(aCommnad).getLeft();

        // then
        Assertions.assertEquals(expectedErrorMessage, aResult.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aResult.getErrors().size());

        Mockito.verify(roleGateway, Mockito.times(1)).findById(Mockito.anyString());
        Mockito.verify(roleGateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    void givenAnInvalidRoleTypeNull_whenCallUpdateRole_shouldReturnDomainException() {
        // given
        final var aName = "ceo";
        final var aDescription = "Chief Executive Officer";
        final String aRoleType = null;
        final var aIsDefault = false;
        final var aRole = Role.newRole("User", "Common User", RoleTypes.COMMON, aIsDefault);

        final var expectedErrorMessage = "RoleType not found, role types available: [COMMON, EMPLOYEES]";
        final var expectedErrorCount = 1;

        final var aCommnad = UpdateRoleCommand.with(
                aRole.getId().getValue(),
                aName,
                aDescription,
                aRoleType,
                aIsDefault
        );

        // when
        Mockito.when(roleGateway.findById(Mockito.anyString())).thenReturn(Optional.of(aRole));

        final var aException = Assertions.assertThrows(DomainException.class,
                () -> useCase.execute(aCommnad));

        // then
        Assertions.assertEquals(expectedErrorMessage, aException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aException.getErrors().size());

        Mockito.verify(roleGateway, Mockito.times(1)).findById(Mockito.anyString());
        Mockito.verify(roleGateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    void givenAValidStringId_whenCallUpdateRoleOutputFrom_shouldReturnUpdateRoleOutputInstance() {
        final var aId = IdUtils.generate();

        final var aResult = UpdateRoleOutput.from(aId);

        Assertions.assertNotNull(aResult);
        Assertions.assertEquals(aId, aResult.id());
        Assertions.assertInstanceOf(UpdateRoleOutput.class, aResult);
    }
}
