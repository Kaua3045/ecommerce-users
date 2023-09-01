package com.kaua.ecommerce.users.domain.roles;

import com.kaua.ecommerce.users.domain.TestValidationHandler;
import com.kaua.ecommerce.users.domain.utils.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RoleTest {

    @Test
    void givenAValidValuesWithDescription_whenCallNewRole_thenRoleShouldBeCreated() {
        final var aName = "ceo";
        final var aDescription = "Ceo of the application";
        final var aRoleType = RoleTypes.EMPLOYEES;

        final var aRole = Role.newRole(aName, aDescription, aRoleType);

        Assertions.assertNotNull(aRole.getId());
        Assertions.assertEquals(aName, aRole.getName());
        Assertions.assertEquals(aDescription, aRole.getDescription());
        Assertions.assertEquals(aRoleType, aRole.getRoleType());
        Assertions.assertNotNull(aRole.getCreatedAt());
        Assertions.assertNotNull(aRole.getUpdatedAt());

        final var aTestValidationHandler = new TestValidationHandler();
        final var aRoleValidator = new RoleValidator(aRole, aTestValidationHandler);

        aRoleValidator.validate();

        Assertions.assertEquals(0, aTestValidationHandler.getErrors().size());
    }

    @Test
    void givenAValidValuesWithNullDescription_whenCallNewRole_thenRoleShouldBeCreated() {
        final var aName = "ceo";
        final String aDescription = null;
        final var aRoleType = RoleTypes.EMPLOYEES;

        final var aRole = Role.newRole(aName, aDescription, aRoleType);

        Assertions.assertNotNull(aRole.getId());
        Assertions.assertEquals(aName, aRole.getName());
        Assertions.assertEquals(aDescription, aRole.getDescription());
        Assertions.assertEquals(aRoleType, aRole.getRoleType());
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

        final var expectedErrorMessage = "'name' should not be null or blank";
        final var expectedErrorCount = 1;

        final var aRole = Role.newRole(aName, aDescription, aRoleType);

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

        final var expectedErrorMessage = "'name' should not be null or blank";
        final var expectedErrorCount = 1;

        final var aRole = Role.newRole(aName, aDescription, aRoleType);

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

        final var expectedErrorMessage = "'name' must be between 3 and 50 characters";
        final var expectedErrorCount = 1;

        final var aRole = Role.newRole(aName, aDescription, aRoleType);

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

        final var expectedErrorMessage = "'name' must be between 3 and 50 characters";
        final var expectedErrorCount = 1;

        final var aRole = Role.newRole(aName, aDescription, aRoleType);

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

        final var expectedErrorMessage = "'description' must be between 0 and 255 characters";
        final var expectedErrorCount = 1;

        final var aRole = Role.newRole(aName, aDescription, aRoleType);

        final var aTestValidationHandler = new TestValidationHandler();
        final var aRoleValidator = new RoleValidator(aRole, aTestValidationHandler);

        aRoleValidator.validate();

        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aTestValidationHandler.getErrors().size());
    }
}
