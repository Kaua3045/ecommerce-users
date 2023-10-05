package com.kaua.ecommerce.users.domain.roles;

import com.kaua.ecommerce.users.domain.TestValidationHandler;
import com.kaua.ecommerce.users.domain.permissions.PermissionID;
import com.kaua.ecommerce.users.domain.utils.IdUtils;
import com.kaua.ecommerce.users.domain.utils.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class RoleTest {

    @Test
    void givenAValidValuesWithDescription_whenCallNewRole_thenRoleShouldBeCreated() {
        final var aName = "ceo";
        final var aDescription = "Ceo of the application";
        final var aRoleType = RoleTypes.EMPLOYEES;
        final var aIsDefault = false;
        final var aPermissionsCount = 0;

        final var aRole = Role.newRole(aName, aDescription, aRoleType, aIsDefault);

        Assertions.assertNotNull(aRole.getId());
        Assertions.assertEquals(aName, aRole.getName());
        Assertions.assertEquals(aDescription, aRole.getDescription());
        Assertions.assertEquals(aRoleType, aRole.getRoleType());
        Assertions.assertFalse(aRole.isDefault());
        Assertions.assertEquals(aPermissionsCount, aRole.getPermissions().size());
        Assertions.assertNotNull(aRole.getCreatedAt());
        Assertions.assertNotNull(aRole.getUpdatedAt());

        Assertions.assertDoesNotThrow(() -> aRole.validate(new TestValidationHandler()));

        Assertions.assertEquals(aRole.getId(), aRole.getId());
        Assertions.assertNotEquals(RoleID.unique(), aRole.getId());
        Assertions.assertNotNull(aRole.getId());
        Assertions.assertFalse(aRole.getId().equals(null));
        Assertions.assertFalse(aRole.getId().equals(new Object()));
        Assertions.assertNotEquals(1201212, aRole.getId().hashCode());
    }

    @Test
    void givenAValidValuesWithNullDescription_whenCallNewRole_thenRoleShouldBeCreated() {
        final var aName = "ceo";
        final String aDescription = null;
        final var aRoleType = RoleTypes.EMPLOYEES;
        final var aIsDefault = true;
        final var aPermissionsCount = 0;

        final var aRole = Role.newRole(aName, aDescription, aRoleType, aIsDefault);

        Assertions.assertNotNull(aRole.getId());
        Assertions.assertEquals(aName, aRole.getName());
        Assertions.assertEquals(aDescription, aRole.getDescription());
        Assertions.assertEquals(aRoleType, aRole.getRoleType());
        Assertions.assertTrue(aRole.isDefault());
        Assertions.assertEquals(aPermissionsCount, aRole.getPermissions().size());
        Assertions.assertNotNull(aRole.getCreatedAt());
        Assertions.assertNotNull(aRole.getUpdatedAt());

        final var aTestValidationHandler = new TestValidationHandler();
        final var aRoleValidator = new RoleValidator(aRole, aTestValidationHandler);

        aRoleValidator.validate();

        Assertions.assertEquals(0, aTestValidationHandler.getErrors().size());
    }

    @Test
    void givenAnInvalidNameNull_whenCallNewRole_shouldReturnADomainException() {
        final String aName = null;
        final var aDescription = "Ceo of the application";
        final var aRoleType = RoleTypes.EMPLOYEES;
        final var aIsDefault = false;

        final var expectedErrorMessage = "'name' should not be null or blank";
        final var expectedErrorCount = 1;

        final var aRole = Role.newRole(aName, aDescription, aRoleType, aIsDefault);

        final var aTestValidationHandler = new TestValidationHandler();
        final var aRoleValidator = new RoleValidator(aRole, aTestValidationHandler);

        aRoleValidator.validate();

        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aTestValidationHandler.getErrors().size());
    }

    @Test
    void givenAnInvalidNameBlank_whenCallNewRole_shouldReturnADomainException() {
        final var aName = "";
        final var aDescription = "Ceo of the application";
        final var aRoleType = RoleTypes.EMPLOYEES;
        final var aIsDefault = false;

        final var expectedErrorMessage = "'name' should not be null or blank";
        final var expectedErrorCount = 1;

        final var aRole = Role.newRole(aName, aDescription, aRoleType, aIsDefault);

        final var aTestValidationHandler = new TestValidationHandler();
        final var aRoleValidator = new RoleValidator(aRole, aTestValidationHandler);

        aRoleValidator.validate();

        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aTestValidationHandler.getErrors().size());
    }

    @Test
    void givenAnInvalidNameLengthLessThan3_whenCallNewRole_shouldReturnADomainException() {
        final var aName = "ce ";
        final var aDescription = "Ceo of the application";
        final var aRoleType = RoleTypes.EMPLOYEES;
        final var aIsDefault = false;

        final var expectedErrorMessage = "'name' must be between 3 and 50 characters";
        final var expectedErrorCount = 1;

        final var aRole = Role.newRole(aName, aDescription, aRoleType, aIsDefault);

        final var aTestValidationHandler = new TestValidationHandler();
        final var aRoleValidator = new RoleValidator(aRole, aTestValidationHandler);

        aRoleValidator.validate();

        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aTestValidationHandler.getErrors().size());
    }

    @Test
    void givenAnInvalidNameLengthMoreThan50_whenCallNewRole_shouldReturnADomainException() {
        final var aName = RandomStringUtils.generateValue(51);
        final var aDescription = "Ceo of the application";
        final var aRoleType = RoleTypes.EMPLOYEES;
        final var aIsDefault = false;

        final var expectedErrorMessage = "'name' must be between 3 and 50 characters";
        final var expectedErrorCount = 1;

        final var aRole = Role.newRole(aName, aDescription, aRoleType, aIsDefault);

        final var aTestValidationHandler = new TestValidationHandler();
        final var aRoleValidator = new RoleValidator(aRole, aTestValidationHandler);

        aRoleValidator.validate();

        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aTestValidationHandler.getErrors().size());
    }

    @Test
    void givenAnInvalidDescriptionLengthMoreThan255_whenCallNewRole_shouldReturnADomainException() {
        final var aName = "ceo";
        final var aDescription = RandomStringUtils.generateValue(256);
        final var aRoleType = RoleTypes.EMPLOYEES;
        final var aIsDefault = false;

        final var expectedErrorMessage = "'description' must be between 0 and 255 characters";
        final var expectedErrorCount = 1;

        final var aRole = Role.newRole(aName, aDescription, aRoleType, aIsDefault);

        final var aTestValidationHandler = new TestValidationHandler();
        final var aRoleValidator = new RoleValidator(aRole, aTestValidationHandler);

        aRoleValidator.validate();

        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aTestValidationHandler.getErrors().size());
    }

    @Test
    void givenAnInvalidRoleTypeNull_whenCallNewRole_shouldReturnADomainException() {
        final var aName = "ceo";
        final String aDescription = null;
        final RoleTypes aRoleType = null;
        final var aIsDefault = false;

        final var expectedErrorMessage = "'roleType' must not be null";
        final var expectedErrorCount = 1;

        final var aRole = Role.newRole(aName, aDescription, aRoleType, aIsDefault);

        final var aTestValidationHandler = new TestValidationHandler();
        final var aRoleValidator = new RoleValidator(aRole, aTestValidationHandler);

        aRoleValidator.validate();

        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aTestValidationHandler.getErrors().size());
    }

    @Test
    void givenAValidIdString_whenCallRoleIdFromString_shouldReturnARoleId() {
        final var aId = IdUtils.generate();
        final var aRoleId = RoleID.from(aId);

        Assertions.assertNotNull(aRoleId);
        Assertions.assertEquals(aId, aRoleId.getValue());
    }

    @Test
    void givenAValidRoleTypeString_whenCallRoleTypesOf_shouldReturnARoleType() {
        final var aRoleType = RoleTypes.of("common");

        Assertions.assertNotNull(aRoleType);
        Assertions.assertEquals(RoleTypes.COMMON, aRoleType.get());
    }

    @Test
    void givenValidValues_whenCalledWithInRole_shouldReturnAnRoleObjectWithDataEqualToWhatWasPassed() {
        final var aName = "ceo";
        final var aDescription = "Ceo of the application";
        final var aRoleType = RoleTypes.EMPLOYEES;
        final var aIsDefault = false;

        final var aRole = Role.newRole(aName, aDescription, aRoleType, aIsDefault);

        final var aRoleCloned = Role.with(
                aRole.getId().getValue(),
                aRole.getName(),
                aRole.getDescription(),
                aRole.getRoleType(),
                aRole.isDefault(),
                aRole.getPermissions(),
                aRole.getCreatedAt(),
                aRole.getUpdatedAt()
        );

        // then
        Assertions.assertEquals(aRole.getId().getValue(), aRoleCloned.getId().getValue());
        Assertions.assertEquals(aRole.getName(), aRoleCloned.getName());
        Assertions.assertEquals(aRole.getDescription(), aRoleCloned.getDescription());
        Assertions.assertEquals(aRole.getRoleType(), aRoleCloned.getRoleType());
        Assertions.assertEquals(aRole.isDefault(), aRoleCloned.isDefault());
        Assertions.assertEquals(aRole.getPermissions(), aRoleCloned.getPermissions());
        Assertions.assertEquals(aRole.getCreatedAt(), aRoleCloned.getCreatedAt());
        Assertions.assertEquals(aRole.getUpdatedAt(), aRoleCloned.getUpdatedAt());
    }

    @Test
    void givenAValidValuesWithDescriptionAndPermissions_whenCallUpdateRole_shouldReturnAnRoleUpdated() {
        final var aName = "ceo";
        final var aDescription = "Ceo of the application";
        final var aRoleType = RoleTypes.EMPLOYEES;
        final var aIsDefault = true;
        final var aPermissions = List.of(
                RolePermission.newRolePermission(
                        PermissionID.unique(),
                        "permission 1"
                ),
                RolePermission.newRolePermission(
                        PermissionID.unique(),
                        "permission 2"
                )
        );

        final var aRole = Role.newRole("user", "Common user", RoleTypes.COMMON, false);

        final var aRoleUpdatedDate = aRole.getUpdatedAt();

        final var aRoleUpdated = aRole.update(
                aName,
                aDescription,
                aRoleType,
                aIsDefault,
                aPermissions
        );

        // then
        Assertions.assertEquals(aRole.getId().getValue(), aRoleUpdated.getId().getValue());
        Assertions.assertEquals(aName, aRoleUpdated.getName());
        Assertions.assertEquals(aDescription, aRoleUpdated.getDescription());
        Assertions.assertEquals(RoleTypes.EMPLOYEES, aRoleUpdated.getRoleType());
        Assertions.assertTrue(aRoleUpdated.isDefault());
        Assertions.assertEquals(aPermissions, aRoleUpdated.getPermissions());
        Assertions.assertEquals(aRole.getCreatedAt(), aRoleUpdated.getCreatedAt());
        Assertions.assertTrue(aRoleUpdated.getUpdatedAt().isAfter(aRoleUpdatedDate));

        Assertions.assertDoesNotThrow(() -> aRoleUpdated.validate(new TestValidationHandler()));
    }

    @Test
    void givenAValidValuesWithNullDescriptionAndNullPermissions_whenCallUpdateRole_shouldReturnAnRoleUpdated() {
        final var aName = "ceo";
        final String aDescription = null;
        final var aRoleType = RoleTypes.EMPLOYEES;
        final var aIsDefault = false;
        final List<RolePermission> aPermissions = null;

        final var aRole = Role.newRole("User", "aaaaaaaaaa", RoleTypes.COMMON, true);

        final var aRoleUpdatedDate = aRole.getUpdatedAt();

        final var aRoleUpdated = aRole.update(
                aName,
                aDescription,
                aRoleType,
                aIsDefault,
                aPermissions
        );

        // then
        Assertions.assertEquals(aRole.getId().getValue(), aRoleUpdated.getId().getValue());
        Assertions.assertEquals(aName, aRoleUpdated.getName());
        Assertions.assertNull(aRoleUpdated.getDescription());
        Assertions.assertEquals(RoleTypes.EMPLOYEES, aRoleUpdated.getRoleType());
        Assertions.assertFalse(aRoleUpdated.isDefault());
        Assertions.assertEquals(0, aRoleUpdated.getPermissions().size());
        Assertions.assertEquals(aRole.getCreatedAt(), aRoleUpdated.getCreatedAt());
        Assertions.assertTrue(aRoleUpdated.getUpdatedAt().isAfter(aRoleUpdatedDate));

        Assertions.assertDoesNotThrow(() -> aRoleUpdated.validate(new TestValidationHandler()));
    }

    @Test
    void givenAnInvalidNameNull_whenCallUpdateRole_shouldReturnADomainException() {
        final String aName = null;
        final var aDescription = "Ceo of the application";
        final var aRoleType = RoleTypes.EMPLOYEES;
        final var aIsDefault = false;
        final List<RolePermission> aPermissions = null;

        final var expectedErrorMessage = "'name' should not be null or blank";
        final var expectedErrorCount = 1;

        final var aRole = Role.newRole("User", aDescription, aRoleType, aIsDefault);

        final var aRoleUpdated = aRole.update(
                aName,
                aDescription,
                aRoleType,
                aIsDefault,
                aPermissions
        );

        final var aTestValidationHandler = new TestValidationHandler();
        final var aRoleValidator = new RoleValidator(aRoleUpdated, aTestValidationHandler);

        aRoleValidator.validate();

        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aTestValidationHandler.getErrors().size());
    }

    @Test
    void givenAnInvalidNameBlank_whenCallUpdateRole_shouldReturnADomainException() {
        final var aName = "";
        final var aDescription = "Ceo of the application";
        final var aRoleType = RoleTypes.EMPLOYEES;
        final var aIsDefault = false;
        final List<RolePermission> aPermissions = null;

        final var expectedErrorMessage = "'name' should not be null or blank";
        final var expectedErrorCount = 1;

        final var aRole = Role.newRole("User", aDescription, aRoleType, aIsDefault);

        final var aRoleUpdated = aRole.update(
                aName,
                aDescription,
                aRoleType,
                aIsDefault,
                aPermissions
        );

        final var aTestValidationHandler = new TestValidationHandler();
        final var aRoleValidator = new RoleValidator(aRoleUpdated, aTestValidationHandler);

        aRoleValidator.validate();

        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aTestValidationHandler.getErrors().size());
    }

    @Test
    void givenAnInvalidNameLengthLessThan3_whenCallUpdateRole_shouldReturnADomainException() {
        final var aName = "ce ";
        final var aDescription = "Ceo of the application";
        final var aRoleType = RoleTypes.EMPLOYEES;
        final var aIsDefault = false;
        final List<RolePermission> aPermissions = null;

        final var expectedErrorMessage = "'name' must be between 3 and 50 characters";
        final var expectedErrorCount = 1;

        final var aRole = Role.newRole("User", aDescription, aRoleType, aIsDefault);

        final var aRoleUpdated = aRole.update(
                aName,
                aDescription,
                aRoleType,
                aIsDefault,
                aPermissions
        );

        final var aTestValidationHandler = new TestValidationHandler();
        final var aRoleValidator = new RoleValidator(aRoleUpdated, aTestValidationHandler);

        aRoleValidator.validate();

        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aTestValidationHandler.getErrors().size());
    }

    @Test
    void givenAnInvalidNameLengthMoreThan50_whenCallUpdateRole_shouldReturnADomainException() {
        final var aName = RandomStringUtils.generateValue(51);
        final var aDescription = "Ceo of the application";
        final var aRoleType = RoleTypes.EMPLOYEES;
        final var aIsDefault = false;
        final List<RolePermission> aPermissions = null;

        final var expectedErrorMessage = "'name' must be between 3 and 50 characters";
        final var expectedErrorCount = 1;

        final var aRole = Role.newRole("User", aDescription, aRoleType, aIsDefault);

        final var aRoleUpdated = aRole.update(
                aName,
                aDescription,
                aRoleType,
                aIsDefault,
                aPermissions
        );

        final var aTestValidationHandler = new TestValidationHandler();
        final var aRoleValidator = new RoleValidator(aRoleUpdated, aTestValidationHandler);

        aRoleValidator.validate();

        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aTestValidationHandler.getErrors().size());
    }

    @Test
    void givenAnInvalidDescriptionLengthMoreThan255_whenCallUpdateRole_shouldReturnADomainException() {
        final var aName = "ceo";
        final var aDescription = RandomStringUtils.generateValue(256);
        final var aRoleType = RoleTypes.EMPLOYEES;
        final var aIsDefault = false;
        final List<RolePermission> aPermissions = null;

        final var expectedErrorMessage = "'description' must be between 0 and 255 characters";
        final var expectedErrorCount = 1;

        final var aRole = Role.newRole("User", "Common user", aRoleType, aIsDefault);

        final var aRoleUpdated = aRole.update(
                aName,
                aDescription,
                aRoleType,
                aIsDefault,
                aPermissions
        );

        final var aTestValidationHandler = new TestValidationHandler();
        final var aRoleValidator = new RoleValidator(aRoleUpdated, aTestValidationHandler);

        aRoleValidator.validate();

        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aTestValidationHandler.getErrors().size());
    }

    @Test
    void givenAnInvalidRoleTypeNull_whenCallUpdateRole_shouldReturnADomainException() {
        final var aName = "ceo";
        final String aDescription = null;
        final RoleTypes aRoleType = null;
        final var aIsDefault = false;
        final List<RolePermission> aPermissions = null;

        final var expectedErrorMessage = "'roleType' must not be null";
        final var expectedErrorCount = 1;

        final var aRole = Role.newRole("User", aDescription, RoleTypes.EMPLOYEES, aIsDefault);

        final var aRoleUpdated = aRole.update(
                aName,
                aDescription,
                aRoleType,
                aIsDefault,
                aPermissions
        );

        final var aTestValidationHandler = new TestValidationHandler();
        final var aRoleValidator = new RoleValidator(aRoleUpdated, aTestValidationHandler);

        aRoleValidator.validate();

        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aTestValidationHandler.getErrors().size());
    }

    @Test
    void givenAValidEmptyRolePermissions_whenCallAddRolePermission_shouldReceiveOk() {
        final var aPermissionOne = RolePermission.newRolePermission(
                PermissionID.unique(),
                "permission 1"
        );
        final var aPermissionTwo = RolePermission.newRolePermission(
                PermissionID.unique(),
                "permission 2"
        );

        final var aName = "ceo";
        final var aDescription = "Ceo of the application";
        final var aRoleType = RoleTypes.EMPLOYEES;
        final var aIsDefault = false;
        final var aPermissions = List.of(aPermissionOne, aPermissionTwo);

        final var actualRole = Role.newRole(aName, aDescription, aRoleType, aIsDefault);

        Assertions.assertEquals(0, actualRole.getPermissions().size());

        final var actualUpdatedAt = actualRole.getUpdatedAt();

        actualRole.addPermission(aPermissionOne);
        actualRole.addPermission(aPermissionTwo);

        Assertions.assertNotNull(actualRole.getId());
        Assertions.assertEquals(aName, actualRole.getName());
        Assertions.assertEquals(aDescription, actualRole.getDescription());
        Assertions.assertEquals(aRoleType, actualRole.getRoleType());
        Assertions.assertFalse(actualRole.isDefault());
        Assertions.assertEquals(aPermissions, actualRole.getPermissions());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualRole.getUpdatedAt()));
        Assertions.assertNotNull(actualRole.getCreatedAt());
    }

    @Test
    void givenAnInvalidNullAsRolePermission_whenCallAddRolePermission_shouldReceiveOk() {
        final var aName = "ceo";
        final var aDescription = "Ceo of the application";
        final var aRoleType = RoleTypes.EMPLOYEES;
        final var aIsDefault = false;
        final var aPermissions = new ArrayList<RolePermission>();

        final var actualRole = Role.newRole(aName, aDescription, aRoleType, aIsDefault);

        Assertions.assertEquals(0, actualRole.getPermissions().size());

        final var actualUpdatedAt = actualRole.getUpdatedAt();

        actualRole.addPermission(null);

        Assertions.assertNotNull(actualRole.getId());
        Assertions.assertEquals(aName, actualRole.getName());
        Assertions.assertEquals(aDescription, actualRole.getDescription());
        Assertions.assertEquals(aRoleType, actualRole.getRoleType());
        Assertions.assertFalse(actualRole.isDefault());
        Assertions.assertEquals(aPermissions, actualRole.getPermissions());
        Assertions.assertFalse(actualUpdatedAt.isBefore(actualRole.getUpdatedAt()));
        Assertions.assertNotNull(actualRole.getCreatedAt());
    }

    @Test
    void givenAValidRoleWithTwoRolePermission_whenCallRemoveRolePermission_shouldReceiveOk() {
        final var aPermissionOne = RolePermission.newRolePermission(
                PermissionID.unique(),
                "permission 1"
        );
        final var aPermissionTwo = RolePermission.newRolePermission(
                PermissionID.unique(),
                "permission 2"
        );

        final var aName = "ceo";
        final var aDescription = "Ceo of the application";
        final var aRoleType = RoleTypes.EMPLOYEES;
        final var aIsDefault = false;
        final var aPermissions = List.of(aPermissionTwo);

        final var actualRole = Role.newRole(aName, aDescription, aRoleType, aIsDefault);
        actualRole.update(aName, aDescription, aRoleType, aIsDefault, List.of(aPermissionOne, aPermissionTwo));

        Assertions.assertEquals(2, actualRole.getPermissions().size());

        final var actualUpdatedAt = actualRole.getUpdatedAt();

        actualRole.removePermission(aPermissionOne);

        Assertions.assertNotNull(actualRole.getId());
        Assertions.assertEquals(aName, actualRole.getName());
        Assertions.assertEquals(aDescription, actualRole.getDescription());
        Assertions.assertEquals(aRoleType, actualRole.getRoleType());
        Assertions.assertFalse(actualRole.isDefault());
        Assertions.assertEquals(aPermissions, actualRole.getPermissions());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualRole.getUpdatedAt()));
        Assertions.assertNotNull(actualRole.getCreatedAt());
    }

    @Test
    void givenAnInvalidNullAsRolePermission_whenCallRemoveRolePermission_shouldReceiveOk() {
        final var aPermissionOne = RolePermission.newRolePermission(
                PermissionID.unique(),
                "permission 1"
        );
        final var aPermissionTwo = RolePermission.newRolePermission(
                PermissionID.unique(),
                "permission 2"
        );

        final var aName = "ceo";
        final var aDescription = "Ceo of the application";
        final var aRoleType = RoleTypes.EMPLOYEES;
        final var aIsDefault = false;
        final var aPermissions = List.of(aPermissionOne, aPermissionTwo);

        final var actualRole = Role.newRole(aName, aDescription, aRoleType, aIsDefault);
        actualRole.update(aName, aDescription, aRoleType, aIsDefault, aPermissions);

        Assertions.assertEquals(2, actualRole.getPermissions().size());

        final var actualUpdatedAt = actualRole.getUpdatedAt();

        actualRole.removePermission(null);

        Assertions.assertNotNull(actualRole.getId());
        Assertions.assertEquals(aName, actualRole.getName());
        Assertions.assertEquals(aDescription, actualRole.getDescription());
        Assertions.assertEquals(aRoleType, actualRole.getRoleType());
        Assertions.assertFalse(actualRole.isDefault());
        Assertions.assertEquals(aPermissions, actualRole.getPermissions());
        Assertions.assertFalse(actualUpdatedAt.isBefore(actualRole.getUpdatedAt()));
        Assertions.assertNotNull(actualRole.getCreatedAt());
    }
}
