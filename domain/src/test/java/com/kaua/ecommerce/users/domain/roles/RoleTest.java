package com.kaua.ecommerce.users.domain.roles;

import com.kaua.ecommerce.users.domain.TestValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RoleTest {

    @Test
    void givenAValidValues_whenCallNewRole_thenRoleShouldBeCreated() {
        final var aName = "ceo";
        final var aDescription = "Ceo of the application";
        final var aRoleType = RoleTypes.EMPLOYEE;

        final var aRole = Role.newRole(aName, aDescription, aRoleType);

        Assertions.assertNotNull(aRole.getId());
        Assertions.assertEquals(aName, aRole.getName());
        Assertions.assertEquals(aDescription, aRole.getDescription());
        Assertions.assertEquals(aRoleType, aRole.getRoleType());
        Assertions.assertNotNull(aRole.getCreatedAt());
        Assertions.assertNotNull(aRole.getUpdatedAt());

        final var aTestValidationHandler = new TestValidationHandler();
        final var aRoleValidator = new RoleValidator(aRole, aTestValidationHandler);

        Assertions.assertDoesNotThrow(aRoleValidator::validate);
    }
}
