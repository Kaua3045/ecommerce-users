package com.kaua.ecommerce.users.domain.roles;

import com.kaua.ecommerce.users.domain.TestValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RoleTest {

    @Test
    void givenAValidValues_whenCallNewRole_thenRoleShouldBeCreated() {
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
    void givenAnInvalidNameBlank_whenCallNewRole_shouldReturnADomainExceptio() {
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
}
