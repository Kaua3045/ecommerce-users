package com.kaua.ecommerce.users.domain.permissions;

import com.kaua.ecommerce.users.domain.TestValidationHandler;
import com.kaua.ecommerce.users.domain.utils.IdUtils;
import com.kaua.ecommerce.users.domain.utils.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PermissionTest {

    @Test
    void givenAValidValuesWithDescription_whenCallNewPermission_thenRoleShouldBeCreated() {
        final var aName = "create-role";
        final var aDescription = "Permission to create a role";

        final var aPermission = Permission.newPermission(aName, aDescription);

        Assertions.assertNotNull(aPermission.getId());
        Assertions.assertEquals(aName, aPermission.getName());
        Assertions.assertEquals(aDescription, aPermission.getDescription());
        Assertions.assertNotNull(aPermission.getCreatedAt());
        Assertions.assertNotNull(aPermission.getUpdatedAt());

        Assertions.assertDoesNotThrow(() -> aPermission.validate(new TestValidationHandler()));

        Assertions.assertEquals(aPermission.getId(), aPermission.getId());
        Assertions.assertNotEquals(PermissionID.unique(), aPermission.getId());
        Assertions.assertNotNull(aPermission.getId());
        Assertions.assertFalse(aPermission.getId().equals(null));
        Assertions.assertFalse(aPermission.getId().equals(new Object()));
        Assertions.assertNotEquals(1201212, aPermission.getId().hashCode());
    }

    @Test
    void givenAValidValuesWithNullDescription_whenCallNewPermission_thenRoleShouldBeCreated() {
        final var aName = "create-role";
        final String aDescription = null;

        final var aPermission = Permission.newPermission(aName, aDescription);

        Assertions.assertNotNull(aPermission.getId());
        Assertions.assertEquals(aName, aPermission.getName());
        Assertions.assertNull(aPermission.getDescription());
        Assertions.assertNotNull(aPermission.getCreatedAt());
        Assertions.assertNotNull(aPermission.getUpdatedAt());

        final var aTestValidationHandler = new TestValidationHandler();
        final var aPermissionValidator = new PermissionValidator(aTestValidationHandler, aPermission);

        aPermissionValidator.validate();

        Assertions.assertEquals(0, aTestValidationHandler.getErrors().size());
    }

    @Test
    void givenAnInvalidNameNull_whenCallNewPermission_shouldReturnADomainException() {
        final String aName = null;
        final var aDescription = "Permission to create a role";

        final var expectedErrorMessage = "'name' should not be null or blank";
        final var expectedErrorCount = 1;

        final var aPermission = Permission.newPermission(aName, aDescription);

        final var aTestValidationHandler = new TestValidationHandler();
        final var aRoleValidator = new PermissionValidator(aTestValidationHandler, aPermission);

        aRoleValidator.validate();

        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aTestValidationHandler.getErrors().size());
    }

    @Test
    void givenAnInvalidNameBlank_whenCallNewPermission_shouldReturnADomainException() {
        final String aName = null;
        final var aDescription = "Permission to create a role";

        final var expectedErrorMessage = "'name' should not be null or blank";
        final var expectedErrorCount = 1;

        final var aPermission = Permission.newPermission(aName, aDescription);

        final var aTestValidationHandler = new TestValidationHandler();
        final var aRoleValidator = new PermissionValidator(aTestValidationHandler, aPermission);

        aRoleValidator.validate();

        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aTestValidationHandler.getErrors().size());
    }

    @Test
    void givenAnInvalidNameLengthLessThan3_whenCallNewPermission_shouldReturnADomainException() {
        final var aName = "cr";
        final var aDescription = "Permission to create a role";

        final var expectedErrorMessage = "'name' must be between 3 and 50 characters";
        final var expectedErrorCount = 1;

        final var aPermission = Permission.newPermission(aName, aDescription);

        final var aTestValidationHandler = new TestValidationHandler();
        final var aRoleValidator = new PermissionValidator(aTestValidationHandler, aPermission);

        aRoleValidator.validate();

        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aTestValidationHandler.getErrors().size());
    }

    @Test
    void givenAnInvalidNameLengthMoreThan50_whenCallNewPermission_shouldReturnADomainException() {
        final var aName = RandomStringUtils.generateValue(51);
        final var aDescription = "Permission to create a role";

        final var expectedErrorMessage = "'name' must be between 3 and 50 characters";
        final var expectedErrorCount = 1;

        final var aPermission = Permission.newPermission(aName, aDescription);

        final var aTestValidationHandler = new TestValidationHandler();
        final var aRoleValidator = new PermissionValidator(aTestValidationHandler, aPermission);

        aRoleValidator.validate();

        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aTestValidationHandler.getErrors().size());
    }

    @Test
    void givenAnInvalidDescriptionLengthMoreThan255_whenCallNewPermission_shouldReturnADomainException() {
        final var aName = "create-role";
        final var aDescription = RandomStringUtils.generateValue(256);

        final var expectedErrorMessage = "'description' must be between 0 and 255 characters";
        final var expectedErrorCount = 1;

        final var aPermission = Permission.newPermission(aName, aDescription);

        final var aTestValidationHandler = new TestValidationHandler();
        final var aRoleValidator = new PermissionValidator(aTestValidationHandler, aPermission);

        aRoleValidator.validate();

        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aTestValidationHandler.getErrors().size());
    }

    @Test
    void givenAValidIdString_whenCallPermissionIdFromString_shouldReturnAPermissionId() {
        final var aId = IdUtils.generate();
        final var aPermissionId = PermissionID.from(aId);

        Assertions.assertNotNull(aPermissionId);
        Assertions.assertEquals(aId, aPermissionId.getValue());
    }

    @Test
    void givenValidValues_whenCalledWithInPermission_shouldReturnAnPermissionObjectWithDataEqualToWhatWasPassed() {
        final var aName = "create-role";
        final var aDescription = "Permission to create a role";

        final var aPermission = Permission.newPermission(aName, aDescription);

        final var aPermissionCloned = Permission.with(
                aPermission.getId().getValue(),
                aPermission.getName(),
                aPermission.getDescription(),
                aPermission.getCreatedAt(),
                aPermission.getUpdatedAt()
        );

        // then
        Assertions.assertEquals(aPermission.getId().getValue(), aPermissionCloned.getId().getValue());
        Assertions.assertEquals(aPermission.getName(), aPermissionCloned.getName());
        Assertions.assertEquals(aPermission.getDescription(), aPermissionCloned.getDescription());
        Assertions.assertEquals(aPermission.getCreatedAt(), aPermissionCloned.getCreatedAt());
        Assertions.assertEquals(aPermission.getUpdatedAt(), aPermissionCloned.getUpdatedAt());
    }

    @Test
    void givenAValidValuesWithDescription_whenCallUpdatePermission_shouldReturnAnPermissionUpdated() {
        final var aName = "create-role";
        final var aDescription = "Permission to create a role";

        final var aPermission = Permission.newPermission("delete-role", null);

        final var aPermissionUpdatedAt = aPermission.getUpdatedAt();

        final var aPermissionUpdated = aPermission.update(aName, aDescription);

        // then
        Assertions.assertEquals(aPermission.getId().getValue(), aPermissionUpdated.getId().getValue());
        Assertions.assertEquals(aName, aPermissionUpdated.getName());
        Assertions.assertEquals(aDescription, aPermissionUpdated.getDescription());
        Assertions.assertEquals(aPermission.getCreatedAt(), aPermissionUpdated.getCreatedAt());
        Assertions.assertTrue(aPermissionUpdated.getUpdatedAt().isAfter(aPermissionUpdatedAt));

        Assertions.assertDoesNotThrow(() -> aPermissionUpdated.validate(new TestValidationHandler()));
    }

    @Test
    void givenAValidValuesWithNullDescription_whenCallUpdatePermission_shouldReturnAnPermissionUpdated() {
        final var aName = "create-role";
        final String aDescription = null;

        final var aPermission = Permission.newPermission("delete-role", "Permission to delete a role");

        final var aPermissionUpdatedAt = aPermission.getUpdatedAt();

        final var aPermissionUpdated = aPermission.update(aName, aDescription);

        // then
        Assertions.assertEquals(aPermission.getId().getValue(), aPermissionUpdated.getId().getValue());
        Assertions.assertEquals(aName, aPermissionUpdated.getName());
        Assertions.assertNull(aPermissionUpdated.getDescription());
        Assertions.assertEquals(aPermission.getCreatedAt(), aPermissionUpdated.getCreatedAt());
        Assertions.assertTrue(aPermissionUpdated.getUpdatedAt().isAfter(aPermissionUpdatedAt));

        Assertions.assertDoesNotThrow(() -> aPermissionUpdated.validate(new TestValidationHandler()));
    }

    @Test
    void givenAnInvalidNameNull_whenCallUpdatePermission_shouldReturnADomainException() {
        final String aName = null;
        final var aDescription = "Permission to create a role";

        final var expectedErrorMessage = "'name' should not be null or blank";
        final var expectedErrorCount = 1;

        final var aPermission = Permission.newPermission("delete-role", null);

        final var aPermissionUpdated = aPermission.update(aName, aDescription);

        final var aTestValidationHandler = new TestValidationHandler();
        final var aPermissionValidator = new PermissionValidator(aTestValidationHandler, aPermissionUpdated);

        aPermissionValidator.validate();

        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aTestValidationHandler.getErrors().size());
    }

    @Test
    void givenAnInvalidNameBlank_whenCallUpdatePermission_shouldReturnADomainException() {
        final var aName = "";
        final var aDescription = "Permission to create a role";

        final var expectedErrorMessage = "'name' should not be null or blank";
        final var expectedErrorCount = 1;

        final var aPermission = Permission.newPermission("delete-role", null);

        final var aPermissionUpdated = aPermission.update(aName, aDescription);

        final var aTestValidationHandler = new TestValidationHandler();
        final var aPermissionValidator = new PermissionValidator(aTestValidationHandler, aPermissionUpdated);

        aPermissionValidator.validate();

        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aTestValidationHandler.getErrors().size());
    }

    @Test
    void givenAnInvalidNameLengthLessThan3_whenCallUpdatePermission_shouldReturnADomainException() {
        final var aName = "cr";
        final var aDescription = "Permission to create a role";

        final var expectedErrorMessage = "'name' must be between 3 and 50 characters";
        final var expectedErrorCount = 1;

        final var aPermission = Permission.newPermission("delete-role", null);

        final var aPermissionUpdated = aPermission.update(aName, aDescription);

        final var aTestValidationHandler = new TestValidationHandler();
        final var aPermissionValidator = new PermissionValidator(aTestValidationHandler, aPermissionUpdated);

        aPermissionValidator.validate();

        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aTestValidationHandler.getErrors().size());
    }

    @Test
    void givenAnInvalidNameLengthMoreThan50_whenCallUpdatePermission_shouldReturnADomainException() {
        final var aName = RandomStringUtils.generateValue(51);
        final var aDescription = "Permission to create a role";

        final var expectedErrorMessage = "'name' must be between 3 and 50 characters";
        final var expectedErrorCount = 1;

        final var aPermission = Permission.newPermission("delete-role", null);

        final var aPermissionUpdated = aPermission.update(aName, aDescription);

        final var aTestValidationHandler = new TestValidationHandler();
        final var aPermissionValidator = new PermissionValidator(aTestValidationHandler, aPermissionUpdated);

        aPermissionValidator.validate();

        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aTestValidationHandler.getErrors().size());
    }

    @Test
    void givenAnInvalidDescriptionLengthMoreThan255_whenCallUpdatePermission_shouldReturnADomainException() {
        final var aName = "create-role";
        final var aDescription = RandomStringUtils.generateValue(256);

        final var expectedErrorMessage = "'description' must be between 0 and 255 characters";
        final var expectedErrorCount = 1;

        final var aPermission = Permission.newPermission("delete-role", null);

        final var aPermissionUpdated = aPermission.update(aName, aDescription);

        final var aTestValidationHandler = new TestValidationHandler();
        final var aPermissionValidator = new PermissionValidator(aTestValidationHandler, aPermissionUpdated);

        aPermissionValidator.validate();

        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aTestValidationHandler.getErrors().size());
    }
}
